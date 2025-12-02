<template>
  <Transition name="modal">
    <div v-if="isOpen" class="modal-overlay" @click.self="closeModal">
      <div class="modal-container">
        <div class="modal-header">
          <h2 class="modal-title">{{ isEditMode ? 'Edit Discount' : 'Create New Discount' }}</h2>
          <button @click="closeModal" class="close-btn">&times;</button>
        </div>

        <div class="modal-body">
          <form @submit.prevent="handleSubmit">
            <div class="form-grid">
              <div class="form-group full-width">
                <label for="name">Discount Name *</label>
                <input
                  id="name"
                  v-model="formData.name"
                  type="text"
                  placeholder="e.g., Back to School Sale"
                  required
                />
              </div>

              <div class="form-group">
                <label for="type">Discount Type *</label>
                <select id="type" v-model="formData.type" required>
                  <option value="">Select Type</option>
                  <option value="percentage">Percentage</option>
                  <option value="fixed">Fixed Amount</option>
                </select>
              </div>

              <div class="form-group">
                <label for="value">Discount Value *</label>
                <input
                  id="value"
                  v-model.number="formData.value"
                  type="number"
                  :placeholder="formData.type === 'percentage' ? '10' : '50'"
                  step="0.01"
                  min="0"
                  required
                />
                <small v-if="formData.type === 'percentage'"
                  >Enter percentage (e.g., 10 for 10%)</small
                >
                <small v-else-if="formData.type === 'fixed'">Enter dollar amount</small>
              </div>

              <div class="form-group">
                <label for="code">Promo Code</label>
                <input
                  id="code"
                  v-model="formData.code"
                  type="text"
                  placeholder="SAVE10"
                  style="text-transform: uppercase"
                />
              </div>

              <div class="form-group">
                <label for="minPurchase">Minimum Purchase ($)</label>
                <input
                  id="minPurchase"
                  v-model.number="formData.minPurchase"
                  type="number"
                  placeholder="0"
                  min="0"
                  step="0.01"
                />
              </div>

              <div class="form-group">
                <label for="startDate">Start Date *</label>
                <input id="startDate" v-model="formData.startDate" type="date" required />
              </div>

              <div class="form-group">
                <label for="endDate">End Date *</label>
                <input id="endDate" v-model="formData.endDate" type="date" required />
              </div>

              <div class="form-group">
                <label for="maxUses">Maximum Uses</label>
                <input
                  id="maxUses"
                  v-model.number="formData.maxUses"
                  type="number"
                  placeholder="Unlimited"
                  min="1"
                />
              </div>

              <div class="form-group full-width">
                <label for="description">Description</label>
                <textarea
                  id="description"
                  v-model="formData.description"
                  rows="3"
                  placeholder="Describe the discount promotion..."
                ></textarea>
              </div>

              <div class="form-group full-width">
                <label class="checkbox-label">
                  <input type="checkbox" v-model="formData.isActive" />
                  <span>Active (Discount is currently available)</span>
                </label>
              </div>
            </div>

            <div class="modal-footer">
              <button type="button" @click="closeModal" class="btn-secondary">Cancel</button>
              <button type="submit" class="btn-primary">
                {{ isEditMode ? 'Update Discount' : 'Create Discount' }}
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
  discount: {
    type: Object,
    default: null,
  },
})

const emit = defineEmits(['close', 'save'])

const formData = ref({
  name: '',
  type: '',
  value: 0,
  code: '',
  minPurchase: 0,
  startDate: '',
  endDate: '',
  maxUses: null,
  description: '',
  isActive: true,
})

const isEditMode = ref(false)

const resetForm = () => {
  formData.value = {
    name: '',
    type: '',
    value: 0,
    code: '',
    minPurchase: 0,
    startDate: '',
    endDate: '',
    maxUses: null,
    description: '',
    isActive: true,
  }
}

watch(
  () => props.discount,
  (newDiscount) => {
    if (newDiscount) {
      isEditMode.value = true
      formData.value = { ...newDiscount }
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
  // Convert code to uppercase
  if (formData.value.code) {
    formData.value.code = formData.value.code.toUpperCase()
  }

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
  background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
  color: #333;
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
  color: #333;
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
  background: rgba(0, 0, 0, 0.1);
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

.form-group small {
  margin-top: 4px;
  font-size: 0.8rem;
  color: #666;
  font-style: italic;
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
  border-color: #fa709a;
  box-shadow: 0 0 0 3px rgba(250, 112, 154, 0.1);
}

.form-group textarea {
  resize: vertical;
  min-height: 80px;
}

.checkbox-label {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  padding: 12px;
  background: #f8f9fa;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.checkbox-label:hover {
  background: #e9ecef;
}

.checkbox-label input[type='checkbox'] {
  width: 20px;
  height: 20px;
  cursor: pointer;
}

.checkbox-label span {
  font-size: 0.95rem;
  color: #333;
  font-weight: 500;
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
  background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
  color: #333;
}

.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(250, 112, 154, 0.4);
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
