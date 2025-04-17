package com.aisino.openapi.parser.service.impl;


import com.aisino.openapi.parser.config.AppConfig;
import com.aisino.openapi.parser.exception.ErrorCode;
import com.aisino.openapi.parser.exception.InvoiceParserException;
import com.aisino.openapi.parser.model.dto.InvoiceDTO;
import com.aisino.openapi.parser.model.enums.InvoiceType;
import com.aisino.openapi.parser.service.InvoiceParserService;
import com.aisino.openapi.parser.service.OllamaService;
import com.aisino.openapi.parser.util.InvoiceExtractorFactory;
import com.aisino.openapi.parser.util.PdfUtils;
import com.aisino.openapi.parser.util.parser.ElectronicNormalInvoiceParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvoiceParserServiceImpl implements InvoiceParserService {

    private final AppConfig appConfig;
    private final InvoiceExtractorFactory extractorFactory;

    private final OllamaService ollamaService;

    @Override
    public InvoiceDTO parseInvoice(MultipartFile file) {


        log.debug("开始解析发票文件: {}", file.getOriginalFilename());
        try {
            // 检查文件类型
            if (!PdfUtils.isPdfFile(file)) {
                log.error("文件类型错误，非PDF文件: {}", file.getOriginalFilename());
                throw new InvoiceParserException(ErrorCode.FILE_FORMAT_ERROR, "只支持PDF格式文件");
            }

            // 加载PDF文档
            final File tempFile = File.createTempFile("temppdf", ".tmp");
            tempFile.deleteOnExit();
            try (FileOutputStream out = new FileOutputStream(tempFile)) {
                IOUtils.copy(file.getInputStream(), out);
            }
            PDDocument document  = Loader.loadPDF(tempFile);
            try {

                // 检查PDF是否有效
                if (document.getNumberOfPages() == 0) {
                    log.error("PDF文件无效，页数为0: {}", file.getOriginalFilename());
                    throw new InvoiceParserException(ErrorCode.PDF_EMPTY_CONTENT, "PDF文件内容为空");
                }
//
//                // 提取文本内容
                String text = PdfUtils.extractText(document);
//                log.debug("提取的PDF文本: {}", text);

//                // 识别发票类型
                InvoiceType invoiceType = PdfUtils.identifyInvoiceType(text);
//                log.info("识别到的发票类型: {}", invoiceType.getDescription());

                // 根据发票类型选择相应的提取器解析发票

                InvoiceDTO invoice = null;

                try{
                    invoice = extractorFactory.getExtractor(invoiceType).extract(document, text);
                } catch (Exception e) {
                    log.error("本地解析异常: {}", e.getMessage());
                    log.info("启用大模型解析 ");
                    invoice = new ElectronicNormalInvoiceParser(ollamaService).parseInvoice(tempFile);

                }

                log.info("发票解析完成: {}", invoice.getInvoiceNumber());

                return invoice;
            } finally {
                document.close();
            }
        } catch (IOException e) {
            log.error("PDF文件处理错误: {}", e.getMessage(), e);
            throw new InvoiceParserException(ErrorCode.PDF_PARSE_ERROR, e);
        } catch (InvoiceParserException e) {
            throw e;
        } catch (Exception e) {
            log.error("发票解析过程中发生未知错误: {}", e.getMessage(), e);
            throw new InvoiceParserException(ErrorCode.SYSTEM_ERROR, e);
        }
    }

    @Async
    @Override
    public CompletableFuture<InvoiceDTO> parseInvoiceAsync(MultipartFile file) {
        return CompletableFuture.completedFuture(parseInvoice(file));
    }
}