<template>
  <div class="dashboard-page">
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
          <router-link to="/admin/overview" class="nav-item" active-class="active">
            <span class="nav-icon"><i class="fas fa-chart-line"></i></span>
            <span class="nav-text">Overview</span>
            <span class="nav-badge">{{ todayOrders }}</span>
          </router-link>
          <router-link to="/admin/orders" class="nav-item">
            <span class="nav-icon"><i class="fas fa-clipboard-list"></i></span>
            <span class="nav-text">Orders</span>
            <span class="nav-badge">{{ dashboardStore.recentOrders.length }}</span>
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

      <!-- Main Dashboard Content -->
      <div class="dashboard-main">
        <!-- Header -->
        <div class="dashboard-header">
          <div class="header-left">
            <h1 class="page-title">
              <span class="title-icon">👋</span>
              Good {{ timeOfDay }}, Admin
            </h1>
            <p class="page-subtitle">Here's what's happening with your store today</p>
          </div>
          <div class="header-right">
            <div class="header-actions">
              <button
                @click="refreshAllData"
                class="action-btn refresh-btn"
                :disabled="dashboardStore.isLoading"
              >
                <span class="icon" :class="{ spinning: dashboardStore.isLoading }">🔄</span>
                <span>Refresh</span>
              </button>
              <div class="last-updated" v-if="dashboardStore.lastUpdated">
                <span class="update-text"
                  >Last updated: {{ formatTime(dashboardStore.lastUpdated) }}</span
                >
              </div>
              <router-link to="/" class="back-to-store-btn">
                <span class="icon"><i class="fas fa-store"></i></span>
                <span>Back to Store</span>
              </router-link>
            </div>
          </div>
        </div>

        <!-- Loading Overlay -->
        <div v-if="dashboardStore.isLoading && !dashboardStore.lastUpdated" class="loading-overlay">
          <div class="loading-spinner">
            <div class="spinner"></div>
            <p>Loading dashboard data...</p>
          </div>
        </div>

        <!-- Main Content -->
        <div v-else class="dashboard-content">
          <!-- Key Metrics Cards -->
          <div class="metrics-grid">
            <div class="metric-card sales" :class="{ loading: dashboardStore.isLoading }">
              <div class="metric-header">
                <div class="metric-icon">💰</div>
                <div class="metric-trend" :class="dashboardStore.stats.totalSales.trend">
                  <span class="trend-icon">{{
                    getTrendIcon(dashboardStore.stats.totalSales.trend)
                  }}</span>
                  <span class="trend-value"
                    >{{ Math.abs(dashboardStore.stats.totalSales.change) }}%</span
                  >
                </div>
              </div>
              <div class="metric-content">
                <h3 class="metric-label">Total Sales</h3>
                <div class="metric-value">
                  {{ formatCurrency(dashboardStore.stats.totalSales.value) }}
                </div>
                <p class="metric-description">Total sales this month</p>
              </div>
            </div>

            <div class="metric-card orders">
              <div class="metric-header">
                <div class="metric-icon">📦</div>
                <div class="metric-trend" :class="dashboardStore.stats.totalOrders.trend">
                  <span class="trend-icon">{{
                    getTrendIcon(dashboardStore.stats.totalOrders.trend)
                  }}</span>
                  <span class="trend-value"
                    >{{ Math.abs(dashboardStore.stats.totalOrders.change) }}%</span
                  >
                </div>
              </div>
              <div class="metric-content">
                <h3 class="metric-label">Total Orders</h3>
                <div class="metric-value">
                  {{ formatNumber(dashboardStore.stats.totalOrders.value) }}
                </div>
                <p class="metric-description">Orders this month</p>
              </div>
            </div>

            <div class="metric-card customers">
              <div class="metric-header">
                <div class="metric-icon">👥</div>
                <div class="metric-trend" :class="dashboardStore.stats.newCustomers.trend">
                  <span class="trend-icon">{{
                    getTrendIcon(dashboardStore.stats.newCustomers.trend)
                  }}</span>
                  <span class="trend-value"
                    >{{ Math.abs(dashboardStore.stats.newCustomers.change) }}%</span
                  >
                </div>
              </div>
              <div class="metric-content">
                <h3 class="metric-label">New Customers</h3>
                <div class="metric-value">
                  {{ formatNumber(dashboardStore.stats.newCustomers.value) }}
                </div>
                <p class="metric-description">New registrations</p>
              </div>
            </div>

            <div class="metric-card inventory">
              <div class="metric-header">
                <div class="metric-icon">📊</div>
                <div class="metric-trend" :class="dashboardStore.stats.productInventory.trend">
                  <span class="trend-icon">{{
                    getTrendIcon(dashboardStore.stats.productInventory.trend)
                  }}</span>
                  <span class="trend-value"
                    >{{ Math.abs(dashboardStore.stats.productInventory.change) }}%</span
                  >
                </div>
              </div>
              <div class="metric-content">
                <h3 class="metric-label">Inventory</h3>
                <div class="metric-value">
                  {{ formatNumber(dashboardStore.stats.productInventory.value) }}
                </div>
                <p class="metric-description">Products in stock</p>
              </div>
            </div>
          </div>

          <!-- Charts and Analytics -->
          <div class="analytics-section">
            <div class="chart-container">
              <div class="chart-header">
                <h2 class="chart-title">Sales & Orders Trend</h2>
                <div class="chart-controls">
                  <select class="chart-period" v-model="selectedPeriod">
                    <option value="7d">Last 7 days</option>
                    <option value="30d">Last 30 days</option>
                    <option value="90d">Last 3 months</option>
                  </select>
                </div>
              </div>
              <div class="chart-content">
                <canvas ref="salesChart" class="sales-chart"></canvas>
              </div>
            </div>

            <div class="quick-stats">
              <div class="quick-stat-item">
                <div class="stat-icon">💵</div>
                <div class="stat-info">
                  <div class="stat-label">Avg. Order Value</div>
                  <div class="stat-value">${{ dashboardStore.stats.avgOrderValue.value }}</div>
                </div>
              </div>
              <div class="quick-stat-item">
                <div class="stat-icon">🎯</div>
                <div class="stat-info">
                  <div class="stat-label">Today's Orders</div>
                  <div class="stat-value">{{ todayOrders }}</div>
                </div>
              </div>
              <div class="quick-stat-item">
                <div class="stat-icon">⚡</div>
                <div class="stat-info">
                  <div class="stat-label">Revenue Growth</div>
                  <div class="stat-value">+{{ dashboardStore.stats.revenue.change }}%</div>
                </div>
              </div>
            </div>
          </div>

          <!-- Main Content Grid -->
          <div class="content-grid">
            <!-- Recent Orders -->
            <div class="content-card orders-card">
              <div class="card-header">
                <h2 class="card-title">
                  <span class="title-icon">🛒</span>
                  Recent Orders
                </h2>
                <div class="card-actions">
                  <button @click="dashboardStore.refreshOrders()" class="card-action-btn">
                    <span class="icon">🔄</span>
                  </button>
                  <router-link to="/admin/orders" class="card-action-btn">
                    <span class="icon">📋</span>
                  </router-link>
                </div>
              </div>
              <div class="card-content">
                <div class="orders-list">
                  <div
                    v-for="order in dashboardStore.recentOrders"
                    :key="order.id"
                    class="order-item"
                    :class="`priority-${order.priority}`"
                  >
                    <div class="order-avatar">
                      <span class="avatar-emoji">{{ order.customer.avatar }}</span>
                    </div>
                    <div class="order-info">
                      <div class="order-header">
                        <span class="order-id">{{ order.id }}</span>
                        <span class="order-status" :class="order.status">{{ order.status }}</span>
                      </div>
                      <div class="order-customer">
                        <span class="customer-name">{{ order.customer.name }}</span>
                        <span class="order-time">{{ formatTimeAgo(order.date) }}</span>
                      </div>
                      <div class="order-items">
                        <span class="items-text">{{ order.items.join(', ') }}</span>
                      </div>
                    </div>
                    <div class="order-amount">
                      <span class="amount-value">{{ formatCurrency(order.total) }}</span>
                      <span class="priority-indicator" :class="order.priority"></span>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <!-- Low Stock Alerts -->
            <div class="content-card stock-card">
              <div class="card-header">
                <h2 class="card-title">
                  <span class="title-icon">⚠️</span>
                  Low Stock Alerts
                </h2>
                <div class="card-actions">
                  <span class="alert-count">{{ dashboardStore.lowStockProducts.length }}</span>
                </div>
              </div>
              <div class="card-content">
                <div class="stock-list">
                  <div
                    v-for="product in dashboardStore.lowStockProducts"
                    :key="product.id"
                    class="stock-item"
                    :class="`urgency-${product.urgency}`"
                  >
                    <div class="product-icon">
                      <span class="icon-emoji">{{ product.image }}</span>
                    </div>
                    <div class="product-info">
                      <div class="product-name">{{ product.name }}</div>
                      <div class="product-category">{{ product.category }}</div>
                    </div>
                    <div class="stock-info">
                      <div class="stock-level">
                        <div class="stock-bar">
                          <div
                            class="stock-fill"
                            :style="{
                              width: (product.currentStock / product.minStock) * 100 + '%',
                            }"
                            :class="product.urgency"
                          ></div>
                        </div>
                        <span class="stock-text"
                          >{{ product.currentStock }}/{{ product.minStock }}</span
                        >
                      </div>
                      <button
                        @click="restockProduct(product)"
                        class="restock-btn"
                        :class="product.urgency"
                      >
                        Restock
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <!-- Top Products -->
            <div class="content-card products-card">
              <div class="card-header">
                <h2 class="card-title">
                  <span class="title-icon">🏆</span>
                  Top Products
                </h2>
              </div>
              <div class="card-content">
                <div class="products-list">
                  <div
                    v-for="(product, index) in dashboardStore.topProducts"
                    :key="product.id"
                    class="product-item"
                  >
                    <div class="product-rank">
                      <span class="rank-number">#{{ index + 1 }}</span>
                    </div>
                    <div class="product-icon">
                      <span class="icon-emoji">{{ product.image }}</span>
                    </div>
                    <div class="product-details">
                      <div class="product-name">{{ product.name }}</div>
                      <div class="product-stats">
                        <span class="sales-count">{{ product.sales }} sold</span>
                        <span class="growth" :class="{ negative: product.growth < 0 }">
                          {{ product.growth > 0 ? '+' : '' }}{{ product.growth }}%
                        </span>
                      </div>
                    </div>
                    <div class="product-revenue">
                      <span class="revenue-value">{{ formatCurrency(product.revenue) }}</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, computed, nextTick, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth.js'
import { useDashboardStore } from '@/stores/dashboard.js'
import {
  Chart,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  LineController,
  BarElement,
  Title,
  Tooltip,
  Legend,
  Filler,
} from 'chart.js'

// Register Chart.js components
Chart.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  LineController,
  BarElement,
  Title,
  Tooltip,
  Legend,
  Filler,
)

export default {
  name: 'DashboardView',
  setup() {
    const router = useRouter()
    const authStore = useAuthStore()
    const dashboardStore = useDashboardStore()

    // Reactive data
    const selectedPeriod = ref('7d')
    const salesChart = ref(null)
    const chartInstance = ref(null)

    // Computed properties
    const timeOfDay = computed(() => {
      const hour = new Date().getHours()
      if (hour < 12) return 'morning'
      if (hour < 18) return 'afternoon'
      return 'evening'
    })

    const todayOrders = computed(() => dashboardStore.todayOrders)

    // Methods
    const formatCurrency = (amount) => {
      return new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'USD',
        minimumFractionDigits: 0,
        maximumFractionDigits: 0,
      }).format(amount)
    }

    const formatNumber = (num) => {
      return new Intl.NumberFormat('en-US').format(num)
    }

    const formatTime = (date) => {
      return new Intl.DateTimeFormat('en-US', {
        hour: 'numeric',
        minute: '2-digit',
        hour12: true,
      }).format(new Date(date))
    }

    const formatTimeAgo = (date) => {
      const now = new Date()
      const diff = now - new Date(date)
      const minutes = Math.floor(diff / 60000)
      const hours = Math.floor(minutes / 60)
      const days = Math.floor(hours / 24)

      if (days > 0) return `${days}d ago`
      if (hours > 0) return `${hours}h ago`
      if (minutes > 0) return `${minutes}m ago`
      return 'Just now'
    }

    const getTrendIcon = (trend) => {
      return trend === 'up' ? '📈' : '📉'
    }

    const refreshAllData = async () => {
      await dashboardStore.loadDashboardData()
      await nextTick()
      initChart()
    }

    const showNotifications = () => {
      Swal.fire({
        icon: 'info',
        title: 'Notifications',
        html: `
          <div style="text-align: left;">
            <p><strong>You have 5 new updates:</strong></p>
            <ul>
              <li>3 new orders</li>
              <li>2 low stock alerts</li>
            </ul>
          </div>
        `,
        confirmButtonText: 'Got it!',
      })
      // TODO: Implement notifications panel
    }

    const openSupport = () => {
      Swal.fire({
        icon: 'question',
        title: 'Support',
        html: `
          <div style="text-align: center;">
            <p>Contact us at:</p>
            <p><strong>support@applestore.com</strong></p>
            <p>or use live chat</p>
          </div>
        `,
        confirmButtonText: 'Close',
      })
      // TODO: Implement support panel
    }

    const restockProduct = (product) => {
      Swal.fire({
        title: 'Restock Product?',
        html: `
          <div style="text-align: left; padding: 10px;">
            <p style="margin-bottom: 15px;">Add <strong>50 items</strong> to stock for:</p>
            <div style="background: #f8f9fa; padding: 15px; border-radius: 8px; margin-bottom: 15px;">
              <div style="display: flex; align-items: center; gap: 12px;">
                <span style="font-size: 2rem;">${product.image}</span>
                <div>
                  <h4 style="margin: 0; font-size: 1.1rem; color: #212529;">${product.name}</h4>
                  <p style="margin: 5px 0 0 0; color: #6c757d; font-size: 0.9rem;">${product.category}</p>
                </div>
              </div>
            </div>
            <div style="background: #e7f3ff; padding: 12px; border-radius: 6px; border-left: 4px solid #007bff;">
              <p style="margin: 0; font-size: 0.9rem; color: #004085;">
                <strong>Current Stock:</strong> ${product.currentStock} → <strong>${product.currentStock + 50}</strong>
              </p>
            </div>
          </div>
        `,
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#667eea',
        cancelButtonColor: '#6c757d',
        confirmButtonText: '✓ Restock +50',
        cancelButtonText: 'Cancel',
      }).then((result) => {
        if (result.isConfirmed) {
          // Update the product stock
          product.currentStock += 50

          // Update urgency level based on new stock
          if (product.currentStock >= 20) {
            // Remove from low stock list if stock is now >= 20
            const index = dashboardStore.lowStockProducts.findIndex((p) => p.id === product.id)
            if (index !== -1) {
              dashboardStore.lowStockProducts.splice(index, 1)
            }
          } else if (product.currentStock >= 10) {
            product.urgency = 'low'
          }

          Swal.fire({
            icon: 'success',
            title: 'Restocked!',
            html: `
              <p><strong>${product.name}</strong> has been restocked.</p>
              <p style="color: #28a745; font-weight: 600; font-size: 1.1rem; margin-top: 10px;">
                New Stock: ${product.currentStock} items
              </p>
            `,
            timer: 2500,
            showConfirmButton: false,
          })
          // TODO: Make API call to update backend
        }
      })
    }

    const initChart = () => {
      if (!salesChart.value) return

      // Destroy existing chart
      if (chartInstance.value) {
        chartInstance.value.destroy()
      }

      const chartData = dashboardStore.salesChart
      if (!chartData || !chartData.labels.length) return

      const ctx = salesChart.value.getContext('2d')

      // Create new Chart.js instance
      chartInstance.value = new Chart(ctx, {
        type: 'line',
        data: {
          labels: chartData.labels,
          datasets: [
            {
              label: 'Sales ($)',
              data: chartData.datasets[0].data,
              borderColor: '#007AFF',
              backgroundColor: 'rgba(0, 122, 255, 0.1)',
              borderWidth: 3,
              tension: 0.4,
              fill: true,
              pointRadius: 4,
              pointHoverRadius: 6,
              pointBackgroundColor: '#007AFF',
              pointBorderColor: '#fff',
              pointBorderWidth: 2,
            },
            {
              label: 'Orders',
              data: chartData.datasets[1].data,
              borderColor: '#34C759',
              backgroundColor: 'rgba(52, 199, 89, 0.1)',
              borderWidth: 3,
              tension: 0.4,
              fill: true,
              pointRadius: 4,
              pointHoverRadius: 6,
              pointBackgroundColor: '#34C759',
              pointBorderColor: '#fff',
              pointBorderWidth: 2,
            },
          ],
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          interaction: {
            mode: 'index',
            intersect: false,
          },
          plugins: {
            legend: {
              display: true,
              position: 'top',
              align: 'end',
              labels: {
                usePointStyle: true,
                padding: 20,
                font: {
                  size: 12,
                  weight: '500',
                },
              },
            },
            tooltip: {
              enabled: true,
              backgroundColor: 'rgba(0, 0, 0, 0.8)',
              titleColor: '#fff',
              bodyColor: '#fff',
              borderColor: '#333',
              borderWidth: 1,
              padding: 12,
              displayColors: true,
              callbacks: {
                label: function (context) {
                  let label = context.dataset.label || ''
                  if (label) {
                    label += ': '
                  }
                  if (context.parsed.y !== null) {
                    if (context.datasetIndex === 0) {
                      label += formatCurrency(context.parsed.y)
                    } else {
                      label += context.parsed.y
                    }
                  }
                  return label
                },
              },
            },
          },
          scales: {
            x: {
              grid: {
                display: false,
              },
              ticks: {
                font: {
                  size: 11,
                },
                color: '#666',
              },
            },
            y: {
              beginAtZero: true,
              grid: {
                color: '#f0f0f0',
                drawBorder: false,
              },
              ticks: {
                font: {
                  size: 11,
                },
                color: '#666',
                callback: function (value) {
                  return '$' + value.toLocaleString()
                },
              },
            },
          },
        },
      })
    } // Auto-refresh every 30 seconds
    const autoRefresh = () => {
      setInterval(async () => {
        if (!document.hidden) {
          await dashboardStore.loadDashboardData()
        }
      }, 30000)
    }

    // Lifecycle
    onMounted(async () => {
      // Check authentication
      if (!authStore.isLoggedIn || !authStore.isAdmin) {
        router.push('/auth')
        return
      }

      // Load dashboard data
      await dashboardStore.loadDashboardData()

      // Initialize chart after DOM update
      await nextTick()
      initChart()

      // Start auto-refresh
      autoRefresh()
    })

    onUnmounted(() => {
      // Cleanup chart instance
      if (chartInstance.value) {
        chartInstance.value.destroy()
      }
    })

    return {
      dashboardStore,
      selectedPeriod,
      salesChart,
      timeOfDay,
      todayOrders,
      formatCurrency,
      formatNumber,
      formatTime,
      formatTimeAgo,
      getTrendIcon,
      refreshAllData,
      showNotifications,
      openSupport,
      restockProduct,
    }
  },
}
</script>

<style scoped>
.dashboard-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  overflow-x: hidden;
}

.dashboard-container {
  display: flex;
  min-height: 100vh;
}

/* Sidebar Styles */
.sidebar {
  position: fixed;
  left: 0;
  top: 0;
  width: 240px;
  height: 100vh;
  background: linear-gradient(180deg, #1e3c72 0%, #2a5298 100%);
  backdrop-filter: blur(20px);
  box-shadow: 4px 0 20px rgba(0, 0, 0, 0.15);
  display: flex;
  flex-direction: column;
  z-index: 100;
  color: white;
}

.sidebar-header {
  padding: 25px 20px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.logo {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
}

.logo-icon {
  font-size: 1.8rem;
}

.sidebar-title {
  font-size: 1.3rem;
  font-weight: 700;
  color: white;
  margin: 0;
}

.sidebar-status {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 0.9rem;
  color: rgba(255, 255, 255, 0.8);
}

.status-indicator {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #34c759;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0%,
  100% {
    opacity: 1;
  }
  50% {
    opacity: 0.5;
  }
}

.sidebar-nav {
  flex: 1;
  padding: 15px 0;
}

.nav-item {
  display: flex;
  align-items: center;
  padding: 12px 20px;
  text-decoration: none;
  color: rgba(255, 255, 255, 0.8);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border-left: 3px solid transparent;
  position: relative;
}

.nav-item:hover,
.nav-item.active {
  background: rgba(255, 255, 255, 0.1);
  color: white;
  border-left-color: #34c759;
  transform: translateX(4px);
}

.nav-item.active::before {
  content: '';
  position: absolute;
  right: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 4px;
  height: 40px;
  background: #34c759;
  border-radius: 2px 0 0 2px;
}

.nav-icon {
  margin-right: 12px;
  font-size: 1.1rem;
  width: 18px;
  text-align: center;
}

.nav-text {
  font-weight: 500;
  flex: 1;
  font-size: 0.9rem;
}

.nav-badge {
  background: #ff3b30;
  color: white;
  padding: 2px 8px;
  border-radius: 12px;
  font-size: 0.75rem;
  font-weight: 600;
  min-width: 18px;
  text-align: center;
}

.sidebar-footer {
  padding: 15px 20px;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
}

.quick-actions {
  display: flex;
  gap: 8px;
}

.quick-action-btn {
  background: rgba(255, 255, 255, 0.1);
  border: none;
  color: white;
  width: 40px;
  height: 40px;
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.quick-action-btn:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: translateY(-2px);
}

/* Main Dashboard */
.dashboard-main {
  flex: 1;
  margin-left: 240px;
  padding: 0;
  overflow-y: auto;
  background: #f8f9fb;
}

.dashboard-header {
  background: white;
  padding: 20px 30px;
  border-bottom: 1px solid #e9ecef;
  display: flex;
  justify-content: space-between;
  align-items: center;
  backdrop-filter: blur(20px);
  position: sticky;
  top: 0;
  z-index: 50;
}

.header-left {
  flex: 1;
}

.page-title {
  font-size: 1.6rem;
  font-weight: 700;
  color: #1a1a1a;
  margin: 0;
  display: flex;
  align-items: center;
  gap: 10px;
}

.title-icon {
  font-size: 2rem;
}

.page-subtitle {
  color: #666;
  font-size: 1rem;
  margin: 8px 0 0 0;
  font-weight: 400;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 16px;
}

.action-btn {
  background: #f8f9fa;
  border: 1px solid #e9ecef;
  color: #495057;
  padding: 12px 20px;
  border-radius: 12px;
  cursor: pointer;
  font-weight: 500;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 8px;
  text-decoration: none;
}

.action-btn:hover {
  background: #e9ecef;
  transform: translateY(-1px);
}

.refresh-btn {
  background: #007aff;
  color: white;
  border-color: #007aff;
}

.refresh-btn:hover {
  background: #0056b3;
}

.refresh-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.spinning {
  animation: spin 1s linear infinite;
}

@keyframes spin {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

.last-updated {
  font-size: 0.85rem;
  color: #666;
  padding: 8px 12px;
  background: #f8f9fa;
  border-radius: 8px;
}

/* Loading States */
.loading-overlay {
  position: fixed;
  top: 0;
  left: 240px;
  right: 0;
  bottom: 0;
  background: rgba(248, 249, 251, 0.9);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.loading-spinner {
  text-align: center;
}

.spinner {
  width: 40px;
  height: 40px;
  border: 3px solid #e9ecef;
  border-top: 3px solid #007aff;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto 16px;
}

/* Dashboard Content */
.dashboard-content {
  padding: 20px 30px;
  animation: fadeIn 0.6s ease-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* Metrics Grid */
.metrics-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 16px;
  margin-bottom: 24px;
}

.metric-card {
  background: white;
  padding: 20px;
  border-radius: 16px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  border: 1px solid #f0f0f0;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.metric-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
}

.metric-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
}

.metric-card.sales::before {
  background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
}
.metric-card.orders::before {
  background: linear-gradient(90deg, #f093fb 0%, #f5576c 100%);
}
.metric-card.customers::before {
  background: linear-gradient(90deg, #4facfe 0%, #00f2fe 100%);
}
.metric-card.inventory::before {
  background: linear-gradient(90deg, #43e97b 0%, #38f9d7 100%);
}

.metric-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.metric-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5rem;
}

.metric-trend {
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 0.85rem;
  font-weight: 600;
}

.metric-trend.up {
  background: #e8f5e8;
  color: #2e7d32;
}

.metric-trend.down {
  background: #fef2f2;
  color: #dc2626;
}

.metric-content h3 {
  font-size: 0.95rem;
  color: #666;
  margin: 0 0 8px 0;
  font-weight: 500;
}

.metric-value {
  font-size: 1.8rem;
  font-weight: 700;
  color: #1a1a1a;
  line-height: 1;
  margin-bottom: 6px;
}

.metric-description {
  color: #999;
  font-size: 0.9rem;
  margin: 0;
}

.metric-card.loading .metric-value {
  background: linear-gradient(90deg, #f0f0f0 25%, #e0e0e0 50%, #f0f0f0 75%);
  background-size: 200% 100%;
  animation: shimmer 2s infinite;
  border-radius: 8px;
  height: 40px;
}

@keyframes shimmer {
  0% {
    background-position: -200% 0;
  }
  100% {
    background-position: 200% 0;
  }
}

/* Analytics Section */
.analytics-section {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 16px;
  margin-bottom: 24px;
}

.chart-container {
  background: white;
  border-radius: 16px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  border: 1px solid #f0f0f0;
}

.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.chart-title {
  font-size: 1.4rem;
  font-weight: 600;
  color: #1a1a1a;
  margin: 0;
}

.chart-period {
  padding: 8px 16px;
  border: 1px solid #e9ecef;
  border-radius: 8px;
  background: white;
  color: #495057;
  font-size: 0.9rem;
}

.sales-chart {
  width: 100%;
  height: 220px;
  border-radius: 12px;
}

.quick-stats {
  background: white;
  border-radius: 20px;
  padding: 30px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  border: 1px solid #f0f0f0;
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.quick-stat-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 20px;
  background: #f8f9fb;
  border-radius: 16px;
  transition: all 0.3s ease;
}

.quick-stat-item:hover {
  background: #f0f2f5;
  transform: translateX(4px);
}

.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.4rem;
  color: white;
}

.stat-info {
  flex: 1;
}

.stat-label {
  font-size: 0.9rem;
  color: #666;
  margin-bottom: 4px;
}

.stat-value {
  font-size: 1.4rem;
  font-weight: 700;
  color: #1a1a1a;
}

/* Content Grid */
.content-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(380px, 1fr));
  gap: 16px;
}

.content-card {
  background: white;
  border-radius: 16px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  border: 1px solid #f0f0f0;
  overflow: hidden;
  transition: all 0.3s ease;
}

.content-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
}

.card-header {
  padding: 16px 20px;
  border-bottom: 1px solid #f0f0f0;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-title {
  font-size: 1.2rem;
  font-weight: 600;
  color: #1a1a1a;
  margin: 0;
  display: flex;
  align-items: center;
  gap: 8px;
}

.card-actions {
  display: flex;
  align-items: center;
  gap: 8px;
}

.card-action-btn {
  background: #f8f9fa;
  border: 1px solid #e9ecef;
  color: #666;
  width: 36px;
  height: 36px;
  border-radius: 8px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
  text-decoration: none;
}

.card-action-btn:hover {
  background: #e9ecef;
  color: #495057;
}

.alert-count {
  background: #ff3b30;
  color: white;
  padding: 4px 8px;
  border-radius: 12px;
  font-size: 0.8rem;
  font-weight: 600;
}

.card-content {
  padding: 16px 20px 20px;
}

/* Orders List */
.orders-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.order-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 20px;
  background: #f8f9fb;
  border-radius: 16px;
  transition: all 0.3s ease;
  border-left: 4px solid transparent;
}

.order-item:hover {
  background: #f0f2f5;
  transform: translateX(4px);
}

.order-item.priority-high {
  border-left-color: #ff3b30;
}

.order-item.priority-medium {
  border-left-color: #ff9500;
}

.order-item.priority-low {
  border-left-color: #34c759;
}

.order-avatar {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.4rem;
}

.order-info {
  flex: 1;
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 4px;
}

.order-id {
  font-weight: 600;
  color: #1a1a1a;
  font-size: 0.9rem;
}

.order-status {
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 0.75rem;
  font-weight: 600;
  text-transform: uppercase;
}

.order-status.processing {
  background: #fff3e0;
  color: #f57c00;
}

.order-status.shipped {
  background: #e3f2fd;
  color: #1976d2;
}

.order-status.delivered {
  background: #e8f5e8;
  color: #2e7d32;
}

.order-status.cancelled {
  background: #fef2f2;
  color: #dc2626;
}

.order-customer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.customer-name {
  font-weight: 500;
  color: #495057;
}

.order-time {
  font-size: 0.8rem;
  color: #999;
}

.order-items {
  font-size: 0.85rem;
  color: #666;
}

.order-amount {
  text-align: right;
}

.amount-value {
  font-weight: 700;
  color: #1a1a1a;
  font-size: 1.1rem;
}

.priority-indicator {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  margin-top: 4px;
  margin-left: auto;
}

.priority-indicator.high {
  background: #ff3b30;
}

.priority-indicator.medium {
  background: #ff9500;
}

.priority-indicator.low {
  background: #34c759;
}

/* Stock List */
.stock-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.stock-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 20px;
  background: #f8f9fb;
  border-radius: 16px;
  transition: all 0.3s ease;
  border-left: 4px solid transparent;
}

.stock-item:hover {
  background: #f0f2f5;
}

.stock-item.urgency-critical {
  border-left-color: #ff3b30;
  background: #fef5f5;
}

.stock-item.urgency-low {
  border-left-color: #ff9500;
  background: #fff8f0;
}

.product-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  background: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.4rem;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.product-info {
  flex: 1;
}

.product-name {
  font-weight: 600;
  color: #1a1a1a;
  margin-bottom: 4px;
}

.product-category {
  font-size: 0.85rem;
  color: #666;
}

.stock-info {
  text-align: right;
}

.stock-level {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
}

.stock-bar {
  width: 80px;
  height: 6px;
  background: #e9ecef;
  border-radius: 3px;
  overflow: hidden;
}

.stock-fill {
  height: 100%;
  transition: width 0.3s ease;
}

.stock-fill.critical {
  background: #ff3b30;
}

.stock-fill.high {
  background: #ff9500;
}

.stock-fill.medium {
  background: #ffcc02;
}

.stock-text {
  font-size: 0.8rem;
  color: #666;
  font-weight: 600;
}

.restock-btn {
  padding: 6px 12px;
  border: none;
  border-radius: 8px;
  font-size: 0.8rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.restock-btn.critical {
  background: #ff3b30;
  color: white;
}

.restock-btn.high {
  background: #ff9500;
  color: white;
}

.restock-btn.medium {
  background: #ffcc02;
  color: #333;
}

/* Products List */
.products-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.product-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 20px;
  background: #f8f9fb;
  border-radius: 16px;
  transition: all 0.3s ease;
}

.product-item:hover {
  background: #f0f2f5;
  transform: translateX(4px);
}

.product-rank {
  width: 32px;
  height: 32px;
  border-radius: 8px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: 700;
  font-size: 0.9rem;
}

.product-details {
  flex: 1;
}

.product-name {
  font-weight: 600;
  color: #1a1a1a;
  margin-bottom: 4px;
}

.product-stats {
  display: flex;
  gap: 12px;
  align-items: center;
}

.sales-count {
  font-size: 0.85rem;
  color: #666;
}

.growth {
  padding: 2px 8px;
  border-radius: 12px;
  font-size: 0.8rem;
  font-weight: 600;
  background: #e8f5e8;
  color: #2e7d32;
}

.growth.negative {
  background: #fef2f2;
  color: #dc2626;
}

.product-revenue {
  text-align: right;
}

.revenue-value {
  font-weight: 700;
  color: #1a1a1a;
  font-size: 1.1rem;
}

/* Responsive Design */
@media (max-width: 1400px) {
  .analytics-section {
    grid-template-columns: 1fr;
  }

  .content-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 1024px) {
  .sidebar {
    width: 240px;
  }

  .dashboard-main {
    margin-left: 240px;
  }

  .metrics-grid {
    grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
    gap: 16px;
  }

  .dashboard-content {
    padding: 20px 30px;
  }

  .dashboard-header {
    padding: 20px 30px;
  }

  .page-title {
    font-size: 1.8rem;
  }
}

@media (max-width: 768px) {
  .dashboard-container {
    flex-direction: column;
  }

  .sidebar {
    position: relative;
    width: 100%;
    height: auto;
    background: linear-gradient(90deg, #1e3c72 0%, #2a5298 100%);
  }

  .dashboard-main {
    margin-left: 0;
  }

  .sidebar-nav {
    display: flex;
    overflow-x: auto;
    padding: 10px 0;
    scrollbar-width: none;
    -ms-overflow-style: none;
  }

  .sidebar-nav::-webkit-scrollbar {
    display: none;
  }

  .nav-item {
    flex-shrink: 0;
    min-width: 120px;
    justify-content: center;
    padding: 12px 16px;
    flex-direction: column;
    gap: 4px;
  }

  .nav-text {
    font-size: 0.8rem;
  }

  .nav-badge {
    position: absolute;
    top: 8px;
    right: 8px;
  }

  .dashboard-header {
    padding: 16px 20px;
    flex-direction: column;
    gap: 16px;
    align-items: stretch;
  }

  .header-actions {
    flex-wrap: wrap;
    gap: 12px;
  }

  .page-title {
    font-size: 1.5rem;
  }

  .dashboard-content {
    padding: 16px 20px;
  }

  .metrics-grid {
    grid-template-columns: 1fr;
    gap: 12px;
  }

  .loading-overlay {
    left: 0;
  }

  .content-grid {
    grid-template-columns: 1fr;
    gap: 16px;
  }

  .card-content {
    padding: 16px 20px 20px;
  }

  .order-item,
  .stock-item,
  .product-item {
    padding: 16px;
  }
}

@media (max-width: 480px) {
  .dashboard-content {
    padding: 12px 16px;
  }

  .dashboard-header {
    padding: 12px 16px;
  }

  .metric-card {
    padding: 20px;
  }

  .metric-value {
    font-size: 2rem;
  }

  .order-item {
    flex-direction: column;
    align-items: stretch;
    text-align: left;
  }

  .order-amount {
    text-align: left;
    margin-top: 8px;
  }
}

/* Back to Store Button - Override scoped styles */
.back-to-store-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%) !important;
  color: white !important;
  padding: 8px 16px !important;
  border-radius: 8px !important;
  text-decoration: none !important;
  font-weight: 500 !important;
  font-size: 0.9rem !important;
  transition: all 0.3s ease !important;
  display: flex !important;
  align-items: center !important;
  gap: 6px !important;
  box-shadow: 0 2px 8px rgba(102, 126, 234, 0.2) !important;
}

.back-to-store-btn .icon {
  font-size: 1rem !important;
}

.back-to-store-btn:hover {
  transform: translateY(-1px) !important;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3) !important;
}
</style>

<style>
@import url('@/assets/styles/AdminStyles.css');
</style>
