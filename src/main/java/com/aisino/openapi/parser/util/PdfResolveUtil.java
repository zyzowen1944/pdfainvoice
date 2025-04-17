package com.aisino.openapi.parser.util;


import com.aisino.openapi.parser.model.dto.InvoiceDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.aisino.openapi.parser.util.InvoiceRegexEnum.*;


@Slf4j
public class PdfResolveUtil {

    private static final String BUYER_NAME = "购方开户银行：";
    private static final String ACCOUNT = "银行账号：";
    private static final String SELLER_NAME = "销方开户银行：";
    private static final String PAYEE = "收款人：";
    private static final String CHECKER = "复核人：";


    /**
     * 调用该方法将前端接受到的文件暂存
     *
     * @param file
     */
    public static InvoiceDTO resolveFile(MultipartFile file) {
        InvoiceDTO invoice = new InvoiceDTO();
        File tempFilePath = null;
        try {
            // 创建一个临时文件并将上传文件的内容写入其中
            Path tempFile = Files.createTempFile("tempPrefix", ".pdf");
            tempFilePath = tempFile.toFile();
            try (FileOutputStream fos = new FileOutputStream(tempFilePath)) {// 将MultipartFile的内容写入到临时文件
                fos.write(file.getBytes());
            }
            invoice = extract(tempFilePath);
        } catch (IOException e) {
            e.printStackTrace();// 处理异常
        } finally {
            // 无论是否发生异常，都尝试删除临时文件
            if (tempFilePath != null && !tempFilePath.delete()) {
                // 记录删除失败的情况
                System.err.println("无法删除临时文件: " + tempFilePath.getAbsolutePath());
            }
        }
        return invoice;// 返回值
    }

    /**
     * 解析PDF 文件，返回发票对象
     *
     * @param file PDF文件
     * @return
     * @throws IOException
     */
    public static InvoiceDTO extract(File file) throws IOException {
        InvoiceDTO invoice = new InvoiceDTO();  // 新建发票对象
        // 接收一个表示 PDF 文件路径的字符串作为参数，并返回一个 PDDocument 对象。
        // 这个对象代表了整个PDF 文档，可以通过这个对象来访问文档的各个部分
        PDDocument doc = Loader.loadPDF(file);
        // 从 PDDocument 对象 doc 中获取第一页，并将这个页面对象赋值给PDPage类型的变量
        // PDPage 对象代表了文档中的一个页面
        PDPage firstPage = doc.getPage(0);
        // 获取页面裁剪框宽度，并将宽度四舍五入为整数
        // 【页面裁剪宽度定义了页面上用于显示内容的区域】
        int pageWidth = Math.round(firstPage.getCropBox().getWidth());
        // PDFTextStripper 用于从PDF文档中提取文本的工具
        PDFTextStripper textStripper = new PDFTextStripper(); // 创建一个实例
        textStripper.setSortByPosition(true); // 提取文本时按照物理位置进行排序
        // 提取整个文档的所有文本内容，并将这些文本内容作为一个长字符串返回
        String fullText = textStripper.getText(doc);
        // 页面翻转？ 不重要
        if (firstPage.getRotation() != 0) {
            pageWidth = Math.round(firstPage.getCropBox().getHeight());
        }
        // 处理文本中可能有错误的符号
        String allText = replace(fullText).replaceAll("（", "(").replaceAll("）", ")").replaceAll("￥", "¥");
        // 提取 新版发票的机器编码、发票代码、发票号码、开票日期和检验码
        {
            Pattern pattern = Pattern.compile(REGULAR_A_NEW.getRegex()); // 新版发票的机器编码、发票代码、发票号码、开票日期和检验码的提取正则
            Pattern patternNumber = Pattern.compile(REGULAR_A_1.getRegex());// 发票号码备用提取正则
            Pattern patternDate = Pattern.compile(REGULAR_A_2.getRegex()); // 开票日期备用提取正则
            // matcer 类对于输入字符串进行解释和匹配操作，这些操作是基于某个Pattern对象定义的规则（正则表达式）进行的。
            // 检查allText 字符串中是否匹配pattern中定义的正则表达式的文本
            Matcher matcher = pattern.matcher(allText);
            while (matcher.find()) {// 在输入字符串allText中查找与模式匹配的第一个子序列
                // 如果 提取到发票号码，则设置发票号码
                if (matcher.group("invoiceNumber") != null) {
                    invoice.setInvoiceNumber(matcher.group("invoiceNumber"));
                } else if (matcher.group("issueDate") != null) {
                    String rawDate = matcher.group("issueDate"); // 发票日期，解析日期并设置日期
                    try {
                        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
                        SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        LocalDate parsedDate = inputDateFormat.parse(rawDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        invoice.setIssueDate(parsedDate);
                    } catch (ParseException e) {
                        System.out.println("无法解析日期：" + rawDate);
                    }
                }
                // 如果没有提取到的话使用备用在进行提取
                if (matcher.group("invoiceNumber") == null) {
                    Matcher matcher2 = patternNumber.matcher(allText);
                    if (matcher2.find()) {
                        invoice.setInvoiceNumber(matcher2.group("invoiceNumber"));
                    }
                }
                if (matcher.group("issueDate") == null) {
                    Matcher matcher3 = patternDate.matcher(allText);
                    if (matcher3.find()) {
                        String rawDate = matcher3.group("issueDate");
                        try {
                            SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyyMM月dd日");
                            SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            LocalDate parsedDate = inputDateFormat.parse(rawDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                            invoice.setIssueDate(parsedDate);
                        } catch (Exception e) {
                            System.out.println("无法解析日期：" + rawDate);
                        }
                    }
                }
            }
        }
        Double amount = null, taxAmount = null;
        // 提取 金额、税额等
        {
            Pattern pattern = Pattern.compile(REGULAR_B.getRegex()); // 金额、税额提取正则，匹配形如“合计￥？金额￥？税额”的文本

            Matcher matcher = pattern.matcher(allText);
            if (matcher.find()) {
                try {
                    amount = Double.parseDouble(matcher.group("amount"));
                } catch (Exception e) {
                    // 不处理
                }
                try {
                    taxAmount = Double.parseDouble(matcher.group("taxAmount"));
                } catch (Exception e) {
                    taxAmount =0d;
                }
            }
        }
        // 如果没有提取到，则再使用备用的正则进行提取
        if (null == amount) {
            Pattern pattern = Pattern.compile(REGULAR_B_1.getRegex());

            Matcher matcher = pattern.matcher(fullText);
            if (matcher.find()) {
                try {
                    amount = Double.parseDouble(matcher.group("amount"));
                } catch (Exception e) {
                    amount = 0d;
                }
                try {
                    taxAmount = Double.parseDouble(matcher.group("taxAmount"));
                } catch (Exception e) {
                    taxAmount = 0d;
                }
            }
        }
        invoice.setTotalWithTax(new BigDecimal(amount + taxAmount));
        // 先创建一个发票备注实例
        String note1 = "";
        // 提取发票备注信息
        {
            // 提取购方开户银行
            Pattern patternBuyer = Pattern.compile(REGULAR_A_NOTE_BUYER.getRegex()); // 提取备注信息
            Pattern patternBuyerAccount = Pattern.compile(REGULAR_A_NOTE_BUYERACCOUNT.getRegex()); // 提取备注信息
            Pattern patternSeller = Pattern.compile(REGULAR_A_NOTE_SELLER.getRegex()); // 提取备注信息
            Pattern patternSellerAccount = Pattern.compile(REGULAR_A_NOTE_SELLERACCOUNT.getRegex()); // 提取备注信息
            Pattern patternPayee = Pattern.compile(REGULAR_A_NOTE_PAYEE.getRegex()); // 提取备注信息
            Pattern patternChecker = Pattern.compile(REGULAR_A_NOTE_CHECKER.getRegex()); // 提取备注信息

            Matcher matcher0 = patternBuyer.matcher(allText);
            if (matcher0.find()) {// 如果查询到的话就设置备注信息
                try {
                    note1 += BUYER_NAME + matcher0.group("buyerName") + ";";
                } catch (Exception e) {
                    // 不处理
                }
            }
            Matcher matcher1 = patternBuyerAccount.matcher(allText);
            if (matcher1.find()) {// 如果查询到的话就设置备注信息
                try {
                    note1 += ACCOUNT + matcher1.group("buyerAccount") + ";";
                } catch (Exception e) {
                    // 不处理
                }
            }
            Matcher matcher2 = patternSeller.matcher(allText);
            if (matcher2.find()) {// 如果查询到的话就设置备注信息
                try {
                    note1 += SELLER_NAME + matcher2.group("sellerName") + ";";
                } catch (Exception e) {
                    // 不处理
                }
            }
            Matcher matcher3 = patternSellerAccount.matcher(allText);
            if (matcher3.find()) {// 如果查询到的话就设置备注信息
                try {
                    note1 += ACCOUNT + matcher3.group("sellerAccount") + ";";
                } catch (Exception e) {
                    // 不处理
                }
            }
            Matcher matcher4 = patternPayee.matcher(allText);
            if (matcher4.find()) {// 如果查询到的话就设置备注信息
                try {
                    note1 += PAYEE + matcher4.group("payee") + ";";
                } catch (Exception e) {
                    // 不处理
                }
            }
            Matcher matcher5 = patternChecker.matcher(allText);
            if (matcher5.find()) {// 如果查询到的话就设置备注信息
                try {
                    note1 += CHECKER + matcher5.group("checker") + ";";
                } catch (Exception e) {
                    // 不处理
                }
            }

        }
        invoice.setRemark(note1);
        doc.close();//关闭doc，否则临时文件无法删除
        return invoice;

    }

    /**
     * 替换字符串中的空格、全角空格、冒号和特殊空白字符为标准字符。
     *
     * @param str 要进行替换的字符串
     * @return 替换后的字符串
     */
    private static String replace(String str) {
        return str.replaceAll(" ", "").replaceAll("　", "").replaceAll("：", ":").replaceAll(" ", "");
    }

}

