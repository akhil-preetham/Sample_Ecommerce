-- Inventory Service Schema Initialization
-- Created: Phase 5

CREATE TABLE IF NOT EXISTS warehouses (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    location VARCHAR(255) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(36),
    updated_by VARCHAR(36),
    INDEX idx_name (name),
    INDEX idx_is_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS inventory_items (
    id VARCHAR(36) PRIMARY KEY,
    product_variant_id VARCHAR(36) NOT NULL,
    warehouse_id VARCHAR(36) NOT NULL,
    available_stock BIGINT NOT NULL DEFAULT 0,
    reserved_stock BIGINT NOT NULL DEFAULT 0,
    reorder_level BIGINT DEFAULT 10,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(36),
    updated_by VARCHAR(36),
    FOREIGN KEY (warehouse_id) REFERENCES warehouses(id),
    INDEX idx_product_variant_id (product_variant_id),
    INDEX idx_warehouse_id (warehouse_id),
    INDEX idx_available_stock (available_stock),
    UNIQUE KEY unique_variant_warehouse (product_variant_id, warehouse_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS stock_movements (
    id VARCHAR(36) PRIMARY KEY,
    inventory_item_id VARCHAR(36) NOT NULL,
    movement_type VARCHAR(50) NOT NULL,
    quantity BIGINT NOT NULL,
    reference TEXT,
    reason VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (inventory_item_id) REFERENCES inventory_items(id) ON DELETE CASCADE,
    INDEX idx_inventory_item_id (inventory_item_id),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS stock_reservations (
    id VARCHAR(36) PRIMARY KEY,
    order_id VARCHAR(36) NOT NULL,
    inventory_item_id VARCHAR(36) NOT NULL,
    quantity BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(36),
    updated_by VARCHAR(36),
    FOREIGN KEY (inventory_item_id) REFERENCES inventory_items(id),
    INDEX idx_order_id (order_id),
    INDEX idx_inventory_item_id (inventory_item_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insert default warehouse
INSERT INTO warehouses (id, name, location, is_active) 
VALUES ('default-warehouse', 'Default Warehouse', 'Default Location', true)
ON DUPLICATE KEY UPDATE name='Default Warehouse';
