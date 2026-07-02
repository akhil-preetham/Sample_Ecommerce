package com.ecommerce.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request to add item to cart")
public class AddToCartRequest {

    @Schema(description = "Product variant ID", example = "var-123")
    private String productVariantId;

    @Schema(description = "Quantity to add", example = "2")
    private Long quantity;

    @Schema(description = "Unit price", example = "99.99")
    private java.math.BigDecimal price;
}
