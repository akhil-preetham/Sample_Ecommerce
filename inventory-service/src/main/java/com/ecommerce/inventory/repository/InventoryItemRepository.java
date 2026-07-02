package com.ecommerce.inventory.repository;

import com.ecommerce.common.repository.BaseRepository;
import com.ecommerce.inventory.entity.InventoryItem;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryItemRepository extends BaseRepository<InventoryItem, String> {

    Optional<InventoryItem> findByProductVariantIdAndWarehouseId(String productVariantId, String warehouseId);

    List<InventoryItem> findByProductVariantId(String productVariantId);

    List<InventoryItem> findByAvailableStockLessThanEqual(Long reorderLevel);

    List<InventoryItem> findByProductVariantIdAndReservedStockGreaterThan(String productVariantId, Long reserved);
}
