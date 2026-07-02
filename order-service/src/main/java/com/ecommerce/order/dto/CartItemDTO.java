package com.ecommerce.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Cart item")
public class CartItemDTO {

    @Schema(description = "Cart item ID", example = "item-789")
    private String id;

    @Schema(description = "Product variant ID", example = "var-123")
    private String productVariantId;

    @Schema(description = "Quantity", example = "2")
    private Long quantity;

    @Schema(description = "Unit price", example = "99.99")
    private BigDecimal price;

    @Schema(description = "Subtotal (quantity * price)", example = "199.98")
    private BigDecimal subtotal;
}
