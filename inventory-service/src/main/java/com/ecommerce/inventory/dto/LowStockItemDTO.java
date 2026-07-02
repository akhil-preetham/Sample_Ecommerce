package com.ecommerce.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LowStockItemDTO {
    private String id;
    private String productVariantId;
    private String warehouseId;
    private Long availableStock;
    private Long reorderLevel;
    private Long reservedStock;
}
