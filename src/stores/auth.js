import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

export const useAuthStore = defineStore('auth', () => {
  const user = ref(null)
  const isLoading = ref(false)
  const error = ref(null)

  // Computed properties
  const isLoggedIn = computed(() => !!user.value)
  const isAdmin = computed(() => user.value?.role === 'admin')
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
    isCustomer,
    userName,
    userInitials,

    // Actions
    login,
    logout,
    updateUser,
    setLoading,
    setError,
    clearError,
    initializeAuth,
  }
})
