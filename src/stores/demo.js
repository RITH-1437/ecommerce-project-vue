import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

export const useDemoStore = defineStore('demo', () => {
  // Demo users with different roles
  const demoUsers = ref([
    {
      id: 'demo-admin-001',
      name: 'Demo Admin',
      email: 'admin@demo.com',
      role: 'admin',
      provider: 'demo',
      firstName: 'Demo',
      lastName: 'Admin',
      avatar: '👑',
      bio: 'System Administrator with full access to all features',
      joinDate: '2024-01-01',
      permissions: [
        'Manage Users',
        'Manage Products',
        'Manage Orders',
        'View Reports',
        'System Settings',
        'Full Access'
      ]
    },
    {
      id: 'demo-employee-001',
      name: 'Demo Employee',
      email: 'employee@demo.com',
      role: 'employee',
      provider: 'demo',
      firstName: 'Demo',
      lastName: 'Employee',
      avatar: '👷',
      bio: 'Store employee with inventory and customer support access',
      joinDate: '2024-01-15',
      permissions: [
        'Manage Inventory',
        'View Reports',
        'Process Orders',
        'Customer Support'
      ]
    },
    {
      id: 'demo-customer-001',
      name: 'Demo Customer',
      email: 'customer@demo.com',
      role: 'customer',
      provider: 'demo',
      firstName: 'Demo',
      lastName: 'Customer',
      avatar: '🛒',
      bio: 'Regular customer with shopping and order tracking access',
      joinDate: '2024-01-20',
      permissions: [
        'Browse Products',
        'Make Purchases',
        'Track Orders',
        'View Profile'
      ]
    },
    {
      id: 'demo-customer-002',
      name: 'Sarah Johnson',
      email: 'sarah.johnson@demo.com',
      role: 'customer',
      provider: 'demo',
      firstName: 'Sarah',
      lastName: 'Johnson',
      avatar: '👩',
      bio: 'Tech enthusiast and frequent shopper',
      joinDate: '2024-02-01',
      permissions: [
        'Browse Products',
        'Make Purchases',
        'Track Orders',
        'View Profile'
      ]
    },
    {
      id: 'demo-customer-003',
      name: 'Mike Chen',
      email: 'mike.chen@demo.com',
      role: 'customer',
      provider: 'demo',
      firstName: 'Mike',
      lastName: 'Chen',
      avatar: '👨',
      bio: 'Apple product collector and reviewer',
      joinDate: '2024-02-10',
      permissions: [
        'Browse Products',
        'Make Purchases',
        'Track Orders',
        'View Profile'
      ]
    }
  ])

  // Current demo user index for cycling
  const currentDemoIndex = ref(0)

  // Computed properties
  const currentDemoUser = computed(() => demoUsers.value[currentDemoIndex.value])
  const demoUsersByRole = computed(() => {
    return demoUsers.value.reduce((acc, user) => {
      if (!acc[user.role]) acc[user.role] = []
      acc[user.role].push(user)
      return acc
    }, {})
  })

  const allRoles = computed(() => ['admin', 'employee', 'customer'])

  // Methods
  const getNextDemoUser = () => {
    currentDemoIndex.value = (currentDemoIndex.value + 1) % demoUsers.value.length
    return currentDemoUser.value
  }

  const getDemoUserByRole = (role) => {
    const roleUsers = demoUsersByRole.value[role] || []
    return roleUsers[Math.floor(Math.random() * roleUsers.length)]
  }

  const getDemoUserById = (id) => {
    return demoUsers.value.find(user => user.id === id)
  }

  const getAllDemoUsers = () => {
    return demoUsers.value
  }

  const resetDemoIndex = () => {
    currentDemoIndex.value = 0
  }

  // Create login-ready user data
  const createLoginData = (user) => {
    return {
      ...user,
      loginTime: new Date().toISOString(),
      isLoggedIn: true,
    }
  }

  return {
    // State
    demoUsers,
    currentDemoIndex,

    // Computed
    currentDemoUser,
    demoUsersByRole,
    allRoles,

    // Methods
    getNextDemoUser,
    getDemoUserByRole,
    getDemoUserById,
    getAllDemoUsers,
    resetDemoIndex,
    createLoginData,
  }
})
