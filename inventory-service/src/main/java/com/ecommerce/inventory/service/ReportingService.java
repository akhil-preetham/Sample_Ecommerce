package com.ecommerce.inventory.service;

import com.ecommerce.inventory.dto.LowStockItemDTO;

import java.util.List;

public interface ReportingService {

    List<LowStockItemDTO> getLowStockItems();

    List<LowStockItemDTO> getLowStockItemsByWarehouse(String warehouseId);

    void checkAndAlertLowStock();
}
