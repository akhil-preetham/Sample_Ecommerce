package com.ecommerce.inventory.client;

import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ProductServiceClientFallback implements ProductServiceClient {

    @Override
    public ProductVariantResponse getVariantDetails(String variantId) {
        log.warn("ProductService unavailable - returning fallback for variantId: {}", variantId);
        return ProductVariantResponse.builder()
            .id(variantId)
            .isActive(true)
            .build();
    }
}
