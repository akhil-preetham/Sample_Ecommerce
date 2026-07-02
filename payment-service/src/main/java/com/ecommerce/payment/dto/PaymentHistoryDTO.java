package com.ecommerce.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Payment history entry")
public class PaymentHistoryDTO {

    @Schema(description = "Payment history ID")
    private String id;

    @Schema(description = "Payment ID", example = "PAY-12345")
    private String paymentId;

    @Schema(description = "Previous payment status", example = "AUTHORIZED")
    private String previousStatus;

    @Schema(description = "New payment status", example = "CAPTURED")
    private String newStatus;

    @Schema(description = "Status transition timestamp")
    private LocalDateTime timestamp;

    @Schema(description = "Additional notes for this transition")
    private String notes;
}
