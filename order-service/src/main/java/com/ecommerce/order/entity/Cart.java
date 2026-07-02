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
@Table(name = "carts", indexes = {
    @Index(name = "idx_user_id", columnList = "user_id")
})
public class Cart extends BaseEntity {

    @Id
    private String id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "is_guest_cart")
    private Boolean isGuestCart = false;

    @Column(name = "merged_at")
    private java.time.LocalDateTime mergedAt;
}
