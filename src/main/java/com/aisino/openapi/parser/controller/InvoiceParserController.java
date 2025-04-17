package com.aisino.openapi.parser.controller;

import com.aisino.openapi.parser.model.common.ApiResponse;
import com.aisino.openapi.parser.model.dto.InvoiceDTO;
import com.aisino.openapi.parser.service.InvoiceParserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping("/invoice")
@RequiredArgsConstructor
public class InvoiceParserController {

    private final InvoiceParserService invoiceParserService;

    @PostMapping(value = "/parse", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<InvoiceDTO> parseInvoice(@RequestPart("file") MultipartFile file) {
        log.info("接收到发票解析请求，路径: /invoice/parse, 文件名: {}, 大小: {} bytes",
                file.getOriginalFilename(), file.getSize());
        InvoiceDTO invoice = invoiceParserService.parseInvoice(file);
        return ApiResponse.success(invoice);
    }

    @PostMapping(value = "/parse-async", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CompletableFuture<ApiResponse<InvoiceDTO>> parseInvoiceAsync(@RequestPart("file") MultipartFile file) {
        log.info("接收到异步发票解析请求，文件名: {}, 大小: {} bytes", file.getOriginalFilename(), file.getSize());
        return invoiceParserService.parseInvoiceAsync(file)
                .thenApply(ApiResponse::success);
    }

    @GetMapping("/test")
    public ApiResponse<String> test() {
        log.info("测试端点被调用");
        return ApiResponse.success("API 工作正常");
    }
}