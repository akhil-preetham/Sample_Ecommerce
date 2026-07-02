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
@Schema(description = "Checkout request")
public class CheckoutRequest {

    @Schema(description = "Shipping address", example = "123 Main St, City, State 12345")
    private String shippingAddress;

    @Schema(description = "Billing address", example = "456 Oak Ave, Town, State 67890")
    private String billingAddress;

    @Schema(description = "Payment method", example = "CREDIT_CARD")
    private String paymentMethod;

    @Schema(description = "Coupon code for discount", example = "SAVE10")
    private String couponCode;

    @Schema(description = "Shipping cost", example = "9.99")
    private BigDecimal shippingCost;

    @Schema(description = "Customer notes", example = "Please deliver after 5pm")
    private String notes;
}
