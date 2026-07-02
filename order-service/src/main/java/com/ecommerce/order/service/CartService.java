package com.ecommerce.order.service;

import com.ecommerce.order.dto.AddToCartRequest;
import com.ecommerce.order.dto.CartDTO;
import com.ecommerce.order.dto.UpdateCartItemRequest;

public interface CartService {
    CartDTO getCart(String userId);
    CartDTO addToCart(String userId, AddToCartRequest request);
    CartDTO updateCartItem(String userId, String cartItemId, UpdateCartItemRequest request);
    void removeCartItem(String userId, String cartItemId);
    void clearCart(String userId);
}
