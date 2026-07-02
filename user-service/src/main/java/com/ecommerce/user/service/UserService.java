package com.ecommerce.user.service;

import com.ecommerce.user.dto.UserProfileDTO;
import com.ecommerce.user.entity.User;
import java.util.Optional;

public interface UserService {

    UserProfileDTO getUserProfile(String userId);

    UserProfileDTO updateUserProfile(String userId, UserProfileDTO profileDTO);

    Optional<User> findUserById(String userId);

    Optional<User> findUserByEmail(String email);

    boolean existsByEmail(String email);

    User createUser(User user);

    void deleteUser(String userId);

    void deactivateUser(String userId);
}
