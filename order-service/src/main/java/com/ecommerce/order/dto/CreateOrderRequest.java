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
@Schema(description = "Request to create an order/checkout")
public class CreateOrderRequest {

    @Schema(description = "Shipping address", example = "123 Main St, City, State 12345")
    private String shippingAddress;

    @Schema(description = "Billing address", example = "456 Oak Ave, Town, State 67890")
    private String billingAddress;

    @Schema(description = "Customer notes", example = "Please deliver after 5pm")
    private String notes;

    @Schema(description = "Coupon/discount code", example = "SAVE10")
    private String couponCode;

    @Schema(description = "Discount amount", example = "10.00")
    private BigDecimal discount;

    @Schema(description = "Shipping cost", example = "9.99")
    private BigDecimal shippingCost;
}
