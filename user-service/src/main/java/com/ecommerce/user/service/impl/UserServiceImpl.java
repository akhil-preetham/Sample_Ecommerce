package com.ecommerce.user.service.impl;

import com.ecommerce.common.exception.ResourceNotFoundException;
import com.ecommerce.user.dto.UserProfileDTO;
import com.ecommerce.user.entity.User;
import com.ecommerce.user.repository.UserRepository;
import com.ecommerce.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileDTO getUserProfile(String userId) {
        log.info("Fetching profile for user: {}", userId);

        User user = userRepository.findByIdAndIsActiveTrue(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return UserProfileDTO.builder()
            .id(user.getId())
            .email(user.getEmail())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .phone(user.getPhone())
            .emailVerified(user.getEmailVerified())
            .isActive(user.getIsActive())
            .build();
    }

    @Override
    public UserProfileDTO updateUserProfile(String userId, UserProfileDTO profileDTO) {
        log.info("Updating profile for user: {}", userId);

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (profileDTO.getFirstName() != null) {
            user.setFirstName(profileDTO.getFirstName());
        }
        if (profileDTO.getLastName() != null) {
            user.setLastName(profileDTO.getLastName());
        }
        if (profileDTO.getPhone() != null) {
            user.setPhone(profileDTO.getPhone());
        }

        user = userRepository.save(user);

        return UserProfileDTO.builder()
            .id(user.getId())
            .email(user.getEmail())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .phone(user.getPhone())
            .emailVerified(user.getEmailVerified())
            .isActive(user.getIsActive())
            .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findUserById(String userId) {
        return userRepository.findById(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User createUser(User user) {
        log.info("Creating new user with email: {}", user.getEmail());
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(String userId) {
        log.info("Deleting user: {}", userId);
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userRepository.delete(user);
    }

    @Override
    public void deactivateUser(String userId) {
        log.info("Deactivating user: {}", userId);
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setIsActive(false);
        userRepository.save(user);
    }
}
