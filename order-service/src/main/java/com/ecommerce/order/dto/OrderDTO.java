package com.ecommerce.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Order details")
public class OrderDTO {

    @Schema(description = "Order ID", example = "order-123")
    private String id;

    @Schema(description = "Order number", example = "ORD-2024-001234")
    private String orderNumber;

    @Schema(description = "User ID", example = "user-456")
    private String userId;

    @Schema(description = "Order status (PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED)", example = "CONFIRMED")
    private String status;

    @Schema(description = "Order items")
    private List<OrderItemDTO> items;

    @Schema(description = "Subtotal amount", example = "199.99")
    private BigDecimal subtotal;

    @Schema(description = "Tax amount", example = "20.00")
    private BigDecimal tax;

    @Schema(description = "Shipping cost", example = "10.00")
    private BigDecimal shipping;

    @Schema(description = "Discount applied", example = "10.00")
    private BigDecimal discount;

    @Schema(description = "Total order amount", example = "219.99")
    private BigDecimal totalAmount;

    @Schema(description = "Shipping address")
    private String shippingAddress;

    @Schema(description = "Billing address")
    private String billingAddress;

    @Schema(description = "Order notes")
    private String notes;

    @Schema(description = "Order creation timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Order last update timestamp")
    private LocalDateTime updatedAt;
}
