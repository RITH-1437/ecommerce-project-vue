<template>
  <Transition name="modal">
    <div v-if="isOpen" class="modal-overlay" @click.self="closeModal">
      <div class="modal-container">
        <div class="modal-header">
          <h2 class="modal-title">User Details</h2>
          <button @click="closeModal" class="close-btn">&times;</button>
        </div>

        <div class="modal-body">
          <div class="user-info-grid">
            <div class="info-section">
              <h3 class="section-title">Personal Information</h3>
              <div class="info-row">
                <span class="info-label">Full Name:</span>
                <span class="info-value">{{ user?.name || 'N/A' }}</span>
              </div>
              <div class="info-row">
                <span class="info-label">Email:</span>
                <span class="info-value">{{ user?.email || 'N/A' }}</span>
              </div>
              <div class="info-row">
                <span class="info-label">Phone:</span>
                <span class="info-value">{{ user?.phone || 'Not provided' }}</span>
              </div>
              <div class="info-row">
                <span class="info-label">Role:</span>
                <span class="role-badge" :class="`role-${user?.role?.toLowerCase()}`">
                  {{ user?.role || 'N/A' }}
                </span>
              </div>
            </div>

            <div class="info-section">
              <h3 class="section-title">Account Status</h3>
              <div class="info-row">
                <span class="info-label">Member Since:</span>
                <span class="info-value">{{ formatDate(user?.createdAt) }}</span>
              </div>
              <div class="info-row">
                <span class="info-label">Last Login:</span>
                <span class="info-value">{{ formatDate(user?.lastLogin) }}</span>
              </div>
              <div class="info-row">
                <span class="info-label">Status:</span>
                <span class="status-badge" :class="user?.isActive ? 'active' : 'inactive'">
                  {{ user?.isActive ? 'Active' : 'Inactive' }}
                </span>
              </div>
              <div class="info-row">
                <span class="info-label">Total Orders:</span>
                <span class="info-value">{{ user?.totalOrders || 0 }}</span>
              </div>
            </div>

            <div class="info-section full-width">
              <h3 class="section-title">Shipping Address</h3>
              <div class="info-row">
                <span class="info-label">Address:</span>
                <span class="info-value">{{ user?.address || 'Not provided' }}</span>
              </div>
              <div class="info-row">
                <span class="info-label">City:</span>
                <span class="info-value">{{ user?.city || 'Not provided' }}</span>
              </div>
              <div class="info-row">
                <span class="info-label">State / Zip:</span>
                <span class="info-value">{{ user?.state || 'N/A' }} {{ user?.zipCode || '' }}</span>
              </div>
            </div>

            <div class="info-section full-width">
              <h3 class="section-title">Change Role</h3>
              <div class="role-change-section">
                <select v-model="selectedRole" class="role-select">
                  <option value="customer">Customer</option>
                  <option value="employee">Employee</option>
                  <option value="admin">Admin</option>
                </select>
                <button @click="updateRole" class="update-role-btn">Update Role</button>
              </div>
            </div>
          </div>

          <div class="modal-footer">
            <button @click="closeModal" class="btn-secondary">Close</button>
            <button @click="deactivateUser" class="btn-danger" v-if="user?.isActive">
              Deactivate Account
            </button>
            <button @click="activateUser" class="btn-success" v-else>Activate Account</button>
          </div>
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
  user: {
    type: Object,
    default: null,
  },
})

const emit = defineEmits(['close', 'updateRole', 'toggleStatus'])

const selectedRole = ref('')

watch(
  () => props.user,
  (newUser) => {
    if (newUser) {
      selectedRole.value = newUser.role || 'customer'
    }
  },
  { immediate: true },
)

const closeModal = () => {
  emit('close')
}

const formatDate = (dateString) => {
  if (!dateString) return 'N/A'
  const date = new Date(dateString)
  return date.toLocaleDateString('en-US', { year: 'numeric', month: 'long', day: 'numeric' })
}

const updateRole = () => {
  emit('updateRole', props.user.id, selectedRole.value)
}

const deactivateUser = () => {
  emit('toggleStatus', props.user.id, false)
}

const activateUser = () => {
  emit('toggleStatus', props.user.id, true)
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
  max-width: 900px;
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

.user-info-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 24px;
}

.info-section {
  background: #f8f9fa;
  padding: 20px;
  border-radius: 12px;
}

.info-section.full-width {
  grid-column: 1 / -1;
}

.section-title {
  font-size: 1.1rem;
  font-weight: 600;
  color: #333;
  margin: 0 0 16px 0;
  padding-bottom: 12px;
  border-bottom: 2px solid #e9ecef;
}

.info-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid #e9ecef;
}

.info-row:last-child {
  border-bottom: none;
}

.info-label {
  font-weight: 600;
  color: #666;
  font-size: 0.9rem;
}

.info-value {
  color: #333;
  font-size: 0.95rem;
}

.role-badge {
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 0.8rem;
  font-weight: 600;
  text-transform: capitalize;
}

.role-badge.role-customer {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.role-badge.role-employee {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  color: white;
}

.role-badge.role-admin {
  background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
  color: #333;
}

.status-badge {
  padding: 4px 12px;
  border-radius: 20px;
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

.role-change-section {
  display: flex;
  gap: 12px;
  align-items: center;
}

.role-select {
  flex: 1;
  padding: 10px 16px;
  border: 2px solid #e9ecef;
  border-radius: 8px;
  font-size: 0.95rem;
  cursor: pointer;
  transition: all 0.3s ease;
}

.role-select:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.update-role-btn {
  padding: 10px 24px;
  border: none;
  border-radius: 8px;
  font-size: 0.95rem;
  font-weight: 600;
  cursor: pointer;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  transition: all 0.3s ease;
}

.update-role-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
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
.btn-danger,
.btn-success {
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

.btn-danger {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  color: white;
}

.btn-danger:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(245, 87, 108, 0.4);
}

.btn-success {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
  color: white;
}

.btn-success:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(67, 233, 123, 0.4);
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
  .user-info-grid {
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

  .role-change-section {
    flex-direction: column;
  }

  .role-select,
  .update-role-btn {
    width: 100%;
  }
}
</style>
