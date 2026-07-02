package com.ecommerce.payment.entity;

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
@Table(name = "payment_history", indexes = {
    @Index(name = "idx_payment_id", columnList = "payment_id"),
    @Index(name = "idx_timestamp", columnList = "timestamp")
})
public class PaymentHistory {

    @Id
    private String id;

    @Column(name = "payment_id", nullable = false)
    private String paymentId;

    @Column(name = "previous_status")
    private String previousStatus;

    @Column(name = "new_status", nullable = false)
    private String newStatus;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }
}
