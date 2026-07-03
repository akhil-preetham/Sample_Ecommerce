package com.ecommerce.order.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import com.ecommerce.common.entity.BaseEntity;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "cart_items", indexes = {
    @Index(name = "idx_cart_id", columnList = "cart_id")
})
public class CartItem extends BaseEntity {

    @Id
    private String id;

    @Column(name = "cart_id", nullable = false)
    private String cartId;

    @Column(name = "product_variant_id", nullable = false)
    private String productVariantId;

    @Builder.Default
    @Column(name = "quantity", nullable = false)
    private Long quantity = 1L;

    @Column(name = "price", nullable = false)
    private BigDecimal price;
}