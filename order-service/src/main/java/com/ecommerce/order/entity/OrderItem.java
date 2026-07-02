package com.ecommerce.order.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.ecommerce.common.entity.BaseEntity;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "order_items", indexes = {
    @Index(name = "idx_order_id", columnList = "order_id")
})
public class OrderItem extends BaseEntity {

    @Id
    private String id;

    @Column(name = "order_id", nullable = false)
    private String orderId;

    @Column(name = "product_variant_id", nullable = false)
    private String productVariantId;

    @Column(name = "quantity", nullable = false)
    private Long quantity;

    @Column(name = "price", nullable = false)
    private BigDecimal price;
}
