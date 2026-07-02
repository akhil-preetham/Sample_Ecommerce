package com.ecommerce.inventory.service;

import com.ecommerce.inventory.dto.StockMovementDTO;

import java.util.List;

public interface StockMovementService {

    void logMovement(String inventoryItemId, String movementType, Long quantity, String reference, String reason);

    List<StockMovementDTO> getMovementHistory(String inventoryItemId);
}
