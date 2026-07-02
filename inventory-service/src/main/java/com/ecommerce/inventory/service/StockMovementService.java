package com.ecommerce.inventory.service;

import java.util.List;

public interface StockMovementService {

    void logMovement(String inventoryItemId, String movementType, Long quantity, String reference, String reason);

    List<Object> getMovementHistory(String inventoryItemId);
}
