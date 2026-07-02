package com.ecommerce.inventory.service.impl;

import com.ecommerce.inventory.dto.LowStockItemDTO;
import com.ecommerce.inventory.entity.InventoryItem;
import com.ecommerce.inventory.repository.InventoryItemRepository;
import com.ecommerce.inventory.service.ReportingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportingServiceImpl implements ReportingService {

    private final InventoryItemRepository inventoryItemRepository;

    @Override
    @Transactional(readOnly = true)
    public List<LowStockItemDTO> getLowStockItems() {
        log.debug("Fetching low stock items");
        return inventoryItemRepository.findByAvailableStockLessThanEqual(10L)
            .stream().map(this::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LowStockItemDTO> getLowStockItemsByWarehouse(String warehouseId) {
        log.debug("Fetching low stock items for warehouse: {}", warehouseId);
        return inventoryItemRepository
            .findByAvailableStockLessThanEqualAndWarehouseId(10L, warehouseId)
            .stream().map(this::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public void checkAndAlertLowStock() {
        List<InventoryItem> lowStockItems = inventoryItemRepository.findByAvailableStockLessThanEqual(10L);
        if (!lowStockItems.isEmpty()) {
            log.warn("Found {} items with low stock", lowStockItems.size());
            lowStockItems.forEach(item ->
                log.warn("Low stock alert - Item: {} Available: {} Reorder Level: {}",
                    item.getProductVariantId(), item.getAvailableStock(), item.getReorderLevel())
            );
        }
    }

    private LowStockItemDTO toDTO(InventoryItem item) {
        return LowStockItemDTO.builder()
            .id(item.getId())
            .productVariantId(item.getProductVariantId())
            .warehouseId(item.getWarehouseId())
            .availableStock(item.getAvailableStock())
            .reorderLevel(item.getReorderLevel())
            .reservedStock(item.getReservedStock())
            .build();
    }
}
