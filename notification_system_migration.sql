-- ============================
-- NOTIFICATION SYSTEM TABLES
-- ============================
-- Migration for Real-time Notification System
-- Created: January 27, 2026
-- ============================
-- 1. NOTIFICATIONS TABLE
-- ============================
-- Stores all user notifications
DROP TABLE IF EXISTS `notifications`;
CREATE TABLE `notifications` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL,
    `type` VARCHAR(50) NOT NULL COMMENT 'Notification type (ORDER, PRODUCT, PROMOTION, SYSTEM)',
    `title` VARCHAR(255) NOT NULL,
    `message` TEXT NOT NULL,
    `action_url` VARCHAR(500) COMMENT 'URL to navigate when clicked',
    `action_text` VARCHAR(100) COMMENT 'Text for action button',
    `is_read` BOOLEAN DEFAULT FALSE,
    `is_sent_email` BOOLEAN DEFAULT FALSE COMMENT 'Whether email notification was sent',
    `priority` VARCHAR(20) DEFAULT 'NORMAL' COMMENT 'LOW, NORMAL, HIGH, URGENT',
    `metadata` JSON COMMENT 'Additional data (order_id, product_id, etc.)',
    `expires_at` TIMESTAMP NULL COMMENT 'When notification should be hidden',
    `read_at` TIMESTAMP NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE,
    INDEX `idx_user_notifications` (`user_id`, `is_read`, `created_at`),
    INDEX `idx_notification_type` (`type`),
    INDEX `idx_created_at` (`created_at`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;
-- ============================
-- 2. NOTIFICATION_PREFERENCES TABLE
-- ============================
-- User notification preferences
DROP TABLE IF EXISTS `notification_preferences`;
CREATE TABLE `notification_preferences` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL UNIQUE,
    `order_updates` BOOLEAN DEFAULT TRUE COMMENT 'Order status updates',
    `order_updates_email` BOOLEAN DEFAULT TRUE,
    `promotional` BOOLEAN DEFAULT TRUE COMMENT 'Promotional notifications',
    `promotional_email` BOOLEAN DEFAULT FALSE,
    `product_updates` BOOLEAN DEFAULT TRUE COMMENT 'Product restock, price drops',
    `product_updates_email` BOOLEAN DEFAULT FALSE,
    `review_responses` BOOLEAN DEFAULT TRUE COMMENT 'Admin replies to reviews',
    `review_responses_email` BOOLEAN DEFAULT TRUE,
    `system_announcements` BOOLEAN DEFAULT TRUE COMMENT 'System maintenance, updates',
    `system_announcements_email` BOOLEAN DEFAULT TRUE,
    `push_enabled` BOOLEAN DEFAULT FALSE COMMENT 'Push notifications enabled',
    `email_frequency` VARCHAR(20) DEFAULT 'INSTANT' COMMENT 'INSTANT, DAILY, WEEKLY, NEVER',
    `quiet_hours_start` TIME COMMENT 'Start of quiet hours (no push)',
    `quiet_hours_end` TIME COMMENT 'End of quiet hours',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;
-- ============================
-- 3. NOTIFICATION_TEMPLATES TABLE
-- ============================
-- Reusable notification templates
DROP TABLE IF EXISTS `notification_templates`;
CREATE TABLE `notification_templates` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `template_key` VARCHAR(100) UNIQUE NOT NULL COMMENT 'Unique template identifier',
    `type` VARCHAR(50) NOT NULL,
    `title_template` VARCHAR(255) NOT NULL COMMENT 'Title with placeholders',
    `message_template` TEXT NOT NULL COMMENT 'Message with placeholders',
    `action_text` VARCHAR(100),
    `action_url_template` VARCHAR(500) COMMENT 'URL with placeholders',
    `email_subject_template` VARCHAR(255),
    `email_body_template` TEXT COMMENT 'HTML email template',
    `priority` VARCHAR(20) DEFAULT 'NORMAL',
    `is_active` BOOLEAN DEFAULT TRUE,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;
-- ============================
-- SAMPLE NOTIFICATION TEMPLATES
-- ============================
INSERT INTO `notification_templates` (
        `template_key`,
        `type`,
        `title_template`,
        `message_template`,
        `action_text`,
        `action_url_template`,
        `priority`,
        `email_subject_template`
    )
VALUES -- Order Templates
    (
        'ORDER_PLACED',
        'ORDER',
        'Order Placed Successfully',
        'Your order #{orderNumber} has been placed successfully. Total: ${total}',
        'View Order',
        '/orders/{orderId}',
        'HIGH',
        'Order Confirmation - #{orderNumber}'
    ),
    (
        'ORDER_CONFIRMED',
        'ORDER',
        'Order Confirmed',
        'Your order #{orderNumber} has been confirmed and is being processed.',
        'Track Order',
        '/orders/{orderId}/tracking',
        'NORMAL',
        'Your Order is Confirmed - #{orderNumber}'
    ),
    (
        'ORDER_SHIPPED',
        'ORDER',
        'Order Shipped! 📦',
        'Your order #{orderNumber} has been shipped. Tracking: {trackingNumber}',
        'Track Package',
        '/orders/{orderId}/tracking',
        'HIGH',
        'Your Order Has Shipped - #{orderNumber}'
    ),
    (
        'ORDER_DELIVERED',
        'ORDER',
        'Order Delivered ✅',
        'Your order #{orderNumber} has been delivered. Enjoy your new products!',
        'Review Order',
        '/orders/{orderId}',
        'HIGH',
        'Your Order Has Been Delivered - #{orderNumber}'
    ),
    (
        'ORDER_CANCELLED',
        'ORDER',
        'Order Cancelled',
        'Your order #{orderNumber} has been cancelled. Refund will be processed shortly.',
        'View Details',
        '/orders/{orderId}',
        'HIGH',
        'Order Cancelled - #{orderNumber}'
    ),
    -- Product Templates
    (
        'PRODUCT_RESTOCK',
        'PRODUCT',
        'Product Back in Stock!',
        '{productName} is now back in stock!',
        'View Product',
        '/product/{productType}/{productId}',
        'NORMAL',
        '{productName} is Back in Stock!'
    ),
    (
        'PRICE_DROP',
        'PRODUCT',
        'Price Drop Alert! 💰',
        '{productName} price dropped to ${newPrice}. Save ${discount}!',
        'Shop Now',
        '/product/{productType}/{productId}',
        'NORMAL',
        'Price Drop on {productName}'
    ),
    (
        'WISHLIST_SALE',
        'PRODUCT',
        'Wishlist Item on Sale',
        '{productName} from your wishlist is on sale! {discount}% off',
        'View Product',
        '/product/{productType}/{productId}',
        'NORMAL',
        'Your Wishlist Item is on Sale!'
    ),
    -- Review Templates
    (
        'REVIEW_APPROVED',
        'REVIEW',
        'Review Published ⭐',
        'Your review for {productName} has been published. Thank you!',
        'View Review',
        '/product/{productType}/{productId}',
        'NORMAL',
        'Your Review Has Been Published'
    ),
    (
        'REVIEW_REPLY',
        'REVIEW',
        'Store Replied to Your Review',
        'Apple Store has replied to your review on {productName}',
        'View Reply',
        '/product/{productType}/{productId}',
        'NORMAL',
        'Response to Your Review'
    ),
    -- Promotional Templates
    (
        'NEW_ARRIVAL',
        'PROMOTION',
        'New Arrival! 🎉',
        'Check out our latest {productName}. Be the first to get it!',
        'Shop Now',
        '/product/{productType}/{productId}',
        'LOW',
        'New Product: {productName}'
    ),
    (
        'FLASH_SALE',
        'PROMOTION',
        'Flash Sale! ⚡',
        'Limited time offer: {discount}% off on selected products!',
        'Shop Sale',
        '/products?filter=sale',
        'HIGH',
        'Flash Sale - Up to {discount}% Off!'
    ),
    (
        'COUPON_AVAILABLE',
        'PROMOTION',
        'Exclusive Coupon for You! 🎁',
        'Use code {couponCode} for {discount}% off. Valid until {expiryDate}',
        'Use Coupon',
        '/products',
        'NORMAL',
        'Your Exclusive Coupon Code'
    ),
    -- System Templates
    (
        'WELCOME',
        'SYSTEM',
        'Welcome to Apple Store! 👋',
        'Thank you for joining us. Explore our products and enjoy exclusive deals!',
        'Start Shopping',
        '/products',
        'NORMAL',
        'Welcome to Apple Store'
    ),
    (
        'ACCOUNT_VERIFIED',
        'SYSTEM',
        'Account Verified ✅',
        'Your email has been verified successfully. You can now access all features!',
        'Go to Dashboard',
        '/dashboard',
        'NORMAL',
        'Email Verified Successfully'
    ),
    (
        'PASSWORD_CHANGED',
        'SYSTEM',
        'Password Changed',
        'Your password has been changed successfully.',
        NULL,
        NULL,
        'HIGH',
        'Password Change Confirmation'
    ),
    (
        'SYSTEM_MAINTENANCE',
        'SYSTEM',
        'Scheduled Maintenance',
        'System maintenance scheduled on {maintenanceDate}. Downtime: {duration}',
        NULL,
        NULL,
        'URGENT',
        'Scheduled System Maintenance'
    );
-- ============================
-- INDEXES FOR PERFORMANCE
-- ============================
-- Additional indexes already created above
-- ============================
-- TRIGGERS
-- ============================
-- Auto-delete old notifications (older than 90 days)
DELIMITER // CREATE EVENT IF NOT EXISTS cleanup_old_notifications ON SCHEDULE EVERY 1 DAY DO BEGIN
DELETE FROM notifications
WHERE created_at < DATE_SUB(NOW(), INTERVAL 90 DAY)
    AND is_read = TRUE;
END // DELIMITER;
-- ============================
-- SAMPLE DATA FOR TESTING
-- ============================
-- Create default notification preferences for existing users
-- INSERT INTO notification_preferences (user_id) 
-- SELECT id FROM users WHERE id NOT IN (SELECT user_id FROM notification_preferences);
-- ============================
-- NOTES
-- ============================
-- 1. Notification Types: ORDER, PRODUCT, PROMOTION, REVIEW, SYSTEM
-- 2. Priority Levels: LOW, NORMAL, HIGH, URGENT
-- 3. Email Frequency: INSTANT, DAILY, WEEKLY, NEVER
-- 4. Metadata JSON can contain: order_id, product_id, coupon_code, etc.
-- 5. Templates use placeholders like {orderNumber}, {productName}, etc.
-- 6. Old read notifications are auto-deleted after 90 days