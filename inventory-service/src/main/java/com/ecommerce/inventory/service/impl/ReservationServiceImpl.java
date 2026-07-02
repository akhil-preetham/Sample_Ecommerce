package com.ecommerce.inventory.service.impl;

import com.ecommerce.inventory.dto.StockReservationDTO;
import com.ecommerce.inventory.entity.StockReservation;
import com.ecommerce.inventory.exception.ItemNotFoundException;
import com.ecommerce.inventory.repository.StockReservationRepository;
import com.ecommerce.inventory.service.ReservationService;
import com.ecommerce.common.util.UUIDUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final StockReservationRepository stockReservationRepository;

    @Override
    @Transactional
    public void createReservation(String orderId, String inventoryItemId, Long quantity) {
        log.debug("Creating reservation - Order: {} Item: {} Quantity: {}", 
            orderId, inventoryItemId, quantity);

        StockReservation reservation = StockReservation.builder()
            .id(UUIDUtil.generateUUID())
            .orderId(orderId)
            .inventoryItemId(inventoryItemId)
            .quantity(quantity)
            .status("RESERVED")
            .build();

        stockReservationRepository.save(reservation);
        
        log.debug("Reservation created successfully - ID: {}", reservation.getId());
    }

    @Override
    @Transactional
    public void releaseReservation(String orderId, String inventoryItemId, Long quantity) {
        log.debug("Releasing reservation - Order: {} Item: {}", orderId, inventoryItemId);

        var reservations = stockReservationRepository.findByOrderIdAndStatus(orderId, "RESERVED");
        
        var reservation = reservations.stream()
            .filter(r -> r.getInventoryItemId().equals(inventoryItemId))
            .findFirst()
            .orElseThrow(() -> new ItemNotFoundException("Reservation not found"));

        if (reservation.getQuantity().equals(quantity)) {
            reservation.setStatus("RELEASED");
            stockReservationRepository.save(reservation);
        } else {
            stockReservationRepository.delete(reservation);
            
            StockReservation remaining = StockReservation.builder()
                .id(UUIDUtil.generateUUID())
                .orderId(orderId)
                .inventoryItemId(inventoryItemId)
                .quantity(reservation.getQuantity() - quantity)
                .status("RESERVED")
                .build();
            
            stockReservationRepository.save(remaining);
        }
        
        log.debug("Reservation released successfully - Order: {}", orderId);
    }

    @Override
    @Transactional
    public void confirmReservation(String orderId) {
        log.debug("Confirming reservation for order: {}", orderId);
        
        var reservations = stockReservationRepository.findByOrderIdAndStatus(orderId, "RESERVED");
        
        for (StockReservation reservation : reservations) {
            reservation.setStatus("CONFIRMED");
            stockReservationRepository.save(reservation);
        }
        
        log.debug("Reservation confirmed - Order: {}", orderId);
    }

    @Override
    @Transactional
    public void cancelReservation(String orderId) {
        log.debug("Cancelling reservation for order: {}", orderId);
        
        var reservations = stockReservationRepository.findByOrderId(orderId);
        
        for (StockReservation reservation : reservations) {
            if ("RESERVED".equals(reservation.getStatus())) {
                reservation.setStatus("CANCELLED");
                stockReservationRepository.save(reservation);
            }
        }
        
        log.debug("Reservation cancelled - Order: {}", orderId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockReservationDTO> getReservationsByOrder(String orderId) {
        log.debug("Getting reservations for order: {}", orderId);
        return stockReservationRepository.findByOrderId(orderId).stream()
            .map(r -> StockReservationDTO.builder()
                .id(r.getId())
                .inventoryItemId(r.getInventoryItemId())
                .quantity(r.getQuantity())
                .status(r.getStatus())
                .build())
            .toList();
    }
}
