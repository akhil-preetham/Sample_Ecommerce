package com.ecommerce.inventory.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariantResponse {

    private String id;

    private String productId;

    private String sku;

    private String name;

    private Boolean isActive;
}
