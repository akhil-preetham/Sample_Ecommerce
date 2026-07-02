package com.ecommerce.order.exception;

import com.ecommerce.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class OrderNotFoundException extends BaseException {
    public OrderNotFoundException(String message) {
        super(message, "ORDER_NOT_FOUND", HttpStatus.NOT_FOUND.value());
    }
}
