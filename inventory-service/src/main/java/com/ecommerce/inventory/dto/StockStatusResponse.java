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
public class StockStatusResponse {

    private String inventoryItemId;

    private String productVariantId;

    private String warehouseId;

    private Long availableStock;

    private Long reservedStock;

    private Long totalStock;

    private Long reorderLevel;

    private Boolean lowStock;

    private LocalDateTime lastUpdated;
}
