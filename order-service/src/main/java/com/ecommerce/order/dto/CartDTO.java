package com.ecommerce.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Shopping cart")
public class CartDTO {

    @Schema(description = "Cart ID", example = "cart-123")
    private String id;

    @Schema(description = "User ID", example = "user-456")
    private String userId;

    @Schema(description = "Cart items")
    private List<CartItemDTO> items;

    @Schema(description = "Subtotal amount", example = "199.99")
    private BigDecimal subtotal;

    @Schema(description = "Tax amount", example = "20.00")
    private BigDecimal tax;

    @Schema(description = "Shipping cost", example = "10.00")
    private BigDecimal shipping;

    @Schema(description = "Total amount", example = "229.99")
    private BigDecimal total;

    @Schema(description = "Whether this is a guest cart", example = "false")
    private Boolean isGuestCart;
}
