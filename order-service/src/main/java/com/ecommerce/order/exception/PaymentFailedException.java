package com.ecommerce.order.exception;

import com.ecommerce.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class PaymentFailedException extends BaseException {
    public PaymentFailedException(String message) {
        super(message, "PAYMENT_FAILED", HttpStatus.PAYMENT_REQUIRED.value());
    }
}
