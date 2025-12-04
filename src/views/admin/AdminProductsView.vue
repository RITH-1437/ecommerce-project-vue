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
          <router-link to="/admin/products" class="nav-item" active-class="active">
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

      <!-- Main Content -->
      <div class="admin-main">
        <!-- Header -->
        <div class="admin-header">
          <div class="header-left">
            <h1 class="page-title">📦 Products</h1>
          </div>
          <div class="header-right">
            <button @click="openAddModal" class="add-btn">+ Add Product</button>
            <router-link to="/" class="back-to-store-btn">
              <span class="icon"><i class="fas fa-store"></i></span>
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
              placeholder="Search products..."
              class="search-input"
            />
          </div>
          <div class="filter-group">
            <label for="categoryFilter">Category:</label>
            <select id="categoryFilter" v-model="selectedCategory" class="category-filter">
              <option value="">All Categories</option>
              <option value="iPhone">iPhone</option>
              <option value="Watch">Watch</option>
              <option value="MacBook">MacBook</option>
              <option value="iPad">iPad</option>
              <option value="AirPod">AirPod</option>
            </select>
          </div>
        </div>

        <!-- Products Table -->
        <div class="content-section">
          <div class="table-container">
            <table class="data-table">
              <thead>
                <tr>
                  <th>Product Name</th>
                  <th>Price</th>
                  <th>Stock</th>
                  <th>Category</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="product in filteredProducts" :key="product.id">
                  <td>{{ product.name }}</td>
                  <td>{{ formatCurrency(product.price) }}</td>
                  <td>
                    <span class="stock-badge" :class="getStockClass(product.stock)">
                      {{ product.stock }}
                    </span>
                  </td>
                  <td>
                    <span class="category" :class="`category-${product.category.toLowerCase()}`">
                      {{ product.category }}
                    </span>
                  </td>
                  <td>
                    <button @click="openEditModal(product)" class="action-btn edit">Edit</button>
                    <button @click="deleteProduct(product.id)" class="action-btn delete">
                      Delete
                    </button>
                  </td>
                </tr>
                <tr v-if="filteredProducts.length === 0">
                  <td colspan="5" class="no-data">No products found</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>

    <!-- Product Modal -->
    <ProductModal
      :is-open="isModalOpen"
      :product="selectedProduct"
      @close="closeModal"
      @save="saveProduct"
    />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import Swal from 'sweetalert2'
import ProductModal from '@/components/admin/ProductModal.vue'

const router = useRouter()

const products = ref([
  {
    id: 1,
    name: 'iPhone 15 Pro Max',
    price: 1199,
    stock: 45,
    category: 'iPhone',
    sku: 'APPL-IPH-001',
  },
  { id: 2, name: 'iPhone 15 Pro', price: 999, stock: 8, category: 'iPhone', sku: 'APPL-IPH-002' },
  { id: 3, name: 'iPhone 15', price: 799, stock: 150, category: 'iPhone', sku: 'APPL-IPH-003' },
  {
    id: 4,
    name: 'MacBook Pro 16"',
    price: 2499,
    stock: 25,
    category: 'MacBook',
    sku: 'APPL-MBP-001',
  },
  {
    id: 5,
    name: 'MacBook Air M2',
    price: 1199,
    stock: 15,
    category: 'MacBook',
    sku: 'APPL-MBA-001',
  },
  {
    id: 6,
    name: 'Apple Watch Series 9',
    price: 399,
    stock: 200,
    category: 'Watch',
    sku: 'APPL-WCH-001',
  },
  {
    id: 7,
    name: 'Apple Watch Ultra 2',
    price: 799,
    stock: 30,
    category: 'Watch',
    sku: 'APPL-WCH-002',
  },
  { id: 8, name: 'iPad Pro 12.9"', price: 1099, stock: 60, category: 'iPad', sku: 'APPL-IPD-001' },
  { id: 9, name: 'iPad Air', price: 599, stock: 18, category: 'iPad', sku: 'APPL-IPD-002' },
  {
    id: 10,
    name: 'AirPods Pro (2nd gen)',
    price: 249,
    stock: 250,
    category: 'AirPod',
    sku: 'APPL-ARP-001',
  },
  { id: 11, name: 'AirPods Max', price: 549, stock: 5, category: 'AirPod', sku: 'APPL-ARP-002' },
])

const searchQuery = ref('')
const selectedCategory = ref('')
const isModalOpen = ref(false)
const selectedProduct = ref(null)

onMounted(() => {
  const user = JSON.parse(localStorage.getItem('user') || '{}')
  if (!user.isLoggedIn || user.role !== 'admin') {
    router.push('/')
  }
})

const filteredProducts = computed(() => {
  return products.value.filter((product) => {
    const matchesSearch = product.name.toLowerCase().includes(searchQuery.value.toLowerCase())
    const matchesCategory = !selectedCategory.value || product.category === selectedCategory.value
    return matchesSearch && matchesCategory
  })
})

const formatCurrency = (amount) => {
  return new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'USD',
    minimumFractionDigits: 0,
    maximumFractionDigits: 0,
  }).format(amount)
}

const getStockClass = (stock) => {
  if (stock < 10) return 'critical'
  if (stock < 20) return 'low'
  return 'good'
}

const openAddModal = () => {
  selectedProduct.value = null
  isModalOpen.value = true
}

const openEditModal = (product) => {
  selectedProduct.value = { ...product }
  isModalOpen.value = true
}

const closeModal = () => {
  isModalOpen.value = false
  selectedProduct.value = null
}

const saveProduct = (productData) => {
  if (productData.id) {
    // Update existing product
    const index = products.value.findIndex((p) => p.id === productData.id)
    if (index !== -1) {
      products.value[index] = productData
      Swal.fire({
        icon: 'success',
        title: 'Updated!',
        text: 'Product updated successfully!',
        timer: 2000,
        showConfirmButton: false,
      })
    }
  } else {
    // Add new product
    const newProduct = {
      ...productData,
      id: Math.max(...products.value.map((p) => p.id), 0) + 1,
    }
    products.value.push(newProduct)
    Swal.fire({
      icon: 'success',
      title: 'Added!',
      text: 'Product added successfully!',
      timer: 2000,
      showConfirmButton: false,
    })
  }
  // TODO: Make API call to save to backend
}

const deleteProduct = (productId) => {
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
      products.value = products.value.filter((p) => p.id !== productId)
      Swal.fire({
        icon: 'success',
        title: 'Deleted!',
        text: 'Product deleted successfully!',
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

.category {
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 0.8rem;
  font-weight: 600;
  text-transform: capitalize;
  display: inline-block;
}

.category-iphone {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.category-watch {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  color: white;
}

.category-macbook {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  color: white;
}

.category-ipad {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
  color: white;
}

.category-airpod {
  background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
  color: white;
}

.stock-badge {
  padding: 4px 10px;
  border-radius: 12px;
  font-size: 0.85rem;
  font-weight: 600;
}

.stock-badge.critical {
  background: #ffebee;
  color: #c62828;
}

.stock-badge.low {
  background: #fff3e0;
  color: #f57c00;
}

.stock-badge.good {
  background: #e8f5e9;
  color: #2e7d32;
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

.category-filter {
  padding: 10px 16px;
  border: 2px solid #e9ecef;
  border-radius: 8px;
  font-size: 0.9rem;
  cursor: pointer;
  transition: all 0.3s ease;
  min-width: 180px;
}

.category-filter:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.add-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 8px;
  font-weight: 600;
  margin-right: 15px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 0.95rem;
}

.add-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
}

.action-btn {
  padding: 6px 14px;
  border: none;
  border-radius: 6px;
  font-size: 0.85rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.action-btn.edit {
  background: linear-gradient(135deg, #0066cc 0%, #0077ed 100%);
  color: white;
  margin-right: 8px;
}

.action-btn.edit:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 102, 204, 0.4);
}

.action-btn.delete {
  background: linear-gradient(135deg, #ff3b30 0%, #ff1744 100%);
  color: white;
}

.action-btn.delete:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(255, 59, 48, 0.4);
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

  .category-filter {
    flex: 1;
  }
}
</style>
