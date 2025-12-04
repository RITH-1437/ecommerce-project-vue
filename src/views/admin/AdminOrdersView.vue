<template>
  <div class="admin-page">
    <div class="dashboard-container">
      <!-- Sidebar Navigation -->
      <div class="sidebar">
        <div class="sidebar-header">
          <div class="logo">
            <span class="logo-icon"><i class="fab fa-apple"></i></span>
            <h2 class="sidebar-title">Apple Store Admin</h2>
          </div>
          <div class="sidebar-status">
            <div class="status-indicator online"></div>
            <span class="status-text">Online</span>
          </div>
        </div>
        <nav class="sidebar-nav">
          <router-link to="/admin/overview" class="nav-item">
            <span class="nav-icon"><i class="fas fa-chart-line"></i></span>
            <span class="nav-text">Overview</span>
          </router-link>
          <router-link to="/admin/orders" class="nav-item" active-class="active">
            <span class="nav-icon"><i class="fas fa-clipboard-list"></i></span>
            <span class="nav-text">Orders</span>
            <span class="nav-badge">{{ orders.length }}</span>
          </router-link>
          <router-link to="/admin/products" class="nav-item">
            <span class="nav-icon"><i class="fas fa-box"></i></span>
            <span class="nav-text">Products</span>
          </router-link>
          <router-link to="/admin/users" class="nav-item">
            <span class="nav-icon"><i class="fas fa-users"></i></span>
            <span class="nav-text">Users</span>
          </router-link>
          <router-link to="/admin/contacts" class="nav-item">
            <span class="nav-icon"><i class="fas fa-envelope"></i></span>
            <span class="nav-text">Contacts</span>
          </router-link>
          <router-link to="/admin/discounts" class="nav-item">
            <span class="nav-icon"><i class="fas fa-tags"></i></span>
            <span class="nav-text">Discounts</span>
          </router-link>
          <router-link to="/admin/categories" class="nav-item">
            <span class="nav-icon"><i class="fas fa-folder"></i></span>
            <span class="nav-text">Categories</span>
          </router-link>
          <router-link to="/admin/reviews" class="nav-item">
            <span class="nav-icon"><i class="fas fa-star"></i></span>
            <span class="nav-text">Reviews</span>
          </router-link>
          <router-link to="/admin/settings" class="nav-item">
            <span class="nav-icon"><i class="fas fa-cog"></i></span>
            <span class="nav-text">Settings</span>
          </router-link>
        </nav>
      </div>

      <!-- Main Content -->
      <div class="admin-main">
        <!-- Header -->
        <div class="admin-header">
          <div class="header-left">
            <h1 class="page-title">📦 Orders</h1>
          </div>
          <div class="header-right">
            <router-link to="/" class="back-to-store-btn">
              <span class="icon"><i class="fas fa-store"></i></span>
              <span>Back to Store</span>
            </router-link>
          </div>
        </div>

        <!-- Orders Table -->
        <div class="content-section">
          <div class="table-container">
            <table class="data-table">
              <thead>
                <tr>
                  <th>Order ID</th>
                  <th>Customer Name</th>
                  <th>Order Date</th>
                  <th>Total Amount</th>
                  <th>Status</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="order in orders" :key="order.id">
                  <td>{{ order.id }}</td>
                  <td>{{ order.customer.name }}</td>
                  <td>{{ formatDate(order.date) }}</td>
                  <td>{{ formatCurrency(order.total) }}</td>
                  <td>
                    <select
                      :value="order.status"
                      @change="updateOrderStatus(order.id, $event.target.value)"
                      class="status-select"
                      :class="`status-${order.status.toLowerCase()}`"
                    >
                      <option value="Pending">Pending</option>
                      <option value="Processing">Processing</option>
                      <option value="Shipped">Shipped</option>
                      <option value="Delivered">Delivered</option>
                      <option value="Cancelled">Cancelled</option>
                    </select>
                  </td>
                  <td>
                    <button @click="viewOrderDetails(order.id)" class="action-btn view-btn">
                      View
                    </button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useDashboardStore } from '@/stores/dashboard'
import Swal from 'sweetalert2'

const router = useRouter()
const dashboardStore = useDashboardStore()

const orders = ref([])

onMounted(async () => {
  const user = JSON.parse(localStorage.getItem('user') || '{}')
  if (!user.isLoggedIn || user.role !== 'admin') {
    router.push('/')
    return
  }

  // Fetch orders from dashboard store
  await dashboardStore.fetchRecentOrders()
  orders.value = dashboardStore.recentOrders
})

const formatDate = (dateString) => {
  const date = new Date(dateString)
  return date.toLocaleDateString('en-US', { year: 'numeric', month: 'short', day: 'numeric' })
}

const formatCurrency = (amount) => {
  return new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'USD',
    minimumFractionDigits: 0,
    maximumFractionDigits: 0,
  }).format(amount)
}

const updateOrderStatus = (orderId, newStatus) => {
  // Find and update the order
  const orderIndex = orders.value.findIndex((o) => o.id === orderId)
  if (orderIndex !== -1) {
    orders.value[orderIndex].status = newStatus
    // TODO: Make API call to update backend when integrated
    console.log(`Order ${orderId} status updated to ${newStatus}`)

    // Show success message
    Swal.fire({
      icon: 'success',
      title: 'Status Updated!',
      text: `Order ${orderId} status updated to ${newStatus}`,
      timer: 2000,
      showConfirmButton: false,
    })
  }
}

const viewOrderDetails = (orderId) => {
  const order = orders.value.find((o) => o.id === orderId)
  if (!order) return

  const statusColor = '#6c757d' // Use single color for all statuses

  Swal.fire({
    title: `<div style="color: #1d1d1f; font-size: 1.5rem; font-weight: 600;">${orderId}</div>`,
    html: `
      <div style="text-align: left; padding: 10px; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;">
        <div style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 20px; border-radius: 12px; margin-bottom: 20px;">
          <div style="display: flex; align-items: center; gap: 12px; margin-bottom: 15px;">
            <span style="font-size: 2.5rem;">${order.customer.avatar}</span>
            <div>
              <h3 style="margin: 0; font-size: 1.2rem; font-weight: 600;">${order.customer.name}</h3>
              <p style="margin: 5px 0 0 0; font-size: 0.9rem; opacity: 0.9;">${order.customer.email}</p>
            </div>
          </div>
          <div style="display: flex; justify-content: space-between; align-items: center;">
            <span style="background: ${statusColor}; padding: 6px 16px; border-radius: 20px; font-size: 0.85rem; font-weight: 600; text-transform: uppercase;">${order.status}</span>
            <span style="font-size: 1.3rem; font-weight: 700;">${formatCurrency(order.total)}</span>
          </div>
        </div>

        <div style="background: #f8f9fa; padding: 15px; border-radius: 8px; margin-bottom: 15px;">
          <h4 style="margin: 0 0 12px 0; color: #495057; font-size: 1rem; font-weight: 600;">📦 Order Information</h4>
          <div style="display: grid; gap: 8px; font-size: 0.9rem;">
            <div style="display: flex; justify-content: space-between;">
              <span style="color: #6c757d; font-weight: 500;">Order Date:</span>
              <span style="color: #212529; font-weight: 600;">${formatDate(order.date)}</span>
            </div>
            <div style="display: flex; justify-content: space-between;">
              <span style="color: #6c757d; font-weight: 500;">Priority:</span>
              <span style="color: #212529; font-weight: 600; text-transform: uppercase;">${order.priority}</span>
            </div>
          </div>
        </div>

        <div style="background: #fff; border: 2px solid #e9ecef; padding: 15px; border-radius: 8px;">
          <h4 style="margin: 0 0 12px 0; color: #495057; font-size: 1rem; font-weight: 600;">🛍️ Items Purchased</h4>
          <ul style="margin: 0; padding-left: 20px; font-size: 0.9rem; color: #495057;">
            ${order.items.map((item) => `<li style="margin: 5px 0; font-weight: 500;">${item}</li>`).join('')}
          </ul>
        </div>

      </div>
    `,
    width: '600px',
    showConfirmButton: true,
    confirmButtonText: '✓ Close',
    confirmButtonColor: '#667eea',
    customClass: {
      popup: 'order-details-popup',
      confirmButton: 'order-details-button',
    },
  })
}
</script>

<style scoped>
@import url('@/assets/styles/AdminStyles.css');

.status-select {
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 0.8rem;
  font-weight: 500;
  text-transform: uppercase;
  border: 2px solid transparent;
  cursor: pointer;
  transition: all 0.3s ease;
  outline: none;
}

.status-select {
  background: #f8f9fa;
  color: #495057;
  border-color: #dee2e6;
}

.status-select:hover {
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.status-select:focus {
  border-color: #1976d2;
  box-shadow: 0 0 0 3px rgba(25, 118, 210, 0.1);
}

.action-btn {
  padding: 6px 16px;
  border-radius: 8px;
  border: none;
  font-size: 0.85rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.view-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.view-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
}

.data-table th:last-child,
.data-table td:last-child {
  text-align: center;
  width: 100px;
}
</style>
