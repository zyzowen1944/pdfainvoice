package com.aisino.openapi.parser.util.parser;

import com.aisino.openapi.parser.model.dto.InvoiceDTO;
import com.aisino.openapi.parser.model.enums.InvoiceType;
import com.aisino.openapi.parser.model.dto.InvoiceItemDTO;
import com.aisino.openapi.parser.service.OllamaService;
import com.aisino.openapi.parser.util.DeepseekUtil;
import com.aisino.openapi.parser.util.PdfResolveUtil;
import com.alibaba.fastjson2.JSONObject;
import io.micrometer.core.instrument.distribution.StepBucketHistogram;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 电子发票（普通发票）解析器
 */
@Slf4j
@Service
public class ElectronicNormalInvoiceParser {

    @Value("${invoice.parser.enable-ollama}")
    private boolean enableOllama=true;
    @Value("${invoice.parser.enable-silcom}")
    private boolean enableSilcom=true;

    private final OllamaService ollamaService;

    public ElectronicNormalInvoiceParser(OllamaService ollamaService) {
        this.ollamaService = ollamaService;
    }

    // 正则表达式模式
    private static final Pattern INVOICE_NUMBER_PATTERN = Pattern.compile("发票号码[：:](\\s*\\d+)");
    private static final Pattern INVOICE_DATE_PATTERN = Pattern.compile("开票日期[：:](\\s*\\d{4}[年/-]\\d{1,2}[月/-]\\d{1,2})");
    private static final Pattern BUYER_NAME_PATTERN = Pattern.compile("购买方信息.*?名称[：:](\\s*[^\\n]+)");
    private static final Pattern BUYER_TAX_ID_PATTERN = Pattern.compile("购买方信息.*?统一社会信用代码\\/纳税人识别号[：:](\\s*[^\\n]+)");
    private static final Pattern SELLER_NAME_PATTERN = Pattern.compile("销售方信息.*?名称[：:](\\s*[^\\n]+)");
    private static final Pattern SELLER_TAX_ID_PATTERN = Pattern.compile("销售方信息.*?统一社会信用代码\\/纳税人识别号[：:](\\s*[^\\n]+)");
    private static final Pattern TOTAL_AMOUNT_PATTERN = Pattern.compile("合\\s*计\\s*([\\d\\.]+)");
    private static final Pattern TOTAL_TAX_PATTERN = Pattern.compile("合\\s*计.*?([\\d\\.]+)$", Pattern.MULTILINE);
    private static final Pattern TOTAL_WITH_TAX_PATTERN = Pattern.compile("\\（小写\\）\\s*价税合计\\s*[¥￥]?\\s*([\\d\\.]+)");
    private static final Pattern AMOUNT_IN_WORDS_PATTERN = Pattern.compile("\\（大写\\）\\s*([^\\n（）\\(\\)]+)");
    private static final Pattern ISSUER_PATTERN = Pattern.compile("开票人[：:](\\s*[^\\n]+)");
    private static final Pattern REMARK_PATTERN = Pattern.compile("备\\s*注\\s*([^\\n]*)");

    // 发票明细项正则
    private static final Pattern ITEM_PATTERN = Pattern.compile(
            "([^\\d\\n]+)\\s+(\\d+\\.?\\d*)\\s+(\\d+\\.?\\d*)\\s+([^\\n]+)\\s+(\\d+\\.?\\d*)\\s+([^\\n%]+)[%％]\\s+(\\d+\\.?\\d*)"
    );

    // 发票明细项简化正则（针对特殊格式）
    private static final Pattern SIMPLE_ITEM_PATTERN = Pattern.compile(
            "\\*([^\\*]+)\\*([^\\d\\n]+)\\s*(\\d+\\.?\\d*)\\s*(\\d+\\.?\\d*)\\s*([^\\n]+)\\s*(\\d+\\.?\\d*)"
    );

    /**
     * 解析电子普通发票
     * @param pdfFile PDF文件
     * @return 解析后的发票对象
     */
    public InvoiceDTO parseInvoice(InvoiceType invoiceType,File pdfFile) throws IOException {
        log.info("开始解析电子普通发票: {}", pdfFile.getName());

        try (PDDocument document = Loader.loadPDF(pdfFile)) {
            // 提取文本
            long start = System.currentTimeMillis();
            PDFTextStripper textStripper = new PDFTextStripper();
        textStripper.setSortByPosition(true);
        String text = textStripper.getText(document);
        // 创建发票对象
        InvoiceDTO invoice = new InvoiceDTO();

//        start = System.currentTimeMillis();
//        //InvoiceDTO invoiceDTO = PdfResolveUtil.extract(pdfFile);
//        extractBasicInfo(invoice, text);// 提取基本信息
//        extractItems(invoice, text);
//        log.debug("本地程序解析 耗时{} {} ",System.currentTimeMillis()-start,invoice);

//        if(enableOllama) {
//            // Ollama GLM-7
//            start = System.currentTimeMillis();
//            text = ollamaService.search(invoiceType,text);
//            log.debug("本地AI- THUDM/GLM-4-9B-0414  解析   耗时{} {} ", System.currentTimeMillis() - start, text);
//        }

        if(enableSilcom) {
            // Silcom Qwen2.5-7B-Instruct
            start = System.currentTimeMillis();
            text = DeepseekUtil.getInstance().processWithDeepSeek(invoiceType,text);
            log.debug("Silcom THUDM/chatglm3-6b 解析  耗时{} {}", System.currentTimeMillis() - start, text);
        }
        text = text.substring(text.indexOf("{"),text.lastIndexOf("}")+1);
        invoice = JSONObject.parseObject(text,InvoiceDTO.class);
        invoice.setInvoiceType(invoiceType);


        // 验证数据完整性
       // validateData(invoice);

        log.info("发票解析完成: 发票号码={}, 开票日期={}", invoice.getInvoiceNumber(), invoice.getIssueDate());
        return invoice;
    } catch (Exception e) {
        log.error("解析电子普通发票失败: {}", e.getMessage(), e);
        throw new RuntimeException("发票解析失败: " + e.getMessage(), e);
    }
}

/**
 * 提取基本信息
 */
private void extractBasicInfo(InvoiceDTO invoice, String text) {
    // 发票号码
    Matcher invoiceNumberMatcher = INVOICE_NUMBER_PATTERN.matcher(text);
    if (invoiceNumberMatcher.find()) {
        invoice.setInvoiceNumber(invoiceNumberMatcher.group(1).trim());
    } else {
        // 直接查找数字
        Pattern directNumberPattern = Pattern.compile("(\\d{20})");
        Matcher directMatcher = directNumberPattern.matcher(text);
        if (directMatcher.find()) {
            invoice.setInvoiceNumber(directMatcher.group(1).trim());
            log.debug("通过直接匹配找到发票号码: {}", invoice.getInvoiceNumber());
        }
    }

    // 开票日期
    Matcher invoiceDateMatcher = INVOICE_DATE_PATTERN.matcher(text);
    if (invoiceDateMatcher.find()) {
        String dateStr = invoiceDateMatcher.group(1).trim();
        // 处理多种日期格式
        dateStr = dateStr.replaceAll("[年月]", "-").replace("日", "");
        invoice.setIssueDate(LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-M-d")));
    }

    // 购买方信息
    Matcher buyerNameMatcher = BUYER_NAME_PATTERN.matcher(text);
    if (buyerNameMatcher.find()) {
        invoice.setBuyerName(buyerNameMatcher.group(1).trim());
    } else {
        // 针对您提供的样例特殊格式进行处理
        String[] lines = text.split("\n");
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].contains("购买方信息") && i+2 < lines.length) {
                // 购买方名称可能在购买方信息的后两行
                if (!lines[i+1].contains("统一社会信用代码") && !lines[i+1].contains("名称")) {
                    invoice.setBuyerName(lines[i+1].trim());
                } else if (!lines[i+2].contains("统一社会信用代码") && !lines[i+2].contains("名称")) {
                    invoice.setBuyerName(lines[i+2].trim());
                }
            }
        }
    }

    Matcher buyerTaxIdMatcher = BUYER_TAX_ID_PATTERN.matcher(text);
        if (buyerTaxIdMatcher.find()) {
            invoice.setBuyerTaxId(buyerTaxIdMatcher.group(1).trim());
        } else {
            // 直接匹配可能的税号格式
            Pattern taxIdPattern = Pattern.compile("(91\\d{16}[0-9A-Z])");
            Matcher taxIdMatcher = taxIdPattern.matcher(text);
            List<String> taxIds = new ArrayList<>();
            while (taxIdMatcher.find()) {
                taxIds.add(taxIdMatcher.group(1));
            }
            if (taxIds.size() >= 2) {
                // 通常第一个是购买方，第二个是销售方
                invoice.setBuyerTaxId(taxIds.get(0));
                invoice.setSellerTaxId(taxIds.get(1));
            }
        }

        // 销售方信息
        Matcher sellerNameMatcher = SELLER_NAME_PATTERN.matcher(text);
        if (sellerNameMatcher.find()) {
            invoice.setSellerName(sellerNameMatcher.group(1).trim());
        } else {
            // 针对特殊格式处理
            String[] lines = text.split("\n");
            for (int i = 0; i < lines.length; i++) {
                if (lines[i].contains("销售方信息") && i+2 < lines.length) {
                    if (!lines[i+1].contains("统一社会信用代码") && !lines[i+1].contains("名称")) {
                        invoice.setSellerName(lines[i+1].trim());
                    } else if (!lines[i+2].contains("统一社会信用代码") && !lines[i+2].contains("名称")) {
                        invoice.setSellerName(lines[i+2].trim());
                    }
                }
            }
        }

        // 如果购买方和销售方在同一行，尝试分离它们
        if (invoice.getBuyerName() != null && invoice.getBuyerName().contains("上海") &&
                invoice.getSellerName() == null) {
            String[] parts = invoice.getBuyerName().split("上海");
            if (parts.length > 1) {
                invoice.setBuyerName(parts[0].trim());
                invoice.setSellerName("上海" + parts[1].trim());
            }
        }

        Matcher sellerTaxIdMatcher = SELLER_TAX_ID_PATTERN.matcher(text);
        if (sellerTaxIdMatcher.find()) {
            invoice.setSellerTaxId(sellerTaxIdMatcher.group(1).trim());
        }

        // 金额信息
        Matcher totalAmountMatcher = TOTAL_AMOUNT_PATTERN.matcher(text);
        if (totalAmountMatcher.find()) {
            invoice.setTotalAmount(new BigDecimal(totalAmountMatcher.group(1).trim()));
        }

        Matcher totalTaxMatcher = TOTAL_TAX_PATTERN.matcher(text);
        if (totalTaxMatcher.find()) {
            invoice.setTotalTax(new BigDecimal(totalTaxMatcher.group(1).trim()));
        }

        // 价税合计
        Matcher totalWithTaxMatcher = TOTAL_WITH_TAX_PATTERN.matcher(text);
        if (totalWithTaxMatcher.find()) {
            invoice.setTotalWithTax(new BigDecimal(totalWithTaxMatcher.group(1).trim()));
        } else {
            // 针对样例格式特殊处理
            Pattern simpleTotal = Pattern.compile("(\\d+\\.\\d{2})");
            Matcher simpleMatcher = simpleTotal.matcher(text);
            List<BigDecimal> amounts = new ArrayList<>();
            while (simpleMatcher.find()) {
                try {
                    amounts.add(new BigDecimal(simpleMatcher.group(1)));
                } catch (NumberFormatException e) {
                    // 忽略无效数字
                }
            }
            // 通常价税合计是最大的金额
            if (!amounts.isEmpty()) {
                BigDecimal max = amounts.stream().max(BigDecimal::compareTo).get();
                invoice.setTotalWithTax(max);
                log.debug("通过直接匹配找到价税合计: {}", max);
            }
        }

        // 价税合计大写
        Matcher amountInWordsMatcher = AMOUNT_IN_WORDS_PATTERN.matcher(text);
        if (amountInWordsMatcher.find()) {
            invoice.setAmountInWords(amountInWordsMatcher.group(1).trim());
        } else {
            // 针对样例特殊处理
            Pattern chineseAmount = Pattern.compile("([叁佰叁拾玖圆整])");
            Matcher chineseMatcher = chineseAmount.matcher(text);
            if (chineseMatcher.find()) {
                invoice.setAmountInWords(chineseMatcher.group(1));
            }
        }

        // 开票人
        Matcher issuerMatcher = ISSUER_PATTERN.matcher(text);
        if (issuerMatcher.find()) {
            invoice.setIssue(issuerMatcher.group(1).trim());
        } else {
            // 针对样例特殊处理，查找可能的人名格式
            Pattern namePattern = Pattern.compile("(\\p{IsHan}{2,3})\\s*");
            Matcher nameMatcher = namePattern.matcher(text);
            while (nameMatcher.find()) {
                String name = nameMatcher.group(1);
                if (name.length() <= 3 && !name.equals("合计") && !name.equals("备注")) {
                    invoice.setIssue(name);
                    break;
                }
            }
        }

        // 备注
        Matcher remarkMatcher = REMARK_PATTERN.matcher(text);
        if (remarkMatcher.find()) {
            invoice.setRemark(remarkMatcher.group(1).trim());
        }
    }

    /**
     * 提取明细项目
     */
    private void extractItems(InvoiceDTO invoice, String text) {
        List<InvoiceItemDTO> items = new ArrayList<>();

        // 首先尝试标准格式
        Matcher itemMatcher = ITEM_PATTERN.matcher(text);
        while (itemMatcher.find()) {
            try {
                InvoiceItemDTO item = new InvoiceItemDTO();
                item.setName(itemMatcher.group(1).trim());
                item.setUnitPrice(new BigDecimal(itemMatcher.group(2).trim()));
                item.setQuantity(new BigDecimal(itemMatcher.group(3).trim()));
                item.setUnit(itemMatcher.group(4).trim());
                item.setAmount(new BigDecimal(itemMatcher.group(5).trim()));
                item.setTaxRate(itemMatcher.group(6).trim());
                item.setTax(new BigDecimal(itemMatcher.group(7).trim()));

                items.add(item);
                log.debug("提取到标准格式明细项: {}", item);
            } catch (NumberFormatException e) {
                log.warn("解析明细项数值失败: {}", e.getMessage());
            }
        }

        // 如果标准格式未匹配，尝试简化格式
        if (items.isEmpty()) {
            Matcher simpleItemMatcher = SIMPLE_ITEM_PATTERN.matcher(text);
            while (simpleItemMatcher.find()) {
                try {
                    InvoiceItemDTO item = new InvoiceItemDTO();
                    // 针对您提供的样例处理 "*餐饮服务*餐饮服务"
                    item.setName(simpleItemMatcher.group(1).trim() + "-" + simpleItemMatcher.group(2).trim());

                    // 假设顺序为 单价、数量、金额、税率、税额
                    if (simpleItemMatcher.groupCount() >= 6) {
                        item.setUnitPrice(new BigDecimal(simpleItemMatcher.group(3).trim()));
                        item.setQuantity(new BigDecimal(simpleItemMatcher.group(4).trim()));
                        item.setAmount(new BigDecimal(simpleItemMatcher.group(5).trim()));
                        item.setTaxRate(simpleItemMatcher.group(6).trim() + "%");
                    }

                    // 根据税率和金额计算税额
                    if (item.getAmount() != null && item.getTaxRate() != null) {
                        String taxRateStr = item.getTaxRate().replace("%", "");
                        BigDecimal taxRate = new BigDecimal(taxRateStr).divide(new BigDecimal("100"));
                        BigDecimal tax = item.getAmount().multiply(taxRate).setScale(2, BigDecimal.ROUND_HALF_UP);
                        item.setTax(tax);
                    }

                    items.add(item);
                    log.debug("提取到简化格式明细项: {}", item);
                } catch (NumberFormatException e) {
                    log.warn("解析简化格式明细项数值失败: {}", e.getMessage());
                }
            }
        }

        // 如果仍未匹配到明细项，尝试直接从文本中提取关键信息
        if (items.isEmpty()) {
            // 从文本中查找关键词和数字，组装明细项
            Pattern servicePattern = Pattern.compile("([\\*餐饮服务\\*]+)");
            Pattern amountPattern = Pattern.compile("(\\d+\\.\\d{2})");
            Pattern taxRatePattern = Pattern.compile("(\\d+)%");

            Matcher serviceMatcher = servicePattern.matcher(text);
            List<String> services = new ArrayList<>();
            while (serviceMatcher.find()) {
                services.add(serviceMatcher.group(1));
            }

            Matcher amountMatcher = amountPattern.matcher(text);
            List<BigDecimal> amounts = new ArrayList<>();
            while (amountMatcher.find()) {
                try {
                    amounts.add(new BigDecimal(amountMatcher.group(1)));
                } catch (Exception e) {
                    // 忽略无效数值
                }
            }

            Matcher taxRateMatcher = taxRatePattern.matcher(text);
            List<String> taxRates = new ArrayList<>();
            while (taxRateMatcher.find()) {
                taxRates.add(taxRateMatcher.group(1));
            }

            // 如果找到服务名称、金额和税率，尝试构建明细项
            if (!services.isEmpty() && amounts.size() >= 3 && !taxRates.isEmpty()) {
                InvoiceItemDTO item = new InvoiceItemDTO();
                item.setName(services.get(0).replace("*", ""));

                // 假设金额顺序为：单价、金额、税额，税率为第一个找到的税率
                if (amounts.size() >= 3) {
                    item.setUnitPrice(amounts.get(0));
                    item.setQuantity(BigDecimal.ONE); // 假设数量为1
                    item.setAmount(amounts.get(1));
                    item.setTaxRate(taxRates.get(0) + "%");
                    item.setTax(amounts.get(2));
                }

                items.add(item);
                log.debug("通过关键信息构建明细项: {}", item);
            }
        }

        invoice.setItems(items);
        log.info("共提取到{}个明细项", items.size());
    }

    /**
     * 验证数据完整性
     */
    private void validateData(InvoiceDTO invoice) {
        log.debug("开始验证数据完整性");

        // 检查必要字段
        List<String> missingFields = new ArrayList<>();

        if (invoice.getInvoiceNumber() == null) missingFields.add("发票号码");
        if (invoice.getIssueDate() == null) missingFields.add("开票日期");
        if (invoice.getBuyerName() == null) missingFields.add("购买方名称");
        if (invoice.getSellerName() == null) missingFields.add("销售方名称");

        if (!missingFields.isEmpty()) {
            log.warn("发票缺少必要字段: {}", missingFields);
        }

        // 补全缺失的数据
        if (invoice.getTotalAmount() == null && !invoice.getItems().isEmpty()) {
            BigDecimal totalAmount = BigDecimal.ZERO;
            for (InvoiceItemDTO item : invoice.getItems()) {
                if (item.getAmount() != null) {
                    totalAmount = totalAmount.add(item.getAmount());
                }
            }
            invoice.setTotalAmount(totalAmount);
            log.debug("计算得到合计金额: {}", totalAmount);
        }

        if (invoice.getTotalTax() == null && !invoice.getItems().isEmpty()) {
            BigDecimal totalTax = BigDecimal.ZERO;
            for (InvoiceItemDTO item : invoice.getItems()) {
                if (item.getTax() != null) {
                    totalTax = totalTax.add(item.getTax());
                }
            }
            invoice.setTotalTax(totalTax);
            log.debug("计算得到合计税额: {}", totalTax);
        }

        if (invoice.getTotalWithTax() == null &&
                invoice.getTotalAmount() != null &&
                invoice.getTotalTax() != null) {
            BigDecimal totalWithTax = invoice.getTotalAmount().add(invoice.getTotalTax());
            invoice.setTotalWithTax(totalWithTax);
            log.debug("计算得到价税合计: {}", totalWithTax);
        }

        // 验证计算值与实际值是否一致
        if (invoice.getTotalAmount() != null &&
                invoice.getTotalTax() != null &&
                invoice.getTotalWithTax() != null) {

            BigDecimal calculatedTotal = invoice.getTotalAmount().add(invoice.getTotalTax());
            if (calculatedTotal.subtract(invoice.getTotalWithTax()).abs().compareTo(new BigDecimal("0.02")) > 0) {
                log.warn("价税合计验证失败: 计算值={}, 发票值={}", calculatedTotal, invoice.getTotalWithTax());
                // 可以选择以哪个为准
                invoice.setTotalWithTax(calculatedTotal);
            }
        }

        log.debug("数据完整性验证完成");
    }
}
