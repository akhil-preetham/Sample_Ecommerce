INSERT INTO categories (id, name, description, is_active)
VALUES
    ('cat-audio', 'Audio', 'Headphones, speakers, and premium listening gear', TRUE),
    ('cat-office', 'Office', 'Workspace essentials for distributed teams', TRUE)
ON DUPLICATE KEY UPDATE
    description = VALUES(description),
    is_active = VALUES(is_active);

INSERT INTO brands (id, name, description, is_active)
VALUES
    ('brand-nimbus', 'Nimbus', 'Performance-first electronics for modern teams', TRUE),
    ('brand-arcwell', 'Arcwell', 'Minimal office gear with durable materials', TRUE)
ON DUPLICATE KEY UPDATE
    description = VALUES(description),
    is_active = VALUES(is_active);

INSERT INTO products (
    id,
    name,
    description,
    category_id,
    brand_id,
    base_price,
    rating,
    review_count,
    is_active,
    sku,
    stock
) VALUES
    (
        'prod-nimbus-headset',
        'Nimbus Pro Headset',
        'Wireless ANC headset tuned for long work sessions and clear calls.',
        'cat-audio',
        'brand-nimbus',
        249.99,
        4.8,
        124,
        TRUE,
        'NIM-HS-PRO',
        45
    ),
    (
        'prod-arcwell-deskpad',
        'Arcwell Desk Pad',
        'Premium vegan leather desk pad with cable management groove.',
        'cat-office',
        'brand-arcwell',
        59.99,
        4.6,
        87,
        TRUE,
        'ARC-DESK-PAD',
        120
    )
ON DUPLICATE KEY UPDATE
    description = VALUES(description),
    base_price = VALUES(base_price),
    rating = VALUES(rating),
    review_count = VALUES(review_count),
    is_active = VALUES(is_active),
    stock = VALUES(stock);

INSERT INTO product_variants (id, product_id, variant_name, price, sku, stock)
VALUES
    (
        'var-headset-graphite',
        'prod-nimbus-headset',
        'Graphite',
        249.99,
        'NIM-HS-PRO-GR',
        25
    ),
    (
        'var-headset-sand',
        'prod-nimbus-headset',
        'Sand',
        249.99,
        'NIM-HS-PRO-SA',
        20
    ),
    (
        'var-deskpad-slate',
        'prod-arcwell-deskpad',
        'Slate',
        59.99,
        'ARC-DESK-PAD-SL',
        70
    )
ON DUPLICATE KEY UPDATE
    price = VALUES(price),
    stock = VALUES(stock);

INSERT INTO product_images (id, product_id, image_url, alt_text, is_default, display_order)
VALUES
    (
        'img-headset-hero',
        'prod-nimbus-headset',
        'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?auto=format&fit=crop&w=1200&q=80',
        'Nimbus Pro Headset hero image',
        TRUE,
        0
    ),
    (
        'img-deskpad-hero',
        'prod-arcwell-deskpad',
        'https://images.unsplash.com/photo-1516387938699-a93567ec168e?auto=format&fit=crop&w=1200&q=80',
        'Arcwell Desk Pad hero image',
        TRUE,
        0
    )
ON DUPLICATE KEY UPDATE
    image_url = VALUES(image_url),
    alt_text = VALUES(alt_text),
    is_default = VALUES(is_default),
    display_order = VALUES(display_order);
