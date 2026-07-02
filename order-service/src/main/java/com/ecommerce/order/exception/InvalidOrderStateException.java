package com.ecommerce.order.exception;

import com.ecommerce.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class InvalidOrderStateException extends BaseException {
    public InvalidOrderStateException(String message) {
        super(message, "INVALID_ORDER_STATE", HttpStatus.BAD_REQUEST.value());
    }
}
