INSERT INTO inventory_items (
    id,
    product_variant_id,
    warehouse_id,
    available_stock,
    reserved_stock,
    reorder_level
) VALUES
    (
        'inv-headset-graphite',
        'var-headset-graphite',
        'default-warehouse',
        25,
        0,
        5
    ),
    (
        'inv-headset-sand',
        'var-headset-sand',
        'default-warehouse',
        20,
        0,
        5
    ),
    (
        'inv-deskpad-slate',
        'var-deskpad-slate',
        'default-warehouse',
        70,
        0,
        10
    )
ON DUPLICATE KEY UPDATE
    available_stock = VALUES(available_stock),
    reserved_stock = VALUES(reserved_stock),
    reorder_level = VALUES(reorder_level);
