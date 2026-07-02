package com.ecommerce.order.repository;

import com.ecommerce.common.repository.BaseRepository;
import com.ecommerce.order.entity.OrderItem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends BaseRepository<OrderItem, String> {
    List<OrderItem> findByOrderId(String orderId);
}
