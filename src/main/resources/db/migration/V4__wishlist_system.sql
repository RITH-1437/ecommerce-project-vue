-- ========================================
-- Wishlist System Database Migration
-- ========================================
-- Create wishlist_items table
CREATE TABLE IF NOT EXISTS wishlist_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    notes VARCHAR(500),
    price_at_addition DECIMAL(10, 2),
    notify_on_price_drop BOOLEAN DEFAULT FALSE,
    notify_on_restock BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_wishlist_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_wishlist_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    -- Prevent duplicate entries
    CONSTRAINT uk_user_product UNIQUE (user_id, product_id),
    -- Indexes for performance
    INDEX idx_wishlist_user (user_id),
    INDEX idx_wishlist_product (product_id),
    INDEX idx_wishlist_added_at (added_at)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;
-- Create wishlist_share table for shared wishlists
CREATE TABLE IF NOT EXISTS wishlist_shares (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    share_token VARCHAR(100) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NULL,
    is_active BOOLEAN DEFAULT TRUE,
    view_count INT DEFAULT 0,
    CONSTRAINT fk_wishlist_share_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_share_token (share_token),
    INDEX idx_share_user (user_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;
-- ========================================
-- Sample Data (Optional)
-- ========================================
-- Add wishlist items for demo (user_id 1 should exist)
-- INSERT INTO wishlist_items (user_id, product_id, price_at_addition, notify_on_price_drop, notify_on_restock)
-- VALUES 
--     (1, 1, 999.00, TRUE, FALSE),
--     (1, 2, 1299.00, TRUE, TRUE);
-- ========================================
-- Note: Triggers for Price Drop Notifications
-- ========================================
-- Triggers using DELIMITER are not supported in Flyway migrations.
-- Price drop and restock notifications should be handled in the application layer
-- or created manually after migration if needed.
-- ========================================
-- Useful Queries
-- ========================================
-- Get wishlist for a user with product details
-- SELECT 
--     w.id,
--     w.added_at,
--     w.price_at_addition,
--     w.notify_on_price_drop,
--     w.notify_on_restock,
--     p.*,
--     (p.price < w.price_at_addition) as price_dropped
-- FROM wishlist_items w
-- JOIN products p ON w.product_id = p.id
-- WHERE w.user_id = ?
-- ORDER BY w.added_at DESC;
-- Get wishlist statistics
-- SELECT 
--     COUNT(*) as total_items,
--     SUM(p.price) as total_current_value,
--     SUM(w.price_at_addition) as total_original_value,
--     SUM(w.price_at_addition - p.price) as total_savings
-- FROM wishlist_items w
-- JOIN products p ON w.product_id = p.id
-- WHERE w.user_id = ?;