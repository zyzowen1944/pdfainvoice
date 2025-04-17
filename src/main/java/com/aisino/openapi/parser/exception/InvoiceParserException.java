package com.aisino.openapi.parser.exception;


import lombok.Getter;

@Getter
public class InvoiceParserException extends RuntimeException {

    private final ErrorCode errorCode;

    public InvoiceParserException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public InvoiceParserException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public InvoiceParserException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }
}