<template>
  <div class="admin-page">
    <div class="dashboard-container">
      <!-- Sidebar Navigation -->
      <div class="sidebar">
        <div class="sidebar-header">
          <div class="logo">
            <span class="logo-icon">🍎</span>
            <h2 class="sidebar-title">Apple Store Admin</h2>
          </div>
          <div class="sidebar-status">
            <div class="status-indicator online"></div>
            <span class="status-text">Online</span>
          </div>
        </div>
        <nav class="sidebar-nav">
          <router-link to="/admin/overview" class="nav-item">
            <span class="nav-icon">📊</span>
            <span class="nav-text">Overview</span>
          </router-link>
          <router-link to="/admin/orders" class="nav-item">
            <span class="nav-icon">📋</span>
            <span class="nav-text">Orders</span>
          </router-link>
          <router-link to="/admin/products" class="nav-item">
            <span class="nav-icon">📱</span>
            <span class="nav-text">Products</span>
          </router-link>
          <router-link to="/admin/users" class="nav-item">
            <span class="nav-icon">👥</span>
            <span class="nav-text">Users</span>
          </router-link>
          <router-link to="/admin/discounts" class="nav-item">
            <span class="nav-icon">💰</span>
            <span class="nav-text">Discounts</span>
          </router-link>
          <router-link to="/admin/categories" class="nav-item">
            <span class="nav-icon">📂</span>
            <span class="nav-text">Categories</span>
          </router-link>
          <router-link to="/admin/settings" class="nav-item" active-class="active">
            <span class="nav-icon">⚙️</span>
            <span class="nav-text">Settings</span>
          </router-link>
        </nav>
      </div>

      <!-- Main Content -->
      <div class="admin-main">
        <!-- Header -->
        <div class="admin-header">
          <div class="header-left">
            <h1 class="page-title">Store Settings</h1>
            <p class="page-subtitle">Manage your store's configuration settings</p>
          </div>
          <div class="header-right">
            <router-link to="/" class="back-to-store-btn">
              <span class="icon">🏪</span>
              <span>Back to Store</span>
            </router-link>
          </div>
        </div>

        <!-- Settings Content -->
        <div class="settings-container">
          <!-- General Information -->
          <div class="settings-card">
            <div class="card-icon">🏪</div>
            <h2 class="card-title">Store Information</h2>
            <div class="form-group">
              <label class="form-label">Store Name</label>
              <input type="text" class="form-input" v-model="settings.storeName" />
            </div>
            <div class="form-group">
              <label class="form-label">Store Email</label>
              <input type="email" class="form-input" v-model="settings.storeEmail" />
            </div>
            <div class="form-group">
              <label class="form-label">Store Phone</label>
              <input type="tel" class="form-input" v-model="settings.storePhone" />
            </div>
            <div class="form-group">
              <label class="form-label">Store Address</label>
              <textarea class="form-textarea" rows="3" v-model="settings.storeAddress"></textarea>
            </div>
          </div>

          <!-- Currency & Tax -->
          <div class="settings-card">
            <div class="card-icon">💰</div>
            <h2 class="card-title">Currency & Tax Settings</h2>
            <div class="form-group">
              <label class="form-label">Default Currency</label>
              <select class="form-select" v-model="settings.currency">
                <option value="USD">USD - US Dollar</option>
                <option value="KHR">KHR - Cambodian Riel</option>
                <option value="EUR">EUR - Euro</option>
                <option value="GBP">GBP - British Pound</option>
              </select>
            </div>
            <div class="form-group">
              <label class="form-label">Tax Rate (%)</label>
              <input
                type="number"
                class="form-input"
                v-model.number="settings.taxRate"
                min="0"
                max="100"
                step="0.1"
              />
            </div>
            <div class="form-group">
              <label class="form-label">Shipping Fee ($)</label>
              <input
                type="number"
                class="form-input"
                v-model.number="settings.shippingFee"
                min="0"
                step="0.01"
              />
            </div>
          </div>

          <!-- Notifications -->
          <div class="settings-card">
            <div class="card-icon">🔔</div>
            <h2 class="card-title">Notification Preferences</h2>
            <div class="toggle-group">
              <label class="toggle-label">
                <span class="toggle-text">
                  <strong>New Order Notifications</strong>
                  <small>Get notified when customers place orders</small>
                </span>
                <label class="toggle-switch">
                  <input type="checkbox" v-model="settings.notifications.newOrders" />
                  <span class="toggle-slider"></span>
                </label>
              </label>
            </div>
            <div class="toggle-group">
              <label class="toggle-label">
                <span class="toggle-text">
                  <strong>Low Stock Alerts</strong>
                  <small>Alert when product stock is running low</small>
                </span>
                <label class="toggle-switch">
                  <input type="checkbox" v-model="settings.notifications.lowStock" />
                  <span class="toggle-slider"></span>
                </label>
              </label>
            </div>
            <div class="toggle-group">
              <label class="toggle-label">
                <span class="toggle-text">
                  <strong>Customer Feedback</strong>
                  <small>Receive customer reviews and feedback</small>
                </span>
                <label class="toggle-switch">
                  <input type="checkbox" v-model="settings.notifications.customerFeedback" />
                  <span class="toggle-slider"></span>
                </label>
              </label>
            </div>
            <div class="toggle-group">
              <label class="toggle-label">
                <span class="toggle-text">
                  <strong>Weekly Reports</strong>
                  <small>Receive weekly sales and performance reports</small>
                </span>
                <label class="toggle-switch">
                  <input type="checkbox" v-model="settings.notifications.weeklyReports" />
                  <span class="toggle-slider"></span>
                </label>
              </label>
            </div>
          </div>

          <!-- Business Hours -->
          <div class="settings-card">
            <div class="card-icon">🕒</div>
            <h2 class="card-title">Business Hours</h2>
            <div class="form-group">
              <label class="form-label">Opening Time</label>
              <input type="time" class="form-input" v-model="settings.openingTime" />
            </div>
            <div class="form-group">
              <label class="form-label">Closing Time</label>
              <input type="time" class="form-input" v-model="settings.closingTime" />
            </div>
          </div>

          <!-- Save Button -->
          <div class="save-section">
            <button @click="saveSettings" class="save-btn">💾 Save All Changes</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import Swal from 'sweetalert2'

const router = useRouter()

const settings = ref({
  storeName: 'Apple Store Cambodia',
  storeEmail: 'contact@applestoreca.com',
  storePhone: '+855 966 273 314',
  storeAddress: 'Russian Conf Norodom Boulevard, Phnom Penh 120404, Cambodia',
  currency: 'USD',
  taxRate: 10,
  shippingFee: 5.0,
  openingTime: '09:00',
  closingTime: '20:00',
  notifications: {
    newOrders: true,
    lowStock: true,
    customerFeedback: false,
    weeklyReports: true,
  },
})

onMounted(() => {
  const user = JSON.parse(localStorage.getItem('user') || '{}')
  if (!user.isLoggedIn || user.role !== 'admin') {
    router.push('/')
  }
})

const saveSettings = () => {
  Swal.fire({
    title: 'Save All Settings?',
    text: 'This will update all your store settings.',
    icon: 'question',
    showCancelButton: true,
    confirmButtonColor: '#667eea',
    cancelButtonColor: '#6c757d',
    confirmButtonText: '💾 Save Changes',
    cancelButtonText: 'Cancel',
    showLoaderOnConfirm: true,
    preConfirm: () => {
      return new Promise((resolve) => {
        setTimeout(() => {
          console.log('Saving settings:', settings.value)
          // TODO: Make API call to save settings to backend
          resolve()
        }, 800)
      })
    },
    allowOutsideClick: () => !Swal.isLoading(),
  }).then((result) => {
    if (result.isConfirmed) {
      Swal.fire({
        icon: 'success',
        title: 'Settings Saved!',
        text: 'All your settings have been updated successfully.',
        timer: 2500,
        showConfirmButton: false,
      })
    }
  })
}
</script>

<style scoped>
@import url('@/assets/styles/AdminStyles.css');

.page-subtitle {
  color: #6c757d;
  font-size: 0.9rem;
  margin: 5px 0 0 0;
}

.settings-container {
  padding: 30px;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(400px, 1fr));
  gap: 24px;
  max-width: 1400px;
}

.settings-card {
  background: white;
  border-radius: 16px;
  padding: 30px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  border: 1px solid #e9ecef;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.settings-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.settings-card:hover {
  box-shadow: 0 6px 30px rgba(0, 0, 0, 0.12);
  transform: translateY(-2px);
}

.card-icon {
  font-size: 2.5rem;
  margin-bottom: 16px;
}

.card-title {
  font-size: 1.3rem;
  font-weight: 600;
  color: #333;
  margin: 0 0 24px 0;
}

.form-group {
  margin-bottom: 20px;
}

.form-label {
  display: block;
  font-weight: 600;
  color: #495057;
  margin-bottom: 8px;
  font-size: 0.9rem;
}

.form-input,
.form-textarea,
.form-select {
  width: 100%;
  padding: 12px 16px;
  border: 2px solid #e9ecef;
  border-radius: 8px;
  font-size: 0.95rem;
  background: white;
  transition: all 0.3s ease;
}

.form-input:focus,
.form-textarea:focus,
.form-select:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.form-textarea {
  resize: vertical;
  font-family: inherit;
}

.toggle-group {
  padding: 16px;
  background: #f8f9fa;
  border-radius: 12px;
  margin-bottom: 12px;
  transition: all 0.3s ease;
}

.toggle-group:hover {
  background: #e9ecef;
}

.toggle-label {
  display: flex;
  justify-content: space-between;
  align-items: center;
  cursor: pointer;
  width: 100%;
}

.toggle-text {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.toggle-text strong {
  color: #333;
  font-size: 0.95rem;
}

.toggle-text small {
  color: #666;
  font-size: 0.85rem;
}

.toggle-switch {
  position: relative;
  width: 50px;
  height: 26px;
  margin-left: 16px;
}

.toggle-switch input {
  opacity: 0;
  width: 0;
  height: 0;
}

.toggle-slider {
  position: absolute;
  cursor: pointer;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: #ccc;
  transition: 0.4s;
  border-radius: 26px;
}

.toggle-slider:before {
  position: absolute;
  content: '';
  height: 20px;
  width: 20px;
  left: 3px;
  bottom: 3px;
  background-color: white;
  transition: 0.4s;
  border-radius: 50%;
}

.toggle-switch input:checked + .toggle-slider {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.toggle-switch input:checked + .toggle-slider:before {
  transform: translateX(24px);
}

.save-section {
  grid-column: 1 / -1;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 16px;
  padding: 40px;
  box-shadow: 0 6px 25px rgba(102, 126, 234, 0.3);
  text-align: center;
}

.save-btn {
  background: white;
  color: #667eea;
  padding: 16px 48px;
  border: none;
  border-radius: 10px;
  font-weight: 600;
  font-size: 1.1rem;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
}

.save-btn:hover {
  transform: translateY(-3px);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.3);
}

@media (max-width: 900px) {
  .settings-container {
    grid-template-columns: 1fr;
  }
}
</style>
