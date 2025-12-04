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
          <router-link to="/admin/users" class="nav-item">
            <span class="nav-icon"><i class="fas fa-users"></i></span>
            <span class="nav-text">Users</span>
          </router-link>
          <router-link to="/admin/discounts" class="nav-item" active-class="active">
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
            <h1 class="page-title">Discounts & Promotions</h1>
          </div>
          <div class="header-right">
            <button @click="openAddModal" class="add-btn">+ New Discount</button>
            <router-link to="/" class="back-to-store-btn">
              <span class="icon"><i class="fas fa-store"></i></span>
              <span>Back to Store</span>
            </router-link>
          </div>
        </div>

        <!-- Promotions Table -->
        <div class="content-section">
          <div class="table-container">
            <table class="data-table">
              <thead>
                <tr>
                  <th>Name</th>
                  <th>Type</th>
                  <th>Value</th>
                  <th>Code</th>
                  <th>Start Date</th>
                  <th>End Date</th>
                  <th>Status</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="discount in discounts" :key="discount.id">
                  <td>{{ discount.name }}</td>
                  <td>
                    <span class="type-badge">{{ discount.type }}</span>
                  </td>
                  <td>
                    <strong>{{ formatDiscount(discount) }}</strong>
                  </td>
                  <td>
                    <code v-if="discount.code">{{ discount.code }}</code
                    ><span v-else>—</span>
                  </td>
                  <td>{{ formatDate(discount.startDate) }}</td>
                  <td>{{ formatDate(discount.endDate) }}</td>
                  <td>
                    <span class="status-badge" :class="getStatusClass(discount)">
                      {{ getStatus(discount) }}
                    </span>
                  </td>
                  <td>
                    <button @click="openEditModal(discount)" class="action-btn edit">Edit</button>
                    <button @click="deleteDiscount(discount.id)" class="action-btn delete">
                      Delete
                    </button>
                  </td>
                </tr>
                <tr v-if="discounts.length === 0">
                  <td colspan="8" class="no-data">No discounts found</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>

    <!-- Discount Modal -->
    <DiscountModal
      :is-open="isModalOpen"
      :discount="selectedDiscount"
      @close="closeModal"
      @save="saveDiscount"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import Swal from 'sweetalert2'
import DiscountModal from '@/components/admin/DiscountModal.vue'

const router = useRouter()

const discounts = ref([
  {
    id: 1,
    name: 'Back to School Sale',
    type: 'percentage',
    value: 15,
    code: 'SCHOOL15',
    startDate: '2024-08-01',
    endDate: '2024-08-31',
    isActive: true,
    minPurchase: 100,
  },
  {
    id: 2,
    name: 'Holiday Gift Guide',
    type: 'fixed',
    value: 50,
    code: 'HOLIDAY50',
    startDate: '2024-11-15',
    endDate: '2024-12-31',
    isActive: false,
    minPurchase: 200,
  },
  {
    id: 3,
    name: 'Student Discount',
    type: 'percentage',
    value: 10,
    code: 'STUDENT10',
    startDate: '2024-01-01',
    endDate: '2025-12-31',
    isActive: true,
    minPurchase: 0,
  },
  {
    id: 4,
    name: 'Trade-In Bonus',
    type: 'fixed',
    value: 100,
    code: 'TRADEIN100',
    startDate: '2024-10-01',
    endDate: '2024-10-31',
    isActive: true,
    minPurchase: 500,
  },
  {
    id: 5,
    name: 'Clearance Sale',
    type: 'percentage',
    value: 20,
    code: 'CLEAR20',
    startDate: '2024-07-01',
    endDate: '2024-07-31',
    isActive: false,
    minPurchase: 50,
  },
])

const isModalOpen = ref(false)
const selectedDiscount = ref(null)

onMounted(() => {
  const user = JSON.parse(localStorage.getItem('user') || '{}')
  if (!user.isLoggedIn || user.role !== 'admin') {
    router.push('/')
  }
})

const formatDate = (dateString) => {
  const date = new Date(dateString)
  return date.toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' })
}

const formatDiscount = (discount) => {
  if (discount.type === 'percentage') {
    return `${discount.value}%`
  } else {
    return `$${discount.value}`
  }
}

const getStatus = (discount) => {
  const now = new Date()
  const start = new Date(discount.startDate)
  const end = new Date(discount.endDate)

  if (!discount.isActive) return 'Inactive'
  if (now < start) return 'Upcoming'
  if (now > end) return 'Expired'
  return 'Active'
}

const getStatusClass = (discount) => {
  const status = getStatus(discount)
  return status.toLowerCase()
}

const openAddModal = () => {
  selectedDiscount.value = null
  isModalOpen.value = true
}

const openEditModal = (discount) => {
  selectedDiscount.value = { ...discount }
  isModalOpen.value = true
}

const closeModal = () => {
  isModalOpen.value = false
  selectedDiscount.value = null
}

const saveDiscount = (discountData) => {
  if (discountData.id) {
    // Update existing discount
    const index = discounts.value.findIndex((d) => d.id === discountData.id)
    if (index !== -1) {
      discounts.value[index] = discountData
      Swal.fire({
        icon: 'success',
        title: 'Updated!',
        text: 'Discount updated successfully!',
        timer: 2000,
        showConfirmButton: false,
      })
    }
  } else {
    // Add new discount
    const newDiscount = {
      ...discountData,
      id: Math.max(...discounts.value.map((d) => d.id), 0) + 1,
    }
    discounts.value.push(newDiscount)
    Swal.fire({
      icon: 'success',
      title: 'Created!',
      text: 'Discount created successfully!',
      timer: 2000,
      showConfirmButton: false,
    })
  }
  // TODO: Make API call to save to backend
}

const deleteDiscount = (discountId) => {
  Swal.fire({
    title: 'Are you sure?',
    text: "You won't be able to revert this!",
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#3085d6',
    cancelButtonColor: '#d33',
    confirmButtonText: 'Yes, delete it!',
  }).then((result) => {
    if (result.isConfirmed) {
      discounts.value = discounts.value.filter((d) => d.id !== discountId)
      Swal.fire({
        icon: 'success',
        title: 'Deleted!',
        text: 'Discount deleted successfully!',
        timer: 2000,
        showConfirmButton: false,
      })
      // TODO: Make API call to delete from backend
    }
  })
}
</script>

<style scoped>
@import url('@/assets/styles/AdminStyles.css');

.add-btn {
  background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
  color: #333;
  padding: 10px 20px;
  border: none;
  border-radius: 8px;
  font-weight: 600;
  margin-right: 15px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.add-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(250, 112, 154, 0.4);
}

.type-badge {
  padding: 4px 10px;
  border-radius: 12px;
  font-size: 0.8rem;
  font-weight: 600;
  text-transform: capitalize;
  background: #f8f9fa;
  color: #495057;
}

code {
  background: #f8f9fa;
  padding: 4px 8px;
  border-radius: 4px;
  font-family: 'Courier New', monospace;
  font-size: 0.85rem;
  font-weight: 600;
  color: #fa709a;
}

.status-badge {
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 0.8rem;
  font-weight: 600;
  text-transform: capitalize;
}

.status-badge.active {
  background: #e8f5e9;
  color: #2e7d32;
}

.status-badge.upcoming {
  background: #e3f2fd;
  color: #1976d2;
}

.status-badge.expired {
  background: #ffebee;
  color: #c62828;
}

.status-badge.inactive {
  background: #f5f5f5;
  color: #999;
}

.action-btn {
  padding: 6px 14px;
  border: none;
  border-radius: 6px;
  font-size: 0.85rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  margin-right: 6px;
}

.action-btn.edit {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  color: white;
}

.action-btn.edit:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(79, 172, 254, 0.4);
}

.action-btn.delete {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  color: white;
}

.action-btn.delete:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(245, 87, 108, 0.4);
}

.no-data {
  text-align: center;
  padding: 40px 20px;
  color: #999;
  font-style: italic;
}
</style>
