package com.ecommerce.inventory.service;

import com.ecommerce.inventory.dto.StockReservationDTO;

import java.util.List;

public interface ReservationService {

    void createReservation(String orderId, String inventoryItemId, Long quantity);

    void releaseReservation(String orderId, String inventoryItemId, Long quantity);

    void confirmReservation(String orderId);

    void cancelReservation(String orderId);

    List<StockReservationDTO> getReservationsByOrder(String orderId);
}
