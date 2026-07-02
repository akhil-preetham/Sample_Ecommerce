package com.ecommerce.common.exception;

import org.springframework.http.HttpStatus;

public class ValidationException extends BaseException {
    public ValidationException(String message) {
        super(message, "VALIDATION_ERROR", HttpStatus.BAD_REQUEST.value());
    }
}
