-- ============================
-- ORDER TRACKING & SHIPMENT TABLES
-- ============================
-- Migration for Order History & Tracking System
-- Created: January 27, 2026
-- ============================
-- 1. ORDER_STATUS_HISTORY TABLE
-- ============================
-- Tracks all status changes for orders (audit trail)
DROP TABLE IF EXISTS `order_status_history`;
CREATE TABLE `order_status_history` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `order_id` BIGINT NOT NULL,
    `previous_status` VARCHAR(50) NOT NULL,
    `new_status` VARCHAR(50) NOT NULL,
    `notes` TEXT,
    `location` TEXT,
    `changed_by_user_id` BIGINT,
    `changed_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`order_id`) REFERENCES `orders`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`changed_by_user_id`) REFERENCES `users`(`id`) ON DELETE
    SET NULL,
        INDEX `idx_order_history` (`order_id`),
        INDEX `idx_status_timestamp` (`changed_at`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;
-- ============================
-- 2. SHIPMENTS TABLE
-- ============================
-- Tracks shipment details for orders
DROP TABLE IF EXISTS `shipments`;
CREATE TABLE `shipments` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `order_id` BIGINT NOT NULL UNIQUE,
    `tracking_number` VARCHAR(255) UNIQUE,
    `carrier` VARCHAR(100) NOT NULL COMMENT 'Shipping carrier (DHL, FedEx, UPS, etc.)',
    `status` VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    `tracking_url` TEXT COMMENT 'URL to track shipment',
    `current_location` TEXT,
    `estimated_delivery_date` TIMESTAMP NULL,
    `shipped_at` TIMESTAMP NULL,
    `delivered_at` TIMESTAMP NULL,
    `delivery_notes` TEXT,
    `recipient_name` VARCHAR(255),
    `recipient_phone` VARCHAR(50),
    `delivery_signature` TEXT COMMENT 'Signature image URL or data',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`order_id`) REFERENCES `orders`(`id`) ON DELETE CASCADE,
    INDEX `idx_tracking_number` (`tracking_number`),
    INDEX `idx_order_shipment` (`order_id`),
    INDEX `idx_shipment_status` (`status`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;
-- ============================
-- 3. TRACKING_EVENTS TABLE
-- ============================
-- Individual tracking events for shipments (detailed timeline)
DROP TABLE IF EXISTS `tracking_events`;
CREATE TABLE `tracking_events` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `shipment_id` BIGINT NOT NULL,
    `event_type` VARCHAR(50) NOT NULL COMMENT 'Type of tracking event',
    `description` TEXT NOT NULL COMMENT 'Event description',
    `location` TEXT COMMENT 'Location where event occurred',
    `event_timestamp` TIMESTAMP NOT NULL COMMENT 'When the event occurred',
    `notes` TEXT,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`shipment_id`) REFERENCES `shipments`(`id`) ON DELETE CASCADE,
    INDEX `idx_shipment_events` (`shipment_id`),
    INDEX `idx_event_timestamp` (`event_timestamp`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;
-- ============================
-- SAMPLE DATA FOR TESTING
-- ============================
-- Note: Replace user_id and order_id with actual values from your database
-- Example: Order Status History
-- INSERT INTO `order_status_history` (`order_id`, `previous_status`, `new_status`, `notes`, `changed_by_user_id`, `changed_at`)
-- VALUES 
--     (1, 'PENDING', 'PROCESSING', 'Order confirmed and being prepared', 1, NOW()),
--     (1, 'PROCESSING', 'SHIPPED', 'Package handed over to carrier', 1, NOW());
-- Example: Shipment
-- INSERT INTO `shipments` (`order_id`, `tracking_number`, `carrier`, `status`, `tracking_url`, `estimated_delivery_date`)
-- VALUES 
--     (1, 'DHL1234567890', 'DHL Express', 'IN_TRANSIT', 'https://www.dhl.com/track?id=DHL1234567890', DATE_ADD(NOW(), INTERVAL 3 DAY));
-- Example: Tracking Events
-- INSERT INTO `tracking_events` (`shipment_id`, `event_type`, `description`, `location`, `event_timestamp`)
-- VALUES 
--     (1, 'SHIPMENT_CREATED', 'Shipment label created', 'Warehouse - New York', NOW()),
--     (1, 'PICKED_UP', 'Package picked up by carrier', 'Warehouse - New York', NOW()),
--     (1, 'IN_TRANSIT', 'Package in transit to sorting facility', 'Transit Hub - Chicago', NOW());
-- ============================
-- INDEXES FOR PERFORMANCE
-- ============================
-- Additional indexes already created above
-- ============================
-- NOTES
-- ============================
-- 1. Shipment Status Values: PENDING, PICKED_UP, IN_TRANSIT, OUT_FOR_DELIVERY, DELIVERED, DELIVERY_FAILED, RETURNED, CANCELLED
-- 2. Tracking Event Types: ORDER_PLACED, ORDER_CONFIRMED, PAYMENT_RECEIVED, PREPARING_SHIPMENT, SHIPMENT_CREATED, PICKED_UP, IN_TRANSIT, ARRIVED_AT_HUB, DEPARTED_FROM_HUB, CUSTOMS_CLEARANCE, OUT_FOR_DELIVERY, DELIVERY_ATTEMPTED, DELIVERED, EXCEPTION, RETURNED_TO_SENDER, CANCELLED
-- 3. All tables use CASCADE delete for order references to maintain referential integrity
-- 4. Timestamps are automatically managed by MySQL triggers (created_at, updated_at)