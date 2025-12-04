import { createRouter, createWebHistory } from 'vue-router'
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

export default router
