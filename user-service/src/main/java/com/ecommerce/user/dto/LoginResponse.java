package com.ecommerce.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {

    private String userId;

    private String email;

    private String firstName;

    private String lastName;

    private String accessToken;

    private String refreshToken;

    private List<String> roles;

    private Long accessTokenExpiration;
}
