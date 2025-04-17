package com.aisino.openapi.parser.util.extractors;


import com.aisino.openapi.parser.config.AppConfig;
import com.aisino.openapi.parser.exception.ErrorCode;
import com.aisino.openapi.parser.exception.InvoiceParserException;
import com.aisino.openapi.parser.model.dto.InvoiceDTO;
import com.aisino.openapi.parser.model.dto.InvoiceItemDTO;
import com.aisino.openapi.parser.model.enums.InvoiceType;
import com.aisino.openapi.parser.util.InvoiceExtractor;
import com.aisino.openapi.parser.util.PdfUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
public class ElectronicNormalExtractor implements InvoiceExtractor {

    private final AppConfig appConfig;

    // 正则表达式模式
    private static final Pattern INVOICE_NUMBER_PATTERN = Pattern.compile("发票号码[：:](\\s*\\d+)");
    private static final Pattern INVOICE_DATE_PATTERN = Pattern.compile("开票日期[：:](\\s*\\d{4}[年/-]\\d{1,2}[月/-]\\d{1,2}[日])");
    private static final Pattern BUYER_NAME_PATTERN = Pattern.compile("(?=.*购)(?=.*名).*?名称[：:](\\\\s*[^\\\\n]+)");
    private static final Pattern BUYER_TAX_ID_PATTERN = Pattern.compile("购\\s*买\\s*方.+?统一社会信用代码\\/纳税人识别号[：:](\\s*[^\\n]+)");
    private static final Pattern SELLER_NAME_PATTERN = Pattern.compile("(?=.*销)(?=.*名).*?名称[：:](\\\\s*[^\\\\n]+)");
    private static final Pattern SELLER_TAX_ID_PATTERN = Pattern.compile("销\\s*售\\s*方.+?统一社会信用代码\\/纳税人识别号[：:](\\s*[^\\n]+)");
    private static final Pattern TOTAL_AMOUNT_PATTERN = Pattern.compile("合\\s*计\\s*([\\d\\.]+)");
    private static final Pattern TOTAL_TAX_PATTERN = Pattern.compile("合\\s*计.+?([\\d\\.]+)");
    private static final Pattern TOTAL_WITH_TAX_PATTERN = Pattern.compile("价税合计.+?小写.+?[¥￥]?\\s*([\\d\\.]+)");
    private static final Pattern AMOUNT_IN_WORDS_PATTERN = Pattern.compile("价税合计.+?大写.+?([^（）\\(\\)]+)");
    private static final Pattern ISSUER_PATTERN = Pattern.compile("开票人[：:](\\s*[^\\n]+)");
    private static final Pattern REMARK_PATTERN = Pattern.compile("备\\s*注[：:]?(\\s*[^\\n]+)");

    // 运输信息模式
    private static final Pattern TRANSPORT_INFO_PATTERN = Pattern.compile(
            "运输工具种类\\s*([^\\n]+)\\s*运输工具牌号\\s*([^\\n]+)\\s*起运地\\s*([^\\n]+)\\s*到达地\\s*([^\\n]+)\\s*运输货物名称\\s*([^\\n]+)"
    );

    // 货物项目模式
    private static final Pattern ITEM_PATTERN = Pattern.compile(
            "([^\\n]+)\\s+(\\d+\\.?\\d*)\\s+(\\d+\\.?\\d*)\\s+([^\\n]+)\\s+(\\d+\\.?\\d*)\\s+([^\\n%]+)[%％]\\s+(\\d+\\.?\\d*)"
    );

    @Override
    public InvoiceDTO extract(PDDocument document, String text) {
        log.debug("开始提取电子普通发票信息");

        try {
            // 提取基本信息
            String invoiceNumber = extractWithPattern(text, INVOICE_NUMBER_PATTERN, "发票号码");
            LocalDate issueDate = extractDate(text, INVOICE_DATE_PATTERN, "开票日期");
            String buyerName = extractWithPattern(text, BUYER_NAME_PATTERN, "购买方名称");
            String buyerTaxId = extractWithPattern(text, BUYER_TAX_ID_PATTERN, "购买方税号");
            String sellerName = extractWithPattern(text, SELLER_NAME_PATTERN, "销售方名称");
            String sellerTaxId = extractWithPattern(text, SELLER_TAX_ID_PATTERN, "销售方税号");

            // 提取金额信息
            Matcher totalMatcher = TOTAL_AMOUNT_PATTERN.matcher(text);
            BigDecimal totalAmount = null;
            BigDecimal totalTax = null;
            if (totalMatcher.find()) {
                totalAmount = new BigDecimal(totalMatcher.group(1));
                totalTax = new BigDecimal(totalMatcher.group(2));
            } else {
                log.warn("未找到合计金额和税额");
            }

            BigDecimal totalWithTax = extractBigDecimal(text, TOTAL_WITH_TAX_PATTERN, "价税合计");
            String amountInWords = extractWithPattern(text, AMOUNT_IN_WORDS_PATTERN, "大写金额");
            String remark = extractWithPatternOrDefault(text, REMARK_PATTERN, "备注", "");

            // 提取明细项
            List<InvoiceItemDTO> items = extractItems(text);

            // 构建发票DTO
            return InvoiceDTO.builder()
                    .invoiceNumber(invoiceNumber)
                    .invoiceType(InvoiceType.ELECTRONIC_NORMAL)
                    //.issueDate(issueDate)
                    .buyerName(buyerName)
                    .buyerTaxId(buyerTaxId)
                    .sellerName(sellerName)
                    .sellerTaxId(sellerTaxId)
                    .totalAmount(totalAmount)
                    .totalTax(totalTax)
                    .totalWithTax(totalWithTax)
                    .amountInWords(amountInWords)
                    .items(items)
                    .remark(remark)
                    .build();
        } catch (InvoiceParserException e) {
            throw e;
        } catch (Exception e) {
            log.error("提取电子普通发票信息失败: {}", e.getMessage(), e);
            throw new InvoiceParserException(ErrorCode.INVOICE_FIELD_MISSING, "提取发票信息失败: " + e.getMessage());
        }
    }

    /**
     * 提取发票明细项
     */
    private List<InvoiceItemDTO> extractItems(String text) {
        List<InvoiceItemDTO> items = new ArrayList<>();
        Matcher matcher = ITEM_PATTERN.matcher(text);

        while (matcher.find()) {
            try {
                InvoiceItemDTO item = InvoiceItemDTO.builder()
                        .name(matcher.group(1).trim())
                        .specification(matcher.group(2).trim())
                        .unit(matcher.group(3).trim())
                        .quantity(new BigDecimal(matcher.group(4).trim()))
                        .unitPrice(new BigDecimal(matcher.group(5).trim()))
                        .amount(new BigDecimal(matcher.group(6).trim()))
                        .taxRate(matcher.group(7).trim())
                        .tax(new BigDecimal(matcher.group(8).trim()))
                        .build();
                items.add(item);
            } catch (NumberFormatException e) {
                log.warn("解析发票明细项数值失败: {}", e.getMessage());
            }
        }

        if (items.isEmpty()) {
            log.warn("未找到发票明细项");
        }

        return items;
    }

    /**
     * 使用正则表达式提取值
     */
    private String extractWithPattern(String text, Pattern pattern, String fieldName) {
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        log.error("未找到字段: {}", fieldName);
        throw new InvoiceParserException(ErrorCode.INVOICE_FIELD_MISSING, "未找到字段: " + fieldName);
    }

    /**
     * 使用正则表达式提取值，如果未找到则返回默认值
     */
    private String extractWithPatternOrDefault(String text, Pattern pattern, String fieldName, String defaultValue) {
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        log.warn("未找到字段: {}, 使用默认值: {}", fieldName, defaultValue);
        return defaultValue;
    }

    /**
     * 提取日期
     */
    private LocalDate extractDate(String text, Pattern pattern, String fieldName) {
        String dateStr = extractWithPattern(text, pattern, fieldName);
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年M月d日");
            return LocalDate.parse(dateStr, formatter);
        } catch (Exception e) {
            log.error("日期格式解析失败: {}, 值: {}", fieldName, dateStr, e);
            throw new InvoiceParserException(ErrorCode.INVOICE_FIELD_MISSING, "日期格式解析失败: " + fieldName);
        }
    }

    /**
     * 提取BigDecimal
     */
    private BigDecimal extractBigDecimal(String text, Pattern pattern, String fieldName) {
        String valueStr = extractWithPattern(text, pattern, fieldName);
        try {
            return new BigDecimal(valueStr);
        } catch (NumberFormatException e) {
            log.error("数值格式解析失败: {}, 值: {}", fieldName, valueStr, e);
            throw new InvoiceParserException(ErrorCode.INVOICE_FIELD_MISSING, "数值格式解析失败: " + fieldName);
        }
    }
}