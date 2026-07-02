package com.ecommerce.order.repository;

import com.ecommerce.common.repository.BaseRepository;
import com.ecommerce.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends BaseRepository<Order, String> {
    Optional<Order> findByOrderNumber(String orderNumber);
    Page<Order> findByUserId(String userId, Pageable pageable);
    Page<Order> findByUserIdAndStatus(String userId, String status, Pageable pageable);
}
