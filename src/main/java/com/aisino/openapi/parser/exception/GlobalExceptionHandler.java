package com.aisino.openapi.parser.exception;


import com.aisino.openapi.parser.model.common.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvoiceParserException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvoiceParserException(InvoiceParserException e) {
        log.error("发票解析异常: {}", e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(e.getErrorCode().getCode(), e.getMessage()));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiResponse<Void>> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        log.error("文件上传大小超限: {}", e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ErrorCode.FILE_TOO_LARGE.getCode(), "上传文件大小不能超过10MB"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        log.error("系统异常: {}", e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(ErrorCode.SYSTEM_ERROR.getCode(), "系统错误，请联系管理员"));
    }
}