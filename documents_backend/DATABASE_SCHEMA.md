# 🗄️ Apple Store E-commerce - Database Schema Documentation

Complete database schema for the Apple Store E-commerce application with MySQL.

## 📊 Entity Relationship Diagram (ERD) Overview

```
Users ─┬─► Orders ──► Order_Items
       ├─► Addresses
       ├─► Reviews
       ├─► Carts ──► Cart_Items
       ├─► Wishlists
       └─► Contact_Messages

Categories ──► Products ─┬─► Product_Images
                         ├─► Product_Colors
                         ├─► Product_Specifications
                         ├─► Reviews
                         ├─► Order_Items
                         ├─► Cart_Items
                         └─► Wishlists

Coupons ──► Coupon_Usage ──► Orders

Admin_Settings
Activity_Logs
```

---

## 📋 Table Definitions

### 1. users
**Purpose**: Store user account information and authentication details.

```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) COMMENT 'NULL for OAuth users',
    phone VARCHAR(20),
    role ENUM('customer', 'employee', 'admin') DEFAULT 'customer',
    is_active BOOLEAN DEFAULT TRUE,
    google_id VARCHAR(255) UNIQUE COMMENT 'Google OAuth ID',
    avatar_url VARCHAR(500),
    email_verified BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    last_login TIMESTAMP NULL,
    total_orders INT DEFAULT 0,
    
    INDEX idx_email (email),
    INDEX idx_google_id (google_id),
    INDEX idx_role (role),
    INDEX idx_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

**Columns**:
- `id`: Unique identifier
- `email`: Unique email for login
- `password_hash`: BCrypt hashed password (NULL for OAuth)
- `role`: Access level (customer/employee/admin)
- `google_id`: For Google OAuth integration
- `is_active`: Soft delete flag
- `total_orders`: Cached order count

**Relationships**:
- Has many: Orders, Addresses, Reviews, Wishlists, Contact Messages
- Has one: Cart

---

### 2. addresses
**Purpose**: Store billing and shipping addresses for users.

```sql
CREATE TABLE addresses (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    address_type ENUM('billing', 'shipping') NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    street VARCHAR(255) NOT NULL,
    city VARCHAR(100) NOT NULL,
    state VARCHAR(50) NOT NULL,
    zip_code VARCHAR(20) NOT NULL,
    country VARCHAR(100) NOT NULL DEFAULT 'US',
    phone VARCHAR(20),
    is_default BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user (user_id),
    INDEX idx_type (address_type),
    INDEX idx_default (is_default)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

**Relationships**:
- Belongs to: User
- Referenced by: Orders (billing_address_id, shipping_address_id)

---

### 3. categories
**Purpose**: Hierarchical product categorization.

```sql
CREATE TABLE categories (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE,
    slug VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    image_url VARCHAR(500),
    parent_id BIGINT NULL COMMENT 'For subcategories',
    is_active BOOLEAN DEFAULT TRUE,
    display_order INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (parent_id) REFERENCES categories(id) ON DELETE SET NULL,
    INDEX idx_slug (slug),
    INDEX idx_parent (parent_id),
    INDEX idx_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

**Sample Data**:
```sql
INSERT INTO categories (name, slug, description) VALUES
('iPhone', 'iphone', 'Latest iPhone models'),
('MacBook', 'macbook', 'MacBook laptops'),
('iPad', 'ipad', 'iPad tablets'),
('Watch', 'watch', 'Apple Watch series'),
('AirPod', 'airpod', 'AirPods and audio devices');
```

**Relationships**:
- Self-referencing: Parent Category (for hierarchy)
- Has many: Products, Child Categories

---

### 4. products
**Purpose**: Main product catalog with pricing and stock.

```sql
CREATE TABLE products (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    slug VARCHAR(255) NOT NULL UNIQUE,
    sku VARCHAR(100) UNIQUE NOT NULL,
    category_id BIGINT NOT NULL,
    description TEXT,
    short_description VARCHAR(500),
    price DECIMAL(10, 2) NOT NULL,
    original_price DECIMAL(10, 2) COMMENT 'For showing discounts',
    stock INT DEFAULT 0,
    min_stock INT DEFAULT 10 COMMENT 'For low stock alerts',
    badge VARCHAR(50) COMMENT 'New, Best Seller, Sale, etc.',
    is_active BOOLEAN DEFAULT TRUE,
    image_url VARCHAR(500),
    rating DECIMAL(3, 2) DEFAULT 0.00 COMMENT 'Average rating (0-5)',
    reviews_count INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE,
    INDEX idx_category (category_id),
    INDEX idx_slug (slug),
    INDEX idx_sku (sku),
    INDEX idx_price (price),
    INDEX idx_stock (stock),
    INDEX idx_active (is_active),
    INDEX idx_rating (rating)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

**Business Rules**:
- SKU must be unique
- Stock tracked at product and color level
- Low stock alert when `stock < min_stock`
- Rating recalculated on new reviews

**Relationships**:
- Belongs to: Category
- Has many: Product Images, Product Colors, Product Specifications, Reviews, Order Items

---

### 5. product_images
**Purpose**: Multiple images per product with ordering.

```sql
CREATE TABLE product_images (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_id BIGINT NOT NULL,
    image_url VARCHAR(500) NOT NULL,
    alt_text VARCHAR(255),
    is_primary BOOLEAN DEFAULT FALSE,
    display_order INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    INDEX idx_product (product_id),
    INDEX idx_primary (is_primary)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

**Business Rules**:
- Only one primary image per product
- Images displayed by `display_order`

---

### 6. product_colors
**Purpose**: Color variants with separate stock tracking.

```sql
CREATE TABLE product_colors (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_id BIGINT NOT NULL,
    color_name VARCHAR(50) NOT NULL,
    color_hex VARCHAR(7) NOT NULL COMMENT 'e.g., #000000',
    stock INT DEFAULT 0,
    display_order INT DEFAULT 0,
    is_available BOOLEAN DEFAULT TRUE,
    
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    INDEX idx_product (product_id),
    INDEX idx_available (is_available)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

**Sample Data**:
```sql
INSERT INTO product_colors (product_id, color_name, color_hex, stock) VALUES
(1, 'Space Black', '#1d1d1f', 50),
(1, 'Silver', '#c0c0c0', 30),
(1, 'Gold', '#e8d7c3', 25);
```

---

### 7. product_specifications
**Purpose**: Key-value specifications for product features.

```sql
CREATE TABLE product_specifications (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_id BIGINT NOT NULL,
    spec_key VARCHAR(100) NOT NULL COMMENT 'e.g., Processor, Storage',
    spec_value TEXT NOT NULL,
    display_order INT DEFAULT 0,
    
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    INDEX idx_product (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

**Sample Data**:
```sql
INSERT INTO product_specifications (product_id, spec_key, spec_value) VALUES
(1, 'Processor', 'A17 Pro chip'),
(1, 'Display', '6.7-inch Super Retina XDR'),
(1, 'Camera', '48MP Main + 12MP Ultra Wide + 12MP Telephoto');
```

---

### 8. orders
**Purpose**: Order management with payment and shipping details.

```sql
CREATE TABLE orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_number VARCHAR(50) UNIQUE NOT NULL COMMENT 'e.g., #ORD-2025-001',
    user_id BIGINT NOT NULL,
    status ENUM('pending', 'processing', 'shipped', 'delivered', 'cancelled') DEFAULT 'pending',
    priority ENUM('low', 'medium', 'high') DEFAULT 'medium',
    
    -- Pricing
    subtotal DECIMAL(10, 2) NOT NULL,
    discount_amount DECIMAL(10, 2) DEFAULT 0.00,
    tax_amount DECIMAL(10, 2) DEFAULT 0.00,
    shipping_amount DECIMAL(10, 2) DEFAULT 0.00,
    total DECIMAL(10, 2) NOT NULL,
    
    -- Addresses
    billing_address_id BIGINT,
    shipping_address_id BIGINT,
    
    -- Payment
    payment_method VARCHAR(50),
    payment_status ENUM('pending', 'completed', 'failed', 'refunded') DEFAULT 'pending',
    transaction_id VARCHAR(255),
    
    -- Additional
    coupon_code VARCHAR(50),
    notes TEXT,
    tracking_number VARCHAR(100),
    
    -- Timestamps
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    shipped_at TIMESTAMP NULL,
    delivered_at TIMESTAMP NULL,
    
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (billing_address_id) REFERENCES addresses(id) ON DELETE SET NULL,
    FOREIGN KEY (shipping_address_id) REFERENCES addresses(id) ON DELETE SET NULL,
    INDEX idx_order_number (order_number),
    INDEX idx_user (user_id),
    INDEX idx_status (status),
    INDEX idx_payment_status (payment_status),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

**Status Flow**:
```
pending → processing → shipped → delivered
   ↓
cancelled (from any status)
```

**Business Rules**:
- Order immutable after creation (only status updates)
- Total = Subtotal - Discount + Tax + Shipping
- Tax rate: 10% (configurable)
- Free shipping for all orders

---

### 9. order_items
**Purpose**: Line items for each order with product snapshot.

```sql
CREATE TABLE order_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    product_name VARCHAR(255) NOT NULL COMMENT 'Snapshot at time of order',
    product_sku VARCHAR(100),
    product_image VARCHAR(500),
    selected_color VARCHAR(50),
    quantity INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL COMMENT 'Price at time of order',
    subtotal DECIMAL(10, 2) NOT NULL,
    
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    INDEX idx_order (order_id),
    INDEX idx_product (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

**Business Rules**:
- Store product data snapshot (immutable)
- Subtotal = Price × Quantity

---

### 10. reviews
**Purpose**: Product reviews and ratings from customers.

```sql
CREATE TABLE reviews (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    order_id BIGINT COMMENT 'Link to verified purchases',
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    title VARCHAR(255),
    comment TEXT,
    is_verified_purchase BOOLEAN DEFAULT FALSE,
    is_approved BOOLEAN DEFAULT TRUE,
    helpful_count INT DEFAULT 0,
    status ENUM('pending', 'approved', 'rejected') DEFAULT 'approved',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE SET NULL,
    INDEX idx_product (product_id),
    INDEX idx_user (user_id),
    INDEX idx_rating (rating),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

**Business Rules**:
- One review per user per product
- Verified purchase if linked to order
- Update product rating on new review

---

### 11. coupons
**Purpose**: Discount codes with rules and limitations.

```sql
CREATE TABLE coupons (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(50) UNIQUE NOT NULL,
    description VARCHAR(255),
    discount_type ENUM('percentage', 'fixed') DEFAULT 'percentage',
    discount_value DECIMAL(10, 2) NOT NULL,
    min_order_amount DECIMAL(10, 2) DEFAULT 0.00,
    max_discount_amount DECIMAL(10, 2) COMMENT 'For percentage discounts',
    usage_limit INT COMMENT 'NULL = unlimited',
    used_count INT DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    valid_from TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    valid_until TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_code (code),
    INDEX idx_active (is_active),
    INDEX idx_valid_dates (valid_from, valid_until)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

**Sample Data**:
```sql
INSERT INTO coupons (code, description, discount_type, discount_value) VALUES
('Miss-me', 'Miss Me 10% Off', 'percentage', 10.00),
('Love-you', 'Love You 15% Off', 'percentage', 15.00),
('Kiss-one', 'Kiss One 20% Off', 'percentage', 20.00);
```

---

### 12. coupon_usage
**Purpose**: Track coupon redemptions per order.

```sql
CREATE TABLE coupon_usage (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    coupon_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    order_id BIGINT NOT NULL,
    discount_amount DECIMAL(10, 2) NOT NULL,
    used_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (coupon_id) REFERENCES coupons(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    INDEX idx_coupon (coupon_id),
    INDEX idx_user (user_id),
    INDEX idx_order (order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

---

### 13. carts
**Purpose**: Shopping cart for guest and registered users.

```sql
CREATE TABLE carts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT COMMENT 'NULL for guest carts',
    session_id VARCHAR(255) COMMENT 'For guest users',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NULL COMMENT 'Auto-cleanup for abandoned carts',
    
    UNIQUE KEY unique_user_cart (user_id),
    INDEX idx_session (session_id),
    INDEX idx_expires (expires_at),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

**Business Rules**:
- One cart per user
- Guest carts expire after 30 days
- Convert guest cart to user cart on login

---

### 14. cart_items
**Purpose**: Items in shopping cart with selections.

```sql
CREATE TABLE cart_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    cart_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    selected_color VARCHAR(50),
    quantity INT NOT NULL DEFAULT 1,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (cart_id) REFERENCES carts(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    INDEX idx_cart (cart_id),
    INDEX idx_product (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

---

### 15. wishlists
**Purpose**: Saved products for future purchase.

```sql
CREATE TABLE wishlists (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    UNIQUE KEY unique_wishlist_item (user_id, product_id),
    INDEX idx_user (user_id),
    INDEX idx_product (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

---

### 16. contact_messages
**Purpose**: Customer inquiries and support tickets.

```sql
CREATE TABLE contact_messages (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT COMMENT 'NULL for guest messages',
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL,
    subject VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    status ENUM('unread', 'read', 'replied', 'archived') DEFAULT 'unread',
    priority ENUM('low', 'medium', 'high') DEFAULT 'medium',
    admin_reply TEXT,
    replied_by BIGINT,
    replied_at TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL,
    FOREIGN KEY (replied_by) REFERENCES users(id) ON DELETE SET NULL,
    INDEX idx_user (user_id),
    INDEX idx_status (status),
    INDEX idx_priority (priority),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

---

### 17. admin_settings
**Purpose**: Configurable application settings.

```sql
CREATE TABLE admin_settings (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    setting_key VARCHAR(100) UNIQUE NOT NULL,
    setting_value TEXT,
    setting_type VARCHAR(50) COMMENT 'string, number, boolean, json',
    description VARCHAR(255),
    category VARCHAR(50),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by BIGINT,
    
    FOREIGN KEY (updated_by) REFERENCES users(id) ON DELETE SET NULL,
    INDEX idx_key (setting_key),
    INDEX idx_category (category)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

**Sample Data**:
```sql
INSERT INTO admin_settings (setting_key, setting_value, setting_type, category) VALUES
('store_name', 'Apple Store', 'string', 'general'),
('store_email', 'support@applestore.com', 'string', 'general'),
('store_phone', '+855 966 273 314', 'string', 'general'),
('tax_rate', '0.10', 'number', 'pricing'),
('currency', 'USD', 'string', 'pricing');
```

---

### 18. activity_logs
**Purpose**: Audit trail for all system activities.

```sql
CREATE TABLE activity_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    action VARCHAR(100) NOT NULL,
    entity_type VARCHAR(50) COMMENT 'product, order, user, etc.',
    entity_id BIGINT,
    old_values JSON,
    new_values JSON,
    ip_address VARCHAR(45),
    user_agent TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL,
    INDEX idx_user (user_id),
    INDEX idx_entity (entity_type, entity_id),
    INDEX idx_action (action),
    INDEX idx_created (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

---

## 📐 Database Constraints

### Foreign Key Relationships

| Child Table | Foreign Key | Parent Table | On Delete |
|-------------|-------------|--------------|-----------|
| addresses | user_id | users | CASCADE |
| categories | parent_id | categories | SET NULL |
| products | category_id | categories | CASCADE |
| product_images | product_id | products | CASCADE |
| product_colors | product_id | products | CASCADE |
| product_specifications | product_id | products | CASCADE |
| orders | user_id | users | CASCADE |
| orders | billing_address_id | addresses | SET NULL |
| orders | shipping_address_id | addresses | SET NULL |
| order_items | order_id | orders | CASCADE |
| order_items | product_id | products | CASCADE |
| reviews | product_id | products | CASCADE |
| reviews | user_id | users | CASCADE |
| reviews | order_id | orders | SET NULL |
| coupon_usage | coupon_id | coupons | CASCADE |
| coupon_usage | user_id | users | CASCADE |
| coupon_usage | order_id | orders | CASCADE |
| carts | user_id | users | CASCADE |
| cart_items | cart_id | carts | CASCADE |
| cart_items | product_id | products | CASCADE |
| wishlists | user_id | users | CASCADE |
| wishlists | product_id | products | CASCADE |
| contact_messages | user_id | users | SET NULL |
| contact_messages | replied_by | users | SET NULL |
| admin_settings | updated_by | users | SET NULL |
| activity_logs | user_id | users | SET NULL |

---

## 🔍 Indexes Strategy

### Primary Indexes
- All tables have `PRIMARY KEY` on `id`

### Foreign Key Indexes
- All foreign keys are indexed automatically

### Search Optimization
- `users.email`: Unique login lookup
- `products.slug`: URL-friendly product pages
- `products.sku`: Inventory management
- `orders.order_number`: Order tracking
- `coupons.code`: Coupon validation

### Filter Optimization
- `products.category_id`: Category browsing
- `orders.status`: Order filtering
- `reviews.rating`: Review filtering
- Date indexes for reporting

---

## 💾 Storage Considerations

### Estimated Table Sizes

| Table | Rows (Est.) | Size per Row | Total Size |
|-------|-------------|--------------|------------|
| users | 10,000 | ~1 KB | 10 MB |
| products | 1,000 | ~2 KB | 2 MB |
| product_images | 5,000 | ~500 B | 2.5 MB |
| orders | 50,000 | ~800 B | 40 MB |
| order_items | 150,000 | ~400 B | 60 MB |
| reviews | 10,000 | ~1 KB | 10 MB |
| activity_logs | 500,000 | ~600 B | 300 MB |

**Total Estimated**: ~500 MB (excluding images stored on disk)

---

## 🔄 Data Migration Scripts

### Initial Schema Creation
```sql
-- Run all CREATE TABLE statements in order
-- Then insert sample data
SOURCE schema.sql;
SOURCE data.sql;
```

### Sample Data Script
```sql
-- data.sql
START TRANSACTION;

-- Categories
INSERT INTO categories (name, slug, description) VALUES
('iPhone', 'iphone', 'Latest iPhone models'),
('MacBook', 'macbook', 'MacBook laptops'),
('iPad', 'ipad', 'iPad tablets'),
('Watch', 'watch', 'Apple Watch series'),
('AirPod', 'airpod', 'AirPods and audio devices');

-- Admin User (password: Admin@123)
INSERT INTO users (name, email, password_hash, role) VALUES
('Admin User', 'admin@applestore.com', '$2a$10$dummyhashforexample', 'admin');

-- Coupons
INSERT INTO coupons (code, description, discount_type, discount_value) VALUES
('Miss-me', 'Miss Me 10% Off', 'percentage', 10.00),
('Love-you', 'Love You 15% Off', 'percentage', 15.00),
('Kiss-one', 'Kiss One 20% Off', 'percentage', 20.00);

COMMIT;
```

---

## 🔒 Security Best Practices

1. **Password Storage**: Use BCrypt hashing (never plain text)
2. **SQL Injection**: Use prepared statements/parameterized queries
3. **Access Control**: Implement row-level security
4. **Audit Logging**: Track all sensitive operations
5. **Data Encryption**: Encrypt sensitive columns (payment info)
6. **Backup Strategy**: Daily automated backups
7. **Connection Pooling**: Use HikariCP for connection management

---

## 📊 Performance Optimization

### Query Optimization Tips
1. Use appropriate indexes
2. Avoid SELECT * queries
3. Use JOIN instead of subqueries when possible
4. Implement pagination for large result sets
5. Cache frequently accessed data (Redis)
6. Use database connection pooling

### Maintenance Tasks
```sql
-- Analyze tables monthly
ANALYZE TABLE products, orders, order_items, users;

-- Optimize tables quarterly
OPTIMIZE TABLE products, orders, order_items;

-- Clean up old data
DELETE FROM activity_logs WHERE created_at < DATE_SUB(NOW(), INTERVAL 1 YEAR);
DELETE FROM carts WHERE expires_at < NOW();
```

---

**Version**: 1.0  
**Last Updated**: December 4, 2025  
**Database Engine**: MySQL 8.0+
