package com.ecommerce.inventory.service.impl;

import com.ecommerce.inventory.client.ProductServiceClient;
import com.ecommerce.inventory.dto.*;
import com.ecommerce.inventory.entity.InventoryItem;
import com.ecommerce.inventory.exception.InsufficientStockException;
import com.ecommerce.inventory.exception.ItemNotFoundException;
import com.ecommerce.inventory.repository.InventoryItemRepository;
import com.ecommerce.inventory.service.InventoryService;
import com.ecommerce.inventory.service.ReservationService;
import com.ecommerce.inventory.service.StockMovementService;
import com.ecommerce.common.util.UUIDUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryItemRepository inventoryItemRepository;
    private final ReservationService reservationService;
    private final StockMovementService stockMovementService;
    private final ProductServiceClient productServiceClient;

    @Override
    @Transactional(readOnly = true)
    public boolean checkAvailability(CheckAvailabilityRequest request) {
        log.debug("Checking availability for variant: {} quantity: {}", 
            request.getProductVariantId(), request.getQuantity());

        var inventoryItem = getLatestInventoryItem(request.getProductVariantId(), request.getWarehouseId());
        
        if (inventoryItem == null) {
            log.warn("No inventory item found for variant: {}", request.getProductVariantId());
            return false;
        }

        boolean available = inventoryItem.getAvailableStock() >= request.getQuantity();
        log.debug("Availability result: {} for variant: {}", available, request.getProductVariantId());
        
        return available;
    }

    @Override
    @Transactional
    @Retryable(backoff = @org.springframework.retry.annotation.Backoff(delay = 100, multiplier = 2.0))
    public String reserveStock(ReserveStockRequest request) {
        log.info("Reserving stock - Order: {} Item: {} Quantity: {}", 
            request.getOrderId(), request.getInventoryItemId(), request.getQuantity());

        InventoryItem inventoryItem = inventoryItemRepository.findById(request.getInventoryItemId())
            .orElseThrow(() -> new ItemNotFoundException(
                "Inventory item not found: " + request.getInventoryItemId()));

        if (inventoryItem.getAvailableStock() < request.getQuantity()) {
            throw new InsufficientStockException(
                "Insufficient stock. Available: " + inventoryItem.getAvailableStock() + 
                " Requested: " + request.getQuantity());
        }

        inventoryItem.setAvailableStock(inventoryItem.getAvailableStock() - request.getQuantity());
        inventoryItem.setReservedStock(inventoryItem.getReservedStock() + request.getQuantity());
        
        InventoryItem updated = inventoryItemRepository.save(inventoryItem);
        
        reservationService.createReservation(request.getOrderId(), request.getInventoryItemId(), 
            request.getQuantity());
        
        stockMovementService.logMovement(request.getInventoryItemId(), "RESERVED", 
            request.getQuantity(), request.getOrderId(), "Order reserved");

        log.info("Stock reserved successfully - Reservation ID: {}", request.getOrderId());
        return request.getOrderId();
    }

    @Override
    @Transactional
    @Retryable(backoff = @org.springframework.retry.annotation.Backoff(delay = 100, multiplier = 2.0))
    public void releaseStock(ReleaseStockRequest request) {
        log.info("Releasing stock - Order: {} Item: {} Quantity: {}", 
            request.getOrderId(), request.getInventoryItemId(), request.getQuantity());

        InventoryItem inventoryItem = inventoryItemRepository.findById(request.getInventoryItemId())
            .orElseThrow(() -> new ItemNotFoundException(
                "Inventory item not found: " + request.getInventoryItemId()));

        if (inventoryItem.getReservedStock() < request.getQuantity()) {
            throw new InsufficientStockException(
                "Cannot release more than reserved. Reserved: " + inventoryItem.getReservedStock() + 
                " Requested: " + request.getQuantity());
        }

        inventoryItem.setReservedStock(inventoryItem.getReservedStock() - request.getQuantity());
        inventoryItem.setAvailableStock(inventoryItem.getAvailableStock() + request.getQuantity());
        
        inventoryItemRepository.save(inventoryItem);
        
        reservationService.releaseReservation(request.getOrderId(), request.getInventoryItemId(), 
            request.getQuantity());
        
        stockMovementService.logMovement(request.getInventoryItemId(), "RELEASED", 
            request.getQuantity(), request.getOrderId(), "Order cancelled/released");

        log.info("Stock released successfully - Order: {}", request.getOrderId());
    }

    @Override
    @Transactional(readOnly = true)
    public StockStatusResponse getInventoryStatus(String variantId) {
        log.debug("Getting inventory status for variant: {}", variantId);
        
        var inventoryItem = getLatestInventoryItem(variantId, null);
        
        if (inventoryItem == null) {
            throw new ItemNotFoundException("Inventory item not found for variant: " + variantId);
        }

        return mapToStatusResponse(inventoryItem);
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryItemDTO getInventoryItemById(String id) {
        log.debug("Getting inventory item by ID: {}", id);
        
        InventoryItem inventoryItem = inventoryItemRepository.findById(id)
            .orElseThrow(() -> new ItemNotFoundException("Inventory item not found: " + id));

        return mapToDTO(inventoryItem);
    }

    private InventoryItem getLatestInventoryItem(String productVariantId, String warehouseId) {
        if (warehouseId != null) {
            return inventoryItemRepository.findByProductVariantIdAndWarehouseId(productVariantId, warehouseId)
                .orElse(null);
        }
        
        var items = inventoryItemRepository.findByProductVariantId(productVariantId);
        return items.stream()
            .max((a, b) -> Long.compare(
                a.getAvailableStock() + a.getReservedStock(),
                b.getAvailableStock() + b.getReservedStock()))
            .orElse(null);
    }

    private StockStatusResponse mapToStatusResponse(InventoryItem item) {
        Long totalStock = item.getAvailableStock() + item.getReservedStock();
        Boolean lowStock = item.getAvailableStock() <= item.getReorderLevel();
        
        return StockStatusResponse.builder()
            .inventoryItemId(item.getId())
            .productVariantId(item.getProductVariantId())
            .warehouseId(item.getWarehouseId())
            .availableStock(item.getAvailableStock())
            .reservedStock(item.getReservedStock())
            .totalStock(totalStock)
            .reorderLevel(item.getReorderLevel())
            .lowStock(lowStock)
            .lastUpdated(item.getUpdatedAt())
            .build();
    }

    private InventoryItemDTO mapToDTO(InventoryItem item) {
        Long totalStock = item.getAvailableStock() + item.getReservedStock();
        
        return InventoryItemDTO.builder()
            .id(item.getId())
            .productVariantId(item.getProductVariantId())
            .warehouseId(item.getWarehouseId())
            .availableStock(item.getAvailableStock())
            .reservedStock(item.getReservedStock())
            .reorderLevel(item.getReorderLevel())
            .totalStock(totalStock)
            .createdAt(item.getCreatedAt())
            .updatedAt(item.getUpdatedAt())
            .version(item.getVersion())
            .build();
    }
}
