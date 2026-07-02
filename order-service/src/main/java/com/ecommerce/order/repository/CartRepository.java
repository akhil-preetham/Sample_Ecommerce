package com.ecommerce.order.repository;

import com.ecommerce.common.repository.BaseRepository;
import com.ecommerce.order.entity.Cart;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends BaseRepository<Cart, String> {
    Optional<Cart> findByUserId(String userId);
    Optional<Cart> findByUserIdAndIsGuestCart(String userId, Boolean isGuestCart);
}
