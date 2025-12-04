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
          <router-link to="/admin/discounts" class="nav-item">
            <span class="nav-icon"><i class="fas fa-tags"></i></span>
            <span class="nav-text">Discounts</span>
          </router-link>
          <router-link to="/admin/categories" class="nav-item" active-class="active">
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
            <h1 class="page-title">Categories</h1>
          </div>
          <div class="header-right">
            <button @click="openAddModal" class="add-btn">+ Add Category</button>
            <router-link to="/" class="back-to-store-btn">
              <span class="icon"><i class="fas fa-store"></i></span>
              <span>Back to Store</span>
            </router-link>
          </div>
        </div>

        <!-- Categories Table -->
        <div class="content-section">
          <div class="table-container">
            <table class="data-table">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Name</th>
                  <th>Description</th>
                  <th>Product Count</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="category in categories" :key="category.id">
                  <td>{{ category.id }}</td>
                  <td>
                    <strong>{{ category.name }}</strong>
                  </td>
                  <td>{{ category.description }}</td>
                  <td>{{ category.productCount }}</td>
                  <td>
                    <button @click="openEditModal(category)" class="action-btn edit">Edit</button>
                    <button @click="deleteCategory(category.id)" class="action-btn delete">
                      Delete
                    </button>
                  </td>
                </tr>
                <tr v-if="categories.length === 0">
                  <td colspan="5" class="no-data">No categories found</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>

    <!-- Category Modal -->
    <Transition name="modal">
      <div v-if="isModalOpen" class="modal-overlay" @click.self="closeModal">
        <div class="modal-container">
          <div class="modal-header">
            <h2 class="modal-title">{{ isEditMode ? 'Edit Category' : 'Add New Category' }}</h2>
            <button @click="closeModal" class="close-btn">&times;</button>
          </div>

          <div class="modal-body">
            <form @submit.prevent="handleSubmit">
              <div class="form-grid">
                <div class="form-group full-width">
                  <label for="name">Category Name *</label>
                  <input
                    id="name"
                    v-model="formData.name"
                    type="text"
                    placeholder="e.g., iPhone, MacBook"
                    required
                  />
                </div>

                <div class="form-group full-width">
                  <label for="description">Description *</label>
                  <textarea
                    id="description"
                    v-model="formData.description"
                    rows="3"
                    placeholder="Describe this category..."
                    required
                  ></textarea>
                </div>

                <div class="form-group full-width">
                  <label for="icon">Icon (Emoji)</label>
                  <input
                    id="icon"
                    v-model="formData.icon"
                    type="text"
                    placeholder="📱"
                    maxlength="2"
                  />
                </div>
              </div>

              <div class="modal-footer">
                <button type="button" @click="closeModal" class="btn-secondary">Cancel</button>
                <button type="submit" class="btn-primary">
                  {{ isEditMode ? 'Update Category' : 'Create Category' }}
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </Transition>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import Swal from 'sweetalert2'

const router = useRouter()

const categories = ref([
  {
    id: 1,
    name: 'iPhone',
    description: 'The latest iPhone models with cutting-edge technology',
    icon: '📱',
    productCount: 12,
  },
  {
    id: 2,
    name: 'MacBook',
    description: 'Powerful laptops for professionals and creators',
    icon: '💻',
    productCount: 8,
  },
  {
    id: 3,
    name: 'iPad',
    description: 'Versatile tablets for work and entertainment',
    icon: '📲',
    productCount: 6,
  },
  {
    id: 4,
    name: 'Watch',
    description: 'Smart watches for health and connectivity',
    icon: '⌚',
    productCount: 5,
  },
  {
    id: 5,
    name: 'AirPod',
    description: 'Premium wireless audio experience',
    icon: '🎧',
    productCount: 4,
  },
])

const isModalOpen = ref(false)
const selectedCategory = ref(null)
const isEditMode = ref(false)

const formData = ref({
  name: '',
  description: '',
  icon: '',
})

onMounted(() => {
  const user = JSON.parse(localStorage.getItem('user') || '{}')
  if (!user.isLoggedIn || user.role !== 'admin') {
    router.push('/')
  }
})

const resetForm = () => {
  formData.value = {
    name: '',
    description: '',
    icon: '',
  }
}

const openAddModal = () => {
  selectedCategory.value = null
  isEditMode.value = false
  resetForm()
  isModalOpen.value = true
}

const openEditModal = (category) => {
  selectedCategory.value = category
  isEditMode.value = true
  formData.value = { ...category }
  isModalOpen.value = true
}

const closeModal = () => {
  isModalOpen.value = false
  selectedCategory.value = null
  resetForm()
}

const handleSubmit = () => {
  if (isEditMode.value && selectedCategory.value) {
    // Update existing category
    const index = categories.value.findIndex((c) => c.id === selectedCategory.value.id)
    if (index !== -1) {
      categories.value[index] = {
        ...categories.value[index],
        ...formData.value,
      }
      Swal.fire({
        icon: 'success',
        title: 'Updated!',
        text: 'Category updated successfully!',
        timer: 2000,
        showConfirmButton: false,
      })
    }
  } else {
    // Add new category
    const newCategory = {
      ...formData.value,
      id: Math.max(...categories.value.map((c) => c.id), 0) + 1,
      productCount: 0,
    }
    categories.value.push(newCategory)
    Swal.fire({
      icon: 'success',
      title: 'Created!',
      text: 'Category created successfully!',
      timer: 2000,
      showConfirmButton: false,
    })
  }
  closeModal()
  // TODO: Make API call to save to backend
}

const deleteCategory = (categoryId) => {
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
      categories.value = categories.value.filter((c) => c.id !== categoryId)
      Swal.fire({
        icon: 'success',
        title: 'Deleted!',
        text: 'Category deleted successfully!',
        timer: 2000,
        showConfirmButton: false,
      })
      // TODO: Make API call to delete from backend
    }
  })
}

const refreshData = () => {
  Swal.fire({
    icon: 'info',
    title: 'Refreshing...',
    text: 'Data is being refreshed',
    timer: 1500,
    showConfirmButton: false,
  })
  // TODO: Implement refresh logic
}

const showNotifications = () => {
  Swal.fire({
    icon: 'info',
    title: 'Notifications',
    text: 'You have 3 new updates',
    confirmButtonText: 'Got it!',
  })
  // TODO: Implement notifications panel
}

const openSupport = () => {
  Swal.fire({
    icon: 'question',
    title: 'Support',
    html: 'Contact us at:<br><strong>support@applestore.com</strong>',
    confirmButtonText: 'Close',
  })
  // TODO: Implement support panel
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
  box-shadow: 0 5px 15px rgba(250, 112, 154, 0.3);
}

.no-data {
  text-align: center;
  padding: 40px;
  color: #999;
  font-style: italic;
}

/* Modal Styles */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  backdrop-filter: blur(4px);
}

.modal-container {
  background: white;
  border-radius: 16px;
  width: 90%;
  max-width: 600px;
  max-height: 90vh;
  overflow-y: auto;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24px 30px;
  border-bottom: 1px solid #e9ecef;
}

.modal-title {
  font-size: 1.5rem;
  font-weight: 700;
  color: #333;
  margin: 0;
}

.close-btn {
  background: none;
  border: none;
  font-size: 2rem;
  color: #999;
  cursor: pointer;
  padding: 0;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  transition: all 0.3s ease;
}

.close-btn:hover {
  background: #f8f9fa;
  color: #333;
}

.modal-body {
  padding: 30px;
}

.form-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}

.form-group {
  display: flex;
  flex-direction: column;
}

.form-group.full-width {
  grid-column: 1 / -1;
}

.form-group label {
  font-weight: 600;
  margin-bottom: 8px;
  color: #495057;
  font-size: 0.9rem;
}

.form-group input,
.form-group textarea {
  padding: 12px 16px;
  border: 1px solid #ced4da;
  border-radius: 8px;
  font-size: 0.95rem;
  transition: all 0.3s ease;
}

.form-group input:focus,
.form-group textarea:focus {
  outline: none;
  border-color: #007aff;
  box-shadow: 0 0 0 3px rgba(0, 122, 255, 0.1);
}

.form-group textarea {
  resize: vertical;
  font-family: inherit;
}

.modal-footer {
  display: flex;
  gap: 12px;
  justify-content: flex-end;
  padding-top: 24px;
  border-top: 1px solid #e9ecef;
  margin-top: 24px;
}

.btn-secondary,
.btn-primary {
  padding: 12px 24px;
  border-radius: 8px;
  font-weight: 600;
  font-size: 0.95rem;
  cursor: pointer;
  transition: all 0.3s ease;
  border: none;
}

.btn-secondary {
  background: #f8f9fa;
  color: #495057;
}

.btn-secondary:hover {
  background: #e9ecef;
}

.btn-primary {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
}

/* Modal Transitions */
.modal-enter-active,
.modal-leave-active {
  transition: all 0.3s ease;
}

.modal-enter-from,
.modal-leave-to {
  opacity: 0;
}

.modal-enter-from .modal-container,
.modal-leave-to .modal-container {
  transform: scale(0.9);
}

.action-btn.edit {
  background: #007bff;
  color: white;
  margin-right: 8px;
}

.action-btn.edit:hover {
  background: #0056b3;
}

.action-btn.delete {
  background: #dc3545;
  color: white;
}

.action-btn.delete:hover {
  background: #c82333;
}
</style>
