# 🔥 Hibernate Quick Reference

## ✅ What Was Fixed & Optimized

### 1. Enhanced Hibernate Configuration

Added comprehensive Hibernate settings in `application.properties`:

- ✅ MySQL 8 Dialect configuration
- ✅ Batch processing (20 operations per batch)
- ✅ Query plan cache optimization
- ✅ Proper naming strategy (camelCase → snake_case)
- ✅ JSON type support configuration

### 2. HikariCP Connection Pool

Optimized database connection pooling:

- ✅ Max pool size: 10 connections
- ✅ Min idle: 5 connections
- ✅ Connection timeout: 20 seconds
- ✅ Idle timeout: 5 minutes
- ✅ Max lifetime: 20 minutes
- ✅ Auto-commit disabled for better performance

### 3. Created Configuration Classes

#### JpaConfig.java

```java
@Configuration
@EnableJpaAuditing
@EnableTransactionManagement
@EnableJpaRepositories
```

- Enables JPA auditing for automatic timestamps
- Enables transaction management
- Configures repository scanning

#### HibernateTypeConfig.java

```java
@Configuration
public class HibernateTypeConfig {
    @Bean
    @Primary
    public ObjectMapper objectMapper()
}
```

- Configures JSON serialization for Hibernate JSON columns
- Handles Java 8 date/time types properly

### 4. Documentation

Created comprehensive documentation:

- ✅ `HIBERNATE_CONFIGURATION.md` - Complete Hibernate guide
- ✅ `package-info.java` - Entity package documentation

## 📊 Entity Inventory (25 Total)

### ✅ All Entities Properly Configured

| Entity               | Table Name             | Special Features                   |
| -------------------- | ---------------------- | ---------------------------------- |
| User                 | users                  | Email index, Role enum             |
| Product              | products               | TEXT description, Multiple indexes |
| Category             | categories             | TEXT description                   |
| ProductImage         | product_images         | Image URL storage                  |
| ProductColor         | product_colors         | Color variants                     |
| ProductSpecification | product_specifications | TEXT spec values                   |
| Order                | orders                 | TEXT notes, Order status           |
| OrderItem            | order_items            | Order line items                   |
| Cart                 | carts                  | User shopping cart                 |
| CartItem             | cart_items             | Cart line items                    |
| Coupon               | coupons                | Discount codes                     |
| CouponUsage          | coupon_usage           | Usage tracking                     |
| Voucher              | voucher                | Promotional vouchers               |
| UserVoucher          | user_voucher           | User-voucher mapping               |
| Review               | reviews                | TEXT content, Rating               |
| Wishlist             | wishlists              | User wishlists                     |
| ActivityLog          | activity_logs          | **JSON columns**                   |
| AdminSetting         | admin_settings         | TEXT values                        |
| Alert                | alerts                 | TEXT message/metadata              |
| ContactMessage       | contact_messages       | TEXT message                       |
| Address              | addresses              | Shipping addresses                 |
| RefreshToken         | refresh_tokens         | JWT tokens                         |
| InventoryMovement    | inventory_movements    | Stock tracking                     |
| PricingRule          | pricing_rules          | Dynamic pricing                    |
| PriceAdjustmentLog   | price_adjustment_logs  | Price history                      |

## 🎯 Key Hibernate Features in Use

### 1. JSON Column Support ✅

Using `hibernate-types-60` library:

```java
@Type(JsonType.class)
@Column(columnDefinition = "json")
private Map<String, Object> metadata;
```

### 2. TEXT Columns ✅

For large content:

```java
@Column(columnDefinition = "TEXT")
private String description;
```

### 3. Indexes ✅

All entities have proper indexes:

```java
@Table(name = "products", indexes = {
    @Index(name = "idx_category", columnList = "category_id"),
    @Index(name = "idx_slug", columnList = "slug"),
    @Index(name = "idx_price", columnList = "price")
})
```

### 4. Lazy Loading ✅

All relationships use lazy loading:

```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "category_id")
private Category category;
```

### 5. Automatic Timestamps ✅

Using lifecycle callbacks:

```java
@PrePersist
public void onCreate() {
    createdAt = LocalDateTime.now();
}

@PreUpdate
public void onUpdate() {
    updatedAt = LocalDateTime.now();
}
```

## 🚀 Performance Optimizations

### Enabled Features:

1. ✅ **Batch Processing** - Reduces DB round-trips
2. ✅ **Query Plan Cache** - Faster query execution
3. ✅ **Connection Pooling** - Efficient connection management
4. ✅ **Lazy Loading** - Load data only when needed
5. ✅ **Indexed Queries** - Fast lookups on key columns
6. ✅ **Ordered Operations** - Better batch processing

### Monitoring:

```properties
# Enable for debugging (disable in production)
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Disable in production for performance
spring.jpa.properties.hibernate.generate_statistics=false
```

## 📝 Quick Commands

### View Hibernate SQL Logs

Check your application logs for formatted SQL:

```
Hibernate:
    select
        u1_0.id,
        u1_0.email,
        ...
    from
        users u1_0
```

### Common Annotations Reference

```java
@Entity                          // Marks as JPA entity
@Table(name = "table_name")     // Custom table name
@Id                              // Primary key
@GeneratedValue(strategy = ...)  // Auto-increment
@Column(...)                     // Column configuration
@ManyToOne                       // Many-to-one relationship
@OneToMany                       // One-to-many relationship
@JoinColumn(name = "...")        // Foreign key
@Index(...)                      // Database index
@Enumerated(EnumType.STRING)     // Enum as string
@PrePersist                      // Before insert
@PreUpdate                       // Before update
```

## ✅ Status: Complete

All Hibernate configuration is properly set up and optimized. The application is ready to run with:

- ✅ 25 entities properly mapped
- ✅ Optimal performance settings
- ✅ Connection pool configured
- ✅ JSON column support
- ✅ Comprehensive indexing
- ✅ Transaction management
- ✅ Auditing enabled

## 🎯 What You See in Terminal

When Hibernate runs, you'll see:

```
Hibernate: alter table activity_logs modify column user_agent TEXT
Hibernate: alter table admin_settings modify column setting_value TEXT
...
```

This is **normal** - Hibernate is updating your database schema to match your entity definitions. It will:

1. Check each table structure
2. Modify columns if needed (VARCHAR → TEXT)
3. Add indexes if missing
4. Update constraints

## 📚 Files Created/Modified

### Modified:

1. ✅ `application.properties` - Enhanced Hibernate config

### Created:

1. ✅ `JpaConfig.java` - JPA configuration class
2. ✅ `HibernateTypeConfig.java` - JSON type configuration
3. ✅ `package-info.java` - Entity package documentation
4. ✅ `HIBERNATE_CONFIGURATION.md` - Complete guide
5. ✅ `HIBERNATE_QUICK_REFERENCE.md` - This file

## 🎉 All Done!

Your Hibernate configuration is production-ready and fully optimized!
