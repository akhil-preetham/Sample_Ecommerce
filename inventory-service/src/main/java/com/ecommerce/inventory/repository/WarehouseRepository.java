package com.ecommerce.inventory.repository;

import com.ecommerce.common.repository.BaseRepository;
import com.ecommerce.inventory.entity.Warehouse;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehouseRepository extends BaseRepository<Warehouse, String> {
}
