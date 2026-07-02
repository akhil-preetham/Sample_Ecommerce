package com.ecommerce.common.exception;

public class BaseException extends RuntimeException {
    private final String errorCode;
    private final int httpStatus;

    public BaseException(String message, String errorCode, int httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public int getHttpStatus() {
        return httpStatus;
    }
}
