package com.ecommerce.user.controller;

import com.ecommerce.common.constant.AppConstants;
import com.ecommerce.common.dto.ResponseWrapper;
import com.ecommerce.user.dto.LoginRequest;
import com.ecommerce.user.dto.LoginResponse;
import com.ecommerce.user.dto.RegisterRequest;
import com.ecommerce.user.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "User authentication endpoints")
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    @Operation(summary = "Register new user", description = "Create a new user account")
    public ResponseEntity<ResponseWrapper<LoginResponse>> register(
            @Valid @RequestBody RegisterRequest request) {
        log.info("Register endpoint called for email: {}", request.getEmail());

        LoginResponse response = authenticationService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ResponseWrapper.success(response, "User registered successfully"));
    }

    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Authenticate user with email and password")
    public ResponseEntity<ResponseWrapper<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request) {
        log.info("Login endpoint called for email: {}", request.getEmail());

        LoginResponse response = authenticationService.login(request);

        return ResponseEntity.ok(ResponseWrapper.success(response, "Login successful"));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh token", description = "Generate new access token using refresh token")
    public ResponseEntity<ResponseWrapper<LoginResponse>> refresh(
            @RequestParam String refreshToken) {
        log.info("Refresh token endpoint called");

        LoginResponse response = authenticationService.refreshToken(refreshToken);

        return ResponseEntity.ok(ResponseWrapper.success(response, "Token refreshed successfully"));
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout user", description = "Invalidate user tokens")
    public ResponseEntity<ResponseWrapper<String>> logout(
            @RequestHeader(AppConstants.AUTH_HEADER) String authHeader) {
        String userId = extractUserIdFromToken(authHeader);
        log.info("Logout endpoint called for user: {}", userId);

        authenticationService.logout(userId);

        return ResponseEntity.ok(ResponseWrapper.success(null, "Logout successful"));
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Forgot password", description = "Request password reset email")
    public ResponseEntity<ResponseWrapper<String>> forgotPassword(
            @RequestParam String email) {
        log.info("Forgot password endpoint called for email: {}", email);

        authenticationService.forgotPassword(email);

        return ResponseEntity.ok(ResponseWrapper.success(null, "Password reset email sent"));
    }

    @PostMapping("/change-password")
    @Operation(summary = "Change password", description = "Change password for authenticated user")
    public ResponseEntity<ResponseWrapper<String>> changePassword(
            @RequestHeader(AppConstants.AUTH_HEADER) String authHeader,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        String userId = extractUserIdFromToken(authHeader);
        log.info("Change password endpoint called for user: {}", userId);

        authenticationService.changePassword(userId, oldPassword, newPassword);

        return ResponseEntity.ok(ResponseWrapper.success(null, "Password changed successfully"));
    }

    @GetMapping("/validate-email")
    @Operation(summary = "Validate email", description = "Check if email is available for registration")
    public ResponseEntity<ResponseWrapper<Boolean>> validateEmail(
            @RequestParam String email) {
        log.info("Validate email endpoint called for: {}", email);

        boolean isValid = authenticationService.validateEmail(email);

        return ResponseEntity.ok(ResponseWrapper.success(isValid, "Email validation completed"));
    }

    private String extractUserIdFromToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith(AppConstants.BEARER_PREFIX)) {
            return authHeader.substring(AppConstants.BEARER_PREFIX.length());
        }
        throw new IllegalArgumentException("Invalid authorization header");
    }
}
