-- Add version column for optimistic locking to inventory_items
-- Phase 5 - Inventory Service Enhancement

ALTER TABLE inventory_items 
ADD COLUMN version BIGINT NOT NULL DEFAULT 0 AFTER updated_by;
