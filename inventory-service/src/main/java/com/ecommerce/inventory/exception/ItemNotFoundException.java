package com.ecommerce.inventory.exception;

import org.springframework.http.HttpStatus;
import com.ecommerce.common.exception.BaseException;

public class ItemNotFoundException extends BaseException {
    public ItemNotFoundException(String message) {
        super(message, "ITEM_NOT_FOUND", HttpStatus.NOT_FOUND.value());
    }
}
