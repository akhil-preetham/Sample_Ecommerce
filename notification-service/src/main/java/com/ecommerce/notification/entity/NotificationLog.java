package com.ecommerce.notification.entity;

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
@Table(name = "notification_logs", indexes = {
    @Index(name = "idx_created_at", columnList = "created_at"),
    @Index(name = "idx_status", columnList = "status")
})
public class NotificationLog {

    @Id
    private String id;

    @Column(name = "notification_type", nullable = false)
    private String notificationType;

    @Column(name = "recipient_email", nullable = false)
    private String recipientEmail;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
