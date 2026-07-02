package com.ecommerce.user.repository;

import com.ecommerce.user.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

    Optional<RefreshToken> findByTokenHash(String tokenHash);

    void deleteByUserId(String userId);

    void deleteByIsRevokedTrue();
}
