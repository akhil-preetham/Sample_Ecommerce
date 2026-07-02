package com.ecommerce.inventory.service.impl;

import com.ecommerce.inventory.dto.StockMovementDTO;
import com.ecommerce.inventory.entity.StockMovement;
import com.ecommerce.inventory.repository.StockMovementRepository;
import com.ecommerce.inventory.service.StockMovementService;
import com.ecommerce.common.util.UUIDUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockMovementServiceImpl implements StockMovementService {

    private final StockMovementRepository stockMovementRepository;

    @Override
    @Transactional
    public void logMovement(String inventoryItemId, String movementType, Long quantity, 
                           String reference, String reason) {
        log.debug("Logging stock movement - Item: {} Type: {} Quantity: {}", 
            inventoryItemId, movementType, quantity);

        StockMovement movement = StockMovement.builder()
            .id(UUIDUtil.generateUUID())
            .inventoryItemId(inventoryItemId)
            .movementType(movementType)
            .quantity(quantity)
            .reference(reference)
            .reason(reason)
            .createdAt(LocalDateTime.now())
            .build();

        stockMovementRepository.save(movement);
        
        log.debug("Stock movement logged successfully - ID: {}", movement.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockMovementDTO> getMovementHistory(String inventoryItemId) {
        log.debug("Getting movement history for item: {}", inventoryItemId);
        return stockMovementRepository.findByInventoryItemId(inventoryItemId).stream()
            .map(m -> StockMovementDTO.builder()
                .id(m.getId())
                .movementType(m.getMovementType())
                .quantity(m.getQuantity())
                .reference(m.getReference())
                .reason(m.getReason())
                .createdAt(m.getCreatedAt())
                .build())
            .toList();
    }
}
