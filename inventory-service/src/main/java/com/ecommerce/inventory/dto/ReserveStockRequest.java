package com.ecommerce.inventory.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReserveStockRequest {

    @NotNull(message = "Order ID is required")
    private String orderId;

    @NotNull(message = "Inventory item ID is required")
    private String inventoryItemId;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    private Long quantity;
}
