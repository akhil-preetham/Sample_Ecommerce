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
@Schema(description = "Order item")
public class OrderItemDTO {

    @Schema(description = "Order item ID", example = "oi-123")
    private String id;

    @Schema(description = "Product variant ID", example = "var-456")
    private String productVariantId;

    @Schema(description = "Quantity ordered", example = "2")
    private Long quantity;

    @Schema(description = "Unit price at time of order", example = "99.99")
    private BigDecimal price;

    @Schema(description = "Computed line subtotal", example = "199.98")
    private BigDecimal subtotal;
}
