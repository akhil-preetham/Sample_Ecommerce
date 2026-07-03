INSERT INTO users (
    id,
    email,
    password_hash,
    first_name,
    last_name,
    phone,
    is_active,
    email_verified
) VALUES
    (
        'demo-user-001',
        'demo@enterprise.local',
        '$2a$12$sCxR7v0S14TYtT403J8V7er4J7tDw/9vyhGQ2vBgm19GaxXmfiWYK',
        'Demo',
        'Customer',
        '+1-555-0100',
        TRUE,
        TRUE
    ),
    (
        'admin-user-001',
        'admin@enterprise.local',
        '$2a$12$sCxR7v0S14TYtT403J8V7er4J7tDw/9vyhGQ2vBgm19GaxXmfiWYK',
        'Platform',
        'Administrator',
        '+1-555-0101',
        TRUE,
        TRUE
    )
ON DUPLICATE KEY UPDATE
    first_name = VALUES(first_name),
    last_name = VALUES(last_name),
    phone = VALUES(phone),
    is_active = VALUES(is_active),
    email_verified = VALUES(email_verified);

INSERT INTO user_roles (user_id, role) VALUES
    ('demo-user-001', 'CUSTOMER'),
    ('admin-user-001', 'ADMINISTRATOR')
ON DUPLICATE KEY UPDATE
    role = VALUES(role);
