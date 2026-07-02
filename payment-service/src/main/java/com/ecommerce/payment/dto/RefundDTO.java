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
@Schema(description = "Refund details")
public class RefundDTO {

    @Schema(description = "Refund ID", example = "REF-12345")
    private String id;

    @Schema(description = "Payment ID", example = "PAY-12345")
    private String paymentId;

    @Schema(description = "Refund amount", example = "1500.50")
    private BigDecimal amount;

    @Schema(description = "Reason for refund")
    private String reason;

    @Schema(description = "Refund status", example = "SUCCESSFUL", allowableValues = {"PENDING", "SUCCESSFUL", "FAILED"})
    private String status;

    @Schema(description = "Gateway transaction ID for refund")
    private String transactionId;

    @Schema(description = "Refund creation timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Refund last update timestamp")
    private LocalDateTime updatedAt;
}
