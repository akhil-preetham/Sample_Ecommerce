package com.ecommerce.product.entity;

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
@Table(name = "products", indexes = {
    @Index(name = "idx_name", columnList = "name"),
    @Index(name = "idx_category_id", columnList = "category_id"),
    @Index(name = "idx_brand_id", columnList = "brand_id"),
    @Index(name = "idx_is_active", columnList = "is_active"),
    @Index(name = "idx_created_at", columnList = "created_at"),
    @Index(name = "idx_name_description", columnList = "name,description")
})
public class Product extends BaseEntity {

    @Id
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "category_id", nullable = false)
    private String categoryId;

    @Column(name = "brand_id", nullable = false)
    private String brandId;

    @Column(name = "base_price", nullable = false)
    private BigDecimal basePrice;

    @Column(name = "rating")
    private Double rating = 0.0;

    @Column(name = "review_count")
    private Long reviewCount = 0L;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "sku", unique = true)
    private String sku;

    @Column(name = "stock")
    private Long stock = 0L;
}
