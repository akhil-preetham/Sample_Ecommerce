-- Notification Service Schema Initialization
-- Created: Phase 5

CREATE TABLE IF NOT EXISTS notifications (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    type VARCHAR(50) NOT NULL,
    title VARCHAR(255) NOT NULL,
    message TEXT,
    is_read BOOLEAN NOT NULL DEFAULT false,
    reference_id VARCHAR(36),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(36),
    updated_by VARCHAR(36),
    INDEX idx_user_id (user_id),
    INDEX idx_is_read (is_read),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS email_templates (
    id VARCHAR(36) PRIMARY KEY,
    template_name VARCHAR(100) NOT NULL UNIQUE,
    subject VARCHAR(255) NOT NULL,
    body LONGTEXT NOT NULL,
    variables TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(36),
    updated_by VARCHAR(36),
    INDEX idx_template_name (template_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS notification_logs (
    id VARCHAR(36) PRIMARY KEY,
    notification_type VARCHAR(50) NOT NULL,
    recipient_email VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    error_message TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_created_at (created_at),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insert default email templates
INSERT INTO email_templates (id, template_name, subject, body, created_by) VALUES
('tpl-welcome', 'WELCOME', 'Welcome to E-Commerce', 'Welcome {{firstName}}!', 'system'),
('tpl-order-confirm', 'ORDER_CONFIRMATION', 'Order Confirmation', 'Your order {{orderNumber}} has been confirmed', 'system'),
('tpl-payment-success', 'PAYMENT_SUCCESS', 'Payment Successful', 'Payment of {{amount}} received', 'system'),
('tpl-order-shipped', 'ORDER_SHIPPED', 'Order Shipped', 'Your order {{orderNumber}} has been shipped', 'system'),
('tpl-password-reset', 'PASSWORD_RESET', 'Password Reset Request', 'Click here to reset your password', 'system')
ON DUPLICATE KEY UPDATE updated_at = CURRENT_TIMESTAMP;
