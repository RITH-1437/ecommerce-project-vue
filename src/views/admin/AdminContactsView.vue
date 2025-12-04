<template>
  <div class="dashboard-page">
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
          <router-link to="/admin/contacts" class="nav-item" active-class="active">
            <span class="nav-icon"><i class="fas fa-envelope"></i></span>
            <span class="nav-text">Contacts</span>
            <span v-if="unreadCount > 0" class="nav-badge">{{ unreadCount }}</span>
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

      <!-- Main Dashboard Content -->
      <div class="dashboard-main">
        <!-- Header -->
        <div class="dashboard-header">
          <div class="header-left">
            <h1 class="page-title">
              <span class="title-icon">📧</span>
              Contact Messages
            </h1>
            <p class="page-subtitle">Manage and respond to customer inquiries</p>
          </div>
          <div class="header-right">
            <div class="header-actions">
              <button @click="refreshMessages" class="action-btn refresh-btn" :disabled="isLoading">
                <span class="icon" :class="{ spinning: isLoading }">🔄</span>
                <span>Refresh</span>
              </button>
              <router-link to="/admin/overview" class="action-btn">
                <span class="icon">📊</span>
                <span>Dashboard</span>
              </router-link>
              <router-link to="/" class="back-to-store-btn">
                <span class="icon"><i class="fas fa-store"></i></span>
                <span>Back to Store</span>
              </router-link>
            </div>
          </div>
        </div>

        <!-- Main Content -->
        <div class="contacts-content">
          <!-- Filters and Stats -->
          <div class="controls-section">
            <div class="filter-group">
              <button
                @click="filterStatus = 'all'"
                class="filter-btn"
                :class="{ active: filterStatus === 'all' }"
              >
                All ({{ contactStore.messages.length }})
              </button>
              <button
                @click="filterStatus = 'unread'"
                class="filter-btn"
                :class="{ active: filterStatus === 'unread' }"
              >
                Unread ({{ unreadCount }})
              </button>
              <button
                @click="filterStatus = 'replied'"
                class="filter-btn"
                :class="{ active: filterStatus === 'replied' }"
              >
                Replied ({{ repliedCount }})
              </button>
            </div>

            <div class="search-group">
              <input
                v-model="searchQuery"
                type="text"
                placeholder="Search by name, email, or subject..."
                class="search-input"
              />
              <span class="search-icon">🔍</span>
            </div>
          </div>

          <!-- Messages List and Details -->
          <div class="messages-container">
            <!-- Messages List -->
            <div class="messages-list">
              <div v-if="filteredMessages.length === 0" class="empty-state">
                <div class="empty-icon">📭</div>
                <h3>No messages found</h3>
                <p>There are no contact messages matching your criteria.</p>
              </div>

              <div
                v-for="message in filteredMessages"
                :key="message.id"
                class="message-item"
                :class="{
                  selected: selectedMessage?.id === message.id,
                  unread: !message.isRead,
                }"
                @click="selectMessage(message)"
              >
                <div class="message-avatar">
                  <span class="avatar-text">{{ getInitials(message.name) }}</span>
                </div>
                <div class="message-info">
                  <div class="message-header">
                    <span class="message-name">{{ message.name }}</span>
                    <span class="message-time">{{ formatTimeAgo(message.createdAt) }}</span>
                  </div>
                  <div class="message-subject">
                    <span class="subject-badge" :class="message.subject">{{
                      getSubjectLabel(message.subject)
                    }}</span>
                  </div>
                  <div class="message-preview">{{ truncateText(message.message, 60) }}</div>
                  <div class="message-footer">
                    <span class="message-email">{{ message.email }}</span>
                    <span v-if="message.reply" class="reply-indicator">
                      <i class="fas fa-reply"></i> Replied
                    </span>
                  </div>
                </div>
                <div v-if="!message.isRead" class="unread-dot"></div>
              </div>
            </div>

            <!-- Message Details -->
            <div class="message-details" v-if="selectedMessage">
              <div class="details-header">
                <div class="sender-info">
                  <div class="sender-avatar">
                    <span class="avatar-text">{{ getInitials(selectedMessage.name) }}</span>
                  </div>
                  <div class="sender-details">
                    <h3 class="sender-name">{{ selectedMessage.name }}</h3>
                    <p class="sender-email">{{ selectedMessage.email }}</p>
                    <p class="message-date">{{ formatDate(selectedMessage.createdAt) }}</p>
                  </div>
                </div>
                <div class="details-actions">
                  <button
                    @click="toggleRead(selectedMessage)"
                    class="action-icon-btn"
                    :title="selectedMessage.isRead ? 'Mark as Unread' : 'Mark as Read'"
                  >
                    <i
                      :class="selectedMessage.isRead ? 'fas fa-envelope-open' : 'fas fa-envelope'"
                    ></i>
                  </button>
                  <button
                    @click="deleteMessage(selectedMessage)"
                    class="action-icon-btn delete"
                    title="Delete"
                  >
                    <i class="fas fa-trash"></i>
                  </button>
                </div>
              </div>

              <div class="details-body">
                <div class="message-subject-display">
                  <span class="subject-label">Subject:</span>
                  <span class="subject-badge large" :class="selectedMessage.subject">{{
                    getSubjectLabel(selectedMessage.subject)
                  }}</span>
                </div>

                <div class="message-content">
                  <h4>Message:</h4>
                  <p>{{ selectedMessage.message }}</p>
                </div>

                <!-- Previous Reply (if exists) -->
                <div v-if="selectedMessage.reply" class="previous-reply">
                  <div class="reply-header">
                    <i class="fas fa-reply"></i>
                    <span>Your Reply</span>
                    <span class="reply-time">{{ formatTimeAgo(selectedMessage.repliedAt) }}</span>
                  </div>
                  <p>{{ selectedMessage.reply }}</p>
                </div>

                <!-- Reply Form -->
                <div class="reply-section">
                  <h4>
                    <i class="fas fa-reply"></i>
                    {{ selectedMessage.reply ? 'Send Another Reply' : 'Reply to Message' }}
                  </h4>
                  <textarea
                    v-model="replyText"
                    placeholder="Type your response here..."
                    rows="6"
                    class="reply-textarea"
                  ></textarea>
                  <div class="reply-actions">
                    <button @click="sendReply" class="send-reply-btn" :disabled="!replyText.trim()">
                      <i class="fas fa-paper-plane"></i>
                      Send Reply
                    </button>
                    <button @click="replyText = ''" class="clear-btn">
                      <i class="fas fa-times"></i>
                      Clear
                    </button>
                  </div>
                </div>
              </div>
            </div>

            <!-- Empty State for Details -->
            <div v-else class="message-details empty">
              <div class="empty-details">
                <div class="empty-icon">📬</div>
                <h3>Select a message</h3>
                <p>Choose a message from the list to view details and reply</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth.js'
import { useContactStore } from '@/stores/contact.js'
import Swal from 'sweetalert2'

export default {
  name: 'AdminContactsView',
  setup() {
    const router = useRouter()
    const authStore = useAuthStore()
    const contactStore = useContactStore()

    // Reactive state
    const isLoading = ref(false)
    const filterStatus = ref('all')
    const searchQuery = ref('')
    const selectedMessage = ref(null)
    const replyText = ref('')

    // Computed
    const unreadCount = computed(() => {
      return contactStore.messages.filter((m) => !m.isRead).length
    })

    const repliedCount = computed(() => {
      return contactStore.messages.filter((m) => m.reply).length
    })

    const filteredMessages = computed(() => {
      let messages = [...contactStore.messages]

      // Filter by status
      if (filterStatus.value === 'unread') {
        messages = messages.filter((m) => !m.isRead)
      } else if (filterStatus.value === 'replied') {
        messages = messages.filter((m) => m.reply)
      }

      // Search filter
      if (searchQuery.value.trim()) {
        const query = searchQuery.value.toLowerCase()
        messages = messages.filter(
          (m) =>
            m.name.toLowerCase().includes(query) ||
            m.email.toLowerCase().includes(query) ||
            m.subject.toLowerCase().includes(query) ||
            m.message.toLowerCase().includes(query),
        )
      }

      // Sort by date (newest first)
      return messages.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt))
    })

    // Methods
    const getInitials = (name) => {
      return name
        .split(' ')
        .map((n) => n[0])
        .join('')
        .toUpperCase()
        .slice(0, 2)
    }

    const getSubjectLabel = (subject) => {
      const labels = {
        order: 'Order Inquiry',
        product: 'Product Question',
        support: 'Technical Support',
        refund: 'Refund Request',
        feedback: 'Feedback',
        other: 'Other',
      }
      return labels[subject] || subject
    }

    const formatDate = (date) => {
      return new Intl.DateTimeFormat('en-US', {
        weekday: 'long',
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: 'numeric',
        minute: '2-digit',
        hour12: true,
      }).format(new Date(date))
    }

    const formatTimeAgo = (date) => {
      const now = new Date()
      const diff = now - new Date(date)
      const minutes = Math.floor(diff / 60000)
      const hours = Math.floor(minutes / 60)
      const days = Math.floor(hours / 24)

      if (days > 0) return `${days}d ago`
      if (hours > 0) return `${hours}h ago`
      if (minutes > 0) return `${minutes}m ago`
      return 'Just now'
    }

    const truncateText = (text, length) => {
      if (text.length <= length) return text
      return text.substring(0, length) + '...'
    }

    const selectMessage = (message) => {
      selectedMessage.value = message
      replyText.value = ''

      // Mark as read when selected
      if (!message.isRead) {
        contactStore.markAsRead(message.id)
      }
    }

    const toggleRead = (message) => {
      contactStore.toggleRead(message.id)
    }

    const deleteMessage = async (message) => {
      const result = await Swal.fire({
        title: 'Delete Message?',
        html: `
          <p>Are you sure you want to delete this message from <strong>${message.name}</strong>?</p>
          <p style="color: #666; font-size: 0.9rem;">This action cannot be undone.</p>
        `,
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#ff3b30',
        cancelButtonColor: '#6c757d',
        confirmButtonText: 'Yes, Delete',
        cancelButtonText: 'Cancel',
      })

      if (result.isConfirmed) {
        contactStore.deleteMessage(message.id)
        selectedMessage.value = null

        Swal.fire({
          icon: 'success',
          title: 'Deleted!',
          text: 'The message has been deleted.',
          timer: 2000,
          showConfirmButton: false,
        })
      }
    }

    const sendReply = async () => {
      if (!replyText.value.trim()) return

      try {
        await contactStore.replyToMessage(selectedMessage.value.id, replyText.value)

        Swal.fire({
          icon: 'success',
          title: 'Reply Sent!',
          html: `
            <p>Your reply has been sent to <strong>${selectedMessage.value.name}</strong></p>
            <p style="color: #666; font-size: 0.9rem;">Email: ${selectedMessage.value.email}</p>
          `,
          confirmButtonColor: '#007aff',
          timer: 3000,
        })

        replyText.value = ''
      } catch (error) {
        Swal.fire({
          icon: 'error',
          title: 'Failed to Send',
          text: 'Could not send reply. Please try again.',
          confirmButtonColor: '#ff3b30',
        })
      }
    }

    const refreshMessages = async () => {
      isLoading.value = true
      try {
        await contactStore.fetchMessages()
        await new Promise((resolve) => setTimeout(resolve, 500))
      } finally {
        isLoading.value = false
      }
    }

    // Lifecycle
    onMounted(async () => {
      if (!authStore.isLoggedIn || !authStore.isAdmin) {
        router.push('/auth')
        return
      }

      await contactStore.fetchMessages()
    })

    return {
      contactStore,
      isLoading,
      filterStatus,
      searchQuery,
      selectedMessage,
      replyText,
      unreadCount,
      repliedCount,
      filteredMessages,
      getInitials,
      getSubjectLabel,
      formatDate,
      formatTimeAgo,
      truncateText,
      selectMessage,
      toggleRead,
      deleteMessage,
      sendReply,
      refreshMessages,
    }
  },
}
</script>

<style scoped>
.dashboard-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
}

.dashboard-container {
  display: flex;
  min-height: 100vh;
}

/* Sidebar Styles */
.sidebar {
  position: fixed;
  left: 0;
  top: 0;
  width: 240px;
  height: 100vh;
  background: linear-gradient(180deg, #1e3c72 0%, #2a5298 100%);
  backdrop-filter: blur(20px);
  box-shadow: 4px 0 20px rgba(0, 0, 0, 0.15);
  display: flex;
  flex-direction: column;
  z-index: 100;
  color: white;
}

.sidebar-header {
  padding: 25px 20px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.logo {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
}

.logo-icon {
  font-size: 1.8rem;
}

.sidebar-title {
  font-size: 1.3rem;
  font-weight: 700;
  color: white;
  margin: 0;
}

.sidebar-status {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 0.9rem;
  color: rgba(255, 255, 255, 0.8);
}

.status-indicator {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #34c759;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0%,
  100% {
    opacity: 1;
  }
  50% {
    opacity: 0.5;
  }
}

.sidebar-nav {
  flex: 1;
  padding: 15px 0;
}

.nav-item {
  display: flex;
  align-items: center;
  padding: 12px 20px;
  text-decoration: none;
  color: rgba(255, 255, 255, 0.8);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border-left: 3px solid transparent;
  position: relative;
}

.nav-item:hover,
.nav-item.active {
  background: rgba(255, 255, 255, 0.1);
  color: white;
  border-left-color: #34c759;
  transform: translateX(4px);
}

.nav-item.active::before {
  content: '';
  position: absolute;
  right: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 4px;
  height: 40px;
  background: #34c759;
  border-radius: 2px 0 0 2px;
}

.nav-icon {
  margin-right: 12px;
  font-size: 1.1rem;
  width: 18px;
  text-align: center;
}

.nav-text {
  font-weight: 500;
  flex: 1;
  font-size: 0.9rem;
}

.nav-badge {
  background: #ff3b30;
  color: white;
  padding: 2px 8px;
  border-radius: 12px;
  font-size: 0.75rem;
  font-weight: 600;
  min-width: 18px;
  text-align: center;
}

/* Main Dashboard */
.dashboard-main {
  flex: 1;
  margin-left: 240px;
  padding: 0;
  overflow-y: auto;
  background: #f8f9fb;
}

/* Sidebar Footer */
.sidebar-footer {
  padding: 15px 20px;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
}

.dashboard-header {
  background: white;
  padding: 20px 30px;
  border-bottom: 1px solid #e9ecef;
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: sticky;
  top: 0;
  z-index: 50;
}

.page-title {
  font-size: 1.6rem;
  font-weight: 700;
  color: #1a1a1a;
  margin: 0;
  display: flex;
  align-items: center;
  gap: 10px;
}

.title-icon {
  font-size: 2rem;
}

.page-subtitle {
  color: #666;
  font-size: 1rem;
  margin: 8px 0 0 0;
}

.header-actions {
  display: flex;
  gap: 12px;
}

.action-btn {
  background: #f8f9fa;
  border: 1px solid #e9ecef;
  color: #495057;
  padding: 12px 20px;
  border-radius: 12px;
  cursor: pointer;
  font-weight: 500;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 8px;
  text-decoration: none;
}

.action-btn:hover {
  background: #e9ecef;
  transform: translateY(-1px);
}

.refresh-btn {
  background: #007aff;
  color: white;
  border-color: #007aff;
}

.refresh-btn:hover {
  background: #0056b3;
}

.spinning {
  animation: spin 1s linear infinite;
}

@keyframes spin {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

/* Contacts Content */
.contacts-content {
  padding: 20px 30px;
}

.controls-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  gap: 20px;
}

.filter-group {
  display: flex;
  gap: 8px;
}

.filter-btn {
  padding: 10px 20px;
  border: 2px solid #e9ecef;
  background: white;
  color: #495057;
  border-radius: 10px;
  cursor: pointer;
  font-weight: 500;
  transition: all 0.3s ease;
}

.filter-btn:hover {
  background: #f8f9fa;
  border-color: #007aff;
}

.filter-btn.active {
  background: #007aff;
  color: white;
  border-color: #007aff;
}

.search-group {
  position: relative;
  flex: 1;
  max-width: 400px;
}

.search-input {
  width: 100%;
  padding: 12px 40px 12px 16px;
  border: 2px solid #e9ecef;
  border-radius: 10px;
  font-size: 0.9rem;
  transition: all 0.3s ease;
}

.search-input:focus {
  outline: none;
  border-color: #007aff;
  box-shadow: 0 0 0 4px rgba(0, 122, 255, 0.1);
}

.search-icon {
  position: absolute;
  right: 12px;
  top: 50%;
  transform: translateY(-50%);
  pointer-events: none;
}

/* Messages Container */
.messages-container {
  display: grid;
  grid-template-columns: 400px 1fr;
  gap: 20px;
  background: white;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  height: calc(100vh - 220px);
}

.messages-list {
  border-right: 1px solid #e9ecef;
  overflow-y: auto;
  background: #f8f9fb;
}

.empty-state {
  text-align: center;
  padding: 60px 20px;
}

.empty-icon {
  font-size: 4rem;
  margin-bottom: 16px;
}

.empty-state h3 {
  color: #1a1a1a;
  margin: 0 0 8px 0;
}

.empty-state p {
  color: #666;
  margin: 0;
}

.message-item {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  padding: 16px;
  border-bottom: 1px solid #e9ecef;
  cursor: pointer;
  transition: all 0.2s ease;
  position: relative;
  background: white;
}

.message-item:hover {
  background: #f0f2f5;
}

.message-item.selected {
  background: #e3f2fd;
  border-left: 4px solid #007aff;
}

.message-item.unread {
  background: #fff8f0;
}

.message-avatar {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.avatar-text {
  color: white;
  font-weight: 700;
  font-size: 1.1rem;
}

.message-info {
  flex: 1;
  min-width: 0;
}

.message-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 6px;
}

.message-name {
  font-weight: 600;
  color: #1a1a1a;
  font-size: 0.95rem;
}

.message-time {
  font-size: 0.75rem;
  color: #999;
}

.message-subject {
  margin-bottom: 6px;
}

.subject-badge {
  display: inline-block;
  padding: 4px 10px;
  border-radius: 12px;
  font-size: 0.7rem;
  font-weight: 600;
  text-transform: uppercase;
}

.subject-badge.large {
  padding: 6px 14px;
  font-size: 0.8rem;
}

.subject-badge.order {
  background: #e3f2fd;
  color: #1976d2;
}
.subject-badge.product {
  background: #f3e5f5;
  color: #7b1fa2;
}
.subject-badge.support {
  background: #fff3e0;
  color: #f57c00;
}
.subject-badge.refund {
  background: #fef2f2;
  color: #dc2626;
}
.subject-badge.feedback {
  background: #e8f5e8;
  color: #2e7d32;
}
.subject-badge.other {
  background: #f5f5f5;
  color: #666;
}

.message-preview {
  font-size: 0.85rem;
  color: #666;
  margin-bottom: 6px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.message-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.message-email {
  font-size: 0.75rem;
  color: #999;
}

.reply-indicator {
  font-size: 0.75rem;
  color: #34c759;
  font-weight: 600;
}

.unread-dot {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  background: #007aff;
  position: absolute;
  right: 16px;
  top: 50%;
  transform: translateY(-50%);
}

/* Message Details */
.message-details {
  overflow-y: auto;
  background: white;
}

.message-details.empty {
  display: flex;
  align-items: center;
  justify-content: center;
}

.empty-details {
  text-align: center;
}

.empty-details .empty-icon {
  font-size: 5rem;
  margin-bottom: 20px;
}

.empty-details h3 {
  color: #1a1a1a;
  margin: 0 0 12px 0;
}

.empty-details p {
  color: #666;
  margin: 0;
}

.details-header {
  padding: 24px;
  border-bottom: 1px solid #e9ecef;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}

.sender-info {
  display: flex;
  gap: 16px;
}

.sender-avatar {
  width: 60px;
  height: 60px;
  border-radius: 16px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.sender-avatar .avatar-text {
  font-size: 1.4rem;
}

.sender-name {
  margin: 0 0 4px 0;
  color: #1a1a1a;
  font-size: 1.3rem;
}

.sender-email {
  color: #666;
  margin: 0 0 4px 0;
  font-size: 0.9rem;
}

.message-date {
  color: #999;
  margin: 0;
  font-size: 0.85rem;
}

.details-actions {
  display: flex;
  gap: 8px;
}

.action-icon-btn {
  width: 40px;
  height: 40px;
  border: 2px solid #e9ecef;
  background: white;
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #666;
}

.action-icon-btn:hover {
  background: #f8f9fa;
  border-color: #007aff;
  color: #007aff;
}

.action-icon-btn.delete:hover {
  border-color: #ff3b30;
  color: #ff3b30;
  background: #fef5f5;
}

.details-body {
  padding: 24px;
}

.message-subject-display {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 24px;
}

.subject-label {
  font-weight: 600;
  color: #666;
}

.message-content h4 {
  color: #1a1a1a;
  margin: 0 0 12px 0;
  font-size: 1.1rem;
}

.message-content p {
  color: #495057;
  line-height: 1.8;
  margin: 0;
  white-space: pre-wrap;
}

.previous-reply {
  background: #f8f9fb;
  padding: 20px;
  border-radius: 12px;
  margin: 24px 0;
  border-left: 4px solid #34c759;
}

.reply-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
  color: #34c759;
  font-weight: 600;
  font-size: 0.9rem;
}

.reply-time {
  color: #999;
  font-weight: 400;
  margin-left: auto;
}

.previous-reply p {
  color: #495057;
  line-height: 1.6;
  margin: 0;
  white-space: pre-wrap;
}

.reply-section {
  margin-top: 32px;
  padding-top: 24px;
  border-top: 2px dashed #e9ecef;
}

.reply-section h4 {
  color: #1a1a1a;
  margin: 0 0 16px 0;
  font-size: 1.1rem;
  display: flex;
  align-items: center;
  gap: 8px;
}

.reply-textarea {
  width: 100%;
  padding: 16px;
  border: 2px solid #e9ecef;
  border-radius: 12px;
  font-size: 0.95rem;
  font-family: inherit;
  resize: vertical;
  transition: all 0.3s ease;
}

.reply-textarea:focus {
  outline: none;
  border-color: #007aff;
  box-shadow: 0 0 0 4px rgba(0, 122, 255, 0.1);
}

.reply-actions {
  display: flex;
  gap: 12px;
  margin-top: 16px;
}

.send-reply-btn {
  background: linear-gradient(135deg, #007aff, #0051d5);
  color: white;
  padding: 14px 28px;
  border: none;
  border-radius: 12px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 8px;
}

.send-reply-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(0, 122, 255, 0.3);
}

.send-reply-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.clear-btn {
  background: white;
  color: #666;
  padding: 14px 28px;
  border: 2px solid #e9ecef;
  border-radius: 12px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 8px;
}

.clear-btn:hover {
  background: #f8f9fa;
  border-color: #666;
}

/* Responsive Design */
@media (max-width: 1024px) {
  .messages-container {
    grid-template-columns: 1fr;
    height: auto;
  }

  .messages-list {
    border-right: none;
    border-bottom: 1px solid #e9ecef;
    max-height: 400px;
  }
}

@media (max-width: 768px) {
  .sidebar {
    position: relative;
    width: 100%;
    height: auto;
  }

  .dashboard-main {
    margin-left: 0;
  }

  .controls-section {
    flex-direction: column;
    align-items: stretch;
  }

  .search-group {
    max-width: 100%;
  }

  .dashboard-header {
    flex-direction: column;
    gap: 16px;
  }
}

/* Back to Store Button - Override scoped styles */
.back-to-store-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%) !important;
  color: white !important;
  padding: 8px 16px !important;
  border-radius: 8px !important;
  text-decoration: none !important;
  font-weight: 500 !important;
  font-size: 0.9rem !important;
  transition: all 0.3s ease !important;
  display: flex !important;
  align-items: center !important;
  gap: 6px !important;
  box-shadow: 0 2px 8px rgba(102, 126, 234, 0.2) !important;
}

.back-to-store-btn .icon {
  font-size: 1rem !important;
}

.back-to-store-btn:hover {
  transform: translateY(-1px) !important;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3) !important;
}
</style>

<style>
@import url('@/assets/styles/AdminStyles.css');
</style>
