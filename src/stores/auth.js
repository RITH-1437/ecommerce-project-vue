import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

export const useAuthStore = defineStore('auth', () => {
  const user = ref(null)
  const isLoading = ref(false)
  const error = ref(null)

  // Computed properties
  const isLoggedIn = computed(() => !!user.value)
  const isAdmin = computed(() => user.value?.role === 'admin')
  const isEmployee = computed(() => user.value?.role === 'employee')
  const isCustomer = computed(() => user.value?.role === 'customer')
  const userName = computed(() => user.value?.name || user.value?.email?.split('@')[0] || '')
  const userInitials = computed(() => {
    if (!user.value?.name) return ''
    return user.value.name
      .split(' ')
      .map((name) => name.charAt(0))
      .join('')
      .toUpperCase()
  })

  // Permissions based on role
  const permissions = computed(() => {
    const role = user.value?.role
    return {
      canBrowse: true, // Everyone can browse
      canPurchase: role === 'customer' || role === 'employee' || role === 'admin',
      canManageInventory: role === 'employee' || role === 'admin',
      canViewReports: role === 'employee' || role === 'admin',
      canManageUsers: role === 'admin',
      canManageDiscounts: role === 'admin',
      canManageSettings: role === 'admin',
      canViewAdminPanel: role === 'admin' || role === 'employee',
    }
  })

  // Helper methods for permission checks
  const hasPermission = (permission) => {
    return permissions.value[permission] || false
  }

  const canAccessAdmin = computed(() => permissions.value.canViewAdminPanel)

  // Initialize auth state from localStorage
  const initializeAuth = () => {
    try {
      const savedUser = localStorage.getItem('user')
      if (savedUser) {
        user.value = JSON.parse(savedUser)
      }
    } catch (error) {
      console.error('Error loading saved user data:', error)
      localStorage.removeItem('user')
    }
  }

  // Actions
  const login = (userData) => {
    user.value = {
      ...userData,
      loginTime: new Date().toISOString(),
      isLoggedIn: true,
    }

    // Save to localStorage
    localStorage.setItem('user', JSON.stringify(user.value))

    // Clear any previous errors
    error.value = null
  }

  const logout = () => {
    user.value = null
    error.value = null

    // Clear localStorage
    localStorage.removeItem('user')

    // Clear any Google Sign-In state
    if (window.google) {
      window.google.accounts.id.disableAutoSelect()
    }
  }

  const updateUser = (updates) => {
    if (user.value) {
      user.value = { ...user.value, ...updates }
      localStorage.setItem('user', JSON.stringify(user.value))
    }
  }

  const setLoading = (loading) => {
    isLoading.value = loading
  }

  const setError = (errorMessage) => {
    error.value = errorMessage
  }

  const clearError = () => {
    error.value = null
  }

  // Initialize on store creation
  initializeAuth()

  return {
    // State
    user,
    isLoading,
    error,

    // Computed
    isLoggedIn,
    isAdmin,
    isEmployee,
    isCustomer,
    userName,
    userInitials,
    permissions,
    canAccessAdmin,

    // Actions
    login,
    logout,
    updateUser,
    setLoading,
    setError,
    clearError,
    initializeAuth,

    // Helpers
    hasPermission,
  }
})
