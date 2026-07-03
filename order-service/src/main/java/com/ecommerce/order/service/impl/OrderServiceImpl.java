package com.ecommerce.order.service.impl;

import com.ecommerce.common.dto.PaginationResponse;
import com.ecommerce.common.exception.ResourceNotFoundException;
import com.ecommerce.common.util.UUIDUtil;
import com.ecommerce.order.dto.CheckoutRequest;
import com.ecommerce.order.dto.CheckoutResponse;
import com.ecommerce.order.dto.OrderDTO;
import com.ecommerce.order.dto.OrderItemDTO;
import com.ecommerce.order.entity.Cart;
import com.ecommerce.order.entity.CartItem;
import com.ecommerce.order.entity.Order;
import com.ecommerce.order.entity.OrderItem;
import com.ecommerce.order.entity.OrderTracking;
import com.ecommerce.order.exception.CartEmptyException;
import com.ecommerce.order.exception.InvalidOrderStateException;
import com.ecommerce.order.repository.CartItemRepository;
import com.ecommerce.order.repository.CartRepository;
import com.ecommerce.order.repository.OrderItemRepository;
import com.ecommerce.order.repository.OrderRepository;
import com.ecommerce.order.repository.OrderTrackingRepository;
import com.ecommerce.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderTrackingRepository orderTrackingRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public CheckoutResponse checkout(String userId, CheckoutRequest request) {
        log.info("Processing checkout for user: {}", userId);

        Cart cart = cartRepository.findByUserId(userId)
            .orElseThrow(() -> new CartEmptyException("Cart not found for user: " + userId));

        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());
        if (cartItems.isEmpty()) throw new CartEmptyException("Cart is empty");

        BigDecimal subtotal = cartItems.stream()
            .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal shipping = request.getShippingCost() != null ? request.getShippingCost() : BigDecimal.ZERO;
        BigDecimal tax = subtotal.multiply(new BigDecimal("0.18"));
        BigDecimal total = subtotal.add(tax).add(shipping);

        String orderId = UUIDUtil.generateUUID();
        String orderNumber = "ORD-" + System.currentTimeMillis();

        Order order = Order.builder()
            .id(orderId)
            .userId(userId)
            .orderNumber(orderNumber)
            .status("CONFIRMED")
            .totalAmount(total)
            .tax(tax)
            .shipping(shipping)
            .discount(BigDecimal.ZERO)
            .notes(request.getNotes())
            .build();
        orderRepository.save(order);

        cartItems.forEach(cartItem -> {
            OrderItem orderItem = OrderItem.builder()
                .id(UUIDUtil.generateUUID())
                .orderId(orderId)
                .productVariantId(cartItem.getProductVariantId())
                .quantity(cartItem.getQuantity())
                .price(cartItem.getPrice())
                .build();
            orderItemRepository.save(orderItem);
        });

        OrderTracking tracking = OrderTracking.builder()
            .id(UUIDUtil.generateUUID())
            .orderId(orderId)
            .status("CONFIRMED")
            .timestamp(LocalDateTime.now())
            .notes("Order placed successfully")
            .build();
        orderTrackingRepository.save(tracking);

        cartItemRepository.deleteByCartId(cart.getId());

        return CheckoutResponse.builder()
            .orderId(orderId)
            .orderNumber(orderNumber)
            .status("CONFIRMED")
            .order(toOrderDTO(order, cartItems))
            .build();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDTO getOrder(String orderId, String userId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));
        List<CartItem> empty = List.of();
        return toOrderDTO(order, empty);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginationResponse<OrderDTO> getUserOrders(String userId, int page, int size) {
        Page<Order> orders = orderRepository.findByUserId(userId, PageRequest.of(page, size));
        return PaginationResponse.<OrderDTO>builder()
            .content(orders.getContent().stream().map(o -> toOrderDTO(o, List.of())).toList())
            .pageNumber(orders.getNumber())
            .pageSize(orders.getSize())
            .totalElements(orders.getTotalElements())
            .totalPages(orders.getTotalPages())
            .hasNext(orders.hasNext())
            .hasPrevious(orders.hasPrevious())
            .build();
    }

    @Override
    public OrderDTO cancelOrder(String orderId, String userId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));
        if ("SHIPPED".equals(order.getStatus()) || "DELIVERED".equals(order.getStatus())) {
            throw new InvalidOrderStateException("Cannot cancel order in status: " + order.getStatus());
        }
        order.setStatus("CANCELLED");
        orderRepository.save(order);
        return toOrderDTO(order, List.of());
    }

    private OrderDTO toOrderDTO(Order order, List<CartItem> cartItems) {
        List<OrderItemDTO> items = cartItems.stream().map(i -> OrderItemDTO.builder()
            .productVariantId(i.getProductVariantId())
            .quantity(i.getQuantity())
            .price(i.getPrice())
            .subtotal(i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
            .build()).toList();

        return OrderDTO.builder()
            .id(order.getId())
            .orderNumber(order.getOrderNumber())
            .userId(order.getUserId())
            .status(order.getStatus())
            .items(items)
            .tax(order.getTax())
            .shipping(order.getShipping())
            .discount(order.getDiscount())
            .totalAmount(order.getTotalAmount())
            .notes(order.getNotes())
            .createdAt(order.getCreatedAt())
            .updatedAt(order.getUpdatedAt())
            .build();
    }
}
