package com.ecommerce.order.repository;

import com.ecommerce.common.repository.BaseRepository;
import com.ecommerce.order.entity.CartItem;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends BaseRepository<CartItem, String> {
    List<CartItem> findByCartId(String cartId);
    Optional<CartItem> findByCartIdAndProductVariantId(String cartId, String productVariantId);
    void deleteByCartId(String cartId);
}
