package com.aisino.openapi.parser.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 系统错误: 1000-1999
    SYSTEM_ERROR(1000, "系统错误"),
    CONFIG_ERROR(1001, "配置错误"),

    // 参数错误: 2000-2999
    INVALID_PARAMETER(2000, "参数无效"),
    FILE_TOO_LARGE(2001, "文件过大"),
    FILE_FORMAT_ERROR(2002, "文件格式错误"),

    // PDF处理错误: 3000-3999
    PDF_PARSE_ERROR(3000, "PDF解析错误"),
    PDF_INVALID_FORMAT(3001, "PDF格式无效"),
    PDF_EMPTY_CONTENT(3002, "PDF内容为空"),
    PDF_ENCRYPTION_ERROR(3003, "PDF加密错误"),

    // OCR错误: 4000-4999
    OCR_ENGINE_ERROR(4000, "OCR引擎错误"),
    OCR_RECOGNITION_ERROR(4001, "OCR识别错误"),

    // 发票识别错误: 5000-5999
    INVOICE_TYPE_UNKNOWN(5000, "未知发票类型"),
    INVOICE_FIELD_MISSING(5001, "发票字段缺失"),
    INVOICE_INVALID_CONTENT(5002, "发票内容无效");

    private final int code;
    private final String message;
}