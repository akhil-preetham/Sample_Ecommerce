package com.ecommerce.order.exception;

import com.ecommerce.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class InsufficientInventoryException extends BaseException {
    public InsufficientInventoryException(String message) {
        super(message, "INSUFFICIENT_INVENTORY", HttpStatus.CONFLICT.value());
    }
}
