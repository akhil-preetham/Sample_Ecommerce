package com.ecommerce.common.exception;

import org.springframework.http.HttpStatus;

public class ServiceException extends BaseException {
    public ServiceException(String message) {
        super(message, "SERVICE_ERROR", HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
