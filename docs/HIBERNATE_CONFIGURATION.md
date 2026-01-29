# 🔥 Hibernate Configuration Guide

## ✅ Overview

This project uses **Hibernate 6.x** (via Spring Boot 3.3.5) as the JPA implementation for database operations with MySQL 8.0.

## 📋 Configuration Summary

### 1. **Hibernate Properties** (application.properties)

```properties
# DDL Auto - Update schema automatically
spring.jpa.hibernate.ddl-auto=update

# SQL Logging
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Prevent N+1 query issues
spring.jpa.open-in-view=false

# MySQL 8 Dialect
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect

# Performance Optimizations
spring.jpa.properties.hibernate.jdbc.batch_size=20
spring.jpa.properties.hibernate.order_inserts=true
spring.jpa.properties.hibernate.order_updates=true

# Naming Strategy
spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy
```

### 2. **Entity Classes**

All entities are located in `com.demo.backend.model` package:

#### Core Entities:

- ✅ **User** - User accounts (customers, admins)
- ✅ **Product** - Product catalog
- ✅ **Category** - Product categories
- ✅ **ProductImage** - Product images
- ✅ **ProductColor** - Product color variants
- ✅ **ProductSpecification** - Product specs

#### Order Management:

- ✅ **Order** - Customer orders
- ✅ **OrderItem** - Order line items
- ✅ **Cart** - Shopping carts
- ✅ **CartItem** - Cart items

#### Marketing & Promotions:

- ✅ **Coupon** - Discount coupons
- ✅ **CouponUsage** - Coupon usage tracking
- ✅ **Voucher** - Promotional vouchers
- ✅ **UserVoucher** - User voucher mapping
- ✅ **PricingRule** - Dynamic pricing rules

#### Reviews & Wishlist:

- ✅ **Review** - Product reviews
- ✅ **Wishlist** - User wishlists

#### System & Admin:

- ✅ **ActivityLog** - User activity tracking (with JSON columns)
- ✅ **AdminSetting** - Admin configuration
- ✅ **Alert** - System alerts
- ✅ **ContactMessage** - Contact form messages
- ✅ **Address** - Shipping addresses
- ✅ **RefreshToken** - JWT refresh tokens

#### Inventory:

- ✅ **InventoryMovement** - Stock movements
- ✅ **PriceAdjustmentLog** - Price change history

### 3. **Special Features**

#### JSON Type Support

The project uses `hibernate-types-60` library for JSON column support:

```java
@Type(JsonType.class)
@Column(columnDefinition = "json")
private Map<String, Object> metadata;
```

#### TEXT Columns

For large text content:

```java
@Column(columnDefinition = "TEXT")
private String description;
```

#### Indexes

All entities have proper indexes for performance:

```java
@Table(name = "users", indexes = {
    @Index(name = "idx_email", columnList = "email"),
    @Index(name = "idx_role", columnList = "role")
})
```

#### Auditing

Automatic timestamps using lifecycle callbacks:

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

### 4. **HikariCP Connection Pool**

Optimized connection pool settings:

```properties
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=20000
spring.datasource.hikari.idle-timeout=300000
spring.datasource.hikari.max-lifetime=1200000
spring.datasource.hikari.auto-commit=false
```

## 🚀 Performance Features

### 1. **Batch Processing**

- Batch size: 20 operations
- Ordered inserts and updates
- Reduces database round-trips

### 2. **Query Plan Cache**

- Plan cache: 2048 entries
- Parameter metadata cache: 128 entries
- Faster query execution

### 3. **Lazy Loading**

- All relationships use `FetchType.LAZY`
- Prevents N+1 query problems
- Better memory management

### 4. **Open-in-View Disabled**

- Forces explicit transaction boundaries
- Prevents lazy loading exceptions
- Better performance

## 📊 Database Schema Management

### DDL Auto Modes:

- **update** (current) - Updates schema without data loss
- **create** - Drops and recreates (development only)
- **create-drop** - Drops on shutdown (testing)
- **validate** - Validates schema matches entities
- **none** - No automatic schema management

### Flyway Integration:

The project also uses Flyway for versioned migrations:

```properties
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true
spring.flyway.locations=classpath:db/migration
```

## 🔧 Configuration Classes

### JpaConfig

- Enables JPA Auditing
- Configures repository scanning
- Enables transaction management

### HibernateTypeConfig

- Configures ObjectMapper for JSON serialization
- Handles Java 8 date/time types
- Optimizes JSON column handling

## 📝 Best Practices

### ✅ DO:

1. Use `@Builder` with Lombok for entity creation
2. Use indexes on frequently queried columns
3. Use `@PrePersist` and `@PreUpdate` for timestamps
4. Use `FetchType.LAZY` for relationships
5. Use `@Transactional` on service methods
6. Use DTOs for API responses (not entities directly)

### ❌ DON'T:

1. Don't use `FetchType.EAGER` unless necessary
2. Don't expose entities directly in REST controllers
3. Don't use `cascade = CascadeType.ALL` without careful consideration
4. Don't enable `show-sql` in production
5. Don't use `ddl-auto=update` in production (use Flyway instead)

## 🐛 Common Issues & Solutions

### Issue: LazyInitializationException

**Solution:** Use `@Transactional` on service methods or fetch data within transaction

### Issue: N+1 Query Problem

**Solution:** Use JOIN FETCH in JPQL queries or EntityGraph

### Issue: JSON Column Not Working

**Solution:** Ensure `hibernate-types-60` dependency is present

### Issue: Column Type Mismatch

**Solution:** Use `columnDefinition` attribute in `@Column`

## 📚 References

- [Hibernate 6 Documentation](https://hibernate.org/orm/documentation/6.0/)
- [Spring Data JPA Reference](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Hibernate Types Library](https://github.com/vladmihalcea/hibernate-types)
- [HikariCP Configuration](https://github.com/brettwooldridge/HikariCP#configuration-knobs-baby)

## ✅ Current Status

✅ Hibernate is properly configured and running
✅ All 25 entities are mapped correctly
✅ JSON type support is working
✅ Connection pool is optimized
✅ Performance features are enabled
✅ Schema updates are automatic

## 🎯 Next Steps (Production Deployment)

1. Change `ddl-auto` from `update` to `validate`
2. Disable SQL logging (`show-sql=false`)
3. Enable Hibernate statistics monitoring if needed
4. Review and optimize slow queries
5. Consider adding database caching (Redis, etc.)
6. Set up database replication for read operations
