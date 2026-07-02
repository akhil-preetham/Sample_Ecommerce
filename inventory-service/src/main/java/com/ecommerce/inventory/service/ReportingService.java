package com.ecommerce.inventory.service;

import java.util.List;

public interface ReportingService {

    List<Object> getLowStockItems();

    List<Object> getLowStockItemsByWarehouse(String warehouseId);

    void checkAndAlertLowStock();
}
