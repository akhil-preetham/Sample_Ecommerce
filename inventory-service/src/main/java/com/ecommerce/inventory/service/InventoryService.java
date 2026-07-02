package com.ecommerce.inventory.service;

import com.ecommerce.inventory.dto.CheckAvailabilityRequest;
import com.ecommerce.inventory.dto.ReserveStockRequest;
import com.ecommerce.inventory.dto.ReleaseStockRequest;
import com.ecommerce.inventory.dto.InventoryItemDTO;
import com.ecommerce.inventory.dto.StockStatusResponse;

public interface InventoryService {

    boolean checkAvailability(CheckAvailabilityRequest request);

    String reserveStock(ReserveStockRequest request);

    void releaseStock(ReleaseStockRequest request);

    StockStatusResponse getInventoryStatus(String variantId);

    InventoryItemDTO getInventoryItemById(String id);
}
