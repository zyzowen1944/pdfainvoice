package com.aisino.openapi.parser.util;

import com.aisino.openapi.parser.exception.ErrorCode;
import com.aisino.openapi.parser.exception.InvoiceParserException;
import com.aisino.openapi.parser.model.enums.InvoiceType;
import com.aisino.openapi.parser.service.OllamaService;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class PdfUtils {



    private static final Pattern INVOICE_TYPE_PATTERN = Pattern.compile(
            "(电子发票（增值税专用发票）)|(电子发票（普通发票）)|(增值税专用发票)|(普通发票)|" +
                    "(不动产租凭)|(航空客运)|电子发票（航空运输电子客票行程单）|(货物运输)|(建筑业发票)|(建筑服务)|(旅客运输)|(农产品)|(铁路客票)|(不动产经营租赁服务)"
    );

    /**
     * 检查是否为PDF文件
     */
    public static boolean isPdfFile(MultipartFile file) {
        String contentType = file.getContentType();
        return Objects.equals(contentType, "application/pdf") ||
                (file.getOriginalFilename() != null && file.getOriginalFilename().toLowerCase().endsWith(".pdf"));
    }

    /**
     * 从PDF文档中提取文本
     */
    public static String extractText(PDDocument document) throws IOException {
        PDFTextStripper stripper = new PDFTextStripper();
        stripper.setSortByPosition(true);
        return stripper.getText(document);
    }

    /**
     * 识别发票类型
     */
    public static InvoiceType identifyInvoiceType(String text) {
        Matcher matcher = INVOICE_TYPE_PATTERN.matcher(text);
        if (matcher.find()) {
            String match = matcher.group();
            if (match.contains("增值税专用发票") && match.contains("电子发票")) {
                return InvoiceType.ELECTRONIC_SPECIAL_VAT;
            } else if (match.contains("普通发票") && match.contains("电子发票")) {
                return InvoiceType.ELECTRONIC_NORMAL;
            } else if (match.contains("增值税专用发票")) {
                return InvoiceType.SPECIAL_VAT;
            } else if (match.contains("普通发票")) {
                return InvoiceType.NORMAL;
            } else if (match.contains("不动产租凭")||match.contains("不动产经营租赁服务")) {
                return InvoiceType.REAL_ESTATE_RENTAL;
            } else if (match.contains("航空客运")||match.contains("电子发票（航空运输电子客票行程单）")) {
                return InvoiceType.AIR_PASSENGER;
            } else if (match.contains("货物运输")) {
                return InvoiceType.FREIGHT_TRANSPORT;
            } else if (match.contains("建筑业发票")||match.contains("建筑服务")) {
                return InvoiceType.CONSTRUCTION;
            } else if (match.contains("旅客运输")) {
                return InvoiceType.PASSENGER_TRANSPORT;
            } else if (match.contains("农产品")) {
                return InvoiceType.AGRICULTURAL_PRODUCT;
            } else if (match.contains("铁路客票")) {
                return InvoiceType.RAILWAY_TICKET;
            }
        }
        log.warn("未能识别发票类型，文本内容: {}", text.substring(0, Math.min(200, text.length())));
        return InvoiceType.UNKNOWN;
    }

    /**
     * 使用OCR识别图像中的文本
     */
    public static String recognizeTextFromImage(BufferedImage image, String dataPath, String language) {
        try {
            Tesseract tesseract = new Tesseract();
            tesseract.setDatapath(dataPath);
            tesseract.setLanguage(language);
            tesseract.setPageSegMode(1); // 自动页面分割
            return tesseract.doOCR(image);
        } catch (TesseractException e) {
            log.error("OCR识别失败: {}", e.getMessage(), e);
            throw new InvoiceParserException(ErrorCode.OCR_RECOGNITION_ERROR, e);
        }
    }

    /**
     * 将PDF页面渲染为图像
     */
    public static List<BufferedImage> renderPdfToImages(PDDocument document, int dpi) throws IOException {
        PDFRenderer renderer = new PDFRenderer(document);
        List<BufferedImage> images = new ArrayList<>();

        for (int i = 0; i < document.getNumberOfPages(); i++) {
            BufferedImage image = renderer.renderImageWithDPI(i, dpi, ImageType.RGB);
            images.add(image);
        }

        return images;
    }
}