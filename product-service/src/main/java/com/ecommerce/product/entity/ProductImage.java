package com.ecommerce.product.entity;

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
@Table(name = "product_images", indexes = {
    @Index(name = "idx_product_id", columnList = "product_id")
})
public class ProductImage extends BaseEntity {

    @Id
    private String id;

    @Column(name = "product_id", nullable = false)
    private String productId;

    @Column(name = "image_url", nullable = false, columnDefinition = "TEXT")
    private String imageUrl;

    @Column(name = "alt_text")
    private String altText;

    @Column(name = "is_default")
    private Boolean isDefault = false;

    @Column(name = "display_order")
    private Integer displayOrder = 0;
}
