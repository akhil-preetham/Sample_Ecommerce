package com.ecommerce.inventory.service;

import java.util.List;

public interface ReservationService {

    void createReservation(String orderId, String inventoryItemId, Long quantity);

    void releaseReservation(String orderId, String inventoryItemId, Long quantity);

    void confirmReservation(String orderId);

    void cancelReservation(String orderId);

    List<Object> getReservationsByOrder(String orderId);
}
