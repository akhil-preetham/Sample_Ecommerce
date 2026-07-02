package com.ecommerce.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileDTO {

    private String id;

    private String email;

    private String firstName;

    private String lastName;

    private String phone;

    private Boolean emailVerified;

    private Boolean isActive;
}
