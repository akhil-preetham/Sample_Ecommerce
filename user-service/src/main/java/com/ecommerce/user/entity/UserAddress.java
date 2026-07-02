package com.ecommerce.user.entity;

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
@Table(name = "user_addresses", indexes = {
    @Index(name = "idx_user_id", columnList = "user_id"),
    @Index(name = "idx_is_default", columnList = "is_default")
})
public class UserAddress extends BaseEntity {

    @Id
    private String id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "address_type", nullable = false)
    private String addressType;

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "zip_code", nullable = false)
    private String zipCode;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "is_default")
    private Boolean isDefault = false;

    @Column(name = "recipient_name")
    private String recipientName;

    @Column(name = "phone")
    private String phone;
}
