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
@Schema(description = "Refund request")
public class RefundRequest {

    @NotBlank(message = "Payment ID is required")
    @Schema(description = "Payment ID to refund", example = "PAY-12345")
    private String paymentId;

    @NotNull(message = "Refund amount is required")
    @Positive(message = "Refund amount must be positive")
    @Schema(description = "Refund amount", example = "1500.50")
    private BigDecimal amount;

    @NotBlank(message = "Refund reason is required")
    @Schema(description = "Reason for refund", example = "Customer requested cancellation")
    private String reason;
}
