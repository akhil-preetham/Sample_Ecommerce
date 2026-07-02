package com.ecommerce.order.repository;

import com.ecommerce.common.repository.BaseRepository;
import com.ecommerce.order.entity.Wishlist;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WishlistRepository extends BaseRepository<Wishlist, String> {
    Optional<Wishlist> findByUserId(String userId);
}
