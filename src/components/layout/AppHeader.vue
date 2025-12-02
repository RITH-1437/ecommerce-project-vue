<template>
  <header class="app-header">
    <div class="container">
      <router-link to="/" class="nav-brand">{{ logoText }}</router-link>
      <div v-if="showSearch" class="search-container">
        <input
          type="text"
          :placeholder="searchPlaceholder"
          class="search-input"
          v-model="searchQuery"
          @input="$emit('search', searchQuery)"
        />
        <span class="search-icon">🔍</span>
      </div>
      <nav class="nav-menu">
        <router-link to="/iphone" class="nav-link">iPhone</router-link>
        <router-link to="/ipad" class="nav-link">iPad</router-link>
        <router-link to="/macbook" class="nav-link">MacBook</router-link>
        <router-link to="/watch" class="nav-link">Watch</router-link>
        <router-link to="/airpods" class="nav-link">AirPods</router-link>
        <router-link
          v-if="isLoggedIn && userRole === 'admin'"
          to="/admin/overview"
          class="nav-link dashboard"
          >Dashboard</router-link
        >
      </nav>
      <div class="header-actions">
        <div v-if="showCart" class="cart-icon" @click="goToCart">
          <span class="icon">🛒</span>
          <span v-if="cartStore.itemCount > 0" class="cart-count">{{ cartStore.itemCount }}</span>
        </div>
        <!-- Show auth buttons when not logged in -->
        <div v-if="showProfile && !isLoggedIn" class="auth-buttons">
          <router-link to="/auth" class="auth-btn login">Sign In</router-link>
        </div>

        <!-- Show profile dropdown when logged in -->
        <div
          v-if="showProfile && isLoggedIn"
          class="profile-dropdown"
          @click="toggleDropdown"
          ref="profileDropdown"
        >
          <div class="profile-icon">
            <img
              v-if="authStore.user?.picture"
              :src="authStore.user.picture"
              :alt="userName"
              class="profile-image"
            />
            <span v-else class="icon">{{ userAvatar }}</span>
          </div>
          <div class="dropdown-menu" :class="{ show: isDropdownOpen }">
            <div class="dropdown-header">
              <div class="user-avatar">
                <img
                  v-if="authStore.user?.picture"
                  :src="authStore.user.picture"
                  :alt="userName"
                  class="avatar-image"
                />
                <span v-else class="avatar-text">{{ userAvatar }}</span>
              </div>
              <div class="user-info">
                <div class="user-name">{{ userName }}</div>
                <div class="user-email">{{ userEmail }}</div>
                <div v-if="isGoogleUser" class="user-provider">
                  <span class="provider-icon">🔗</span>
                  <span>Google Account</span>
                </div>
              </div>
            </div>
            <div class="dropdown-divider"></div>
            <div v-if="userRole === 'admin'" class="dropdown-item" @click="goToDashboard">
              <span class="item-icon">📊</span>
              <span>Admin Dashboard</span>
            </div>
            <div class="dropdown-item" @click="viewProfile">
              <span class="item-icon">👨‍💼</span>
              <span>My Profile</span>
            </div>
            <div class="dropdown-item" @click="viewOrders">
              <span class="item-icon">📦</span>
              <span>My Orders</span>
            </div>
            <div class="dropdown-item" @click="viewSettings">
              <span class="item-icon">⚙️</span>
              <span>Settings</span>
            </div>
            <div class="dropdown-divider"></div>
            <div class="dropdown-item logout" @click="logout">
              <span class="item-icon">🚪</span>
              <span>Logout</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </header>
</template>

<script>
import { useCartStore } from '@/stores/counter.js'
import { useAuthStore } from '@/stores/auth.js'
import { useGoogleAuth } from '@/composables/useGoogleAuth.js'

export default {
  name: 'AppHeader',
  setup() {
    const cartStore = useCartStore()
    const authStore = useAuthStore()
    const { signOut } = useGoogleAuth()

    return {
      cartStore,
      authStore,
      googleSignOut: signOut,
    }
  },
  props: {
    showSearch: {
      type: Boolean,
      default: true,
    },
    showProfile: {
      type: Boolean,
      default: true,
    },
    showCart: {
      type: Boolean,
      default: true,
    },
    logoText: {
      type: String,
      default: 'Apple Store',
    },
    searchPlaceholder: {
      type: String,
      default: 'Search products...',
    },
    theme: {
      type: String,
      default: 'light',
      validator: (value) => ['light', 'dark'].includes(value),
    },
  },
  data() {
    return {
      searchQuery: '',
      isDropdownOpen: false,
    }
  },
  computed: {
    userName() {
      return this.authStore.userName || 'Guest'
    },
    userEmail() {
      return this.authStore.user?.email || 'guest@example.com'
    },
    isLoggedIn() {
      return this.authStore.isLoggedIn
    },
    userRole() {
      return this.authStore.user?.role || null
    },
    userAvatar() {
      // Show Google profile picture if available, otherwise use initials
      if (this.authStore.user?.picture) {
        return this.authStore.user.picture
      }
      return this.authStore.userInitials || '👤'
    },
    isGoogleUser() {
      return this.authStore.user?.provider === 'google'
    },
  },
  mounted() {
    document.addEventListener('click', this.handleClickOutside)
  },
  beforeUnmount() {
    document.removeEventListener('click', this.handleClickOutside)
  },
  methods: {
    toggleDropdown() {
      this.isDropdownOpen = !this.isDropdownOpen
    },
    handleClickOutside(event) {
      if (this.$refs.profileDropdown && !this.$refs.profileDropdown.contains(event.target)) {
        this.isDropdownOpen = false
      }
    },
    viewProfile() {
      this.isDropdownOpen = false
      console.log('Navigate to profile')
      // Add navigation logic here
    },
    viewOrders() {
      this.isDropdownOpen = false
      console.log('Navigate to orders')
      // Add navigation logic here
    },
    viewSettings() {
      this.isDropdownOpen = false
      console.log('Navigate to settings')
      // Add navigation logic here
    },
    goToDashboard() {
      this.isDropdownOpen = false
      this.$router.push('/admin/overview')
    },
    goToCart() {
      this.$router.push('/checkout/cart')
    },
    async logout() {
      this.isDropdownOpen = false

      try {
        // Use Google sign out if user signed in with Google
        if (this.isGoogleUser) {
          await this.googleSignOut()
        }

        // Clear auth store
        this.authStore.logout()

        // Redirect to home page
        this.$router.push('/')

        console.log('User logged out successfully')
        this.$emit('logout')
      } catch (error) {
        console.error('Logout error:', error)
        // Fallback logout
        this.authStore.logout()
        this.$router.push('/')
      }
    },
  },
  emits: ['search', 'logout'],
}
</script>

<style scoped>
.app-header {
  background: white;
  box-shadow: 0 2px 20px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 100;
  backdrop-filter: blur(20px);
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 60px;
}

.nav-brand {
  font-size: 22px;
  font-weight: 700;
  color: #1d1d1f;
  text-decoration: none;
  transition: all 0.3s ease;
}

.nav-brand:hover {
  color: #0071e3;
}

.search-container {
  position: relative;
  flex: 1;
  max-width: 400px;
  margin: 0 40px;
}

.search-input {
  width: 100%;
  padding: 10px 40px 10px 16px;
  border: 2px solid #f5f5f7;
  border-radius: 25px;
  outline: none;
  font-size: 14px;
  background: #f5f5f7;
  transition: all 0.3s ease;
}

.search-input:focus {
  border-color: #0071e3;
  background: white;
  box-shadow: 0 0 0 4px rgba(0, 113, 227, 0.1);
}

.search-icon {
  position: absolute;
  right: 12px;
  top: 50%;
  transform: translateY(-50%);
  color: #86868b;
  pointer-events: none;
}

.nav-menu {
  display: flex;
  gap: 32px;
}

.nav-link {
  color: #1d1d1f;
  text-decoration: none;
  font-weight: 500;
  font-size: 15px;
  transition: all 0.3s ease;
  position: relative;
}

.nav-link:hover,
.nav-link.router-link-active {
  color: #0071e3;
}

.nav-link::after {
  content: '';
  position: absolute;
  bottom: -8px;
  left: 0;
  width: 0;
  height: 2px;
  background: #0071e3;
  transition: width 0.3s ease;
}

.nav-link.router-link-active::after {
  width: 100%;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 16px;
}

.cart-icon {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: #f5f5f7;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  z-index: 1;
}

.cart-icon::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, #0066cc, #0077ed);
  opacity: 0;
  border-radius: 50%;
  transition: opacity 0.3s ease;
}

.cart-icon:hover {
  background: #e8e8ed;
  transform: scale(1.1) translateY(-2px);
  box-shadow: 0 8px 25px rgba(0, 102, 204, 0.2);
}

.cart-icon:hover::before {
  opacity: 0.1;
}

.cart-icon:active {
  transform: scale(0.95);
}

.cart-icon .icon {
  font-size: 18px;
  position: relative;
  z-index: 1;
  transition: transform 0.3s ease;
}

.cart-icon:hover .icon {
  transform: scale(1.1);
  animation: wiggle 0.6s ease;
}

@keyframes wiggle {
  0%,
  100% {
    transform: rotate(0deg) scale(1.1);
  }
  25% {
    transform: rotate(-3deg) scale(1.1);
  }
  75% {
    transform: rotate(3deg) scale(1.1);
  }
}

.cart-count {
  position: absolute;
  top: -8px;
  right: -8px;
  background: linear-gradient(135deg, #ff3b30, #ff6b6b);
  color: white;
  border-radius: 12px;
  width: 22px;
  height: 22px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: 700;
  box-shadow: 0 3px 12px rgba(255, 59, 48, 0.5);
  animation: pulse 2s infinite;
  z-index: 10;
  border: 2px solid white;
}

@keyframes pulse {
  0%,
  100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.1);
  }
}

.auth-buttons {
  display: flex;
  gap: 12px;
}

.auth-btn {
  padding: 10px 20px;
  border-radius: 25px;
  text-decoration: none;
  font-weight: 600;
  font-size: 14px;
  transition: all 0.3s ease;
  border: 2px solid transparent;
}

.auth-btn.login {
  background: #0071e3;
  color: white;
}

.auth-btn.login:hover {
  background: #0056b3;
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(0, 113, 227, 0.3);
}

.profile-dropdown {
  position: relative;
  display: inline-block;
}

.profile-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  cursor: pointer;
  transition: all 0.3s ease;
}

.profile-icon:hover {
  transform: scale(1.05);
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
}

.profile-icon .icon {
  font-size: 18px;
  color: white;
}

.dropdown-menu {
  position: absolute;
  top: calc(100% + 8px);
  right: 0;
  width: 280px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.15);
  opacity: 0;
  visibility: hidden;
  transform: translateY(-10px);
  transition: all 0.3s ease;
  z-index: 1000;
  border: 1px solid #f0f0f0;
}

.dropdown-menu.show {
  opacity: 1;
  visibility: visible;
  transform: translateY(0);
}

.dropdown-header {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 12px 12px 0 0;
}

.user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  color: white;
}

.user-info {
  flex: 1;
}

.user-name {
  font-size: 16px;
  font-weight: 600;
  color: #1d1d1f;
  margin-bottom: 2px;
}

.user-email {
  font-size: 13px;
  color: #86868b;
}

.user-provider {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 11px;
  color: #0071e3;
  margin-top: 4px;
}

.provider-icon {
  font-size: 10px;
}

.profile-image,
.avatar-image {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  object-fit: cover;
}

.avatar-text {
  font-size: 18px;
  color: white;
}

.dropdown-divider {
  height: 1px;
  background: #f0f0f0;
  margin: 8px 0;
}

.dropdown-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 20px;
  cursor: pointer;
  transition: all 0.2s ease;
  font-size: 14px;
  color: #1d1d1f;
}

.dropdown-item:hover {
  background: #f5f5f7;
}

.dropdown-item.logout {
  color: #ff3b30;
}

.dropdown-item.logout:hover {
  background: #fff5f5;
}

.item-icon {
  font-size: 16px;
  width: 20px;
  text-align: center;
}

@media (max-width: 768px) {
  .container {
    flex-direction: column;
    height: auto;
    padding: 16px 20px;
    gap: 16px;
  }

  .search-container {
    margin: 0;
    max-width: 100%;
  }

  .nav-menu {
    gap: 20px;
    flex-wrap: wrap;
    justify-content: center;
  }

  .auth-buttons {
    gap: 8px;
  }

  .auth-btn {
    padding: 8px 16px;
    font-size: 13px;
  }
}

/* Dashboard Navigation Styling */
.nav-link.dashboard {
  background: linear-gradient(135deg, #007aff, #0051d5);
  color: white;
  border-radius: 8px;
  padding: 8px 16px;
  margin-left: 12px;
  font-weight: 600;
  transition: all 0.3s ease;
}

.nav-link.dashboard:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(0, 122, 255, 0.3);
}

.nav-link.dashboard::after {
  display: none;
}
</style>
