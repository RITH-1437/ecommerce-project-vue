-- ============================
-- ALL TABLES QUERIES - Apple Store Database
-- ============================
-- Date: December 25, 2025
-- Database: appl_store
-- Purpose: Complete database structure and sample data queries

-- ============================
-- 1. USERS TABLE
-- ============================
-- Users table with authentication and role management
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `email` VARCHAR(255) NOT NULL UNIQUE,
    `password_hash` VARCHAR(255) NOT NULL,
    `role` VARCHAR(50) DEFAULT 'CUSTOMER',
    `is_active` BOOLEAN DEFAULT TRUE,
    `email_verified` BOOLEAN DEFAULT FALSE,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX `idx_users_email` (`email`),
    INDEX `idx_users_role` (`role`)
);

-- Insert seed accounts
INSERT INTO `users` (`email`, `password_hash`, `role`, `is_active`, `email_verified`, `created_at`, `updated_at`)
VALUES
  ('admin@applestore.com', '$2a$10$9gd.BcPRbAY3..QqY/s3gu9srB0nxj61knMFKsccBBcVNgVp7.zyu', 'ADMIN', TRUE, TRUE, NOW(), NOW()),
  ('user@example.com', '$2a$10$WRfl0GzrVQgkMTT1mnhjmOZ4n0OHSjvTPnXeQUXUk1ndSmV6CT.0m', 'CUSTOMER', TRUE, TRUE, NOW(), NOW())
ON DUPLICATE KEY UPDATE
  `password_hash` = VALUES(`password_hash`),
  `role` = VALUES(`role`),
  `is_active` = VALUES(`is_active`),
  `email_verified` = VALUES(`email_verified`),
  `updated_at` = NOW();

-- ============================
-- 2. REFRESH_TOKENS TABLE
-- ============================
-- JWT refresh tokens management
DROP TABLE IF EXISTS `refresh_tokens`;
CREATE TABLE `refresh_tokens` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `token` VARCHAR(255) NOT NULL UNIQUE,
    `user_id` BIGINT NOT NULL,
    `expiry_date` TIMESTAMP NOT NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE,
    INDEX `idx_refresh_tokens_token` (`token`),
    INDEX `idx_refresh_tokens_user_id` (`user_id`)
);

-- ============================
-- 3. PRODUCTS TABLE
-- ============================
-- Main products catalog
DROP TABLE IF EXISTS `products`;
CREATE TABLE `products` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL,
    `description` TEXT,
    `price` DECIMAL(10,2) NOT NULL,
    `stock_quantity` INT DEFAULT 0,
    `category` VARCHAR(100),
    `brand` VARCHAR(100),
    `model` VARCHAR(100),
    `sku` VARCHAR(100) UNIQUE,
    `is_active` BOOLEAN DEFAULT TRUE,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX `idx_products_category` (`category`),
    INDEX `idx_products_brand` (`brand`),
    INDEX `idx_products_sku` (`sku`),
    INDEX `idx_products_is_active` (`is_active`)
);

-- Sample products
INSERT INTO `products` (`name`, `description`, `price`, `stock_quantity`, `category`, `brand`, `model`, `sku`) VALUES
('iPhone 15 Pro', 'Latest iPhone with Pro features', 999.99, 50, 'Phone', 'Apple', 'iPhone 15 Pro', 'IPHONE15PRO'),
('iPhone 15', 'Latest iPhone standard model', 799.99, 100, 'Phone', 'Apple', 'iPhone 15', 'IPHONE15'),
('MacBook Pro 14"', 'Professional laptop with M3 chip', 1999.99, 25, 'Laptop', 'Apple', 'MacBook Pro 14', 'MBP14M3'),
('iPad Pro', 'Professional tablet with M2 chip', 1099.99, 30, 'Tablet', 'Apple', 'iPad Pro', 'IPADPRO'),
('AirPods Pro', 'Premium wireless earphones', 249.99, 75, 'Audio', 'Apple', 'AirPods Pro 2', 'AIRPODSPRO2');

-- ============================
-- 4. PRODUCT_IMAGES TABLE
-- ============================
-- Product image management
DROP TABLE IF EXISTS `product_images`;
CREATE TABLE `product_images` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `product_id` BIGINT NOT NULL,
    `image_url` VARCHAR(500) NOT NULL,
    `alt_text` VARCHAR(255),
    `is_primary` BOOLEAN DEFAULT FALSE,
    `display_order` INT DEFAULT 0,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`product_id`) REFERENCES `products`(`id`) ON DELETE CASCADE,
    INDEX `idx_product_images_product_id` (`product_id`),
    INDEX `idx_product_images_is_primary` (`is_primary`)
);

-- ============================
-- 5. PRODUCT_SPECIFICATIONS TABLE
-- ============================
-- Product technical specifications
DROP TABLE IF EXISTS `product_specifications`;
CREATE TABLE `product_specifications` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `product_id` BIGINT NOT NULL,
    `spec_name` VARCHAR(100) NOT NULL,
    `spec_value` VARCHAR(255) NOT NULL,
    `spec_category` VARCHAR(100),
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`product_id`) REFERENCES `products`(`id`) ON DELETE CASCADE,
    INDEX `idx_product_specs_product_id` (`product_id`),
    INDEX `idx_product_specs_category` (`spec_category`)
);

-- ============================
-- 6. PRODUCT_COLORS TABLE
-- ============================
-- Product color variants
DROP TABLE IF EXISTS `product_colors`;
CREATE TABLE `product_colors` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `product_id` BIGINT NOT NULL,
    `color_name` VARCHAR(50) NOT NULL,
    `color_code` VARCHAR(7), -- Hex color code
    `additional_price` DECIMAL(10,2) DEFAULT 0.00,
    `stock_quantity` INT DEFAULT 0,
    `is_available` BOOLEAN DEFAULT TRUE,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`product_id`) REFERENCES `products`(`id`) ON DELETE CASCADE,
    INDEX `idx_product_colors_product_id` (`product_id`)
);

-- ============================
-- 7. ORDERS TABLE
-- ============================
-- Customer orders
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL,
    `order_number` VARCHAR(50) UNIQUE NOT NULL,
    `status` VARCHAR(50) DEFAULT 'PENDING',
    `total_amount` DECIMAL(10,2) NOT NULL,
    `shipping_address` TEXT,
    `payment_status` VARCHAR(50) DEFAULT 'PENDING',
    `payment_method` VARCHAR(50),
    `notes` TEXT,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE,
    INDEX `idx_orders_user_id` (`user_id`),
    INDEX `idx_orders_status` (`status`),
    INDEX `idx_orders_order_number` (`order_number`)
);

-- ============================
-- 8. ORDER_ITEMS TABLE
-- ============================
-- Items within orders
DROP TABLE IF EXISTS `order_items`;
CREATE TABLE `order_items` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `order_id` BIGINT NOT NULL,
    `product_id` BIGINT NOT NULL,
    `quantity` INT NOT NULL,
    `unit_price` DECIMAL(10,2) NOT NULL,
    `total_price` DECIMAL(10,2) NOT NULL,
    `product_name` VARCHAR(255), -- Snapshot of product name at order time
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`order_id`) REFERENCES `orders`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`product_id`) REFERENCES `products`(`id`) ON DELETE RESTRICT,
    INDEX `idx_order_items_order_id` (`order_id`),
    INDEX `idx_order_items_product_id` (`product_id`)
);

-- ============================
-- 9. REVIEWS TABLE
-- ============================
-- Product reviews and ratings
DROP TABLE IF EXISTS `reviews`;
CREATE TABLE `reviews` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `product_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `rating` INT CHECK (`rating` >= 1 AND `rating` <= 5),
    `comment` TEXT,
    `is_verified_purchase` BOOLEAN DEFAULT FALSE,
    `is_approved` BOOLEAN DEFAULT TRUE,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`product_id`) REFERENCES `products`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE,
    UNIQUE KEY `unique_user_product_review` (`product_id`, `user_id`),
    INDEX `idx_reviews_product_id` (`product_id`),
    INDEX `idx_reviews_user_id` (`user_id`),
    INDEX `idx_reviews_rating` (`rating`)
);

-- ============================
-- 10. WISHLISTS TABLE
-- ============================
-- User wishlists/favorites
DROP TABLE IF EXISTS `wishlists`;
CREATE TABLE `wishlists` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL,
    `product_id` BIGINT NOT NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`product_id`) REFERENCES `products`(`id`) ON DELETE CASCADE,
    UNIQUE KEY `unique_user_product_wishlist` (`user_id`, `product_id`),
    INDEX `idx_wishlists_user_id` (`user_id`),
    INDEX `idx_wishlists_product_id` (`product_id`)
);

-- ============================
-- 11. COUPONS TABLE
-- ============================
-- Discount coupons system
DROP TABLE IF EXISTS `coupons`;
CREATE TABLE `coupons` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `code` VARCHAR(50) UNIQUE NOT NULL,
    `description` VARCHAR(255),
    `discount_type` VARCHAR(20) NOT NULL, -- 'PERCENTAGE' or 'FIXED'
    `discount_value` DECIMAL(10,2) NOT NULL,
    `minimum_order_amount` DECIMAL(10,2) DEFAULT 0,
    `maximum_discount_amount` DECIMAL(10,2),
    `usage_limit` INT DEFAULT NULL, -- NULL for unlimited
    `used_count` INT DEFAULT 0,
    `valid_from` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `valid_until` TIMESTAMP,
    `is_active` BOOLEAN DEFAULT TRUE,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX `idx_coupons_code` (`code`),
    INDEX `idx_coupons_valid_dates` (`valid_from`, `valid_until`),
    INDEX `idx_coupons_is_active` (`is_active`)
);

-- Sample coupons
INSERT INTO `coupons` (`code`, `description`, `discount_type`, `discount_value`, `minimum_order_amount`, `valid_until`) VALUES
('WELCOME10', 'Welcome discount 10%', 'PERCENTAGE', 10.00, 100.00, DATE_ADD(NOW(), INTERVAL 1 YEAR)),
('SAVE50', 'Save $50 on orders over $500', 'FIXED', 50.00, 500.00, DATE_ADD(NOW(), INTERVAL 6 MONTH)),
('FREESHIP', 'Free shipping coupon', 'FIXED', 15.00, 50.00, DATE_ADD(NOW(), INTERVAL 3 MONTH));

-- ============================
-- 12. COUPON_USAGE TABLE
-- ============================
-- Track coupon usage by users
DROP TABLE IF EXISTS `coupon_usage`;
CREATE TABLE `coupon_usage` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `coupon_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `order_id` BIGINT,
    `used_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`coupon_id`) REFERENCES `coupons`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`order_id`) REFERENCES `orders`(`id`) ON DELETE SET NULL,
    INDEX `idx_coupon_usage_coupon_id` (`coupon_id`),
    INDEX `idx_coupon_usage_user_id` (`user_id`)
);

-- ============================
-- 13. VOUCHER TABLE
-- ============================
-- Gift vouchers system
DROP TABLE IF EXISTS `voucher`;
CREATE TABLE `voucher` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `code` VARCHAR(50) UNIQUE NOT NULL,
    `amount` DECIMAL(10,2) NOT NULL,
    `currency` VARCHAR(3) DEFAULT 'USD',
    `valid_from` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `valid_until` TIMESTAMP,
    `is_active` BOOLEAN DEFAULT TRUE,
    `description` VARCHAR(255),
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX `idx_voucher_code` (`code`),
    INDEX `idx_voucher_valid_dates` (`valid_from`, `valid_until`),
    INDEX `idx_voucher_is_active` (`is_active`)
);

-- ============================
-- 14. USER_VOUCHER TABLE
-- ============================
-- Track voucher ownership and usage
DROP TABLE IF EXISTS `user_voucher`;
CREATE TABLE `user_voucher` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL,
    `voucher_id` BIGINT NOT NULL,
    `status` VARCHAR(20) DEFAULT 'ACTIVE', -- 'ACTIVE', 'USED', 'EXPIRED'
    `used_at` TIMESTAMP NULL,
    `order_id` BIGINT,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`voucher_id`) REFERENCES `voucher`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`order_id`) REFERENCES `orders`(`id`) ON DELETE SET NULL,
    INDEX `idx_user_voucher_user_id` (`user_id`),
    INDEX `idx_user_voucher_voucher_id` (`voucher_id`),
    INDEX `idx_user_voucher_status` (`status`)
);

-- ============================
-- 15. PRICING_RULES TABLE
-- ============================
-- Dynamic pricing rules
DROP TABLE IF EXISTS `pricing_rules`;
CREATE TABLE `pricing_rules` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `rule_name` VARCHAR(100) NOT NULL,
    `rule_type` VARCHAR(50) NOT NULL, -- 'BULK_DISCOUNT', 'TIME_BASED', 'USER_TYPE'
    `condition_data` JSON,
    `discount_type` VARCHAR(20), -- 'PERCENTAGE', 'FIXED'
    `discount_value` DECIMAL(10,2),
    `priority` INT DEFAULT 1,
    `is_active` BOOLEAN DEFAULT TRUE,
    `valid_from` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `valid_until` TIMESTAMP,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX `idx_pricing_rules_type` (`rule_type`),
    INDEX `idx_pricing_rules_priority` (`priority`),
    INDEX `idx_pricing_rules_active` (`is_active`)
);

-- ============================
-- 16. PRICE_ADJUSTMENT_LOGS TABLE
-- ============================
-- Track price changes and adjustments
DROP TABLE IF EXISTS `price_adjustment_logs`;
CREATE TABLE `price_adjustment_logs` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `product_id` BIGINT NOT NULL,
    `old_price` DECIMAL(10,2) NOT NULL,
    `new_price` DECIMAL(10,2) NOT NULL,
    `adjustment_reason` VARCHAR(255),
    `adjusted_by` BIGINT, -- user_id who made the change
    `pricing_rule_id` BIGINT,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`product_id`) REFERENCES `products`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`adjusted_by`) REFERENCES `users`(`id`) ON DELETE SET NULL,
    FOREIGN KEY (`pricing_rule_id`) REFERENCES `pricing_rules`(`id`) ON DELETE SET NULL,
    INDEX `idx_price_logs_product_id` (`product_id`),
    INDEX `idx_price_logs_date` (`created_at`)
);

-- ============================
-- 17. INVENTORY_MOVEMENTS TABLE
-- ============================
-- Track stock movements
DROP TABLE IF EXISTS `inventory_movements`;
CREATE TABLE `inventory_movements` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `product_id` BIGINT NOT NULL,
    `movement_type` VARCHAR(20) NOT NULL, -- 'IN', 'OUT', 'ADJUSTMENT'
    `quantity` INT NOT NULL,
    `previous_stock` INT NOT NULL,
    `new_stock` INT NOT NULL,
    `reference_type` VARCHAR(50), -- 'ORDER', 'PURCHASE', 'ADJUSTMENT'
    `reference_id` BIGINT,
    `notes` TEXT,
    `created_by` BIGINT,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`product_id`) REFERENCES `products`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`created_by`) REFERENCES `users`(`id`) ON DELETE SET NULL,
    INDEX `idx_inventory_movements_product_id` (`product_id`),
    INDEX `idx_inventory_movements_type` (`movement_type`),
    INDEX `idx_inventory_movements_date` (`created_at`)
);

-- ============================
-- 18. CONTACT_MESSAGES TABLE
-- ============================
-- Customer contact/support messages
DROP TABLE IF EXISTS `contact_messages`;
CREATE TABLE `contact_messages` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(100) NOT NULL,
    `email` VARCHAR(255) NOT NULL,
    `subject` VARCHAR(255),
    `message` TEXT NOT NULL,
    `status` VARCHAR(20) DEFAULT 'NEW', -- 'NEW', 'IN_PROGRESS', 'RESOLVED'
    `user_id` BIGINT, -- NULL for guest messages
    `assigned_to` BIGINT, -- admin user handling the message
    `response` TEXT,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE SET NULL,
    FOREIGN KEY (`assigned_to`) REFERENCES `users`(`id`) ON DELETE SET NULL,
    INDEX `idx_contact_messages_status` (`status`),
    INDEX `idx_contact_messages_user_id` (`user_id`),
    INDEX `idx_contact_messages_date` (`created_at`)
);

-- ============================
-- 19. FLYWAY_SCHEMA_HISTORY TABLE
-- ============================
-- Flyway database migration history (auto-created)
-- This table is managed by Flyway automatically

-- ============================
-- SAMPLE DATA QUERIES
-- ============================

-- Query all users
SELECT id, email, role, is_active, email_verified, created_at FROM users ORDER BY created_at DESC;

-- Query all products with stock
SELECT id, name, price, stock_quantity, category, brand, is_active FROM products WHERE is_active = TRUE ORDER BY name;

-- Query orders with user information
SELECT
    o.id,
    o.order_number,
    u.email,
    o.status,
    o.total_amount,
    o.created_at
FROM orders o
JOIN users u ON o.user_id = u.id
ORDER BY o.created_at DESC;

-- Query product reviews with ratings
SELECT
    p.name AS product_name,
    u.email,
    r.rating,
    r.comment,
    r.created_at
FROM reviews r
JOIN products p ON r.product_id = p.id
JOIN users u ON r.user_id = u.id
ORDER BY r.created_at DESC;

-- Query active coupons
SELECT code, description, discount_type, discount_value, valid_until FROM coupons WHERE is_active = TRUE AND (valid_until IS NULL OR valid_until > NOW());

-- ============================
-- USEFUL MAINTENANCE QUERIES
-- ============================

-- Reset database (use with caution)
-- SET FOREIGN_KEY_CHECKS = 0;
-- DROP TABLE IF EXISTS users, refresh_tokens, products, product_images, product_specifications, product_colors, orders, order_items, reviews, wishlists, coupons, coupon_usage, voucher, user_voucher, pricing_rules, price_adjustment_logs, inventory_movements, contact_messages;
-- SET FOREIGN_KEY_CHECKS = 1;

-- Check database size
-- SELECT
--     table_name AS 'Table',
--     ROUND(((data_length + index_length) / 1024 / 1024), 2) AS 'Size (MB)'
-- FROM information_schema.TABLES
-- WHERE table_schema = 'appl_store'
-- ORDER BY (data_length + index_length) DESC;

-- ============================
-- SEED ACCOUNT CREDENTIALS
-- ============================
-- Admin Account:
--   Email: admin@applestore.com
--   Password: admin@12345
--
-- Sample User Account:
--   Email: user@example.com
--   Password: user@123345
--
-- NOTE: Passwords are BCrypt encoded in the database
-- ============================
