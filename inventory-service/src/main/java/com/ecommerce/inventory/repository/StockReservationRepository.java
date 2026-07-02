package com.ecommerce.inventory.repository;

import com.ecommerce.common.repository.BaseRepository;
import com.ecommerce.inventory.entity.StockReservation;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StockReservationRepository extends BaseRepository<StockReservation, String> {

    List<StockReservation> findByOrderId(String orderId);

    List<StockReservation> findByOrderIdAndStatus(String orderId, String status);

    List<StockReservation> findByStatus(String status);
}
