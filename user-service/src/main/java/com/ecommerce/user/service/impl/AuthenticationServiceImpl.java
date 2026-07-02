package com.ecommerce.user.service.impl;

import com.ecommerce.common.exception.ResourceNotFoundException;
import com.ecommerce.common.exception.ValidationException;
import com.ecommerce.common.util.UUIDUtil;
import com.ecommerce.common.util.ValidationUtil;
import com.ecommerce.common.config.JwtUtil;
import com.ecommerce.user.dto.LoginRequest;
import com.ecommerce.user.dto.LoginResponse;
import com.ecommerce.user.dto.RegisterRequest;
import com.ecommerce.user.entity.RefreshToken;
import com.ecommerce.user.entity.User;
import com.ecommerce.user.entity.UserRole;
import com.ecommerce.user.repository.RefreshTokenRepository;
import com.ecommerce.user.repository.UserRepository;
import com.ecommerce.user.repository.UserRoleRepository;
import com.ecommerce.user.service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Slf4j
@Service
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthenticationServiceImpl(UserRepository userRepository,
                                    UserRoleRepository userRoleRepository,
                                    RefreshTokenRepository refreshTokenRepository,
                                    PasswordEncoder passwordEncoder,
                                    JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public LoginResponse register(RegisterRequest request) {
        log.info("Processing registration for email: {}", request.getEmail());

        ValidationUtil.validateEmail(request.getEmail());
        ValidationUtil.validatePassword(request.getPassword());

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ValidationException("Email already registered");
        }

        String userId = UUIDUtil.generateUUID();
        User user = User.builder()
            .id(userId)
            .email(request.getEmail())
            .passwordHash(passwordEncoder.encode(request.getPassword()))
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .phone(request.getPhone())
            .isActive(true)
            .emailVerified(false)
            .build();

        user = userRepository.save(user);

        UserRole customerRole = UserRole.builder()
            .user(user)
            .role("CUSTOMER")
            .build();
        userRoleRepository.save(customerRole);

        log.info("User registered successfully: {}", userId);

        Collection<String> roles = Collections.singleton("CUSTOMER");
        String accessToken = jwtUtil.generateAccessToken(userId, roles);
        String refreshToken = generateAndSaveRefreshToken(userId);

        return LoginResponse.builder()
            .userId(userId)
            .email(user.getEmail())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .roles(Collections.singletonList("CUSTOMER"))
            .accessTokenExpiration(3600000L)
            .build();
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        log.info("Processing login for email: {}", request.getEmail());

        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + request.getEmail()));

        if (!user.getIsActive()) {
            throw new ValidationException("User account is inactive");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new ValidationException("Invalid email or password");
        }

        java.util.List<UserRole> userRoles = userRoleRepository.findByUserId(user.getId());
        Collection<String> roles = userRoles.stream()
            .map(UserRole::getRole)
            .toList();

        String accessToken = jwtUtil.generateAccessToken(user.getId(), roles);
        String refreshToken = generateAndSaveRefreshToken(user.getId());

        log.info("User logged in successfully: {}", user.getId());

        return LoginResponse.builder()
            .userId(user.getId())
            .email(user.getEmail())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .roles(userRoles.stream().map(UserRole::getRole).toList())
            .accessTokenExpiration(3600000L)
            .build();
    }

    @Override
    public LoginResponse refreshToken(String refreshTokenStr) {
        log.info("Processing refresh token request");

        String tokenHash = hashToken(refreshTokenStr);
        RefreshToken refreshToken = refreshTokenRepository.findByTokenHash(tokenHash)
            .orElseThrow(() -> new ValidationException("Invalid refresh token"));

        if (refreshToken.getIsRevoked() || refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Refresh token has expired or been revoked");
        }

        User user = userRepository.findById(refreshToken.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        java.util.List<UserRole> userRoles = userRoleRepository.findByUserId(user.getId());
        Collection<String> roles = userRoles.stream()
            .map(UserRole::getRole)
            .toList();

        String newAccessToken = jwtUtil.generateAccessToken(user.getId(), roles);

        return LoginResponse.builder()
            .userId(user.getId())
            .email(user.getEmail())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .accessToken(newAccessToken)
            .refreshToken(refreshTokenStr)
            .roles(userRoles.stream().map(UserRole::getRole).toList())
            .accessTokenExpiration(3600000L)
            .build();
    }

    @Override
    public void logout(String userId) {
        log.info("User logging out: {}", userId);
        refreshTokenRepository.deleteByUserId(userId);
    }

    @Override
    public void forgotPassword(String email) {
        log.info("Processing forgot password for email: {}", email);
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        log.info("Forgot password email would be sent to: {}", email);
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        log.info("Processing password reset");
        ValidationUtil.validatePassword(newPassword);
    }

    @Override
    public void changePassword(String userId, String oldPassword, String newPassword) {
        log.info("Changing password for user: {}", userId);

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(oldPassword, user.getPasswordHash())) {
            throw new ValidationException("Old password is incorrect");
        }

        ValidationUtil.validatePassword(newPassword);

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        log.info("Password changed successfully for user: {}", userId);
    }

    @Override
    public boolean validateEmail(String email) {
        ValidationUtil.validateEmail(email);
        return !userRepository.existsByEmail(email);
    }

    private String generateAndSaveRefreshToken(String userId) {
        String token = jwtUtil.generateRefreshToken(userId);
        String tokenHash = hashToken(token);

        RefreshToken refreshToken = RefreshToken.builder()
            .id(UUIDUtil.generateUUID())
            .userId(userId)
            .token(token)
            .tokenHash(tokenHash)
            .expiryDate(LocalDateTime.now().plusDays(7))
            .isRevoked(false)
            .build();

        refreshTokenRepository.save(refreshToken);
        return token;
    }

    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing token", e);
        }
    }
}
