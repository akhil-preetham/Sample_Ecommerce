package com.ecommerce.user.service;

import com.ecommerce.user.dto.LoginRequest;
import com.ecommerce.user.dto.LoginResponse;
import com.ecommerce.user.dto.RegisterRequest;

public interface AuthenticationService {

    LoginResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    LoginResponse refreshToken(String refreshToken);

    void logout(String userId);

    void forgotPassword(String email);

    void resetPassword(String token, String newPassword);

    void changePassword(String userId, String oldPassword, String newPassword);

    boolean validateEmail(String email);
}
