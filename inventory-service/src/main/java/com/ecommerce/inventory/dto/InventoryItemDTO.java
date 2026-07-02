package com.ecommerce.inventory.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryItemDTO {

    private String id;

    @NotNull(message = "Product variant ID is required")
    private String productVariantId;

    @NotNull(message = "Warehouse ID is required")
    private String warehouseId;

    @NotNull(message = "Available stock is required")
    private Long availableStock;

    @NotNull(message = "Reserved stock is required")
    private Long reservedStock;

    @NotNull(message = "Reorder level is required")
    private Long reorderLevel;

    private Long totalStock;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Long version;
}
