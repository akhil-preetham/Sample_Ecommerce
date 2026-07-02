package com.ecommerce.order.repository;

import com.ecommerce.common.repository.BaseRepository;
import com.ecommerce.order.entity.WishlistItem;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistItemRepository extends BaseRepository<WishlistItem, String> {
    List<WishlistItem> findByWishlistId(String wishlistId);
    Optional<WishlistItem> findByWishlistIdAndProductVariantId(String wishlistId, String productVariantId);
    @Transactional
    void deleteByWishlistId(String wishlistId);
}
