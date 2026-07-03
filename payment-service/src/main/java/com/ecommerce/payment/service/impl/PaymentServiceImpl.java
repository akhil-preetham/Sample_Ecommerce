package com.ecommerce.payment.service.impl;

import com.ecommerce.common.exception.ResourceNotFoundException;
import com.ecommerce.common.util.UUIDUtil;
import com.ecommerce.payment.dto.CreatePaymentRequest;
import com.ecommerce.payment.dto.PaymentDTO;
import com.ecommerce.payment.dto.RefundDTO;
import com.ecommerce.payment.dto.RefundRequest;
import com.ecommerce.payment.entity.Payment;
import com.ecommerce.payment.entity.Refund;
import com.ecommerce.payment.repository.PaymentRepository;
import com.ecommerce.payment.repository.RefundRepository;
import com.ecommerce.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final RefundRepository refundRepository;

    @Override
    public PaymentDTO createPayment(CreatePaymentRequest request) {
        log.info("Creating payment for order: {}", request.getOrderId());
        Payment payment = Payment.builder()
            .id(UUIDUtil.generateUUID())
            .orderId(request.getOrderId())
            .amount(request.getAmount())
            .currency(request.getCurrency() != null ? request.getCurrency() : "INR")
            .paymentMethod(request.getPaymentMethod())
            .status("CAPTURED")
            .transactionId("TXN-" + UUIDUtil.generateUUID().substring(0, 8).toUpperCase())
            .build();
        payment = paymentRepository.save(payment);
        return toDTO(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentDTO getPaymentByOrderId(String orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Payment not found for order: " + orderId));
        return toDTO(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentDTO getPaymentById(String paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new ResourceNotFoundException("Payment not found: " + paymentId));
        return toDTO(payment);
    }

    @Override
    public RefundDTO processRefund(String paymentId, RefundRequest request) {
        log.info("Processing refund for payment: {}", paymentId);
        Payment payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new ResourceNotFoundException("Payment not found: " + paymentId));
        payment.setStatus("REFUNDED");
        paymentRepository.save(payment);

        Refund refund = Refund.builder()
            .id(UUIDUtil.generateUUID())
            .paymentId(paymentId)
            .amount(request.getAmount())
            .reason(request.getReason())
            .status("SUCCESSFUL")
            .transactionId("REF-" + UUIDUtil.generateUUID().substring(0, 8).toUpperCase())
            .build();
        refund = refundRepository.save(refund);
        return toRefundDTO(refund);
    }

    private PaymentDTO toDTO(Payment p) {
        return PaymentDTO.builder()
            .id(p.getId())
            .orderId(p.getOrderId())
            .amount(p.getAmount())
            .currency(p.getCurrency())
            .paymentMethod(p.getPaymentMethod())
            .status(p.getStatus())
            .transactionId(p.getTransactionId())
            .errorMessage(p.getErrorMessage())
            .createdAt(p.getCreatedAt())
            .updatedAt(p.getUpdatedAt())
            .build();
    }

    private RefundDTO toRefundDTO(Refund r) {
        return RefundDTO.builder()
            .id(r.getId())
            .paymentId(r.getPaymentId())
            .amount(r.getAmount())
            .reason(r.getReason())
            .status(r.getStatus())
            .transactionId(r.getTransactionId())
            .createdAt(r.getCreatedAt())
            .updatedAt(r.getUpdatedAt())
            .build();
    }
}
