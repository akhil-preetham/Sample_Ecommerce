package com.ecommerce.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockMovementDTO {
    private String id;
    private String movementType;
    private Long quantity;
    private String reference;
    private String reason;
    private LocalDateTime createdAt;
}
