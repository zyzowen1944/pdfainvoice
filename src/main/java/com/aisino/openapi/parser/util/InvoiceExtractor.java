package com.aisino.openapi.parser.util;


import com.aisino.openapi.parser.model.dto.InvoiceDTO;
import org.apache.pdfbox.pdmodel.PDDocument;

public interface InvoiceExtractor {
    /**
     * 从PDF文档和文本中提取发票信息
     * @param document PDF文档
     * @param text 提取的文本内容
     * @return 发票数据
     */
    InvoiceDTO extract(PDDocument document, String text);
}