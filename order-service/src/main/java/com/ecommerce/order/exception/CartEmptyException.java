package com.ecommerce.order.exception;

import com.ecommerce.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class CartEmptyException extends BaseException {
    public CartEmptyException(String message) {
        super(message, "CART_EMPTY", HttpStatus.BAD_REQUEST.value());
    }
}
