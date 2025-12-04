<template>
  <div class="customer-orders-page">
    <AppHeader />

    <main class="orders-main">
      <div class="container">
        <div class="page-header">
          <h1 class="page-title">My Orders</h1>
          <p class="page-subtitle">Track and manage your order history</p>
        </div>

        <div v-if="orders.length === 0" class="empty-orders">
          <div class="empty-icon"><i class="fas fa-box-open"></i></div>
          <h2>No Orders Yet</h2>
          <p>You haven't placed any orders yet. Start shopping to see your orders here!</p>
          <router-link to="/" class="shop-btn">Start Shopping</router-link>
        </div>

        <div v-else class="orders-list">
          <div v-for="order in orders" :key="order.id" class="order-card">
            <div class="order-header">
              <div class="order-info">
                <h3 class="order-number">Order #{{ order.orderNumber }}</h3>
                <span class="order-date">{{ formatDate(order.date) }}</span>
              </div>
              <span class="order-status" :class="order.status.toLowerCase()">
                {{ order.status }}
              </span>
            </div>

            <div class="order-items">
              <div v-for="item in order.items" :key="item.id" class="order-item">
                <div class="item-icon"><i class="fas fa-mobile-alt"></i></div>
                <div class="item-details">
                  <h4>{{ item.name }}</h4>
                  <p>Quantity: {{ item.quantity }}</p>
                </div>
                <div class="item-price">{{ item.price }}</div>
              </div>
            </div>

            <div class="order-footer">
              <div class="order-total">
                <span>Total:</span>
                <span class="total-amount">${{ order.total.toFixed(2) }}</span>
              </div>
              <button @click="viewOrderDetails(order)" class="view-btn">View Details</button>
            </div>
          </div>
        </div>
      </div>
    </main>

    <AppFooter />
  </div>
</template>

<script>
import AppHeader from '@/components/layout/AppHeader.vue'
import AppFooter from '@/components/layout/AppFooter.vue'
import { useAuthStore } from '@/stores/auth.js'
import Swal from 'sweetalert2'

export default {
  name: 'CustomerOrdersView',
  components: {
    AppHeader,
    AppFooter,
  },
  data() {
    return {
      orders: [],
    }
  },
  mounted() {
    const authStore = useAuthStore()

    // Redirect to home if not logged in
    if (!authStore.isLoggedIn) {
      this.$router.push('/')
      return
    }

    // Redirect admin to admin orders page
    if (authStore.user?.role === 'admin') {
      this.$router.push('/admin/orders')
      return
    }

    this.loadOrders()
  },
  methods: {
    loadOrders() {
      // Try to load orders from localStorage (from previous purchases)
      const lastOrder = localStorage.getItem('lastOrder')

      if (lastOrder) {
        try {
          const orderData = JSON.parse(lastOrder)
          this.orders = [
            {
              id: 1,
              orderNumber: orderData.orderNumber,
              date: orderData.orderDate,
              status: 'Processing',
              items: orderData.items,
              total: orderData.totals.total,
              fullData: orderData,
            },
          ]
        } catch (error) {
          console.error('Error loading order:', error)
        }
      }

      // In a real app, you would fetch orders from an API
      // Example: this.orders = await fetchCustomerOrders()
    },
    formatDate(dateString) {
      const date = new Date(dateString)
      return date.toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
      })
    },
    viewOrderDetails(order) {
      const orderData = order.fullData || order

      Swal.fire({
        title: `Order #${order.orderNumber}`,
        html: `
          <div style="text-align: left; padding: 15px;">
            <div style="margin-bottom: 20px;">
              <p style="margin: 5px 0; color: #86868b;"><strong>Date:</strong> ${this.formatDate(order.date)}</p>
              <p style="margin: 5px 0;"><strong>Status:</strong> <span style="color: #34c759; font-weight: 600;">${order.status}</span></p>
            </div>
            
            <div style="margin-bottom: 20px;">
              <h4 style="margin-bottom: 10px; color: #1d1d1f;">Items:</h4>
              ${order.items
                .map(
                  (item) => `
                <div style="display: flex; justify-content: space-between; padding: 8px 0; border-bottom: 1px solid #f5f5f7;">
                  <span>${item.image || '📱'} ${item.name} (x${item.quantity})</span>
                  <span style="font-weight: 600;">${item.price}</span>
                </div>
              `,
                )
                .join('')}
            </div>

            ${
              orderData.billingAddress
                ? `
              <div style="margin-bottom: 20px;">
                <h4 style="margin-bottom: 10px; color: #1d1d1f;">Billing Address:</h4>
                <p style="margin: 5px 0; color: #86868b;">${orderData.billingAddress.fullName}</p>
                <p style="margin: 5px 0; color: #86868b;">${orderData.billingAddress.street}</p>
                <p style="margin: 5px 0; color: #86868b;">${orderData.billingAddress.city}, ${orderData.billingAddress.state} ${orderData.billingAddress.zip}</p>
              </div>
            `
                : ''
            }

            <div style="background: #f5f5f7; padding: 15px; border-radius: 8px; text-align: center;">
              <h4 style="margin: 0; color: #1d1d1f;">Total Amount</h4>
              <p style="margin: 10px 0 0 0; font-size: 1.5rem; font-weight: 700; color: #0066cc;">$${order.total.toFixed(2)}</p>
            </div>
          </div>
        `,
        confirmButtonColor: '#667eea',
        confirmButtonText: 'Close',
        width: 600,
      })
    },
  },
}
</script>

<style scoped>
.customer-orders-page {
  min-height: 100vh;
  background: #f5f5f7;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
}

.orders-main {
  padding: 80px 0 40px 0;
  min-height: calc(100vh - 200px);
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.page-header {
  text-align: center;
  margin-bottom: 50px;
}

.page-title {
  font-size: 3rem;
  font-weight: 800;
  color: #1d1d1f;
  margin-bottom: 10px;
  background: linear-gradient(135deg, #667eea, #764ba2);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.page-subtitle {
  font-size: 1.2rem;
  color: #86868b;
}

/* Empty State */
.empty-orders {
  text-align: center;
  background: white;
  border-radius: 24px;
  padding: 80px 40px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
}

.empty-icon {
  font-size: 5rem;
  margin-bottom: 20px;
  opacity: 0.8;
}

.empty-orders h2 {
  color: #1d1d1f;
  margin-bottom: 10px;
  font-size: 2rem;
}

.empty-orders p {
  color: #86868b;
  margin-bottom: 30px;
  font-size: 1.1rem;
}

.shop-btn {
  display: inline-block;
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: white;
  padding: 15px 40px;
  border-radius: 30px;
  text-decoration: none;
  font-weight: 600;
  font-size: 1.1rem;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
}

.shop-btn:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.4);
}

/* Orders List */
.orders-list {
  display: flex;
  flex-direction: column;
  gap: 25px;
}

.order-card {
  background: white;
  border-radius: 20px;
  padding: 30px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
  border: 1px solid rgba(0, 0, 0, 0.06);
  transition: all 0.3s ease;
}

.order-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.1);
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 2px solid #f5f5f7;
}

.order-info {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.order-number {
  font-size: 1.3rem;
  font-weight: 700;
  color: #1d1d1f;
  margin: 0;
}

.order-date {
  color: #86868b;
  font-size: 0.9rem;
}

.order-status {
  padding: 8px 20px;
  border-radius: 20px;
  font-weight: 600;
  font-size: 0.9rem;
}

.order-status.processing {
  background: #fff3cd;
  color: #856404;
}

.order-status.shipped {
  background: #d1ecf1;
  color: #0c5460;
}

.order-status.delivered {
  background: #d4edda;
  color: #155724;
}

.order-status.cancelled {
  background: #f8d7da;
  color: #721c24;
}

/* Order Items */
.order-items {
  margin-bottom: 20px;
}

.order-item {
  display: grid;
  grid-template-columns: 60px 1fr auto;
  gap: 15px;
  align-items: center;
  padding: 15px;
  background: #f5f5f7;
  border-radius: 12px;
  margin-bottom: 10px;
}

.order-item:last-child {
  margin-bottom: 0;
}

.item-icon {
  font-size: 2rem;
  text-align: center;
}

.item-details h4 {
  margin: 0 0 5px 0;
  color: #1d1d1f;
  font-size: 1rem;
}

.item-details p {
  margin: 0;
  color: #86868b;
  font-size: 0.9rem;
}

.item-price {
  font-weight: 600;
  color: #0066cc;
  font-size: 1.1rem;
}

/* Order Footer */
.order-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 20px;
  border-top: 2px solid #f5f5f7;
}

.order-total {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 1.1rem;
  color: #86868b;
}

.total-amount {
  font-size: 1.5rem;
  font-weight: 700;
  color: #1d1d1f;
}

.view-btn {
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: white;
  border: none;
  padding: 12px 30px;
  border-radius: 25px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
}

.view-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
}

/* Responsive */
@media (max-width: 768px) {
  .page-title {
    font-size: 2rem;
  }

  .page-subtitle {
    font-size: 1rem;
  }

  .order-card {
    padding: 20px;
  }

  .order-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 15px;
  }

  .order-item {
    grid-template-columns: 50px 1fr;
    gap: 10px;
  }

  .item-price {
    grid-column: 2;
    justify-self: end;
  }

  .order-footer {
    flex-direction: column;
    gap: 15px;
    align-items: stretch;
  }

  .order-total {
    justify-content: space-between;
  }

  .view-btn {
    width: 100%;
  }
}
</style>
