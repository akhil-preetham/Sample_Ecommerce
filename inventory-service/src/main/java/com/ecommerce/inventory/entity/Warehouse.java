package com.ecommerce.inventory.entity;

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
@Table(name = "warehouses", indexes = {
    @Index(name = "idx_name", columnList = "name", unique = true),
    @Index(name = "idx_is_active", columnList = "is_active")
})
public class Warehouse extends BaseEntity {

    @Id
    private String id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}
