package com.ecommerce.inventory.repository;

import com.ecommerce.inventory.entity.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StockMovementRepository extends JpaRepository<StockMovement, String> {

    List<StockMovement> findByInventoryItemId(String inventoryItemId);

    List<StockMovement> findByMovementType(String movementType);
}
