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
@Table(name = "inventory_items", indexes = {
    @Index(name = "idx_product_variant_id", columnList = "product_variant_id"),
    @Index(name = "idx_warehouse_id", columnList = "warehouse_id"),
    @Index(name = "idx_available_stock", columnList = "available_stock")
})
public class InventoryItem extends BaseEntity {

    @Id
    private String id;

    @Column(name = "product_variant_id", nullable = false)
    private String productVariantId;

    @Column(name = "warehouse_id", nullable = false)
    private String warehouseId;

    @Column(name = "available_stock", nullable = false)
    private Long availableStock = 0L;

    @Column(name = "reserved_stock", nullable = false)
    private Long reservedStock = 0L;

    @Column(name = "reorder_level")
    private Long reorderLevel = 10L;

    @Version
    @Column(name = "version")
    private Long version = 0L;
}
