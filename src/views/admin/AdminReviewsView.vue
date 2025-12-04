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
          <router-link to="/admin/reviews" class="nav-item" active-class="active">
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
            <h1 class="page-title">
              <span class="title-icon"><i class="fas fa-star"></i></span>
              Product Reviews Management
            </h1>
            <p class="page-subtitle">Approve or reject customer reviews</p>
          </div>
          <div class="header-right">
            <div class="stats-summary"></div>
            <router-link to="/" class="back-to-store-btn">
              <span class="icon"><i class="fas fa-store"></i></span>
              <span>Back to Store</span>
            </router-link>
          </div>
        </div>

        <!-- Filter Tabs -->
        <div class="filter-tabs">
          <button
            class="filter-tab"
            :class="{ active: activeFilter === 'pending' }"
            @click="activeFilter = 'pending'"
          >
            Pending ({{ pendingReviews.length }})
          </button>
          <button
            class="filter-tab"
            :class="{ active: activeFilter === 'approved' }"
            @click="activeFilter = 'approved'"
          >
            Approved ({{ approvedReviews.length }})
          </button>
          <button
            class="filter-tab"
            :class="{ active: activeFilter === 'rejected' }"
            @click="activeFilter = 'rejected'"
          >
            Rejected ({{ rejectedReviews.length }})
          </button>
          <button
            class="filter-tab"
            :class="{ active: activeFilter === 'all' }"
            @click="activeFilter = 'all'"
          >
            All ({{ allReviews.length }})
          </button>
        </div>

        <!-- Reviews List -->
        <div class="reviews-container">
          <div v-if="filteredReviews.length === 0" class="empty-state">
            <div class="empty-icon"><i class="fas fa-inbox"></i></div>
            <h3>No {{ activeFilter }} reviews</h3>
            <p>{{ getEmptyMessage }}</p>
          </div>

          <div v-else class="reviews-list">
            <div
              v-for="review in filteredReviews"
              :key="review.id"
              class="review-card"
              :class="review.status"
            >
              <div class="review-header">
                <div class="product-info">
                  <div class="product-name">{{ review.productName }}</div>
                  <div class="product-id">ID: {{ review.productId }}</div>
                </div>
                <div class="review-status-badge" :class="review.status">
                  <span v-if="review.status === 'pending'">⏳ Pending</span>
                  <span v-else-if="review.status === 'approved'">✅ Approved</span>
                  <span v-else-if="review.status === 'rejected'">❌ Rejected</span>
                </div>
              </div>

              <div class="review-body">
                <div class="review-author-section">
                  <div class="author-avatar">{{ review.authorAvatar }}</div>
                  <div class="author-details">
                    <div class="author-name">{{ review.authorName }}</div>
                    <div class="review-meta">
                      <span class="review-date">{{ formatDate(review.date) }}</span>
                      <span class="separator">•</span>
                      <span class="user-id">User ID: {{ review.userId }}</span>
                    </div>
                  </div>
                </div>

                <div class="review-rating">
                  <span
                    v-for="star in 5"
                    :key="star"
                    class="star"
                    :class="{ filled: star <= review.rating }"
                  >
                    ★
                  </span>
                  <span class="rating-text">{{ review.rating }}/5</span>
                </div>

                <div class="review-comment">
                  <p>{{ review.comment }}</p>
                </div>

                <div v-if="review.helpfulCount" class="review-stats">
                  <span class="helpful-count">👍 {{ review.helpfulCount }} found this helpful</span>
                </div>
              </div>

              <div class="review-actions">
                <button
                  v-if="review.status === 'pending' || review.status === 'rejected'"
                  @click="approveReview(review.id)"
                  class="action-btn approve-btn"
                >
                  <span>✓</span>
                  <span>Approve</span>
                </button>
                <button
                  v-if="review.status === 'pending' || review.status === 'approved'"
                  @click="rejectReview(review.id)"
                  class="action-btn reject-btn"
                >
                  <span>✕</span>
                  <span>Reject</span>
                </button>
                <button @click="deleteReview(review.id)" class="action-btn delete-btn">
                  <span>🗑️</span>
                  <span>Delete</span>
                </button>
                <button @click="viewDetails(review)" class="action-btn details-btn">
                  <span>👁️</span>
                  <span>Details</span>
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Swal from 'sweetalert2'

export default {
  name: 'AdminReviewsView',
  data() {
    return {
      allReviews: [],
      activeFilter: 'pending',
    }
  },
  computed: {
    pendingReviews() {
      return this.allReviews.filter((r) => r.status === 'pending')
    },
    approvedReviews() {
      return this.allReviews.filter((r) => r.status === 'approved')
    },
    rejectedReviews() {
      return this.allReviews.filter((r) => r.status === 'rejected')
    },
    filteredReviews() {
      if (this.activeFilter === 'all') return this.allReviews
      return this.allReviews.filter((r) => r.status === this.activeFilter)
    },
    getEmptyMessage() {
      const messages = {
        pending: 'All caught up! No reviews waiting for approval.',
        approved: 'No approved reviews yet.',
        rejected: 'No rejected reviews.',
        all: 'No reviews have been submitted yet.',
      }
      return messages[this.activeFilter] || ''
    },
  },
  mounted() {
    this.loadReviews()
  },
  methods: {
    loadReviews() {
      const reviews = JSON.parse(localStorage.getItem('productReviews') || '[]')
      this.allReviews = reviews.sort((a, b) => new Date(b.date) - new Date(a.date))
    },
    formatDate(dateString) {
      const date = new Date(dateString)
      return date.toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
      })
    },
    async approveReview(reviewId) {
      const result = await Swal.fire({
        title: 'Approve Review?',
        text: 'This review will be visible to all customers.',
        icon: 'question',
        showCancelButton: true,
        confirmButtonText: 'Yes, Approve',
        cancelButtonText: 'Cancel',
        confirmButtonColor: '#28a745',
        cancelButtonColor: '#6c757d',
      })

      if (result.isConfirmed) {
        this.updateReviewStatus(reviewId, 'approved')
        Swal.fire({
          icon: 'success',
          title: 'Review Approved!',
          text: 'The review is now visible to customers.',
          timer: 2000,
          showConfirmButton: false,
        })
      }
    },
    async rejectReview(reviewId) {
      const result = await Swal.fire({
        title: 'Reject Review?',
        text: 'This review will be hidden from customers.',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Yes, Reject',
        cancelButtonText: 'Cancel',
        confirmButtonColor: '#dc3545',
        cancelButtonColor: '#6c757d',
      })

      if (result.isConfirmed) {
        this.updateReviewStatus(reviewId, 'rejected')
        Swal.fire({
          icon: 'success',
          title: 'Review Rejected',
          text: 'The review has been rejected.',
          timer: 2000,
          showConfirmButton: false,
        })
      }
    },
    async deleteReview(reviewId) {
      const result = await Swal.fire({
        title: 'Delete Review?',
        text: 'This action cannot be undone!',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Yes, Delete',
        cancelButtonText: 'Cancel',
        confirmButtonColor: '#dc3545',
        cancelButtonColor: '#6c757d',
      })

      if (result.isConfirmed) {
        const reviews = JSON.parse(localStorage.getItem('productReviews') || '[]')
        const updatedReviews = reviews.filter((r) => r.id !== reviewId)
        localStorage.setItem('productReviews', JSON.stringify(updatedReviews))
        this.loadReviews()

        Swal.fire({
          icon: 'success',
          title: 'Review Deleted',
          text: 'The review has been permanently removed.',
          timer: 2000,
          showConfirmButton: false,
        })
      }
    },
    updateReviewStatus(reviewId, status) {
      const reviews = JSON.parse(localStorage.getItem('productReviews') || '[]')
      const reviewIndex = reviews.findIndex((r) => r.id === reviewId)

      if (reviewIndex !== -1) {
        reviews[reviewIndex].status = status
        reviews[reviewIndex].reviewedAt = new Date().toISOString()
        localStorage.setItem('productReviews', JSON.stringify(reviews))
        this.loadReviews()
      }
    },
    viewDetails(review) {
      Swal.fire({
        title: 'Review Details',
        html: `
          <div style="text-align: left; padding: 20px;">
            <div style="margin-bottom: 20px;">
              <h4 style="margin-bottom: 10px; color: #0071e3;">Product Information</h4>
              <p style="margin: 5px 0;"><strong>Product:</strong> ${review.productName}</p>
              <p style="margin: 5px 0;"><strong>Product ID:</strong> ${review.productId}</p>
            </div>
            <div style="margin-bottom: 20px;">
              <h4 style="margin-bottom: 10px; color: #0071e3;">Reviewer Information</h4>
              <p style="margin: 5px 0;"><strong>Name:</strong> ${review.authorName}</p>
              <p style="margin: 5px 0;"><strong>User ID:</strong> ${review.userId}</p>
              <p style="margin: 5px 0;"><strong>Date:</strong> ${this.formatDate(review.date)}</p>
            </div>
            <div style="margin-bottom: 20px;">
              <h4 style="margin-bottom: 10px; color: #0071e3;">Review Content</h4>
              <p style="margin: 5px 0;"><strong>Rating:</strong> ${'⭐'.repeat(review.rating)} (${review.rating}/5)</p>
              <p style="margin: 5px 0;"><strong>Comment:</strong></p>
              <p style="margin: 10px 0; padding: 15px; background: #f5f5f7; border-radius: 8px; line-height: 1.6;">
                ${review.comment}
              </p>
            </div>
            <div>
              <h4 style="margin-bottom: 10px; color: #0071e3;">Statistics</h4>
              <p style="margin: 5px 0;"><strong>Status:</strong> <span style="color: ${
                review.status === 'approved'
                  ? '#28a745'
                  : review.status === 'rejected'
                    ? '#dc3545'
                    : '#ffc107'
              }; font-weight: 600;">${review.status.toUpperCase()}</span></p>
              <p style="margin: 5px 0;"><strong>Helpful Count:</strong> ${review.helpfulCount || 0}</p>
              ${review.reviewedAt ? `<p style="margin: 5px 0;"><strong>Reviewed At:</strong> ${this.formatDate(review.reviewedAt)}</p>` : ''}
            </div>
          </div>
        `,
        width: 600,
        confirmButtonText: 'Close',
        confirmButtonColor: '#0071e3',
      })
    },
  },
}
</script>

<style scoped>
@import url('@/assets/styles/AdminStyles.css');

/* Filter Tabs */
.filter-tabs {
  display: flex;
  gap: 12px;
  margin-bottom: 30px;
  background: white;
  padding: 12px;
  border-radius: 12px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.filter-tab {
  flex: 1;
  padding: 12px 24px;
  border: none;
  background: transparent;
  border-radius: 8px;
  font-weight: 600;
  color: #666;
  cursor: pointer;
  transition: all 0.3s ease;
}

.filter-tab:hover {
  background: #f5f5f7;
}

.filter-tab.active {
  background: #0071e3;
  color: white;
}

/* Reviews Container */
.reviews-container {
  background: white;
  border-radius: 16px;
  padding: 30px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

/* Empty State */
.empty-state {
  text-align: center;
  padding: 80px 20px;
}

.empty-icon {
  font-size: 5rem;
  margin-bottom: 20px;
  opacity: 0.5;
}

.empty-state h3 {
  font-size: 1.5rem;
  color: #1d1d1f;
  margin-bottom: 10px;
}

.empty-state p {
  color: #86868b;
  font-size: 1rem;
}

/* Reviews List */
.reviews-list {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}

.review-card {
  background: white;
  border: 2px solid #f0f0f0;
  border-radius: 16px;
  padding: 25px;
  transition: all 0.3s ease;
}

.review-card:hover {
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
}

.review-card.pending {
  border-left: 4px solid #ffc107;
}

.review-card.approved {
  border-left: 4px solid #28a745;
}

.review-card.rejected {
  border-left: 4px solid #dc3545;
}

/* Review Header */
.review-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 2px solid #f5f5f7;
}

.product-info {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.product-name {
  font-size: 1.2rem;
  font-weight: 700;
  color: #1d1d1f;
}

.product-id {
  font-size: 0.85rem;
  color: #86868b;
}

.review-status-badge {
  padding: 8px 16px;
  border-radius: 20px;
  font-weight: 600;
  font-size: 0.9rem;
}

.review-status-badge.pending {
  background: #fff8e1;
  color: #f57c00;
}

.review-status-badge.approved {
  background: #e8f5e9;
  color: #2e7d32;
}

.review-status-badge.rejected {
  background: #ffebee;
  color: #c62828;
}

/* Review Body */
.review-body {
  margin-bottom: 20px;
}

.review-author-section {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-bottom: 15px;
}

.author-avatar {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5rem;
  font-weight: 700;
}

.author-details {
  flex: 1;
}

.author-name {
  font-weight: 600;
  color: #1d1d1f;
  font-size: 1rem;
  margin-bottom: 4px;
}

.review-meta {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 0.85rem;
  color: #86868b;
}

.separator {
  color: #ccc;
}

.review-rating {
  margin-bottom: 15px;
  display: flex;
  align-items: center;
  gap: 10px;
}

.star {
  font-size: 1.3rem;
  color: #ddd;
}

.star.filled {
  color: #ffd700;
}

.rating-text {
  font-weight: 600;
  color: #666;
}

.review-comment {
  background: #f9f9f9;
  padding: 15px;
  border-radius: 12px;
  margin-bottom: 15px;
}

.review-comment p {
  color: #333;
  line-height: 1.6;
  margin: 0;
}

.review-stats {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 10px;
  background: #f5f5f7;
  border-radius: 8px;
}

.helpful-count {
  font-size: 0.9rem;
  color: #666;
  font-weight: 500;
}

/* Review Actions */
.review-actions {
  display: flex;
  gap: 12px;
  padding-top: 15px;
  border-top: 2px solid #f5f5f7;
}

.action-btn {
  flex: 1;
  padding: 12px 20px;
  border: none;
  border-radius: 10px;
  font-weight: 600;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  transition: all 0.3s ease;
  font-size: 0.95rem;
}

.approve-btn {
  background: #28a745;
  color: white;
}

.approve-btn:hover {
  background: #218838;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(40, 167, 69, 0.3);
}

.reject-btn {
  background: #ffc107;
  color: #000;
}

.reject-btn:hover {
  background: #e0a800;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(255, 193, 7, 0.4);
}

.delete-btn {
  background: #dc3545;
  color: white;
}

.delete-btn:hover {
  background: #c82333;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(220, 53, 69, 0.3);
}

.details-btn {
  background: #0071e3;
  color: white;
}

.details-btn:hover {
  background: #005bb5;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 113, 227, 0.3);
}

/* Responsive */
@media (max-width: 1200px) {
  .reviews-header {
    flex-direction: column;
    gap: 20px;
    align-items: flex-start;
  }

  .stats-summary {
    width: 100%;
    justify-content: space-around;
  }
}

@media (max-width: 1024px) {
  .reviews-list {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .reviews-list {
    grid-template-columns: 1fr;
  }

  .admin-reviews-page {
    padding: 15px;
  }

  .reviews-header {
    padding: 20px;
  }

  .page-title {
    font-size: 1.5rem;
  }

  .filter-tabs {
    flex-direction: column;
  }

  .filter-tab {
    padding: 10px 16px;
  }

  .reviews-container {
    padding: 20px;
  }

  .review-card {
    padding: 20px;
  }

  .review-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }

  .review-actions {
    flex-direction: column;
  }

  .action-btn {
    padding: 10px 16px;
  }

  .stats-summary {
    gap: 15px;
  }

  .stat-number {
    font-size: 1.5rem;
  }
}
</style>
