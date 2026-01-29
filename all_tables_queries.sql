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
-- Admin password: admin@123
-- User password: User123!
INSERT INTO `users` (
        `email`,
        `password_hash`,
        `role`,
        `is_active`,
        `email_verified`,
        `created_at`,
        `updated_at`
    )
VALUES (
        'admin@applestore.com',
        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'ADMIN',
        TRUE,
        TRUE,
        NOW(),
        NOW()
    ),
    (
        'user@example.com',
        '$2a$10$WRfl0GzrVQgkMTT1mnhjmOZ4n0OHSjvTPnXeQUXUk1ndSmV6CT.0m',
        'CUSTOMER',
        TRUE,
        TRUE,
        NOW(),
        NOW()
    ) ON DUPLICATE KEY
UPDATE `password_hash` =
VALUES(`password_hash`),
    `role` =
VALUES(`role`),
    `is_active` =
VALUES(`is_active`),
    `email_verified` =
VALUES(`email_verified`),
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
-- 3. CATEGORIES TABLE
-- ============================
-- Product categories
DROP TABLE IF EXISTS `categories`;
CREATE TABLE `categories` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(100) NOT NULL UNIQUE,
    `slug` VARCHAR(100) NOT NULL UNIQUE,
    `description` TEXT,
    `image_url` VARCHAR(500),
    `parent_id` BIGINT,
    `is_active` BOOLEAN DEFAULT TRUE,
    `display_order` INT DEFAULT 0,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`parent_id`) REFERENCES `categories`(`id`) ON DELETE
    SET NULL,
        INDEX `idx_categories_slug` (`slug`),
        INDEX `idx_categories_parent_id` (`parent_id`),
        INDEX `idx_categories_is_active` (`is_active`)
);
-- Insert categories
INSERT INTO `categories` (
        `name`,
        `slug`,
        `description`,
        `display_order`,
        `is_active`
    )
VALUES (
        'iPhone',
        'iphone',
        'Latest iPhone models with cutting-edge technology',
        1,
        TRUE
    ),
    (
        'MacBook',
        'macbook',
        'Powerful MacBook laptops for professionals',
        2,
        TRUE
    ),
    (
        'iPad',
        'ipad',
        'Versatile iPad tablets for work and play',
        3,
        TRUE
    ),
    (
        'Apple Watch',
        'apple-watch',
        'Smart watches for health and fitness',
        4,
        TRUE
    ),
    (
        'AirPods',
        'airpods',
        'Wireless earbuds and headphones',
        5,
        TRUE
    ),
    (
        'Accessories',
        'accessories',
        'Essential Apple accessories',
        6,
        TRUE
    );
-- ============================
-- 4. PRODUCTS TABLE
-- ============================
-- Main products catalog
DROP TABLE IF EXISTS `products`;
CREATE TABLE `products` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL,
    `slug` VARCHAR(255) NOT NULL UNIQUE,
    `sku` VARCHAR(100) UNIQUE,
    `category_id` BIGINT,
    `description` TEXT,
    `short_description` VARCHAR(500),
    `price` DECIMAL(10, 2) NOT NULL,
    `original_price` DECIMAL(10, 2),
    `stock` INT DEFAULT 0,
    `min_stock` INT DEFAULT 10,
    `badge` VARCHAR(50),
    `is_active` BOOLEAN DEFAULT TRUE,
    `is_featured` BOOLEAN DEFAULT FALSE,
    `rating` DECIMAL(3, 2) DEFAULT 0.00,
    `reviews_count` INT DEFAULT 0,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`category_id`) REFERENCES `categories`(`id`) ON DELETE
    SET NULL,
        INDEX `idx_products_category_id` (`category_id`),
        INDEX `idx_products_slug` (`slug`),
        INDEX `idx_products_sku` (`sku`),
        INDEX `idx_products_price` (`price`),
        INDEX `idx_products_stock` (`stock`),
        INDEX `idx_products_is_active` (`is_active`)
);
-- Insert iPhone products (9 products)
INSERT INTO `products` (
        `name`,
        `slug`,
        `sku`,
        `category_id`,
        `description`,
        `short_description`,
        `price`,
        `original_price`,
        `stock`,
        `badge`,
        `is_featured`,
        `rating`
    )
VALUES (
        'iPhone 17 Pro Max',
        'iphone-17-pro-max',
        'IP17PMAX',
        1,
        'The ultimate iPhone with advanced AI features, titanium design, and revolutionary camera system. Features A18 Pro chip, 6.9-inch Super Retina XDR display, and up to 2TB storage.',
        'Latest flagship iPhone with Pro Max features',
        1399.99,
        1499.99,
        50,
        'NEW',
        TRUE,
        4.9
    ),
    (
        'iPhone 17 Pro',
        'iphone-17-pro',
        'IP17PRO',
        1,
        'Professional iPhone with advanced features, A18 Pro chip, and pro camera system. Available in Natural Titanium, Blue Titanium, White Titanium, and Black Titanium.',
        'Pro-level iPhone with advanced capabilities',
        1199.99,
        1299.99,
        75,
        'NEW',
        TRUE,
        4.8
    ),
    (
        'iPhone 17',
        'iphone-17',
        'IP17',
        1,
        'The newest iPhone with stunning design, powerful A18 chip, and advanced dual-camera system. Features 6.1-inch Super Retina XDR display and all-day battery life.',
        'Latest standard iPhone model',
        899.99,
        999.99,
        100,
        'NEW',
        TRUE,
        4.7
    ),
    (
        'iPhone 16 Pro Max',
        'iphone-16-pro-max',
        'IP16PMAX',
        1,
        'Previous generation Pro Max with excellent performance, titanium design, and professional camera features. Still an amazing choice.',
        'Powerful Pro Max from 2025',
        1199.99,
        1399.99,
        60,
        'SALE',
        FALSE,
        4.8
    ),
    (
        'iPhone 16 Pro',
        'iphone-16-pro',
        'IP16PRO',
        1,
        'Last year Pro model with A17 Pro chip and excellent camera system. Great value for pro features.',
        'Previous Pro model with great features',
        999.99,
        1199.99,
        80,
        'SALE',
        FALSE,
        4.7
    ),
    (
        'iPhone 16',
        'iphone-16',
        'IP16',
        1,
        'Previous standard model with great performance and features. Excellent choice at this price.',
        'Last year standard model',
        699.99,
        899.99,
        120,
        'SALE',
        FALSE,
        4.6
    ),
    (
        'iPhone 15 Pro',
        'iphone-15-pro',
        'IP15PRO',
        1,
        'iPhone 15 Pro with A17 Pro chip, titanium design, and Action button. Still a powerful device.',
        'iPhone 15 Pro with titanium design',
        899.99,
        1099.99,
        90,
        'POPULAR',
        FALSE,
        4.7
    ),
    (
        'iPhone 15',
        'iphone-15',
        'IP15',
        1,
        'iPhone 15 with Dynamic Island, 48MP camera, and USB-C. Great value smartphone.',
        'Solid iPhone with great features',
        599.99,
        799.99,
        150,
        'POPULAR',
        FALSE,
        4.6
    ),
    (
        'iPhone SE (2024)',
        'iphone-se-2024',
        'IPSE24',
        1,
        'Compact and affordable iPhone with A16 Bionic chip and classic design. Perfect for those who prefer smaller phones.',
        'Affordable compact iPhone',
        429.99,
        499.99,
        200,
        'VALUE',
        FALSE,
        4.5
    );
-- Insert MacBook products (9 products)
INSERT INTO `products` (
        `name`,
        `slug`,
        `sku`,
        `category_id`,
        `description`,
        `short_description`,
        `price`,
        `original_price`,
        `stock`,
        `badge`,
        `is_featured`,
        `rating`
    )
VALUES (
        'MacBook Pro 16" M4 Max',
        'macbook-pro-16-m4-max',
        'MBP16M4MAX',
        2,
        'Ultimate MacBook Pro with M4 Max chip, 16-inch Liquid Retina XDR display, up to 128GB RAM, and 8TB SSD. Perfect for professionals.',
        'Top-tier MacBook for professionals',
        3499.99,
        3799.99,
        25,
        'NEW',
        TRUE,
        5.0
    ),
    (
        'MacBook Pro 16" M4 Pro',
        'macbook-pro-16-m4-pro',
        'MBP16M4PRO',
        2,
        'Powerful MacBook Pro with M4 Pro chip, stunning 16-inch display, and exceptional battery life. For serious work.',
        'Professional laptop with M4 Pro',
        2799.99,
        2999.99,
        35,
        'NEW',
        TRUE,
        4.9
    ),
    (
        'MacBook Pro 14" M4 Pro',
        'macbook-pro-14-m4-pro',
        'MBP14M4PRO',
        2,
        'Compact pro laptop with M4 Pro chip, 14-inch Liquid Retina XDR display. Perfect balance of power and portability.',
        'Portable pro laptop with power',
        2199.99,
        2399.99,
        40,
        'NEW',
        TRUE,
        4.9
    ),
    (
        'MacBook Pro 14" M4',
        'macbook-pro-14-m4',
        'MBP14M4',
        2,
        'Entry-level MacBook Pro with M4 chip, professional features, and stunning display. Great for creators.',
        'Entry pro laptop with M4 chip',
        1799.99,
        1999.99,
        50,
        'POPULAR',
        FALSE,
        4.8
    ),
    (
        'MacBook Air 15" M3',
        'macbook-air-15-m3',
        'MBA15M3',
        2,
        'Spacious MacBook Air with M3 chip, 15-inch display, and all-day battery. Ultra-thin and light.',
        'Large screen Air with M3',
        1499.99,
        1599.99,
        60,
        'POPULAR',
        TRUE,
        4.8
    ),
    (
        'MacBook Air 13" M3',
        'macbook-air-13-m3',
        'MBA13M3',
        2,
        'Perfect MacBook Air with M3 chip, 13-inch display, and incredible battery life. Ideal for students and professionals.',
        'Classic Air with latest M3 chip',
        1199.99,
        1299.99,
        80,
        'POPULAR',
        TRUE,
        4.8
    ),
    (
        'MacBook Air 13" M2',
        'macbook-air-13-m2',
        'MBA13M2',
        2,
        'Previous generation Air with M2 chip. Still incredibly fast and efficient. Great value.',
        'Last year Air, excellent value',
        999.99,
        1199.99,
        100,
        'SALE',
        FALSE,
        4.7
    ),
    (
        'MacBook Pro 13" M2',
        'macbook-pro-13-m2',
        'MBP13M2',
        2,
        'Compact MacBook Pro with M2 chip and Touch Bar. Great for professionals on the go.',
        'Portable Pro with M2 chip',
        1299.99,
        1499.99,
        45,
        'SALE',
        FALSE,
        4.6
    ),
    (
        'MacBook Air 13" M1',
        'macbook-air-13-m1',
        'MBA13M1',
        2,
        'The revolutionary M1 MacBook Air. Still incredibly capable and efficient. Best budget option.',
        'M1 Air, proven performance',
        849.99,
        999.99,
        120,
        'VALUE',
        FALSE,
        4.7
    );
-- Insert iPad products (9 products)
INSERT INTO `products` (
        `name`,
        `slug`,
        `sku`,
        `category_id`,
        `description`,
        `short_description`,
        `price`,
        `original_price`,
        `stock`,
        `badge`,
        `is_featured`,
        `rating`
    )
VALUES (
        'iPad Pro 13" M4',
        'ipad-pro-13-m4',
        'IPADP13M4',
        3,
        'Ultimate iPad with M4 chip, stunning 13-inch Ultra Retina XDR display, and Face ID. The most powerful iPad ever.',
        'Top iPad with M4 and huge display',
        1399.99,
        1499.99,
        40,
        'NEW',
        TRUE,
        4.9
    ),
    (
        'iPad Pro 11" M4',
        'ipad-pro-11-m4',
        'IPADP11M4',
        3,
        'Powerful iPad Pro with M4 chip, 11-inch Ultra Retina XDR display. Perfect for professionals and creators.',
        'Pro iPad with M4 chip',
        1099.99,
        1199.99,
        50,
        'NEW',
        TRUE,
        4.9
    ),
    (
        'iPad Air 13" M2',
        'ipad-air-13-m2',
        'IPADA13M2',
        3,
        'Large iPad Air with M2 chip and 13-inch Liquid Retina display. Powerful and versatile.',
        'Big screen Air with M2',
        899.99,
        949.99,
        60,
        'POPULAR',
        TRUE,
        4.8
    ),
    (
        'iPad Air 11" M2',
        'ipad-air-11-m2',
        'IPADA11M2',
        3,
        'Perfect size iPad Air with M2 chip. Great balance of performance, size, and price.',
        'Balanced Air with M2 chip',
        699.99,
        749.99,
        75,
        'POPULAR',
        TRUE,
        4.8
    ),
    (
        'iPad 11" (2024)',
        'ipad-11-2024',
        'IPAD11-24',
        3,
        'Latest standard iPad with A16 Bionic chip, 11-inch Retina display, and all-day battery. Great for everyone.',
        'Latest standard iPad',
        499.99,
        549.99,
        100,
        'POPULAR',
        FALSE,
        4.7
    ),
    (
        'iPad 10.9" (2023)',
        'ipad-109-2023',
        'IPAD109-23',
        3,
        'Previous generation iPad with A14 Bionic. Still very capable for everyday tasks.',
        'Last year iPad, great value',
        399.99,
        449.99,
        120,
        'SALE',
        FALSE,
        4.6
    ),
    (
        'iPad mini 7',
        'ipad-mini-7',
        'IPADMINI7',
        3,
        'Compact and powerful iPad mini with A17 Pro chip. Perfect for on-the-go.',
        'Pocket-sized power with A17 Pro',
        599.99,
        649.99,
        80,
        'NEW',
        FALSE,
        4.8
    ),
    (
        'iPad mini 6',
        'ipad-mini-6',
        'IPADMINI6',
        3,
        'Previous mini with A15 Bionic. Still excellent for portability and performance.',
        'Portable iPad with great features',
        449.99,
        499.99,
        90,
        'VALUE',
        FALSE,
        4.7
    ),
    (
        'iPad 9th Gen',
        'ipad-9th-gen',
        'IPAD9GEN',
        3,
        'Classic iPad with Home button and A13 Bionic. Most affordable iPad option.',
        'Budget-friendly iPad',
        299.99,
        329.99,
        150,
        'VALUE',
        FALSE,
        4.5
    );
-- Insert Apple Watch products (9 products)
INSERT INTO `products` (
        `name`,
        `slug`,
        `sku`,
        `category_id`,
        `description`,
        `short_description`,
        `price`,
        `original_price`,
        `stock`,
        `badge`,
        `is_featured`,
        `rating`
    )
VALUES (
        'Apple Watch Ultra 3',
        'apple-watch-ultra-3',
        'AWULTRA3',
        4,
        'Most capable Apple Watch with titanium case, precision dual-frequency GPS, and up to 72 hours battery. For extreme adventures.',
        'Ultimate adventure smartwatch',
        899.99,
        949.99,
        35,
        'NEW',
        TRUE,
        4.9
    ),
    (
        'Apple Watch Series 10',
        'apple-watch-series-10',
        'AWS10',
        4,
        'Latest Apple Watch with larger display, advanced health features, and faster charging. Perfect everyday companion.',
        'Latest flagship Apple Watch',
        499.99,
        549.99,
        80,
        'NEW',
        TRUE,
        4.8
    ),
    (
        'Apple Watch Series 9',
        'apple-watch-series-9',
        'AWS9',
        4,
        'Previous generation with S9 chip, double tap gesture, and bright display. Great features at better price.',
        'Last year flagship watch',
        399.99,
        499.99,
        100,
        'SALE',
        FALSE,
        4.8
    ),
    (
        'Apple Watch SE (2024)',
        'apple-watch-se-2024',
        'AWSE24',
        4,
        'Latest SE with essential features, crash detection, and great battery life. Best value Apple Watch.',
        'Affordable Apple Watch',
        279.99,
        299.99,
        120,
        'VALUE',
        TRUE,
        4.7
    ),
    (
        'Apple Watch Ultra 2',
        'apple-watch-ultra-2',
        'AWULTRA2',
        4,
        'Previous Ultra with incredible features and durability. Still amazing for adventures.',
        'Previous Ultra, still powerful',
        749.99,
        799.99,
        40,
        'SALE',
        FALSE,
        4.8
    ),
    (
        'Apple Watch Series 8',
        'apple-watch-series-8',
        'AWS8',
        4,
        'Series 8 with temperature sensing and crash detection. Solid choice.',
        'Series 8 with health features',
        329.99,
        399.99,
        90,
        'VALUE',
        FALSE,
        4.6
    ),
    (
        'Apple Watch SE (2022)',
        'apple-watch-se-2022',
        'AWSE22',
        4,
        'Previous SE model. Still great for fitness and notifications.',
        'Older SE, budget option',
        229.99,
        279.99,
        110,
        'VALUE',
        FALSE,
        4.5
    ),
    (
        'Apple Watch Hermès Series 10',
        'apple-watch-hermes-s10',
        'AWHERMES10',
        4,
        'Luxury Apple Watch with exclusive Hermès bands and watch faces. Ultimate style statement.',
        'Luxury Apple Watch collaboration',
        1399.99,
        1499.99,
        15,
        'LUXURY',
        FALSE,
        5.0
    ),
    (
        'Apple Watch Edition',
        'apple-watch-edition',
        'AWEDITION',
        4,
        'Premium titanium Apple Watch with exclusive finishes. For those who demand the best.',
        'Premium titanium edition',
        899.99,
        999.99,
        20,
        'LUXURY',
        FALSE,
        4.9
    );
-- Insert AirPods products (9 products)
INSERT INTO `products` (
        `name`,
        `slug`,
        `sku`,
        `category_id`,
        `description`,
        `short_description`,
        `price`,
        `original_price`,
        `stock`,
        `badge`,
        `is_featured`,
        `rating`
    )
VALUES (
        'AirPods Pro 3',
        'airpods-pro-3',
        'APPRO3',
        5,
        'Latest AirPods Pro with adaptive audio, personalized spatial audio, and USB-C charging. Best noise cancellation ever.',
        'Top AirPods with adaptive audio',
        279.99,
        299.99,
        100,
        'NEW',
        TRUE,
        4.9
    ),
    (
        'AirPods Max 2',
        'airpods-max-2',
        'APMAX2',
        5,
        'Premium over-ear headphones with spatial audio, adaptive EQ, and up to 30 hours battery. Ultimate sound experience.',
        'Premium over-ear headphones',
        599.99,
        629.99,
        40,
        'NEW',
        TRUE,
        4.8
    ),
    (
        'AirPods 4 with ANC',
        'airpods-4-anc',
        'AP4ANC',
        5,
        'Latest AirPods with active noise cancellation, personalized spatial audio, and USB-C. Open-ear design.',
        'New AirPods with ANC',
        199.99,
        219.99,
        120,
        'NEW',
        TRUE,
        4.7
    ),
    (
        'AirPods 4',
        'airpods-4',
        'AP4',
        5,
        'Latest standard AirPods with spatial audio and USB-C charging. Great sound at better price.',
        'Latest standard AirPods',
        149.99,
        169.99,
        150,
        'POPULAR',
        TRUE,
        4.7
    ),
    (
        'AirPods Pro 2',
        'airpods-pro-2',
        'APPRO2',
        5,
        'Previous Pro with H2 chip, excellent ANC, and great sound. Still amazing choice.',
        'Previous Pro, excellent value',
        229.99,
        249.99,
        130,
        'SALE',
        FALSE,
        4.8
    ),
    (
        'AirPods Max',
        'airpods-max',
        'APMAX',
        5,
        'Original AirPods Max with stunning design and sound. Great value now.',
        'Original Max, great sound',
        499.99,
        549.99,
        35,
        'SALE',
        FALSE,
        4.7
    ),
    (
        'AirPods 3',
        'airpods-3',
        'AP3',
        5,
        'AirPods 3 with spatial audio and better battery. Still great wireless earbuds.',
        '3rd gen with spatial audio',
        129.99,
        169.99,
        160,
        'VALUE',
        FALSE,
        4.6
    ),
    (
        'AirPods 2',
        'airpods-2',
        'AP2',
        5,
        'Classic AirPods 2. Simple, reliable, and affordable wireless earbuds.',
        'Classic AirPods',
        99.99,
        129.99,
        180,
        'VALUE',
        FALSE,
        4.5
    ),
    (
        'Beats Studio Pro',
        'beats-studio-pro',
        'BSPRO',
        5,
        'Premium Beats headphones with lossless audio via USB-C, ANC, and up to 40 hours battery.',
        'Premium Beats over-ear',
        349.99,
        379.99,
        70,
        'POPULAR',
        FALSE,
        4.7
    );
-- Insert Accessories products (9 products)
INSERT INTO `products` (
        `name`,
        `slug`,
        `sku`,
        `category_id`,
        `description`,
        `short_description`,
        `price`,
        `original_price`,
        `stock`,
        `badge`,
        `is_featured`,
        `rating`
    )
VALUES (
        'Magic Keyboard for iPad Pro 13"',
        'magic-keyboard-ipad-pro-13',
        'MKIPADP13',
        6,
        'Perfect typing experience for iPad Pro with trackpad, backlit keys, and USB-C port. Transforms iPad into laptop.',
        'Premium iPad Pro keyboard',
        349.99,
        379.99,
        60,
        'NEW',
        FALSE,
        4.8
    ),
    (
        'Apple Pencil Pro',
        'apple-pencil-pro',
        'APPENPRO',
        6,
        'Latest Apple Pencil with squeeze gesture, barrel roll, and haptic feedback. Perfect for creators.',
        'Advanced Apple Pencil',
        129.99,
        139.99,
        150,
        'NEW',
        TRUE,
        4.9
    ),
    (
        'MagSafe Charger',
        'magsafe-charger',
        'MAGSAFE',
        6,
        'Official MagSafe wireless charger for iPhone. Fast 15W charging with perfect alignment.',
        'Official wireless iPhone charger',
        39.99,
        44.99,
        300,
        'POPULAR',
        FALSE,
        4.7
    ),
    (
        'Apple USB-C to Lightning Cable (2m)',
        'usb-c-lightning-cable-2m',
        'UCLTNG2M',
        6,
        'Official charging cable, 2 meters long. Fast charging for iPhone and iPad.',
        'Official 2m charging cable',
        29.99,
        34.99,
        400,
        'POPULAR',
        FALSE,
        4.6
    ),
    (
        '20W USB-C Power Adapter',
        'usbc-power-adapter-20w',
        'PWR20W',
        6,
        'Fast charging power adapter for iPhone and iPad. Compact and efficient.',
        'Fast 20W power adapter',
        19.99,
        24.99,
        500,
        'POPULAR',
        FALSE,
        4.6
    ),
    (
        'AirTag 4 Pack',
        'airtag-4-pack',
        'AIRTAG4',
        6,
        'Track your items with precision. Water resistant and easy to set up.',
        'Item trackers 4-pack',
        99.99,
        109.99,
        200,
        'POPULAR',
        TRUE,
        4.8
    ),
    (
        'Magic Mouse',
        'magic-mouse',
        'MMOUSE',
        6,
        'Wireless rechargeable mouse with Multi-Touch surface. Perfect for Mac.',
        'Apple wireless mouse',
        79.99,
        89.99,
        150,
        NULL,
        FALSE,
        4.5
    ),
    (
        'Magic Keyboard with Touch ID',
        'magic-keyboard-touch-id',
        'MKBTID',
        6,
        'Wireless keyboard with Touch ID and numeric keypad. Secure and convenient.',
        'Keyboard with Touch ID',
        199.99,
        219.99,
        80,
        NULL,
        FALSE,
        4.7
    ),
    (
        'Apple Silicone Case iPhone 17 Pro',
        'silicone-case-iphone-17-pro',
        'SCIP17P',
        6,
        'Official silicone case with MagSafe. Perfect fit and protection for iPhone 17 Pro.',
        'Official iPhone 17 Pro case',
        49.99,
        54.99,
        250,
        NULL,
        FALSE,
        4.7
    );
-- ============================
-- 5. PRODUCT_IMAGES TABLE
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
-- 6. PRODUCT_SPECIFICATIONS TABLE
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
-- 7. PRODUCT_COLORS TABLE
-- ============================
-- Product color variants
DROP TABLE IF EXISTS `product_colors`;
CREATE TABLE `product_colors` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `product_id` BIGINT NOT NULL,
    `color_name` VARCHAR(50) NOT NULL,
    `color_code` VARCHAR(7),
    -- Hex color code
    `additional_price` DECIMAL(10, 2) DEFAULT 0.00,
    `stock_quantity` INT DEFAULT 0,
    `is_available` BOOLEAN DEFAULT TRUE,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`product_id`) REFERENCES `products`(`id`) ON DELETE CASCADE,
    INDEX `idx_product_colors_product_id` (`product_id`)
);
-- ============================
-- 8. ORDERS TABLE
-- ============================
-- Customer orders
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL,
    `order_number` VARCHAR(50) UNIQUE NOT NULL,
    `status` VARCHAR(50) DEFAULT 'PENDING',
    `total_amount` DECIMAL(10, 2) NOT NULL,
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
-- 9. ORDER_ITEMS TABLE
-- ============================
-- Items within orders
DROP TABLE IF EXISTS `order_items`;
CREATE TABLE `order_items` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `order_id` BIGINT NOT NULL,
    `product_id` BIGINT NOT NULL,
    `quantity` INT NOT NULL,
    `unit_price` DECIMAL(10, 2) NOT NULL,
    `total_price` DECIMAL(10, 2) NOT NULL,
    `product_name` VARCHAR(255),
    -- Snapshot of product name at order time
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`order_id`) REFERENCES `orders`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`product_id`) REFERENCES `products`(`id`) ON DELETE RESTRICT,
    INDEX `idx_order_items_order_id` (`order_id`),
    INDEX `idx_order_items_product_id` (`product_id`)
);
-- ============================
-- 10. REVIEWS TABLE
-- ============================
-- Product reviews and ratings
DROP TABLE IF EXISTS `reviews`;
CREATE TABLE `reviews` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `product_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `rating` INT CHECK (
        `rating` >= 1
        AND `rating` <= 5
    ),
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
-- 11. WISHLISTS TABLE
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
-- 12. COUPONS TABLE
-- ============================
-- Discount coupons system
DROP TABLE IF EXISTS `coupons`;
CREATE TABLE `coupons` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `code` VARCHAR(50) UNIQUE NOT NULL,
    `description` VARCHAR(255),
    `discount_type` VARCHAR(20) NOT NULL,
    -- 'PERCENTAGE' or 'FIXED'
    `discount_value` DECIMAL(10, 2) NOT NULL,
    `minimum_order_amount` DECIMAL(10, 2) DEFAULT 0,
    `maximum_discount_amount` DECIMAL(10, 2),
    `usage_limit` INT DEFAULT NULL,
    -- NULL for unlimited
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
INSERT INTO `coupons` (
        `code`,
        `description`,
        `discount_type`,
        `discount_value`,
        `minimum_order_amount`,
        `valid_until`
    )
VALUES (
        'WELCOME10',
        'Welcome discount 10%',
        'PERCENTAGE',
        10.00,
        100.00,
        DATE_ADD(NOW(), INTERVAL 1 YEAR)
    ),
    (
        'SAVE50',
        'Save $50 on orders over $500',
        'FIXED',
        50.00,
        500.00,
        DATE_ADD(NOW(), INTERVAL 6 MONTH)
    ),
    (
        'FREESHIP',
        'Free shipping coupon',
        'FIXED',
        15.00,
        50.00,
        DATE_ADD(NOW(), INTERVAL 3 MONTH)
    );
-- ============================
-- 13. COUPON_USAGE TABLE
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
    FOREIGN KEY (`order_id`) REFERENCES `orders`(`id`) ON DELETE
    SET NULL,
        INDEX `idx_coupon_usage_coupon_id` (`coupon_id`),
        INDEX `idx_coupon_usage_user_id` (`user_id`)
);
-- ============================
-- 14. VOUCHER TABLE
-- ============================
-- Gift vouchers system
DROP TABLE IF EXISTS `voucher`;
CREATE TABLE `voucher` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `code` VARCHAR(50) UNIQUE NOT NULL,
    `amount` DECIMAL(10, 2) NOT NULL,
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
-- 15. USER_VOUCHER TABLE
-- ============================
-- Track voucher ownership and usage
DROP TABLE IF EXISTS `user_voucher`;
CREATE TABLE `user_voucher` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL,
    `voucher_id` BIGINT NOT NULL,
    `status` VARCHAR(20) DEFAULT 'ACTIVE',
    -- 'ACTIVE', 'USED', 'EXPIRED'
    `used_at` TIMESTAMP NULL,
    `order_id` BIGINT,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`voucher_id`) REFERENCES `voucher`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`order_id`) REFERENCES `orders`(`id`) ON DELETE
    SET NULL,
        INDEX `idx_user_voucher_user_id` (`user_id`),
        INDEX `idx_user_voucher_voucher_id` (`voucher_id`),
        INDEX `idx_user_voucher_status` (`status`)
);
-- ============================
-- 16. PRICING_RULES TABLE
-- ============================
-- Dynamic pricing rules
DROP TABLE IF EXISTS `pricing_rules`;
CREATE TABLE `pricing_rules` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `rule_name` VARCHAR(100) NOT NULL,
    `rule_type` VARCHAR(50) NOT NULL,
    -- 'BULK_DISCOUNT', 'TIME_BASED', 'USER_TYPE'
    `condition_data` JSON,
    `discount_type` VARCHAR(20),
    -- 'PERCENTAGE', 'FIXED'
    `discount_value` DECIMAL(10, 2),
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
-- 17. PRICE_ADJUSTMENT_LOGS TABLE
-- ============================
-- Track price changes and adjustments
DROP TABLE IF EXISTS `price_adjustment_logs`;
CREATE TABLE `price_adjustment_logs` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `product_id` BIGINT NOT NULL,
    `old_price` DECIMAL(10, 2) NOT NULL,
    `new_price` DECIMAL(10, 2) NOT NULL,
    `adjustment_reason` VARCHAR(255),
    `adjusted_by` BIGINT,
    -- user_id who made the change
    `pricing_rule_id` BIGINT,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`product_id`) REFERENCES `products`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`adjusted_by`) REFERENCES `users`(`id`) ON DELETE
    SET NULL,
        FOREIGN KEY (`pricing_rule_id`) REFERENCES `pricing_rules`(`id`) ON DELETE
    SET NULL,
        INDEX `idx_price_logs_product_id` (`product_id`),
        INDEX `idx_price_logs_date` (`created_at`)
);
-- ============================
-- 18. INVENTORY_MOVEMENTS TABLE
-- ============================
-- Track stock movements
DROP TABLE IF EXISTS `inventory_movements`;
CREATE TABLE `inventory_movements` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `product_id` BIGINT NOT NULL,
    `movement_type` VARCHAR(20) NOT NULL,
    -- 'IN', 'OUT', 'ADJUSTMENT'
    `quantity` INT NOT NULL,
    `previous_stock` INT NOT NULL,
    `new_stock` INT NOT NULL,
    `reference_type` VARCHAR(50),
    -- 'ORDER', 'PURCHASE', 'ADJUSTMENT'
    `reference_id` BIGINT,
    `notes` TEXT,
    `created_by` BIGINT,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`product_id`) REFERENCES `products`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`created_by`) REFERENCES `users`(`id`) ON DELETE
    SET NULL,
        INDEX `idx_inventory_movements_product_id` (`product_id`),
        INDEX `idx_inventory_movements_type` (`movement_type`),
        INDEX `idx_inventory_movements_date` (`created_at`)
);
-- ============================
-- 19. CONTACT_MESSAGES TABLE
-- ============================
-- Customer contact/support messages
DROP TABLE IF EXISTS `contact_messages`;
CREATE TABLE `contact_messages` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(100) NOT NULL,
    `email` VARCHAR(255) NOT NULL,
    `subject` VARCHAR(255),
    `message` TEXT NOT NULL,
    `status` VARCHAR(20) DEFAULT 'NEW',
    -- 'NEW', 'IN_PROGRESS', 'RESOLVED'
    `user_id` BIGINT,
    -- NULL for guest messages
    `assigned_to` BIGINT,
    -- admin user handling the message
    `response` TEXT,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE
    SET NULL,
        FOREIGN KEY (`assigned_to`) REFERENCES `users`(`id`) ON DELETE
    SET NULL,
        INDEX `idx_contact_messages_status` (`status`),
        INDEX `idx_contact_messages_user_id` (`user_id`),
        INDEX `idx_contact_messages_date` (`created_at`)
);
-- ============================
-- 20. FLYWAY_SCHEMA_HISTORY TABLE
-- ============================
-- Flyway database migration history (auto-created)
-- This table is managed by Flyway automatically
-- ============================
-- SAMPLE DATA QUERIES
-- ============================
-- Query all users
SELECT id,
    email,
    role,
    is_active,
    email_verified,
    created_at
FROM users
ORDER BY created_at DESC;
-- Query all products with stock
SELECT id,
    name,
    price,
    stock_quantity,
    category,
    brand,
    is_active
FROM products
WHERE is_active = TRUE
ORDER BY name;
-- Query orders with user information
SELECT o.id,
    o.order_number,
    u.email,
    o.status,
    o.total_amount,
    o.created_at
FROM orders o
    JOIN users u ON o.user_id = u.id
ORDER BY o.created_at DESC;
-- Query product reviews with ratings
SELECT p.name AS product_name,
    u.email,
    r.rating,
    r.comment,
    r.created_at
FROM reviews r
    JOIN products p ON r.product_id = p.id
    JOIN users u ON r.user_id = u.id
ORDER BY r.created_at DESC;
-- Query active coupons
SELECT code,
    description,
    discount_type,
    discount_value,
    valid_until
FROM coupons
WHERE is_active = TRUE
    AND (
        valid_until IS NULL
        OR valid_until > NOW()
    );
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