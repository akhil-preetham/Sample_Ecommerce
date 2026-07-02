package com.ecommerce.inventory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "stock_movements", indexes = {
    @Index(name = "idx_inventory_item_id", columnList = "inventory_item_id"),
    @Index(name = "idx_created_at", columnList = "created_at")
})
public class StockMovement {

    @Id
    private String id;

    @Column(name = "inventory_item_id", nullable = false)
    private String inventoryItemId;

    @Column(name = "movement_type", nullable = false)
    private String movementType;

    @Column(name = "quantity", nullable = false)
    private Long quantity;

    @Column(name = "reference", columnDefinition = "TEXT")
    private String reference;

    @Column(name = "reason")
    private String reason;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
