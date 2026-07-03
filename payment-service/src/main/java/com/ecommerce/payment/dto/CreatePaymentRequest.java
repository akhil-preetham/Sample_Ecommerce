package com.ecommerce.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Payment creation request")
public class CreatePaymentRequest {

    @NotBlank(message = "Order ID is required")
    @Schema(description = "Order ID", example = "ORD-12345")
    private String orderId;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    @Schema(description = "Payment amount", example = "1500.50")
    private BigDecimal amount;

    @Builder.Default
    @NotBlank(message = "Currency is required")
    @Schema(description = "Currency code", example = "INR", defaultValue = "INR")
    private String currency = "INR";

    @NotBlank(message = "Payment method is required")
    @Schema(description = "Payment method (CREDIT_CARD, DEBIT_CARD, UPI, NET_BANKING)", example = "CREDIT_CARD")
    private String paymentMethod;
}