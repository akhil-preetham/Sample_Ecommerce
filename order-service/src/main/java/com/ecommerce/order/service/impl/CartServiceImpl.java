package com.ecommerce.order.service.impl;

import com.ecommerce.common.exception.ResourceNotFoundException;
import com.ecommerce.common.util.UUIDUtil;
import com.ecommerce.order.dto.AddToCartRequest;
import com.ecommerce.order.dto.CartDTO;
import com.ecommerce.order.dto.CartItemDTO;
import com.ecommerce.order.dto.UpdateCartItemRequest;
import com.ecommerce.order.entity.Cart;
import com.ecommerce.order.entity.CartItem;
import com.ecommerce.order.repository.CartItemRepository;
import com.ecommerce.order.repository.CartRepository;
import com.ecommerce.order.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    @Transactional(readOnly = true)
    public CartDTO getCart(String userId) {
        Cart cart = cartRepository.findByUserId(userId).orElse(null);
        if (cart == null) return emptyCart(userId);
        return buildCartDTO(cart);
    }

    @Override
    public CartDTO addToCart(String userId, AddToCartRequest request) {
        Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> {
            Cart newCart = Cart.builder().id(UUIDUtil.generateUUID()).userId(userId).isGuestCart(false).build();
            return cartRepository.save(newCart);
        });

        cartItemRepository.findByCartIdAndProductVariantId(cart.getId(), request.getProductVariantId())
            .ifPresentOrElse(existing -> {
                existing.setQuantity(existing.getQuantity() + request.getQuantity());
                cartItemRepository.save(existing);
            }, () -> {
                CartItem item = CartItem.builder()
                    .id(UUIDUtil.generateUUID())
                    .cartId(cart.getId())
                    .productVariantId(request.getProductVariantId())
                    .quantity(request.getQuantity())
                    .price(request.getPrice())
                    .build();
                cartItemRepository.save(item);
            });

        return buildCartDTO(cart);
    }

    @Override
    public CartDTO updateCartItem(String userId, String cartItemId, UpdateCartItemRequest request) {
        Cart cart = cartRepository.findByUserId(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        CartItem item = cartItemRepository.findById(cartItemId)
            .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));
        item.setQuantity(request.getQuantity());
        cartItemRepository.save(item);
        return buildCartDTO(cart);
    }

    @Override
    public void removeCartItem(String userId, String cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    @Override
    public void clearCart(String userId) {
        cartRepository.findByUserId(userId).ifPresent(cart -> cartItemRepository.deleteByCartId(cart.getId()));
    }

    private CartDTO buildCartDTO(Cart cart) {
        List<CartItem> items = cartItemRepository.findByCartId(cart.getId());
        List<CartItemDTO> itemDTOs = items.stream().map(i -> CartItemDTO.builder()
            .id(i.getId())
            .productVariantId(i.getProductVariantId())
            .quantity(i.getQuantity())
            .price(i.getPrice())
            .subtotal(i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
            .build()).toList();

        BigDecimal subtotal = itemDTOs.stream()
            .map(CartItemDTO::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        return CartDTO.builder()
            .id(cart.getId())
            .userId(cart.getUserId())
            .items(itemDTOs)
            .subtotal(subtotal)
            .tax(BigDecimal.ZERO)
            .shipping(BigDecimal.ZERO)
            .total(subtotal)
            .isGuestCart(cart.getIsGuestCart())
            .build();
    }

    private CartDTO emptyCart(String userId) {
        return CartDTO.builder().userId(userId).items(List.of())
            .subtotal(BigDecimal.ZERO).tax(BigDecimal.ZERO)
            .shipping(BigDecimal.ZERO).total(BigDecimal.ZERO).build();
    }
}
