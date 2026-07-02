package com.ecommerce.order.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.ecommerce.common.entity.BaseEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "wishlist_items", indexes = {
    @Index(name = "idx_wishlist_id", columnList = "wishlist_id")
})
public class WishlistItem extends BaseEntity {

    @Id
    private String id;

    @Column(name = "wishlist_id", nullable = false)
    private String wishlistId;

    @Column(name = "product_variant_id", nullable = false)
    private String productVariantId;
}
