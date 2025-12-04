# 🍎 Apple Store E-commerce Backend

A comprehensive RESTful API backend for the Apple Store e-commerce application built with Spring Boot, MySQL, and JWT authentication.

## 📋 Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Database Schema](#database-schema)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [API Documentation](#api-documentation)
- [Configuration](#configuration)
- [Security](#security)
- [Deployment](#deployment)
- [Contributing](#contributing)
- [License](#license)

## ✨ Features

### Core Functionality
- ✅ **User Authentication & Authorization**
  - JWT-based authentication
  - Google OAuth2 integration
  - Role-based access control (Customer, Employee, Admin)
  - Email verification

- ✅ **Product Management**
  - CRUD operations for products
  - Multiple product images
  - Color variants
  - Product specifications
  - Stock management
  - Category organization

- ✅ **Shopping Cart**
  - Add/remove items
  - Update quantities
  - Session-based cart for guests
  - User-based cart persistence

- ✅ **Order Processing**
  - Multi-step checkout
  - Order tracking
  - Status management
  - Order history
  - Invoice generation

- ✅ **Review System**
  - Product ratings and reviews
  - Verified purchase badges
  - Review moderation
  - Helpful vote counting

- ✅ **Coupon System**
  - Percentage and fixed discounts
  - Usage limits
  - Expiration dates
  - Minimum order requirements

- ✅ **Admin Dashboard**
  - User management
  - Product management
  - Order management
  - Analytics and statistics
  - Settings configuration

- ✅ **Contact System**
  - Customer inquiry management
  - Admin responses
  - Priority levels
  - Status tracking

## 🛠️ Tech Stack

### Backend Framework
- **Spring Boot 3.2.0** - Application framework
- **Spring Data JPA** - Data persistence
- **Spring Security** - Authentication & authorization
- **Spring Web** - RESTful APIs
- **Spring Mail** - Email notifications

### Database
- **MySQL 8.0+** - Primary database
- **H2 Database** - Testing

### Security
- **JWT (JSON Web Tokens)** - Token-based authentication
- **BCrypt** - Password encryption
- **OAuth2** - Google authentication

### Tools & Libraries
- **Lombok** - Reduce boilerplate code
- **MapStruct** - Object mapping
- **Swagger/OpenAPI 3** - API documentation
- **Maven** - Dependency management
- **Commons IO** - File operations

### Development
- **Spring Boot DevTools** - Hot reload
- **JUnit 5** - Unit testing
- **Mockito** - Mocking framework

## 🗄️ Database Schema

### Core Tables (18 Tables)

#### 1. **users**
Stores user account information with authentication details.
```sql
- id, name, email, password_hash, phone
- role (customer/employee/admin)
- google_id, avatar_url
- is_active, email_verified
- created_at, updated_at, last_login
```

#### 2. **addresses**
Manages billing and shipping addresses for users.
```sql
- id, user_id, address_type (billing/shipping)
- full_name, street, city, state, zip_code, country
- phone, is_default
```

#### 3. **categories**
Product category hierarchy with parent-child relationships.
```sql
- id, name, slug, description
- image_url, parent_id
- is_active, display_order
```

#### 4. **products**
Main product catalog with pricing and stock information.
```sql
- id, name, slug, sku, category_id
- description, short_description
- price, original_price, stock, min_stock
- badge, is_active, image_url
- rating, reviews_count
```

#### 5. **product_images**
Multiple images per product with ordering.
```sql
- id, product_id, image_url
- alt_text, is_primary, display_order
```

#### 6. **product_colors**
Color variants for products with separate stock tracking.
```sql
- id, product_id, color_name, color_hex
- stock, display_order, is_available
```

#### 7. **product_specifications**
Key-value specifications for product features.
```sql
- id, product_id, spec_key, spec_value
- display_order
```

#### 8. **orders**
Order management with payment and shipping details.
```sql
- id, order_number, user_id, status, priority
- subtotal, discount_amount, tax_amount, shipping_amount, total
- billing_address_id, shipping_address_id
- payment_method, payment_status, transaction_id
- coupon_code, notes, tracking_number
- timestamps (created, updated, shipped, delivered)
```

#### 9. **order_items**
Line items for each order with snapshot of product data.
```sql
- id, order_id, product_id
- product_name, product_sku, product_image
- selected_color, quantity, price, subtotal
```

#### 10. **reviews**
Product reviews and ratings from customers.
```sql
- id, product_id, user_id, order_id
- rating (1-5), title, comment
- is_verified_purchase, is_approved
- helpful_count, status
```

#### 11. **coupons**
Discount codes with rules and limitations.
```sql
- id, code, description
- discount_type (percentage/fixed), discount_value
- min_order_amount, max_discount_amount
- usage_limit, used_count, is_active
- valid_from, valid_until
```

#### 12. **coupon_usage**
Tracking coupon redemptions per order.
```sql
- id, coupon_id, user_id, order_id
- discount_amount, used_at
```

#### 13. **carts**
Shopping cart for guest and registered users.
```sql
- id, user_id, session_id
- created_at, updated_at, expires_at
```

#### 14. **cart_items**
Items in shopping cart with selections.
```sql
- id, cart_id, product_id
- selected_color, quantity
- added_at, updated_at
```

#### 15. **wishlists**
Saved products for future purchase.
```sql
- id, user_id, product_id
- added_at
```

#### 16. **contact_messages**
Customer inquiries and support tickets.
```sql
- id, user_id, name, email
- subject, message, status
- priority, admin_reply
- replied_by, replied_at
```

#### 17. **admin_settings**
Configurable application settings.
```sql
- id, setting_key, setting_value
- setting_type, description, category
- updated_at, updated_by
```

#### 18. **activity_logs**
Audit trail for all system activities.
```sql
- id, user_id, action
- entity_type, entity_id
- old_values, new_values (JSON)
- ip_address, user_agent
- created_at
```

### Relationships
- One-to-Many: User → Orders, Product → Reviews
- Many-to-One: Order → User, Product → Category
- Many-to-Many: Users ↔ Products (via Wishlist)
- Self-referencing: Categories (parent-child)

## 📁 Project Structure

```
apple-store-backend/
├── src/
│   ├── main/
│   │   ├── java/com/applestore/
│   │   │   ├── AppleStoreApplication.java
│   │   │   │
│   │   │   ├── config/
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   ├── WebConfig.java
│   │   │   │   ├── JwtConfig.java
│   │   │   │   └── OpenApiConfig.java
│   │   │   │
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── UserController.java
│   │   │   │   ├── ProductController.java
│   │   │   │   ├── CategoryController.java
│   │   │   │   ├── OrderController.java
│   │   │   │   ├── CartController.java
│   │   │   │   ├── ReviewController.java
│   │   │   │   ├── CouponController.java
│   │   │   │   ├── WishlistController.java
│   │   │   │   ├── ContactController.java
│   │   │   │   ├── AddressController.java
│   │   │   │   └── AdminController.java
│   │   │   │
│   │   │   ├── model/
│   │   │   │   ├── User.java
│   │   │   │   ├── Address.java
│   │   │   │   ├── Category.java
│   │   │   │   ├── Product.java
│   │   │   │   ├── ProductImage.java
│   │   │   │   ├── ProductColor.java
│   │   │   │   ├── ProductSpecification.java
│   │   │   │   ├── Order.java
│   │   │   │   ├── OrderItem.java
│   │   │   │   ├── Review.java
│   │   │   │   ├── Coupon.java
│   │   │   │   ├── CouponUsage.java
│   │   │   │   ├── Cart.java
│   │   │   │   ├── CartItem.java
│   │   │   │   ├── Wishlist.java
│   │   │   │   ├── ContactMessage.java
│   │   │   │   ├── AdminSetting.java
│   │   │   │   └── ActivityLog.java
│   │   │   │
│   │   │   ├── repository/
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── ProductRepository.java
│   │   │   │   ├── OrderRepository.java
│   │   │   │   └── ... (all repositories)
│   │   │   │
│   │   │   ├── service/
│   │   │   │   ├── AuthService.java
│   │   │   │   ├── UserService.java
│   │   │   │   ├── ProductService.java
│   │   │   │   ├── OrderService.java
│   │   │   │   └── ... (all services)
│   │   │   │
│   │   │   ├── dto/
│   │   │   │   ├── request/
│   │   │   │   │   ├── LoginRequest.java
│   │   │   │   │   ├── RegisterRequest.java
│   │   │   │   │   └── ... (all requests)
│   │   │   │   └── response/
│   │   │   │       ├── AuthResponse.java
│   │   │   │       ├── ProductResponse.java
│   │   │   │       └── ... (all responses)
│   │   │   │
│   │   │   ├── security/
│   │   │   │   ├── JwtTokenProvider.java
│   │   │   │   ├── JwtAuthenticationFilter.java
│   │   │   │   ├── UserDetailsServiceImpl.java
│   │   │   │   └── OAuth2LoginSuccessHandler.java
│   │   │   │
│   │   │   ├── exception/
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   ├── ResourceNotFoundException.java
│   │   │   │   ├── BadRequestException.java
│   │   │   │   └── UnauthorizedException.java
│   │   │   │
│   │   │   └── util/
│   │   │       ├── DateUtil.java
│   │   │       ├── PriceCalculator.java
│   │   │       └── OrderNumberGenerator.java
│   │   │
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-dev.properties
│   │       ├── application-prod.properties
│   │       ├── data.sql
│   │       └── schema.sql
│   │
│   └── test/
│       └── java/com/applestore/
│           ├── controller/
│           ├── service/
│           └── repository/
│
├── .gitignore
├── pom.xml
├── README.md
├── DATABASE_SCHEMA.md
└── API_DOCUMENTATION.md
```

## 🚀 Getting Started

### Prerequisites

- **Java 17** or higher
- **Maven 3.8+**
- **MySQL 8.0+**
- **Git**
- IDE (IntelliJ IDEA, Eclipse, or VS Code)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/apple-store-backend.git
   cd apple-store-backend
   ```

2. **Create MySQL Database**
   ```sql
   CREATE DATABASE apple_store CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

3. **Configure Database**
   
   Edit `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/apple_store
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

4. **Configure JWT Secret**
   ```properties
   jwt.secret=YourVeryLongSecretKeyMinimum256BitsForHS512Algorithm
   jwt.expiration=86400000
   ```

5. **Build the project**
   ```bash
   mvn clean install
   ```

6. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

The API will be available at `http://localhost:8080`

### Quick Start with Sample Data

```bash
# The application will auto-create tables on first run
# To load sample data, run:
mvn spring-boot:run -Dspring-boot.run.arguments=--spring.jpa.hibernate.ddl-auto=create
```

## 📚 API Documentation

### Access Swagger UI
Once the application is running, visit:
```
http://localhost:8080/swagger-ui.html
```

### API Endpoints Overview

#### Authentication
```
POST   /api/auth/register          - Register new user
POST   /api/auth/login             - Login user
POST   /api/auth/google            - Google OAuth login
POST   /api/auth/refresh           - Refresh JWT token
POST   /api/auth/logout            - Logout user
```

#### Users
```
GET    /api/users/me               - Get current user profile
PUT    /api/users/me               - Update profile
PUT    /api/users/me/password      - Change password
GET    /api/users                  - Get all users (Admin)
GET    /api/users/{id}             - Get user by ID (Admin)
PUT    /api/users/{id}/role        - Update user role (Admin)
DELETE /api/users/{id}             - Soft delete user (Admin)
```

#### Products
```
GET    /api/products               - Get all products (paginated)
GET    /api/products/{id}          - Get product by ID
GET    /api/products/category/{id} - Get products by category
POST   /api/products               - Create product (Admin)
PUT    /api/products/{id}          - Update product (Admin)
DELETE /api/products/{id}          - Delete product (Admin)
GET    /api/products/search        - Search products
```

#### Categories
```
GET    /api/categories             - Get all categories
GET    /api/categories/{id}        - Get category by ID
POST   /api/categories             - Create category (Admin)
PUT    /api/categories/{id}        - Update category (Admin)
DELETE /api/categories/{id}        - Delete category (Admin)
```

#### Cart
```
GET    /api/cart                   - Get user's cart
POST   /api/cart/items             - Add item to cart
PUT    /api/cart/items/{id}        - Update cart item
DELETE /api/cart/items/{id}        - Remove cart item
DELETE /api/cart                   - Clear cart
```

#### Orders
```
GET    /api/orders                 - Get user's orders
GET    /api/orders/{id}            - Get order by ID
POST   /api/orders                 - Create order
PUT    /api/orders/{id}/status     - Update order status (Admin)
GET    /api/orders/admin           - Get all orders (Admin)
```

#### Reviews
```
GET    /api/reviews/product/{id}   - Get product reviews
POST   /api/reviews                - Create review
PUT    /api/reviews/{id}           - Update review
DELETE /api/reviews/{id}           - Delete review
PUT    /api/reviews/{id}/approve   - Approve review (Admin)
```

#### Coupons
```
GET    /api/coupons                - Get all coupons (Admin)
POST   /api/coupons                - Create coupon (Admin)
PUT    /api/coupons/{id}           - Update coupon (Admin)
DELETE /api/coupons/{id}           - Delete coupon (Admin)
POST   /api/coupons/validate       - Validate coupon code
```

#### Contact
```
GET    /api/contacts               - Get all messages (Admin)
POST   /api/contacts               - Send message
PUT    /api/contacts/{id}/reply    - Reply to message (Admin)
PUT    /api/contacts/{id}/status   - Update status (Admin)
```

#### Wishlist
```
GET    /api/wishlist               - Get user's wishlist
POST   /api/wishlist               - Add to wishlist
DELETE /api/wishlist/{id}          - Remove from wishlist
```

#### Admin Dashboard
```
GET    /api/admin/stats            - Get dashboard statistics
GET    /api/admin/sales            - Get sales data
GET    /api/admin/low-stock        - Get low stock products
```

### Response Format

#### Success Response
```json
{
  "success": true,
  "message": "Operation successful",
  "data": { ... },
  "timestamp": "2025-12-04T10:30:00Z"
}
```

#### Error Response
```json
{
  "success": false,
  "message": "Error description",
  "errors": ["Detailed error 1", "Detailed error 2"],
  "timestamp": "2025-12-04T10:30:00Z"
}
```

## ⚙️ Configuration

### Environment Variables

Create `.env` file or set environment variables:

```bash
# Database
DB_HOST=localhost
DB_PORT=3306
DB_NAME=apple_store
DB_USERNAME=root
DB_PASSWORD=yourpassword

# JWT
JWT_SECRET=YourVeryLongSecretKeyMinimum256BitsForHS512Algorithm
JWT_EXPIRATION=86400000

# Google OAuth
GOOGLE_CLIENT_ID=your-google-client-id
GOOGLE_CLIENT_SECRET=your-google-client-secret

# Email
EMAIL_USERNAME=your-email@gmail.com
EMAIL_PASSWORD=your-app-password

# CORS
CORS_ALLOWED_ORIGINS=http://localhost:5173,http://localhost:3000

# File Upload
UPLOAD_DIR=./uploads
MAX_FILE_SIZE=10MB
```

### Application Profiles

#### Development (`application-dev.properties`)
```properties
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
logging.level.com.applestore=DEBUG
```

#### Production (`application-prod.properties`)
```properties
spring.jpa.show-sql=false
spring.jpa.hibernate.ddl-auto=validate
logging.level.com.applestore=INFO
```

Run with profile:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
# or
java -jar target/apple-store-backend.jar --spring.profiles.active=prod
```

## 🔒 Security

### Authentication Flow

1. **Registration**
   - User submits registration form
   - Password is hashed with BCrypt
   - Verification email sent (optional)
   - User account created

2. **Login**
   - User submits credentials
   - Credentials validated
   - JWT token generated and returned
   - Token used for subsequent requests

3. **Authorization**
   - JWT token extracted from request header
   - Token validated and user extracted
   - User permissions checked
   - Access granted or denied

### Protected Endpoints

- **Public**: Register, Login, Browse Products
- **User**: Cart, Orders, Wishlist, Reviews
- **Admin**: User Management, Product Management, Dashboard

### Password Requirements

- Minimum 8 characters
- At least one uppercase letter
- At least one lowercase letter
- At least one number
- At least one special character

## 📊 Testing

### Run Tests
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=ProductServiceTest

# Run with coverage
mvn clean test jacoco:report
```

### Test Coverage
- Unit Tests: Controllers, Services, Repositories
- Integration Tests: API endpoints
- Security Tests: Authentication, Authorization

## 🚢 Deployment

### Docker Deployment

1. **Create Dockerfile**
   ```dockerfile
   FROM openjdk:17-jdk-slim
   VOLUME /tmp
   COPY target/*.jar app.jar
   ENTRYPOINT ["java","-jar","/app.jar"]
   ```

2. **Build and Run**
   ```bash
   docker build -t apple-store-backend .
   docker run -p 8080:8080 apple-store-backend
   ```

### Docker Compose

```yaml
version: '3.8'
services:
  db:
    image: mysql:8.0
    environment:
      MYSQL_DATABASE: apple_store
      MYSQL_ROOT_PASSWORD: yourpassword
    ports:
      - "3306:3306"
  
  backend:
    build: .
    ports:
      - "8080:8080"
    depends_on:
      - db
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://db:3306/apple_store
```

### Cloud Deployment Options

- **AWS**: Elastic Beanstalk, ECS, EC2
- **Azure**: App Service, Container Instances
- **Google Cloud**: App Engine, Cloud Run
- **Heroku**: Container Registry

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Coding Standards

- Follow Java naming conventions
- Write meaningful commit messages
- Add unit tests for new features
- Update documentation
- Use Lombok annotations
- Follow REST API best practices

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Authors

- **Your Name** - *Initial work* - [YourGitHub](https://github.com/yourusername)

## 🙏 Acknowledgments

- Spring Boot Team
- Vue.js Community
- Contributors and Testers

## 📞 Support

For support, email support@applestore.com or create an issue in the repository.

---

**Built with ❤️ using Spring Boot**

**Version**: 1.0.0  
**Last Updated**: December 4, 2025
