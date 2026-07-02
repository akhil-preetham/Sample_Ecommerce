package com.ecommerce.inventory.service.impl;

import com.ecommerce.inventory.entity.InventoryItem;
import com.ecommerce.inventory.repository.InventoryItemRepository;
import com.ecommerce.inventory.service.ReportingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportingServiceImpl implements ReportingService {

    private final InventoryItemRepository inventoryItemRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Object> getLowStockItems() {
        log.debug("Fetching low stock items");
        
        List<InventoryItem> lowStockItems = inventoryItemRepository.findByAvailableStockLessThanEqual(10L);
        
        return lowStockItems.stream()
            .map(this::mapToLowStockResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object> getLowStockItemsByWarehouse(String warehouseId) {
        log.debug("Fetching low stock items for warehouse: {}", warehouseId);
        
        List<InventoryItem> allItems = inventoryItemRepository.findByAvailableStockLessThanEqual(10L);
        
        return allItems.stream()
            .filter(item -> item.getWarehouseId().equals(warehouseId))
            .map(this::mapToLowStockResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public void checkAndAlertLowStock() {
        log.info("Checking for low stock items");
        
        List<InventoryItem> lowStockItems = inventoryItemRepository.findByAvailableStockLessThanEqual(10L);
        
        if (!lowStockItems.isEmpty()) {
            log.warn("Found {} items with low stock", lowStockItems.size());
            
            lowStockItems.forEach(item -> 
                log.warn("Low stock alert - Item: {} Available: {} Reorder Level: {}", 
                    item.getProductVariantId(), item.getAvailableStock(), item.getReorderLevel())
            );
        }
    }

    private Object mapToLowStockResponse(InventoryItem item) {
        return new Object() {
            public final String id = item.getId();
            public final String productVariantId = item.getProductVariantId();
            public final String warehouseId = item.getWarehouseId();
            public final Long availableStock = item.getAvailableStock();
            public final Long reorderLevel = item.getReorderLevel();
            public final Long reservedStock = item.getReservedStock();
        };
    }
}
