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
@Table(name = "categories", indexes = {
    @Index(name = "idx_name", columnList = "name", unique = true),
    @Index(name = "idx_is_active", columnList = "is_active")
})
public class Category extends BaseEntity {

    @Id
    private String id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}
