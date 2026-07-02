package com.ecommerce.order.entity;

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
@Table(name = "order_tracking", indexes = {
    @Index(name = "idx_order_id", columnList = "order_id"),
    @Index(name = "idx_timestamp", columnList = "timestamp")
})
public class OrderTracking {

    @Id
    private String id;

    @Column(name = "order_id", nullable = false)
    private String orderId;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }
}
