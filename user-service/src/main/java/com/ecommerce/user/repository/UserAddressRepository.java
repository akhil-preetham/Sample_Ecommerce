package com.ecommerce.user.repository;

import com.ecommerce.common.repository.BaseRepository;
import com.ecommerce.user.entity.UserAddress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserAddressRepository extends BaseRepository<UserAddress, String> {

    Page<UserAddress> findByUserId(String userId, Pageable pageable);

    Optional<UserAddress> findByIdAndUserId(String id, String userId);

    Optional<UserAddress> findByUserIdAndIsDefaultTrue(String userId);
}
