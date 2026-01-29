# Ecommerce Backend Features

This document describes the key features implemented in the ecommerce backend project.

## Authentication and Authorization
- JWT-based authentication with login, register, refresh token, and logout endpoints.
- OAuth2 integration with Google for social login.
- Role-based access control (User, Admin roles).

## User Management
- CRUD operations for users.
- User search and filtering.
- Password change functionality.
- User profiles with email verification.

## Product Management
- Full CRUD for products, including categories, images, colors, and specifications.
- Product search and filtering by category, slug, etc.
- Inventory tracking with stock levels.

## Shopping Cart
- Cart creation and management for authenticated and guest users.
- Add, update, and remove cart items.
- Session-based carts for non-logged-in users.

## Wishlist
- Add and remove products from user wishlists.
- Retrieve wishlist items by user.

## Order Management
- Order creation, retrieval, and updates.
- Order status tracking (e.g., pending, shipped, delivered).
- Order items management.
- Order history for users.

## Payment Integration
- ABA Payway payment gateway integration.
- QR code payment support.
- Payment status checking and callbacks.
- Secure payment processing.

## Reviews and Ratings
- Product reviews with ratings.
- Review approval workflow (pending, approved, rejected).
- Retrieve reviews by product or user.

## Coupon System
- Create and manage discount coupons.
- Coupon usage tracking per user and order.
- Support for percentage and fixed amount discounts.

## Address Management
- User address CRUD operations.
- Multiple addresses per user (billing, shipping).

## Customer Support
- Contact message system for customer inquiries.
- Message status management (open, in progress, resolved).

## Admin Features
- Admin settings for configuration.
- User management for admins.
- Activity logging for audit trails.

## AI Features
- AI-powered chat for customer assistance.
- Product comparison using AI to analyze specifications.

## Inventory Management
- Track inventory movements (in, out, adjustments).
- Stock level monitoring.

## Additional Features
- Global exception handling.
- Security configurations with CORS and CSRF protection.
- Refresh token management for session persistence.

This backend is built with Spring Boot, providing RESTful APIs for a complete ecommerce platform.