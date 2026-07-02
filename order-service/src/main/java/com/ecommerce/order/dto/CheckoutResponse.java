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
@Schema(description = "Checkout response with order confirmation")
public class CheckoutResponse {

    @Schema(description = "Order ID", example = "order-123")
    private String orderId;

    @Schema(description = "Order number", example = "ORD-2024-001234")
    private String orderNumber;

    @Schema(description = "Payment transaction ID", example = "txn-789")
    private String paymentTransactionId;

    @Schema(description = "Order status", example = "CONFIRMED")
    private String status;

    @Schema(description = "Complete order details")
    private OrderDTO order;
}
