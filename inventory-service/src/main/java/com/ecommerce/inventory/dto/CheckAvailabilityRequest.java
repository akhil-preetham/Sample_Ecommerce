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
public class CheckAvailabilityRequest {

    @NotNull(message = "Product variant ID is required")
    private String productVariantId;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    private Long quantity;

    private String warehouseId;
}
