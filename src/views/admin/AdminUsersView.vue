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
          <router-link to="/admin/users" class="nav-item" active-class="active">
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
          <router-link to="/admin/settings" class="nav-item">
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
            <h1 class="page-title">Users</h1>
          </div>
          <div class="header-right">
            <router-link to="/" class="back-to-store-btn">
              <span class="icon">🏪</span>
              <span>Back to Store</span>
            </router-link>
          </div>
        </div>

        <!-- Search and Filter Bar -->
        <div class="search-section">
          <div class="search-bar">
            <input
              v-model="searchQuery"
              type="text"
              placeholder="Search users..."
              class="search-input"
            />
          </div>
          <div class="filter-group">
            <label for="roleFilter">Role:</label>
            <select id="roleFilter" v-model="selectedRole" class="role-filter">
              <option value="">All Roles</option>
              <option value="customer">Customer</option>
              <option value="employee">Employee</option>
              <option value="admin">Admin</option>
            </select>
          </div>
        </div>

        <!-- Users Table -->
        <div class="content-section">
          <div class="table-container">
            <table class="data-table">
              <thead>
                <tr>
                  <th>Name</th>
                  <th>Email</th>
                  <th>Role</th>
                  <th>Status</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="user in filteredUsers" :key="user.id">
                  <td>{{ user.name }}</td>
                  <td>{{ user.email }}</td>
                  <td>
                    <span class="role" :class="`role-${user.role}`">
                      {{ user.role }}
                    </span>
                  </td>
                  <td>
                    <span class="status-badge" :class="user.isActive ? 'active' : 'inactive'">
                      {{ user.isActive ? 'Active' : 'Inactive' }}
                    </span>
                  </td>
                  <td>
                    <button @click="viewUser(user)" class="action-btn">View</button>
                  </td>
                </tr>
                <tr v-if="filteredUsers.length === 0">
                  <td colspan="5" class="no-data">No users found</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>

    <!-- User Modal -->
    <UserModal
      :is-open="isModalOpen"
      :user="selectedUser"
      @close="closeModal"
      @updateRole="updateUserRole"
      @toggleStatus="toggleUserStatus"
    />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import Swal from 'sweetalert2'
import UserModal from '@/components/admin/UserModal.vue'

const router = useRouter()

const users = ref([
  {
    id: 1,
    name: 'Ethan Carter',
    email: 'ethan.carter@gmail.com',
    role: 'customer',
    isActive: true,
    phone: '+1 (555) 123-4567',
    address: '123 Main St',
    city: 'New York',
    state: 'NY',
    zipCode: '10001',
    createdAt: '2023-01-15',
    lastLogin: '2024-01-20',
    totalOrders: 12,
  },
  {
    id: 2,
    name: 'Olivia Bennett',
    email: 'olivia.bennett@gmail.com',
    role: 'customer',
    isActive: true,
    phone: '+1 (555) 234-5678',
    createdAt: '2023-03-20',
    lastLogin: '2024-01-19',
    totalOrders: 8,
  },
  {
    id: 3,
    name: 'Noah Thompson',
    email: 'noah.thompson@gmail.com',
    role: 'employee',
    isActive: true,
    phone: '+1 (555) 345-6789',
    createdAt: '2022-06-10',
    lastLogin: '2024-01-21',
    totalOrders: 0,
  },
  {
    id: 4,
    name: 'Ava Hudson',
    email: 'ava.hudson@gmail.com',
    role: 'customer',
    isActive: false,
    createdAt: '2023-08-05',
    lastLogin: '2023-12-01',
    totalOrders: 3,
  },
  {
    id: 5,
    name: 'Liam Foster',
    email: 'liam.foster@gmail.com',
    role: 'customer',
    isActive: true,
    createdAt: '2023-09-12',
    lastLogin: '2024-01-18',
    totalOrders: 15,
  },
  {
    id: 6,
    name: 'Sophia Hayes',
    email: 'sophia.hayes@gmail.com',
    role: 'employee',
    isActive: true,
    createdAt: '2022-11-20',
    lastLogin: '2024-01-21',
    totalOrders: 0,
  },
  {
    id: 7,
    name: 'Jackson Reed',
    email: 'jackson.reed@gmail.com',
    role: 'customer',
    isActive: true,
    createdAt: '2023-04-18',
    lastLogin: '2024-01-17',
    totalOrders: 6,
  },
  {
    id: 8,
    name: 'Isabella Morgan',
    email: 'isabella.morgan@gmail.com',
    role: 'admin',
    isActive: true,
    createdAt: '2022-01-10',
    lastLogin: '2024-01-21',
    totalOrders: 0,
  },
  {
    id: 9,
    name: 'Lucas Parker',
    email: 'lucas.parker@gmail.com',
    role: 'employee',
    isActive: true,
    createdAt: '2023-02-14',
    lastLogin: '2024-01-20',
    totalOrders: 0,
  },
  {
    id: 10,
    name: 'Mia Coleman',
    email: 'mia.coleman@gmail.com',
    role: 'customer',
    isActive: true,
    createdAt: '2023-10-30',
    lastLogin: '2024-01-16',
    totalOrders: 20,
  },
])

const searchQuery = ref('')
const selectedRole = ref('')
const isModalOpen = ref(false)
const selectedUser = ref(null)

onMounted(() => {
  const user = JSON.parse(localStorage.getItem('user') || '{}')
  if (!user.isLoggedIn || user.role !== 'admin') {
    router.push('/')
  }
})

const filteredUsers = computed(() => {
  return users.value.filter((user) => {
    const matchesSearch =
      user.name.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
      user.email.toLowerCase().includes(searchQuery.value.toLowerCase())
    const matchesRole = !selectedRole.value || user.role === selectedRole.value
    return matchesSearch && matchesRole
  })
})

const viewUser = (user) => {
  selectedUser.value = { ...user }
  isModalOpen.value = true
}

const closeModal = () => {
  isModalOpen.value = false
  selectedUser.value = null
}

const updateUserRole = (userId, newRole) => {
  const userIndex = users.value.findIndex((u) => u.id === userId)
  if (userIndex === -1) return

  const user = users.value[userIndex]
  const oldRole = user.role

  Swal.fire({
    title: 'Update User Role?',
    html: `
      <div style="text-align: left; padding: 15px;">
        <p style="margin-bottom: 15px;">Change user role:</p>
        <div style="background: #f8f9fa; padding: 15px; border-radius: 8px; margin-bottom: 15px;">
          <div style="display: flex; align-items: center; gap: 10px; margin-bottom: 10px;">
            <span style="font-weight: 600; color: #6c757d;">Current Role:</span>
            <span style="padding: 4px 12px; background: #007bff; color: white; border-radius: 12px; font-size: 0.85rem; text-transform: capitalize;">${oldRole}</span>
          </div>
          <div style="display: flex; align-items: center; gap: 10px;">
            <span style="font-weight: 600; color: #6c757d;">New Role:</span>
            <span style="padding: 4px 12px; background: #28a745; color: white; border-radius: 12px; font-size: 0.85rem; text-transform: capitalize;">${newRole}</span>
          </div>
        </div>
      </div>
    `,
    icon: 'question',
    showCancelButton: true,
    confirmButtonColor: '#667eea',
    cancelButtonColor: '#6c757d',
    confirmButtonText: '✓ Update Role',
    cancelButtonText: 'Cancel',
  }).then((result) => {
    if (result.isConfirmed) {
      users.value[userIndex].role = newRole
      closeModal()
      Swal.fire({
        icon: 'success',
        title: 'Role Updated!',
        text: `User role has been changed from ${oldRole} to ${newRole}`,
        timer: 2000,
        showConfirmButton: false,
      })
      // TODO: Make API call to update backend
    }
  })
}

const toggleUserStatus = (userId, isActive) => {
  const userIndex = users.value.findIndex((u) => u.id === userId)
  if (userIndex !== -1) {
    const action = isActive ? 'activate' : 'deactivate'
    const actionTitle = isActive ? 'Activate' : 'Deactivate'

    Swal.fire({
      title: `${actionTitle} User Account?`,
      html: `Are you sure you want to <strong>${action}</strong> this user account?`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: isActive ? '#28a745' : '#dc3545',
      cancelButtonColor: '#6c757d',
      confirmButtonText: `Yes, ${action} it!`,
      cancelButtonText: 'Cancel',
    }).then((result) => {
      if (result.isConfirmed) {
        users.value[userIndex].isActive = isActive
        closeModal()
        Swal.fire({
          icon: 'success',
          title: `Account ${isActive ? 'Activated' : 'Deactivated'}!`,
          text: `User ${isActive ? 'activated' : 'deactivated'} successfully!`,
          timer: 2000,
          showConfirmButton: false,
        })
        // TODO: Make API call to update backend
      }
    })
  }
}
</script>

<style scoped>
@import url('@/assets/styles/AdminStyles.css');

.role {
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 0.8rem;
  font-weight: 600;
  text-transform: capitalize;
  display: inline-block;
}

.role-customer {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.role-employee {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  color: white;
}

.role-admin {
  background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
  color: #333;
}

.status-badge {
  padding: 4px 10px;
  border-radius: 12px;
  font-size: 0.8rem;
  font-weight: 600;
}

.status-badge.active {
  background: #e8f5e9;
  color: #2e7d32;
}

.status-badge.inactive {
  background: #ffebee;
  color: #c62828;
}

.search-section {
  padding: 20px 30px;
  background: white;
  border-bottom: 1px solid #e9ecef;
  display: flex;
  gap: 20px;
  align-items: center;
}

.search-bar {
  flex: 1;
  max-width: 400px;
}

.search-input {
  width: 100%;
  padding: 12px 16px;
  border: 2px solid #e9ecef;
  border-radius: 8px;
  font-size: 0.9rem;
  transition: all 0.3s ease;
}

.search-input:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.filter-group {
  display: flex;
  align-items: center;
  gap: 12px;
}

.filter-group label {
  font-weight: 600;
  color: #495057;
  font-size: 0.9rem;
}

.role-filter {
  padding: 10px 16px;
  border: 2px solid #e9ecef;
  border-radius: 8px;
  font-size: 0.9rem;
  cursor: pointer;
  transition: all 0.3s ease;
  min-width: 150px;
}

.role-filter:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.action-btn {
  padding: 6px 16px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 0.85rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.action-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
}

.no-data {
  text-align: center;
  padding: 40px 20px;
  color: #999;
  font-style: italic;
}

@media (max-width: 768px) {
  .search-section {
    flex-direction: column;
    align-items: stretch;
  }

  .search-bar {
    max-width: 100%;
  }

  .filter-group {
    width: 100%;
  }

  .role-filter {
    flex: 1;
  }
}
</style>
