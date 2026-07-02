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
@Table(name = "wishlists", indexes = {
    @Index(name = "idx_user_id", columnList = "user_id")
})
public class Wishlist extends BaseEntity {

    @Id
    private String id;

    @Column(name = "user_id", nullable = false)
    private String userId;
}
