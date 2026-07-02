package com.ecommerce.order.repository;

import com.ecommerce.common.repository.BaseRepository;
import com.ecommerce.order.entity.OrderTracking;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderTrackingRepository extends BaseRepository<OrderTracking, String> {
    List<OrderTracking> findByOrderIdOrderByTimestampDesc(String orderId);
}
