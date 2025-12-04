# Apple Store E-Commerce - Project Structure

## 📁 Directory Structure

```
Apple-store/
│
├── 📄 index.html                      # Main HTML entry point
├── 📄 package.json                    # Project dependencies and scripts
├── 📄 vite.config.js                  # Vite build configuration
├── 📄 vitest.config.js                # Vitest testing configuration
├── 📄 jsconfig.json                   # JavaScript configuration
├── 📄 README.md                       # Project README
│
├── 📁 document/                       # Documentation folder
│   ├── 📄 PROJECT_FEATURES.md         # Complete features documentation
│   ├── 📄 PROJECT_STRUCTURE.md        # This file - project organization
│   ├── 📄 TODO_LIST.md                # Development tasks and todos
│   ├── 📄 REPORT.md                   # Project report
│   ├── 📄 GOOGLE_OAUTH_SETUP.md       # Google OAuth setup guide
│   ├── 📄 GOOGLE_SETUP_QUICK.md       # Quick Google setup guide
│   └── 📄 FIX_GOOGLE_OAUTH_ERROR.md   # OAuth troubleshooting
│
├── 📁 public/                         # Static public assets
│   └── [Static files served as-is]
│
└── 📁 src/                            # Source code directory
    │
    ├── 📄 App.vue                     # Root Vue component
    ├── 📄 main.js                     # Application entry point
    │
    ├── 📁 assets/                     # Project assets
    │   ├── 📄 base.css                # Base/reset styles
    │   ├── 📄 main.css                # Main global styles
    │   ├── 📁 images/                 # Image assets
    │   │   └── 📁 footer-icon/        # Footer icons
    │   └── 📁 styles/                 # Additional stylesheets
    │       ├── 📄 AdminStyles.css     # Admin-specific styles
    │       └── 📄 global.css          # Global style utilities
    │
    ├── 📁 components/                 # Reusable Vue components
    │   │
    │   ├── 📁 admin/                  # Admin-related components
    │   │   ├── 📄 DiscountModal.vue   # Discount management modal
    │   │   ├── 📄 ProductModal.vue    # Product management modal
    │   │   └── 📄 UserModal.vue       # User management modal
    │   │
    │   ├── 📁 demo/                   # Demo/example components
    │   │   └── 📄 PropsDemo.vue       # Props demonstration
    │   │
    │   ├── 📁 global/                 # Global shared components
    │   │   └── 📄 LocationPopup.vue   # Location selection popup
    │   │
    │   ├── 📁 layout/                 # Layout components
    │   │   ├── 📄 AppFooter.vue       # Application footer
    │   │   └── 📄 AppHeader.vue       # Application header/nav
    │   │
    │   └── 📁 product/                # Product-related components
    │       ├── 📄 AirPodsPage.vue     # AirPods product page template
    │       ├── 📄 iPhonePage.vue      # iPhone product page template
    │       ├── 📄 MacBookPage.vue     # MacBook product page template
    │       ├── 📄 ProductCard.vue     # Individual product card
    │       ├── 📄 ProductGrid.vue     # Product grid layout
    │       ├── 📄 ProductPage.vue     # Generic product page template
    │       ├── 📄 ProductRating.vue   # Product rating display
    │       └── 📄 WatchPage.vue       # Watch product page template
    │
    ├── 📁 composables/                # Vue composables (reusable logic)
    │   ├── 📄 useGoogleAuth.js        # Google authentication composable
    │   ├── 📄 useProductFilter.js     # Product filtering logic
    │   └── 📄 useProps.js             # Props utilities
    │
    ├── 📁 router/                     # Vue Router configuration
    │   └── 📄 index.js                # Router setup and routes
    │
    ├── 📁 stores/                     # Pinia stores (state management)
    │   ├── 📄 auth.js                 # Authentication store
    │   ├── 📄 counter.js              # Cart store (counter = cart)
    │   └── 📄 dashboard.js            # Dashboard/admin store
    │
    ├── 📁 utils/                      # Utility functions
    │   └── 📄 productUtils.js         # Product-related utilities
    │
    └── 📁 views/                      # Page-level Vue components
        │
        ├── 📄 AirPodsView.vue         # AirPods category page
        ├── 📄 AuthView.vue            # Authentication page (login/register)
        ├── 📄 CustomerOrdersView.vue  # Customer orders page
        ├── 📄 DashboardView.vue       # Admin dashboard overview
        ├── 📄 HomeView.vue            # Homepage
        ├── 📄 IPadView.vue            # iPad category page
        ├── 📄 IPhoneView.vue          # iPhone category page
        ├── 📄 MacBookView.vue         # MacBook category page
        ├── 📄 ProductDetailView.vue   # Individual product details
        ├── 📄 WatchView.vue           # Watch category page
        │
        ├── 📁 admin/                  # Admin pages
        │   ├── 📄 AdminCategoriesView.vue   # Category management
        │   ├── 📄 AdminDiscountsView.vue    # Discount management
        │   ├── 📄 AdminOrdersView.vue       # Order management
        │   ├── 📄 AdminProductsView.vue     # Product management
        │   ├── 📄 AdminReviewsView.vue      # Review management
        │   ├── 📄 AdminSettingsView.vue     # Settings management
        │   └── 📄 AdminUsersView.vue        # User management
        │
        └── 📁 checkout/               # Checkout flow pages
            ├── 📄 CheckoutCartView.vue      # Cart review page (Step 1)
            ├── 📄 CheckoutPaymentView.vue   # Payment details page (Step 2)
            └── 📄 CheckoutReceiptView.vue   # Order receipt page (Step 3)
```

---

## 🗂️ Component Hierarchy

### Layout Structure

```
App.vue (Root)
│
├── AppHeader (Global Navigation)
│   ├── Logo/Brand
│   ├── Search Bar
│   ├── Category Links
│   ├── Cart Icon (with badge)
│   └── User Profile/Auth Buttons
│
├── Router View (Dynamic Content)
│   └── [Current Route Component]
│
└── AppFooter (Global Footer)
    ├── Links
    ├── Social Media
    └── Copyright Info
```

### Page Structures

#### 🏠 Home Page (HomeView.vue)

```
HomeView
├── Hero Section
│   ├── Gradient Background
│   ├── Animated Elements
│   └── CTA Buttons
│
├── Popular Products Slider
│   └── ProductCard (multiple)
│
├── Featured Categories Grid
│   └── Category Cards
│
├── Product Highlights
│   └── Highlight Cards
│
├── Statistics Section
│
├── Testimonials Carousel
│
└── Newsletter Signup
```

#### 📱 Category Pages (e.g., IPhoneView.vue)

```
[Category]View
└── ProductPage Component
    ├── Page Header
    ├── Product Sections (by series)
    │   └── ProductGrid
    │       └── ProductCard (multiple)
    └── Category Description
```

#### 🛒 Checkout Flow

```
Step 1: CheckoutCartView
├── Progress Bar Card
├── Cart Items List
│   └── Cart Item (multiple)
│       ├── Product Image
│       ├── Details
│       ├── Quantity Controls
│       └── Remove Button
└── Order Summary Sidebar
    ├── Promo Code Input
    ├── Price Breakdown
    └── Proceed Button

Step 2: CheckoutPaymentView
├── Progress Bar Card
├── Payment Method Selection
│   ├── Credit/Debit Card
│   ├── PayPal
│   └── Apple Pay
├── Payment Form
└── Order Summary Sidebar

Step 3: CheckoutReceiptView
├── Progress Bar Card
├── Success Message
├── Order Details
├── Payment Info
├── Delivery Address
└── Action Buttons
```

#### 🔐 Authentication (AuthView.vue)

```
AuthView
├── Auth Header
│   ├── Back Link
│   └── Title
│
└── Auth Form Container
    ├── Tab Switcher (Login/Register)
    │
    ├── Login Form
    │   ├── Email Input
    │   ├── Password Input
    │   ├── Role Select
    │   ├── Remember Me
    │   ├── Sign In Button
    │   └── Google Sign In Button
    │
    └── Register Form
        ├── Name Inputs
        ├── Email Input
        ├── Password Inputs
        ├── Role Select
        ├── Terms Checkbox
        ├── Create Account Button
        └── Google Sign Up Button
```

#### 👨‍💼 Admin Dashboard (DashboardView.vue + Admin Views)

```
DashboardView (Overview)
├── Statistics Cards
├── Recent Orders
└── Quick Actions

AdminUsersView
├── User List Table
└── User Management Modals

AdminProductsView
├── Product List Table
└── Product Management Modals

AdminOrdersView
├── Orders List Table
└── Order Details Modal

[Similar structure for other admin pages]
```

---

## 🔄 Data Flow Architecture

### State Management (Pinia Stores)

```
┌─────────────────────────────────────────┐
│          Pinia Store Layer              │
├─────────────────────────────────────────┤
│                                         │
│  ┌─────────────┐  ┌──────────────┐      │
│  │  auth.js    │  │  counter.js  │      │
│  │  (User/Auth)│  │  (Cart)      │      │
│  └─────────────┘  └──────────────┘      │
│                                         │
│  ┌─────────────┐                        │
│  │dashboard.js │                        │
│  │(Admin Data) │                        │
│  └─────────────┘                        │
│                                         │
└─────────────────────────────────────────┘
         ↕                    ↕
┌─────────────────────────────────────────┐
│         Component Layer                 │
├─────────────────────────────────────────┤
│  Views, Components consume store data   │
│  and trigger store actions              │
└─────────────────────────────────────────┘
         ↕
┌─────────────────────────────────────────┐
│       LocalStorage Layer                │
├─────────────────────────────────────────┤
│  Persistence for user session, cart,    │
│  and application state                  │
└─────────────────────────────────────────┘
```

### Routing Flow

```
User Action (Click/Navigate)
    ↓
Vue Router Intercepts
    ↓
Route Guard Checks (if needed)
    ↓
Component Loads
    ↓
Component Fetches Data (from store)
    ↓
Component Renders
    ↓
User Sees Updated Page
```

---

## 🎨 Styling Architecture

### CSS Organization

```
Global Styles (base.css, main.css)
    ↓
Component Scoped Styles (<style scoped>)
    ↓
Inline Styles (dynamic :style bindings)
```

### Style Layers

1. **Base/Reset** (`base.css`) - CSS reset and base styles
2. **Global Utilities** (`main.css`, `global.css`) - Reusable classes
3. **Component Specific** (Scoped in .vue files) - Component styles
4. **Admin Specific** (`AdminStyles.css`) - Admin panel styles

---

## 🔌 Key Integrations

### Current Integrations

- Vue Router (Navigation)
- Pinia (State Management)
- LocalStorage (Data Persistence)

### Ready for Integration

- Google OAuth (Authentication)
- Payment Gateways (Stripe, PayPal, Apple Pay)
- Backend API (REST/GraphQL)
- Database (Firebase, MongoDB, PostgreSQL)

---

## 📦 Component Reusability

### Highly Reusable Components

- **ProductCard** - Used across all product pages
- **ProductGrid** - Layout for product listings
- **ProductPage** - Template for category pages
- **AppHeader** - Global navigation
- **AppFooter** - Global footer

### Specialized Components

- **Admin Modals** - Specific to admin functions
- **Checkout Views** - Checkout flow specific
- **Auth Forms** - Authentication specific

---

## 🚀 Build & Development

### Development Commands

```bash
npm run dev          # Start development server
npm run build        # Build for production
npm run preview      # Preview production build
npm run test:unit    # Run unit tests
```

### Environment

- **Dev Server**: Vite (http://localhost:5173)
- **Hot Module Replacement**: Enabled
- **Vue DevTools**: Supported

---

## 📊 File Size Overview

### Large Components (Main Views)

- HomeView.vue (~800+ lines) - Complex with many sections
- CheckoutCartView.vue (~1000+ lines) - Full cart functionality
- CheckoutPaymentView.vue (~900+ lines) - Payment processing
- AuthView.vue (~600+ lines) - Login/Register forms

### Medium Components

- Product Views (300-500 lines each)
- Admin Views (400-600 lines each)
- ProductPage.vue (~300 lines)

### Small Components

- ProductCard.vue (~200 lines)
- Modals (~200-300 lines)
- Layout components (~300-500 lines)

---

## 🔗 Inter-Component Communication

### Props Down

- Parent components pass data to children via props
- Example: ProductPage → ProductGrid → ProductCard

### Events Up

- Child components emit events to parents
- Example: ProductCard @click → ProductGrid → ProductPage

### Store for Global State

- Cart operations (add, remove, update)
- User authentication state
- Admin dashboard data

---

## 🎯 Key Design Patterns Used

1. **Component Composition** - Building complex UIs from simple components
2. **Props & Events** - Parent-child communication
3. **Scoped Slots** - Flexible component templates
4. **Composables** - Reusable logic extraction
5. **Store Pattern** - Centralized state management
6. **Route Guards** - Protected routes for admin
7. **Conditional Rendering** - Dynamic UI based on state

---

## 📝 Naming Conventions

### Files

- **Views**: `[Name]View.vue` (e.g., HomeView.vue)
- **Components**: `[Name].vue` or `[Category][Name].vue`
- **Stores**: `[name].js` (lowercase)
- **Utils**: `[name]Utils.js`

### Code

- **Components**: PascalCase (ProductCard)
- **Methods**: camelCase (handleLogin)
- **Props**: camelCase (showCart)
- **Events**: kebab-case (product-click)
- **CSS Classes**: kebab-case (cart-icon)

---

**Last Updated**: December 3, 2025
**Version**: 1.0
