package com.ecommerce.inventory.service;

import com.ecommerce.inventory.dto.WarehouseDTO;
import java.util.List;

public interface WarehouseService {

    WarehouseDTO createWarehouse(WarehouseDTO warehouseDTO);

    WarehouseDTO updateWarehouse(String id, WarehouseDTO warehouseDTO);

    void deleteWarehouse(String id);

    WarehouseDTO getWarehouseById(String id);

    List<WarehouseDTO> getAllWarehouses();
}
