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
          <router-link to="/admin/orders" class="nav-item">
            <span class="nav-icon"><i class="fas fa-clipboard-list"></i></span>
            <span class="nav-text">Orders</span>
          </router-link>
          <router-link to="/admin/products" class="nav-item">
            <span class="nav-icon"><i class="fas fa-box"></i></span>
            <span class="nav-text">Products</span>
          </router-link>
          <router-link to="/admin/users" class="nav-item" active-class="active">
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
            <h1 class="page-title">👥 User Management</h1>
            <p class="page-subtitle">Manage user accounts and roles</p>
          </div>
          <div class="header-right">
            <router-link to="/" class="back-to-store-btn">
              <span class="icon"><i class="fas fa-store"></i></span>
              <span>Back to Store</span>
            </router-link>
          </div>
        </div>

        <div class="content-section">
          <!-- Search and Filters -->
          <div class="filters-section">
            <div class="search-box">
              <input
                type="text"
                v-model="searchQuery"
                placeholder="Search users by name or email..."
                class="search-input"
              />
              <span class="search-icon">🔍</span>
            </div>

            <div class="filter-tabs">
              <button
                v-for="role in ['all', 'customer', 'employee', 'admin']"
                :key="role"
                :class="['filter-tab', { active: activeFilter === role }]"
                @click="activeFilter = role"
              >
                {{
                  role === 'all' ? 'All Users' : role.charAt(0).toUpperCase() + role.slice(1) + 's'
                }}
                <span class="count-badge">{{ getRoleCount(role) }}</span>
              </button>
            </div>
          </div>

          <!-- Users Table -->
          <div class="users-table-container">
            <table class="users-table">
              <thead>
                <tr>
                  <th>User</th>
                  <th>Email</th>
                  <th>Role</th>
                  <th>Status</th>
                  <th>Joined</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="user in filteredUsers" :key="user.id" class="user-row">
                  <td class="user-cell">
                    <div class="user-info">
                      <div class="user-avatar">
                        {{ user.name.charAt(0).toUpperCase() }}
                      </div>
                      <div class="user-details">
                        <div class="user-name">{{ user.name }}</div>
                        <div class="user-id">ID: {{ user.id }}</div>
                      </div>
                    </div>
                  </td>
                  <td class="email-cell">{{ user.email }}</td>
                  <td class="role-cell">
                    <span :class="['role-badge', user.role]">
                      {{ user.role.charAt(0).toUpperCase() + user.role.slice(1) }}
                    </span>
                  </td>
                  <td class="status-cell">
                    <span :class="['status-badge', user.status || 'active']">
                      {{
                        (user.status || 'active').charAt(0).toUpperCase() +
                        (user.status || 'active').slice(1)
                      }}
                    </span>
                  </td>
                  <td class="joined-cell">
                    {{ formatDate(user.loginTime || user.createdAt) }}
                  </td>
                  <td class="actions-cell">
                    <div class="action-buttons">
                      <button
                        @click="openRoleModal(user)"
                        class="action-btn edit-role"
                        title="Change Role"
                      >
                        👤
                      </button>
                      <button
                        @click="toggleUserStatus(user)"
                        class="action-btn"
                        :class="user.status === 'suspended' ? 'activate' : 'suspend'"
                        :title="user.status === 'suspended' ? 'Activate User' : 'Suspend User'"
                      >
                        {{ user.status === 'suspended' ? '✅' : '🚫' }}
                      </button>
                      <button
                        @click="viewUserDetails(user)"
                        class="action-btn view-details"
                        title="View Details"
                      >
                        👁��
                      </button>
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>

            <!-- Empty State -->
            <div v-if="filteredUsers.length === 0" class="empty-state">
              <div class="empty-icon">👥</div>
              <h3>No users found</h3>
              <p>Try adjusting your search or filter criteria.</p>
            </div>
          </div>

          <!-- Pagination -->
          <div v-if="totalPages > 1" class="pagination">
            <button @click="currentPage--" :disabled="currentPage === 1" class="pagination-btn">
              ← Previous
            </button>

            <span class="pagination-info"> Page {{ currentPage }} of {{ totalPages }} </span>

            <button
              @click="currentPage++"
              :disabled="currentPage === totalPages"
              class="pagination-btn"
            >
              Next →
            </button>
          </div>
        </div>

        <!-- Role Change Modal -->
        <div v-if="showRoleModal" class="modal-overlay" @click="closeRoleModal">
          <div class="modal-content" @click.stop>
            <div class="modal-header">
              <h3>Change User Role</h3>
              <button @click="closeRoleModal" class="modal-close">×</button>
            </div>

            <div class="modal-body">
              <div class="user-summary">
                <div class="user-avatar-large">
                  {{ selectedUser?.name.charAt(0).toUpperCase() }}
                </div>
                <div class="user-summary-details">
                  <h4>{{ selectedUser?.name }}</h4>
                  <p>{{ selectedUser?.email }}</p>
                  <p>
                    Current Role:
                    <span :class="['role-badge', selectedUser?.role]">{{
                      selectedUser?.role
                    }}</span>
                  </p>
                </div>
              </div>

              <div class="role-selection">
                <label>Select New Role:</label>
                <div class="role-options">
                  <label
                    v-for="role in ['customer', 'employee', 'admin']"
                    :key="role"
                    class="role-option"
                  >
                    <input
                      type="radio"
                      :value="role"
                      v-model="newRole"
                      :disabled="role === selectedUser?.role"
                    />
                    <span class="role-option-content">
                      <span class="role-icon">{{ getRoleIcon(role) }}</span>
                      <span class="role-name">{{
                        role.charAt(0).toUpperCase() + role.slice(1)
                      }}</span>
                      <span class="role-description">{{ getRoleDescription(role) }}</span>
                    </span>
                  </label>
                </div>
              </div>
            </div>

            <div class="modal-footer">
              <button @click="closeRoleModal" class="btn-secondary">Cancel</button>
              <button
                @click="confirmRoleChange"
                :disabled="!newRole || newRole === selectedUser?.role"
                class="btn-primary"
              >
                Update Role
              </button>
            </div>
          </div>
        </div>

        <!-- User Details Modal -->
        <div v-if="showDetailsModal" class="modal-overlay" @click="closeDetailsModal">
          <div class="modal-content large" @click.stop>
            <div class="modal-header">
              <h3>User Details</h3>
              <button @click="closeDetailsModal" class="modal-close">×</button>
            </div>

            <div class="modal-body">
              <div class="user-profile">
                <div class="profile-header">
                  <div class="user-avatar-xl">
                    {{ selectedUser?.name.charAt(0).toUpperCase() }}
                  </div>
                  <div class="profile-info">
                    <h2>{{ selectedUser?.name }}</h2>
                    <p class="email">{{ selectedUser?.email }}</p>
                    <div class="role-status">
                      <span :class="['role-badge', selectedUser?.role]">{{
                        selectedUser?.role
                      }}</span>
                      <span :class="['status-badge', selectedUser?.status || 'active']">
                        {{ selectedUser?.status || 'active' }}
                      </span>
                    </div>
                  </div>
                </div>

                <div class="profile-details">
                  <div class="detail-section">
                    <h4>Account Information</h4>
                    <div class="detail-grid">
                      <div class="detail-item">
                        <label>User ID:</label>
                        <span>{{ selectedUser?.id }}</span>
                      </div>
                      <div class="detail-item">
                        <label>Provider:</label>
                        <span>{{ selectedUser?.provider || 'email' }}</span>
                      </div>
                      <div class="detail-item">
                        <label>Joined:</label>
                        <span>{{
                          formatDate(selectedUser?.loginTime || selectedUser?.createdAt)
                        }}</span>
                      </div>
                      <div class="detail-item">
                        <label>Last Login:</label>
                        <span>{{ formatDate(selectedUser?.loginTime) }}</span>
                      </div>
                    </div>
                  </div>

                  <div class="detail-section">
                    <h4>Permissions</h4>
                    <div class="permissions-list">
                      <div
                        v-for="permission in getUserPermissions(selectedUser?.role)"
                        :key="permission.key"
                        class="permission-item"
                      >
                        <span class="permission-icon">{{ permission.icon }}</span>
                        <span class="permission-name">{{ permission.name }}</span>
                        <span class="permission-status" :class="{ granted: permission.granted }">
                          {{ permission.granted ? '✓' : '✗' }}
                        </span>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div class="modal-footer">
              <button @click="closeDetailsModal" class="btn-secondary">Close</button>
              <button @click="openRoleModal(selectedUser)" class="btn-primary">Change Role</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { useAuthStore } from '@/stores/auth.js'
import Swal from 'sweetalert2'

export default {
  name: 'AdminUsersView',
  setup() {
    const authStore = useAuthStore()
    return { authStore }
  },
  data() {
    return {
      searchQuery: '',
      activeFilter: 'all',
      currentPage: 1,
      itemsPerPage: 10,
      showRoleModal: false,
      showDetailsModal: false,
      selectedUser: null,
      newRole: '',
      // Mock user data - in real app, this would come from API
      users: [
        {
          id: '1',
          name: 'John Doe',
          email: 'john@example.com',
          role: 'customer',
          status: 'active',
          provider: 'email',
          loginTime: '2024-01-15T10:30:00Z',
          createdAt: '2024-01-15T10:30:00Z',
        },
        {
          id: '2',
          name: 'Jane Smith',
          email: 'jane@example.com',
          role: 'employee',
          status: 'active',
          provider: 'google',
          loginTime: '2024-01-20T14:45:00Z',
          createdAt: '2024-01-20T14:45:00Z',
        },
        {
          id: '3',
          name: 'Admin User',
          email: 'admin@example.com',
          role: 'admin',
          status: 'active',
          provider: 'email',
          loginTime: '2024-01-10T09:00:00Z',
          createdAt: '2024-01-10T09:00:00Z',
        },
        {
          id: '4',
          name: 'Suspended User',
          email: 'suspended@example.com',
          role: 'customer',
          status: 'suspended',
          provider: 'email',
          loginTime: '2024-01-05T16:20:00Z',
          createdAt: '2024-01-05T16:20:00Z',
        },
      ],
    }
  },
  computed: {
    filteredUsers() {
      let filtered = this.users

      // Filter by role
      if (this.activeFilter !== 'all') {
        filtered = filtered.filter((user) => user.role === this.activeFilter)
      }

      // Filter by search query
      if (this.searchQuery.trim()) {
        const query = this.searchQuery.toLowerCase()
        filtered = filtered.filter(
          (user) =>
            user.name.toLowerCase().includes(query) || user.email.toLowerCase().includes(query),
        )
      }

      // Pagination
      const start = (this.currentPage - 1) * this.itemsPerPage
      const end = start + this.itemsPerPage
      return filtered.slice(start, end)
    },
    totalPages() {
      let filtered = this.users
      if (this.activeFilter !== 'all') {
        filtered = filtered.filter((user) => user.role === this.activeFilter)
      }
      if (this.searchQuery.trim()) {
        const query = this.searchQuery.toLowerCase()
        filtered = filtered.filter(
          (user) =>
            user.name.toLowerCase().includes(query) || user.email.toLowerCase().includes(query),
        )
      }
      return Math.ceil(filtered.length / this.itemsPerPage)
    },
  },
  methods: {
    getRoleCount(role) {
      if (role === 'all') return this.users.length
      return this.users.filter((user) => user.role === role).length
    },
    formatDate(dateString) {
      if (!dateString) return 'Never'
      try {
        return new Date(dateString).toLocaleDateString('en-US', {
          year: 'numeric',
          month: 'short',
          day: 'numeric',
        })
      } catch (e) {
        return 'Invalid Date'
      }
    },
    openRoleModal(user) {
      this.selectedUser = user
      this.newRole = user.role
      this.showRoleModal = true
    },
    closeRoleModal() {
      this.showRoleModal = false
      this.selectedUser = null
      this.newRole = ''
    },
    async confirmRoleChange() {
      if (!this.selectedUser || !this.newRole) return

      try {
        // In real app, make API call here
        const userIndex = this.users.findIndex((u) => u.id === this.selectedUser.id)
        if (userIndex !== -1) {
          this.users[userIndex].role = this.newRole
        }

        await Swal.fire({
          icon: 'success',
          title: 'Role Updated',
          text: `${this.selectedUser.name}'s role has been changed to ${this.newRole.charAt(0).toUpperCase() + this.newRole.slice(1)}.`,
          confirmButtonColor: '#0071e3',
        })

        this.closeRoleModal()
      } catch (error) {
        Swal.fire({
          icon: 'error',
          title: 'Update Failed',
          text: 'Failed to update user role. Please try again.',
          confirmButtonColor: '#dc3545',
        })
      }
    },
    async toggleUserStatus(user) {
      const newStatus = user.status === 'suspended' ? 'active' : 'suspended'
      const action = newStatus === 'active' ? 'activate' : 'suspend'

      const result = await Swal.fire({
        title: `${action.charAt(0).toUpperCase() + action.slice(1)} User?`,
        text: `Are you sure you want to ${action} ${user.name}?`,
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: newStatus === 'active' ? '#28a745' : '#dc3545',
        cancelButtonColor: '#6c757d',
        confirmButtonText: `Yes, ${action}`,
      })

      if (result.isConfirmed) {
        try {
          // In real app, make API call here
          const userIndex = this.users.findIndex((u) => u.id === user.id)
          if (userIndex !== -1) {
            this.users[userIndex].status = newStatus
          }

          Swal.fire({
            icon: 'success',
            title: 'Status Updated',
            text: `${user.name} has been ${newStatus === 'active' ? 'activated' : 'suspended'}.`,
            confirmButtonColor: '#0071e3',
          })
        } catch (error) {
          Swal.fire({
            icon: 'error',
            title: 'Update Failed',
            text: 'Failed to update user status. Please try again.',
            confirmButtonColor: '#dc3545',
          })
        }
      }
    },
    viewUserDetails(user) {
      this.selectedUser = user
      this.showDetailsModal = true
    },
    closeDetailsModal() {
      this.showDetailsModal = false
      this.selectedUser = null
    },
    getRoleIcon(role) {
      const icons = {
        customer: '🛒',
        employee: '👷',
        admin: '👑',
      }
      return icons[role] || '👤'
    },
    getRoleDescription(role) {
      const descriptions = {
        customer: 'Can browse and purchase products',
        employee: 'Can manage inventory and view reports',
        admin: 'Full system access and user management',
      }
      return descriptions[role] || ''
    },
    getUserPermissions(role) {
      const allPermissions = [
        { key: 'browse', name: 'Browse Products', icon: '🛍️', granted: true },
        {
          key: 'purchase',
          name: 'Make Purchases',
          icon: '💳',
          granted: role === 'customer' || role === 'employee' || role === 'admin',
        },
        {
          key: 'manage_inventory',
          name: 'Manage Inventory',
          icon: '📦',
          granted: role === 'employee' || role === 'admin',
        },
        {
          key: 'view_reports',
          name: 'View Reports',
          icon: '📊',
          granted: role === 'employee' || role === 'admin',
        },
        { key: 'manage_users', name: 'Manage Users', icon: '👥', granted: role === 'admin' },
        { key: 'system_settings', name: 'System Settings', icon: '⚙️', granted: role === 'admin' },
      ]
      return allPermissions
    },
  },
  watch: {
    searchQuery() {
      this.currentPage = 1 // Reset to first page when searching
    },
    activeFilter() {
      this.currentPage = 1 // Reset to first page when filtering
    },
  },
}
</script>

<style scoped>
@import url('@/assets/styles/AdminStyles.css');

/* Filters Section */
.filters-section {
  padding: 24px;
  border-bottom: 1px solid #f0f0f0;
}

.search-box {
  position: relative;
  margin-bottom: 20px;
}

.search-input {
  width: 100%;
  padding: 12px 16px 12px 44px;
  border: 2px solid #e5e5e7;
  border-radius: 12px;
  font-size: 16px;
  transition: border-color 0.3s ease;
}

.search-input:focus {
  outline: none;
  border-color: #0071e3;
}

.search-icon {
  position: absolute;
  left: 14px;
  top: 50%;
  transform: translateY(-50%);
  color: #86868b;
  font-size: 18px;
}

.filter-tabs {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.filter-tab {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  border: 2px solid #e5e5e7;
  background: white;
  border-radius: 20px;
  font-size: 14px;
  font-weight: 600;
  color: #666;
  cursor: pointer;
  transition: all 0.3s ease;
}

.filter-tab:hover {
  border-color: #0071e3;
  color: #0071e3;
}

.filter-tab.active {
  background: #0071e3;
  border-color: #0071e3;
  color: white;
}

.count-badge {
  background: rgba(255, 255, 255, 0.2);
  padding: 2px 8px;
  border-radius: 10px;
  font-size: 12px;
  font-weight: 700;
}

/* Users Table */
.users-table-container {
  overflow-x: auto;
}

.users-table {
  width: 100%;
  border-collapse: collapse;
}

.users-table th,
.users-table td {
  padding: 16px;
  text-align: left;
  border-bottom: 1px solid #f0f0f0;
}

.users-table th {
  background: #f8f9fa;
  font-weight: 600;
  color: #1d1d1f;
  font-size: 14px;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.user-row:hover {
  background: #f8f9ff;
}

.user-cell {
  min-width: 200px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: linear-gradient(135deg, #0071e3, #0056b3);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  font-size: 16px;
}

.user-details {
  flex: 1;
}

.user-name {
  font-weight: 600;
  color: #1d1d1f;
  margin: 0 0 4px 0;
}

.user-id {
  font-size: 12px;
  color: #86868b;
}

.email-cell {
  min-width: 200px;
  color: #666;
}

.role-cell {
  min-width: 100px;
}

.role-badge {
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 600;
  text-transform: uppercase;
}

.role-badge.customer {
  background: #e3f2fd;
  color: #1976d2;
}

.role-badge.employee {
  background: #f3e5f5;
  color: #7b1fa2;
}

.role-badge.admin {
  background: #ffebee;
  color: #d32f2f;
}

.status-cell {
  min-width: 100px;
}

.status-badge {
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 600;
  text-transform: uppercase;
}

.status-badge.active {
  background: #e8f5e9;
  color: #2e7d32;
}

.status-badge.suspended {
  background: #ffebee;
  color: #c62828;
}

.joined-cell {
  min-width: 120px;
  color: #666;
  font-size: 14px;
}

.actions-cell {
  min-width: 150px;
}

.action-buttons {
  display: flex;
  gap: 8px;
}

.action-btn {
  width: 32px;
  height: 32px;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  transition: all 0.3s ease;
}

.action-btn.edit-role {
  background: #e3f2fd;
  color: #1976d2;
}

.action-btn.edit-role:hover {
  background: #1976d2;
  color: white;
  transform: scale(1.1);
}

.action-btn.suspend {
  background: #ffebee;
  color: #c62828;
}

.action-btn.suspend:hover {
  background: #c62828;
  color: white;
  transform: scale(1.1);
}

.action-btn.activate {
  background: #e8f5e9;
  color: #2e7d32;
}

.action-btn.activate:hover {
  background: #2e7d32;
  color: white;
  transform: scale(1.1);
}

.action-btn.view-details {
  background: #f3e5f5;
  color: #7b1fa2;
}

.action-btn.view-details:hover {
  background: #7b1fa2;
  color: white;
  transform: scale(1.1);
}

/* Empty State */
.empty-state {
  text-align: center;
  padding: 60px 20px;
  color: #86868b;
}

.empty-icon {
  font-size: 4rem;
  margin-bottom: 20px;
  opacity: 0.5;
}

.empty-state h3 {
  font-size: 1.5rem;
  margin: 0 0 8px 0;
  color: #1d1d1f;
}

.empty-state p {
  margin: 0;
}

/* Pagination */
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 20px;
  padding: 20px;
  border-top: 1px solid #f0f0f0;
}

.pagination-btn {
  padding: 8px 16px;
  border: 2px solid #e5e5e7;
  background: white;
  border-radius: 8px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.pagination-btn:hover:not(:disabled) {
  border-color: #0071e3;
  color: #0071e3;
}

.pagination-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.pagination-info {
  color: #666;
  font-weight: 600;
}

/* Modal Styles */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  border-radius: 16px;
  max-width: 500px;
  width: 90%;
  max-height: 90vh;
  overflow-y: auto;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
}

.modal-content.large {
  max-width: 700px;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24px;
  border-bottom: 1px solid #f0f0f0;
}

.modal-header h3 {
  margin: 0;
  font-size: 1.5rem;
  font-weight: 700;
  color: #1d1d1f;
}

.modal-close {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: #86868b;
  padding: 0;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  transition: background 0.3s ease;
}

.modal-close:hover {
  background: #f0f0f0;
}

.modal-body {
  padding: 24px;
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding: 24px;
  border-top: 1px solid #f0f0f0;
}

.btn-secondary {
  padding: 10px 20px;
  border: 2px solid #e5e5e7;
  background: white;
  border-radius: 8px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-secondary:hover {
  border-color: #0071e3;
  color: #0071e3;
}

.btn-primary {
  padding: 10px 20px;
  border: none;
  background: #0071e3;
  color: white;
  border-radius: 8px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-primary:hover:not(:disabled) {
  background: #0056b3;
  transform: translateY(-1px);
}

.btn-primary:disabled {
  opacity: 0.5;
  cursor: not-allowed;
  transform: none;
}

/* Role Modal Specific */
.user-summary {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 24px;
  padding: 16px;
  background: #f8f9fa;
  border-radius: 12px;
}

.user-avatar-large {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background: linear-gradient(135deg, #0071e3, #0056b3);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  font-size: 24px;
}

.user-summary-details h4 {
  margin: 0 0 4px 0;
  font-size: 1.2rem;
  color: #1d1d1f;
}

.user-summary-details p {
  margin: 4px 0;
  color: #666;
  font-size: 14px;
}

.role-selection label {
  display: block;
  font-weight: 600;
  color: #1d1d1f;
  margin-bottom: 12px;
}

.role-options {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.role-option {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  padding: 16px;
  border: 2px solid #e5e5e7;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.role-option:hover {
  border-color: #0071e3;
  background: #f8f9ff;
}

.role-option input {
  margin-top: 2px;
}

.role-option input:disabled {
  opacity: 0.5;
}

.role-option-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.role-icon {
  font-size: 20px;
}

.role-name {
  font-weight: 600;
  color: #1d1d1f;
}

.role-description {
  font-size: 14px;
  color: #666;
}

/* User Details Modal */
.user-profile {
  max-width: 600px;
  margin: 0 auto;
}

.profile-header {
  display: flex;
  align-items: center;
  gap: 20px;
  margin-bottom: 32px;
  padding-bottom: 24px;
  border-bottom: 1px solid #f0f0f0;
}

.user-avatar-xl {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  background: linear-gradient(135deg, #0071e3, #0056b3);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  font-size: 32px;
}

.profile-info h2 {
  margin: 0 0 4px 0;
  font-size: 1.8rem;
  color: #1d1d1f;
}

.profile-info .email {
  color: #666;
  margin: 0 0 12px 0;
  font-size: 16px;
}

.role-status {
  display: flex;
  gap: 8px;
}

.profile-details {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.detail-section h4 {
  margin: 0 0 16px 0;
  font-size: 1.2rem;
  color: #1d1d1f;
  font-weight: 600;
}

.detail-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

.detail-item {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.detail-item label {
  font-size: 12px;
  color: #86868b;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.detail-item span {
  font-size: 14px;
  color: #1d1d1f;
  font-weight: 500;
}

.permissions-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.permission-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  background: #f8f9fa;
  border-radius: 8px;
}

.permission-icon {
  font-size: 18px;
}

.permission-name {
  flex: 1;
  font-weight: 500;
  color: #1d1d1f;
}

.permission-status {
  font-weight: 700;
  font-size: 16px;
}

.permission-status.granted {
  color: #28a745;
}

.permission-status:not(.granted) {
  color: #dc3545;
}

@media (max-width: 768px) {
  .admin-users-page {
    padding: 10px;
  }

  .filters-section {
    padding: 16px;
  }

  .filter-tabs {
    justify-content: center;
  }

  .users-table th,
  .users-table td {
    padding: 12px 8px;
  }

  .user-cell {
    min-width: 150px;
  }

  .modal-content {
    margin: 20px;
    width: calc(100% - 40px);
  }

  .profile-header {
    flex-direction: column;
    text-align: center;
    gap: 16px;
  }

  .detail-grid {
    grid-template-columns: 1fr;
  }
}
</style>
