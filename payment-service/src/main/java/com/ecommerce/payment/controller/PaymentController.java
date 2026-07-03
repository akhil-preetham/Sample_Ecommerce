package com.ecommerce.payment.controller;

import com.ecommerce.common.dto.ResponseWrapper;
import com.ecommerce.payment.dto.CreatePaymentRequest;
import com.ecommerce.payment.dto.PaymentDTO;
import com.ecommerce.payment.dto.RefundDTO;
import com.ecommerce.payment.dto.RefundRequest;
import com.ecommerce.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<ResponseWrapper<PaymentDTO>> createPayment(
            @Valid @RequestBody CreatePaymentRequest request) {
        PaymentDTO payment = paymentService.createPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ResponseWrapper.success(payment, "Payment created successfully"));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<ResponseWrapper<PaymentDTO>> getPaymentByOrderId(
            @PathVariable String orderId) {
        PaymentDTO payment = paymentService.getPaymentByOrderId(orderId);
        return ResponseEntity.ok(ResponseWrapper.success(payment, "Payment retrieved successfully"));
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<ResponseWrapper<PaymentDTO>> getPaymentById(
            @PathVariable String paymentId) {
        PaymentDTO payment = paymentService.getPaymentById(paymentId);
        return ResponseEntity.ok(ResponseWrapper.success(payment, "Payment retrieved successfully"));
    }

    @PostMapping("/{paymentId}/refund")
    public ResponseEntity<ResponseWrapper<RefundDTO>> processRefund(
            @PathVariable String paymentId,
            @Valid @RequestBody RefundRequest request) {
        RefundDTO refund = paymentService.processRefund(paymentId, request);
        return ResponseEntity.ok(ResponseWrapper.success(refund, "Refund processed successfully"));
    }
}
