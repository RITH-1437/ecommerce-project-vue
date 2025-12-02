<template>
  <Transition name="modal">
    <div v-if="isOpen" class="modal-overlay" @click.self="closeModal">
      <div class="modal-container">
        <div class="modal-header">
          <h2 class="modal-title">{{ isEditMode ? 'Edit Product' : 'Add New Product' }}</h2>
          <button @click="closeModal" class="close-btn">&times;</button>
        </div>

        <div class="modal-body">
          <form @submit.prevent="handleSubmit">
            <div class="form-grid">
              <div class="form-group full-width">
                <label for="name">Product Name *</label>
                <input
                  id="name"
                  v-model="formData.name"
                  type="text"
                  placeholder="e.g., iPhone 15 Pro Max"
                  required
                />
              </div>

              <div class="form-group">
                <label for="category">Category *</label>
                <select id="category" v-model="formData.category" required>
                  <option value="">Select Category</option>
                  <option value="iPhone">iPhone</option>
                  <option value="Watch">Watch</option>
                  <option value="MacBook">MacBook</option>
                  <option value="iPad">iPad</option>
                  <option value="AirPod">AirPod</option>
                </select>
              </div>

              <div class="form-group">
                <label for="price">Price ($) *</label>
                <input
                  id="price"
                  v-model.number="formData.price"
                  type="number"
                  placeholder="999"
                  step="0.01"
                  min="0"
                  required
                />
              </div>

              <div class="form-group">
                <label for="stock">Stock Quantity *</label>
                <input
                  id="stock"
                  v-model.number="formData.stock"
                  type="number"
                  placeholder="100"
                  min="0"
                  required
                />
              </div>

              <div class="form-group">
                <label for="sku">SKU</label>
                <input id="sku" v-model="formData.sku" type="text" placeholder="APPL-IPH-001" />
              </div>

              <div class="form-group full-width">
                <label for="description">Description</label>
                <textarea
                  id="description"
                  v-model="formData.description"
                  rows="4"
                  placeholder="Enter product description..."
                ></textarea>
              </div>

              <div class="form-group full-width">
                <label for="image">Image URL</label>
                <input
                  id="image"
                  v-model="formData.image"
                  type="url"
                  placeholder="https://example.com/product-image.jpg"
                />
              </div>

              <div class="form-group">
                <label for="color">Color</label>
                <input
                  id="color"
                  v-model="formData.color"
                  type="text"
                  placeholder="e.g., Space Black"
                />
              </div>

              <div class="form-group">
                <label for="storage">Storage</label>
                <input
                  id="storage"
                  v-model="formData.storage"
                  type="text"
                  placeholder="e.g., 256GB"
                />
              </div>
            </div>

            <div class="modal-footer">
              <button type="button" @click="closeModal" class="btn-secondary">Cancel</button>
              <button type="submit" class="btn-primary">
                {{ isEditMode ? 'Update Product' : 'Add Product' }}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </Transition>
</template>

<script setup>
import { ref, watch } from 'vue'

const props = defineProps({
  isOpen: {
    type: Boolean,
    default: false,
  },
  product: {
    type: Object,
    default: null,
  },
})

const emit = defineEmits(['close', 'save'])

const formData = ref({
  name: '',
  category: '',
  price: 0,
  stock: 0,
  sku: '',
  description: '',
  image: '',
  color: '',
  storage: '',
})

const isEditMode = ref(false)

const resetForm = () => {
  formData.value = {
    name: '',
    category: '',
    price: 0,
    stock: 0,
    sku: '',
    description: '',
    image: '',
    color: '',
    storage: '',
  }
}

watch(
  () => props.product,
  (newProduct) => {
    if (newProduct) {
      isEditMode.value = true
      formData.value = { ...newProduct }
    } else {
      isEditMode.value = false
      resetForm()
    }
  },
  { immediate: true },
)

const closeModal = () => {
  emit('close')
  resetForm()
}

const handleSubmit = () => {
  emit('save', { ...formData.value })
  closeModal()
}
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.6);
  backdrop-filter: blur(4px);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 10000;
  padding: 20px;
}

.modal-container {
  background: white;
  border-radius: 16px;
  max-width: 800px;
  width: 100%;
  max-height: 90vh;
  overflow: hidden;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  display: flex;
  flex-direction: column;
}

.modal-header {
  padding: 24px 30px;
  border-bottom: 1px solid #e9ecef;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.modal-title {
  font-size: 1.5rem;
  font-weight: 600;
  margin: 0;
}

.close-btn {
  background: none;
  border: none;
  font-size: 2rem;
  color: white;
  cursor: pointer;
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.close-btn:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: rotate(90deg);
}

.modal-body {
  padding: 30px;
  overflow-y: auto;
  flex: 1;
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
  font-size: 0.9rem;
  font-weight: 600;
  color: #333;
  margin-bottom: 8px;
}

.form-group input,
.form-group select,
.form-group textarea {
  padding: 12px 16px;
  border: 2px solid #e9ecef;
  border-radius: 8px;
  font-size: 0.95rem;
  transition: all 0.3s ease;
  font-family: inherit;
}

.form-group input:focus,
.form-group select:focus,
.form-group textarea:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.form-group textarea {
  resize: vertical;
  min-height: 100px;
}

.modal-footer {
  padding: 20px 30px;
  border-top: 1px solid #e9ecef;
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  background: #f8f9fa;
}

.btn-secondary,
.btn-primary {
  padding: 12px 24px;
  border: none;
  border-radius: 8px;
  font-size: 0.95rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-secondary {
  background: #e9ecef;
  color: #495057;
}

.btn-secondary:hover {
  background: #dee2e6;
}

.btn-primary {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
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
  transform: scale(0.9) translateY(-20px);
}

@media (max-width: 768px) {
  .form-grid {
    grid-template-columns: 1fr;
  }

  .modal-container {
    max-height: 95vh;
  }

  .modal-header,
  .modal-body,
  .modal-footer {
    padding: 20px;
  }
}
</style>
