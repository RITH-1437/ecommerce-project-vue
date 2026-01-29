/**
 * Domain Model Entities
 * 
 * This package contains all JPA entity classes for the Apple Store E-Commerce
 * application.
 * 
 * Key Entities:
 * - User: Customer and admin user accounts
 * - Product: Product catalog with variants and specifications
 * - Category: Product categorization
 * - Order: Customer orders and order items
 * - Cart: Shopping cart functionality
 * - Review: Product reviews and ratings
 * - Coupon/Voucher: Discount and promotion management
 * - Address: Customer shipping addresses
 * - Payment: Payment transaction records
 * 
 * All entities use Hibernate annotations and follow JPA best practices:
 * - @Entity and @Table for mapping
 * - @Index for query optimization
 * - Lazy loading for relationships
 * - Proper cascade and fetch strategies
 * - Auditing timestamps via @PrePersist and @PreUpdate
 * 
 * Naming Convention:
 * - Table names: snake_case (users, product_images)
 * - Column names: snake_case (created_at, user_id)
 * - Java fields: camelCase (createdAt, userId)
 */
package com.demo.backend.model;
