package com.ecommerce.inventory.service.impl;

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
import java.util.stream.Collectors;

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
    public List<Object> getMovementHistory(String inventoryItemId) {
        log.debug("Getting movement history for item: {}", inventoryItemId);
        
        List<StockMovement> movements = stockMovementRepository.findByInventoryItemId(inventoryItemId);
        
        return movements.stream()
            .map(m -> new Object() {
                public final String id = m.getId();
                public final String movementType = m.getMovementType();
                public final Long quantity = m.getQuantity();
                public final String reference = m.getReference();
                public final String reason = m.getReason();
                public final LocalDateTime createdAt = m.getCreatedAt();
            })
            .collect(Collectors.toList());
    }
}
