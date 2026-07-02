package com.ecommerce.inventory.exception;

import org.springframework.http.HttpStatus;
import com.ecommerce.common.exception.BaseException;

public class InsufficientStockException extends BaseException {
    public InsufficientStockException(String message) {
        super(message, "INSUFFICIENT_STOCK", HttpStatus.BAD_REQUEST.value());
    }
}
