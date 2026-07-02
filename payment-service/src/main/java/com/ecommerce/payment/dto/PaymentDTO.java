package com.ecommerce.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Payment details")
public class PaymentDTO {

    @Schema(description = "Payment ID", example = "PAY-12345")
    private String id;

    @Schema(description = "Order ID", example = "ORD-12345")
    private String orderId;

    @Schema(description = "Payment amount", example = "1500.50")
    private BigDecimal amount;

    @Schema(description = "Currency code", example = "INR")
    private String currency;

    @Schema(description = "Payment method", example = "CREDIT_CARD")
    private String paymentMethod;

    @Schema(description = "Payment status", example = "CAPTURED", allowableValues = {"PENDING", "AUTHORIZED", "CAPTURED", "FAILED", "REFUNDED"})
    private String status;

    @Schema(description = "Transaction ID from gateway", example = "TXN-98765")
    private String transactionId;

    @Schema(description = "Error message if payment failed")
    private String errorMessage;

    @Schema(description = "Payment creation timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Payment last update timestamp")
    private LocalDateTime updatedAt;
}
