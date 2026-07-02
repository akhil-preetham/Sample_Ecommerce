package com.ecommerce.inventory.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
    name = "product-service",
    fallback = ProductServiceClientFallback.class
)
public interface ProductServiceClient {

    @GetMapping("/api/products/variants/{variantId}")
    ProductVariantResponse getVariantDetails(@PathVariable String variantId);
}
