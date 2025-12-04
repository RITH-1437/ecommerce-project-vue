import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../stores/auth.js'
import HomeView from '../views/HomeView.vue'
import IPhoneView from '../views/IPhoneView.vue'
import IPadView from '../views/IPadView.vue'
import MacBookView from '../views/MacBookView.vue'
import WatchView from '../views/WatchView.vue'
import AirPodsView from '../views/AirPodsView.vue'
import AuthView from '../views/AuthView.vue'
import DashboardView from '../views/DashboardView.vue'
import AdminUsersView from '../views/admin/AdminUsersView.vue'
import AdminProductsView from '../views/admin/AdminProductsView.vue'
import AdminCategoriesView from '../views/admin/AdminCategoriesView.vue'
import AdminOrdersView from '../views/admin/AdminOrdersView.vue'
import AdminDiscountsView from '../views/admin/AdminDiscountsView.vue'
import AdminSettingsView from '../views/admin/AdminSettingsView.vue'
import AdminReviewsView from '../views/admin/AdminReviewsView.vue'
import AdminContactsView from '../views/admin/AdminContactsView.vue'
import CheckoutCartView from '../views/checkout/CheckoutCartView.vue'
import CheckoutPaymentView from '../views/checkout/CheckoutPaymentView.vue'
import CheckoutReceiptView from '../views/checkout/CheckoutReceiptView.vue'
import CustomerOrdersView from '../views/CustomerOrdersView.vue'
import ProductDetailView from '../views/ProductDetailView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'Home',
      component: HomeView,
    },
    {
      path: '/iphone',
      name: 'iPhone',
      component: IPhoneView,
    },
    {
      path: '/ipad',
      name: 'iPad',
      component: IPadView,
    },
    {
      path: '/macbook',
      name: 'MacBook',
      component: MacBookView,
    },
    {
      path: '/watch',
      name: 'Watch',
      component: WatchView,
    },
    {
      path: '/airpods',
      name: 'AirPods',
      component: AirPodsView,
    },
    {
      path: '/product/:type/:id',
      name: 'ProductDetail',
      component: ProductDetailView,
    },
    {
      path: '/auth',
      name: 'Auth',
      component: AuthView,
    },
    // Customer routes
    {
      path: '/my-orders',
      name: 'CustomerOrders',
      component: CustomerOrdersView,
    },
    // Checkout routes
    {
      path: '/checkout/cart',
      name: 'CheckoutCart',
      component: CheckoutCartView,
    },
    {
      path: '/checkout/payment',
      name: 'CheckoutPayment',
      component: CheckoutPaymentView,
    },
    {
      path: '/checkout/receipt',
      name: 'CheckoutReceipt',
      component: CheckoutReceiptView,
    },
    // Admin routes
    {
      path: '/admin/overview',
      name: 'AdminOverview',
      component: DashboardView,
    },
    {
      path: '/admin/users',
      name: 'AdminUsers',
      component: AdminUsersView,
    },
    {
      path: '/admin/products',
      name: 'AdminProducts',
      component: AdminProductsView,
    },
    {
      path: '/admin/categories',
      name: 'AdminCategories',
      component: AdminCategoriesView,
    },
    {
      path: '/admin/orders',
      name: 'AdminOrders',
      component: AdminOrdersView,
    },
    {
      path: '/admin/discounts',
      name: 'AdminDiscounts',
      component: AdminDiscountsView,
    },
    {
      path: '/admin/settings',
      name: 'AdminSettings',
      component: AdminSettingsView,
    },
    {
      path: '/admin/reviews',
      name: 'AdminReviews',
      component: AdminReviewsView,
    },
    {
      path: '/admin/contacts',
      name: 'AdminContacts',
      component: AdminContactsView,
    },
    // Legacy dashboard route redirect
    {
      path: '/dashboard',
      redirect: '/admin/overview',
    },
    // Catch all route - redirect to home
    {
      path: '/:pathMatch(.*)*',
      redirect: '/',
    },
  ],
})

// Navigation guards
router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()

  // Define routes that require authentication
  const requiresAuth = [
    '/my-orders',
    '/checkout/cart',
    '/checkout/payment',
    '/checkout/receipt',
    '/admin/overview',
    '/admin/users',
    '/admin/products',
    '/admin/categories',
    '/admin/orders',
    '/admin/discounts',
    '/admin/settings',
    '/admin/reviews',
    '/admin/contacts',
  ]

  // Check if route requires authentication
  const requiresAuthentication = requiresAuth.some(route => to.path.startsWith(route))

  if (requiresAuthentication && !authStore.isLoggedIn) {
    // Redirect to auth page with return URL
    next({ name: 'Auth', query: { redirect: to.fullPath } })
    return
  }

  // Check admin routes
  if (to.path.startsWith('/admin/')) {
    if (!authStore.canAccessAdmin) {
      // Not authorized for admin access
      next('/')
      return
    }

    // Specific admin route permissions
    const adminRoutes = {
      '/admin/users': 'canManageUsers',
      '/admin/discounts': 'canManageDiscounts',
      '/admin/settings': 'canManageSettings',
    }

    const requiredPermission = adminRoutes[to.path]
    if (requiredPermission && !authStore.hasPermission(requiredPermission)) {
      // Not authorized for this specific admin feature
      next('/admin/overview')
      return
    }
  }

  // Check customer routes
  if (to.path === '/my-orders' && !authStore.hasPermission('canPurchase')) {
    next('/')
    return
  }

  // If user is logged in and trying to access auth page, redirect to appropriate dashboard
  if (to.name === 'Auth' && authStore.isLoggedIn) {
    const redirectPath = authStore.isAdmin ? '/admin/overview' : '/'
    next(redirectPath)
    return
  }

  next()
})

// Router-level error handler
router.onError((err) => {
  try {
    // Lazy import to avoid circular/early pinia usage
    const { useErrorStore } = require('../stores/error.js')
    const store = useErrorStore()
    store.capture(err, 'Router navigation error')
  } catch (e) {
    // eslint-disable-next-line no-console
    console.error('Router onError failed', e)
  }
})

export default router
