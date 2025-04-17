package com.aisino.openapi.parser.service;


import com.aisino.openapi.parser.model.dto.InvoiceDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;

public interface InvoiceParserService {
    /**
     * 解析发票PDF文件
     * @param file PDF文件
     * @return 发票数据
     */
    InvoiceDTO parseInvoice(MultipartFile file);

    /**
     * 异步解析发票PDF文件
     * @param file PDF文件
     * @return 包含发票数据的CompletableFuture
     */
    CompletableFuture<InvoiceDTO> parseInvoiceAsync(MultipartFile file);
}