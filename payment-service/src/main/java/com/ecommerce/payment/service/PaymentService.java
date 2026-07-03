package com.ecommerce.payment.service;

import com.ecommerce.payment.dto.CreatePaymentRequest;
import com.ecommerce.payment.dto.PaymentDTO;
import com.ecommerce.payment.dto.RefundDTO;
import com.ecommerce.payment.dto.RefundRequest;

public interface PaymentService {
    PaymentDTO createPayment(CreatePaymentRequest request);
    PaymentDTO getPaymentByOrderId(String orderId);
    PaymentDTO getPaymentById(String paymentId);
    RefundDTO processRefund(String paymentId, RefundRequest request);
}
