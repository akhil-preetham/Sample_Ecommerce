package com.ecommerce.inventory.service.impl;

import com.ecommerce.inventory.dto.WarehouseDTO;
import com.ecommerce.inventory.entity.Warehouse;
import com.ecommerce.inventory.exception.ItemNotFoundException;
import com.ecommerce.inventory.repository.WarehouseRepository;
import com.ecommerce.inventory.service.WarehouseService;
import com.ecommerce.common.util.UUIDUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository warehouseRepository;

    @Override
    @Transactional
    public WarehouseDTO createWarehouse(WarehouseDTO warehouseDTO) {
        log.debug("Creating warehouse: {}", warehouseDTO.getName());

        Warehouse warehouse = Warehouse.builder()
            .id(UUIDUtil.generateUUID())
            .name(warehouseDTO.getName())
            .location(warehouseDTO.getLocation())
            .isActive(true)
            .build();

        Warehouse saved = warehouseRepository.save(warehouse);
        
        log.info("Warehouse created successfully - ID: {}", saved.getId());
        
        return mapToDTO(saved);
    }

    @Override
    @Transactional
    public WarehouseDTO updateWarehouse(String id, WarehouseDTO warehouseDTO) {
        log.debug("Updating warehouse: {}", id);

        Warehouse warehouse = warehouseRepository.findById(id)
            .orElseThrow(() -> new ItemNotFoundException("Warehouse not found: " + id));

        warehouse.setName(warehouseDTO.getName());
        warehouse.setLocation(warehouseDTO.getLocation());
        
        if (warehouseDTO.getIsActive() != null) {
            warehouse.setIsActive(warehouseDTO.getIsActive());
        }

        Warehouse updated = warehouseRepository.save(warehouse);
        
        log.info("Warehouse updated successfully - ID: {}", updated.getId());
        
        return mapToDTO(updated);
    }

    @Override
    @Transactional
    public void deleteWarehouse(String id) {
        log.debug("Deleting warehouse: {}", id);

        Warehouse warehouse = warehouseRepository.findById(id)
            .orElseThrow(() -> new ItemNotFoundException("Warehouse not found: " + id));

        warehouseRepository.delete(warehouse);
        
        log.info("Warehouse deleted successfully - ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public WarehouseDTO getWarehouseById(String id) {
        log.debug("Getting warehouse: {}", id);

        Warehouse warehouse = warehouseRepository.findById(id)
            .orElseThrow(() -> new ItemNotFoundException("Warehouse not found: " + id));

        return mapToDTO(warehouse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WarehouseDTO> getAllWarehouses() {
        log.debug("Getting all warehouses");

        return warehouseRepository.findAll().stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    private WarehouseDTO mapToDTO(Warehouse warehouse) {
        return WarehouseDTO.builder()
            .id(warehouse.getId())
            .name(warehouse.getName())
            .location(warehouse.getLocation())
            .isActive(warehouse.getIsActive())
            .createdAt(warehouse.getCreatedAt())
            .updatedAt(warehouse.getUpdatedAt())
            .build();
    }
}
