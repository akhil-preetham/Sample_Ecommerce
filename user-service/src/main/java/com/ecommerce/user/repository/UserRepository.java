package com.ecommerce.user.repository;

import com.ecommerce.common.repository.BaseRepository;
import com.ecommerce.user.entity.User;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<User, String> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> findByIdAndIsActiveTrue(String id);
}
