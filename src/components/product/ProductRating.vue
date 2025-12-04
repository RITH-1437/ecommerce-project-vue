<template>
  <div class="product-rating-section">
    <!-- Rating Summary -->
    <div class="rating-summary">
      <div class="average-rating">
        <div class="rating-number">{{ averageRating }}</div>
        <div class="stars-large">
          <span
            v-for="star in 5"
            :key="star"
            class="star"
            :class="{ filled: star <= Math.floor(averageRating) }"
          >
            ★
          </span>
        </div>
        <div class="total-reviews">{{ totalReviews }} reviews</div>
      </div>
      <div class="rating-breakdown">
        <div v-for="rating in [5, 4, 3, 2, 1]" :key="rating" class="rating-bar-item">
          <span class="rating-label">{{ rating }} ★</span>
          <div class="rating-bar">
            <div
              class="rating-bar-fill"
              :style="{ width: getRatingPercentage(rating) + '%' }"
            ></div>
          </div>
          <span class="rating-count">{{ getRatingCount(rating) }}</span>
        </div>
      </div>
    </div>

    <!-- Write Review Button (Only for logged-in users) -->
    <div v-if="authStore.isLoggedIn && !userHasReviewed" class="write-review-section">
      <button @click="openReviewForm" class="write-review-btn">
        <span class="icon"><i class="fas fa-pen"></i></span>
        <span>Write a Review</span>
      </button>
    </div>

    <!-- User's Existing Review -->
    <div v-if="userReview" class="user-existing-review">
      <div class="review-notice">
        <span class="icon"><i class="fas fa-check-circle"></i></span>
        <span>You've already reviewed this product</span>
      </div>
      <div class="review-card user-review">
        <div class="review-header">
          <div class="review-author">
            <div class="author-avatar">{{ userReview.authorAvatar }}</div>
            <div class="author-info">
              <div class="author-name">{{ userReview.authorName }} (You)</div>
              <div class="review-date">{{ formatDate(userReview.date) }}</div>
            </div>
          </div>
          <div class="review-rating">
            <span
              v-for="star in 5"
              :key="star"
              class="star"
              :class="{ filled: star <= userReview.rating }"
            >
              ★
            </span>
          </div>
        </div>
        <div class="review-content">
          <p>{{ userReview.comment }}</p>
        </div>
        <div class="review-status">
          <span class="status-badge" :class="userReview.status">
            {{ userReview.status === 'approved' ? '✓ Approved' : '⏳ Pending Approval' }}
          </span>
        </div>
      </div>
    </div>

    <!-- Reviews List -->
    <div class="reviews-list">
      <h3 class="reviews-title">Customer Reviews</h3>
      <div v-if="approvedReviews.length === 0" class="no-reviews">
        <span class="icon"><i class="fas fa-comment-dots"></i></span>
        <p>No reviews yet. Be the first to review this product!</p>
      </div>
      <div v-else>
        <div v-for="review in approvedReviews" :key="review.id" class="review-card">
          <div class="review-header">
            <div class="review-author">
              <div class="author-avatar">{{ review.authorAvatar }}</div>
              <div class="author-info">
                <div class="author-name">{{ review.authorName }}</div>
                <div class="review-date">{{ formatDate(review.date) }}</div>
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
            </div>
          </div>
          <div class="review-content">
            <p>{{ review.comment }}</p>
          </div>
          <div v-if="review.helpful" class="review-helpful">
            <button @click="markHelpful(review.id)" class="helpful-btn">
              <span>👍</span>
              <span>Helpful ({{ review.helpfulCount }})</span>
            </button>
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
  name: 'ProductRating',
  props: {
    productId: {
      type: [String, Number],
      required: true,
    },
    productName: {
      type: String,
      required: true,
    },
  },
  setup() {
    const authStore = useAuthStore()
    return { authStore }
  },
  data() {
    return {
      reviews: [],
    }
  },
  computed: {
    approvedReviews() {
      return this.reviews.filter((review) => review.status === 'approved')
    },
    userReview() {
      if (!this.authStore.isLoggedIn) return null
      return this.reviews.find((review) => review.userId === this.authStore.user?.id)
    },
    userHasReviewed() {
      return !!this.userReview
    },
    averageRating() {
      if (this.approvedReviews.length === 0) return 0
      const sum = this.approvedReviews.reduce((acc, review) => acc + review.rating, 0)
      return (sum / this.approvedReviews.length).toFixed(1)
    },
    totalReviews() {
      return this.approvedReviews.length
    },
  },
  mounted() {
    this.loadReviews()
  },
  methods: {
    loadReviews() {
      const allReviews = JSON.parse(localStorage.getItem('productReviews') || '[]')
      this.reviews = allReviews.filter((review) => review.productId === this.productId)
    },
    getRatingCount(rating) {
      return this.approvedReviews.filter((review) => review.rating === rating).length
    },
    getRatingPercentage(rating) {
      if (this.approvedReviews.length === 0) return 0
      const count = this.getRatingCount(rating)
      return (count / this.approvedReviews.length) * 100
    },
    formatDate(dateString) {
      const date = new Date(dateString)
      return date.toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
      })
    },
    async openReviewForm() {
      const { value: formValues } = await Swal.fire({
        title: `Review ${this.productName}`,
        html: `
          <div style="text-align: left; padding: 20px;">
            <div style="margin-bottom: 20px;">
              <label style="font-weight: 600; display: block; margin-bottom: 10px;">
                Your Rating
              </label>
              <div class="star-rating-input" style="display: flex; gap: 5px; font-size: 2rem; justify-content: center;">
                <span class="star-input" data-value="1" style="cursor: pointer; color: #ddd;">★</span>
                <span class="star-input" data-value="2" style="cursor: pointer; color: #ddd;">★</span>
                <span class="star-input" data-value="3" style="cursor: pointer; color: #ddd;">★</span>
                <span class="star-input" data-value="4" style="cursor: pointer; color: #ddd;">★</span>
                <span class="star-input" data-value="5" style="cursor: pointer; color: #ddd;">★</span>
              </div>
              <input type="hidden" id="rating-value" value="0">
            </div>
            <div>
              <label for="review-comment" style="font-weight: 600; display: block; margin-bottom: 10px;">
                Your Review
              </label>
              <textarea
                id="review-comment"
                class="swal2-textarea"
                placeholder="Share your experience with this product..."
                style="width: 100%; min-height: 120px; padding: 12px; border: 2px solid #e5e5e7; border-radius: 8px; font-size: 14px;"
              ></textarea>
            </div>
          </div>
        `,
        focusConfirm: false,
        showCancelButton: true,
        confirmButtonText: 'Submit Review',
        cancelButtonText: 'Cancel',
        confirmButtonColor: '#0071e3',
        didOpen: () => {
          const stars = document.querySelectorAll('.star-input')
          const ratingInput = document.getElementById('rating-value')

          stars.forEach((star) => {
            star.addEventListener('click', function () {
              const value = parseInt(this.getAttribute('data-value'))
              ratingInput.value = value

              stars.forEach((s, index) => {
                if (index < value) {
                  s.style.color = '#ffd700'
                } else {
                  s.style.color = '#ddd'
                }
              })
            })

            star.addEventListener('mouseenter', function () {
              const value = parseInt(this.getAttribute('data-value'))
              stars.forEach((s, index) => {
                if (index < value) {
                  s.style.color = '#ffd700'
                }
              })
            })
          })

          document.querySelector('.star-rating-input').addEventListener('mouseleave', function () {
            const currentValue = parseInt(ratingInput.value)
            stars.forEach((s, index) => {
              if (index < currentValue) {
                s.style.color = '#ffd700'
              } else {
                s.style.color = '#ddd'
              }
            })
          })
        },
        preConfirm: () => {
          const rating = parseInt(document.getElementById('rating-value').value)
          const comment = document.getElementById('review-comment').value

          if (!rating || rating === 0) {
            Swal.showValidationMessage('Please select a rating')
            return false
          }
          if (!comment || comment.trim().length < 10) {
            Swal.showValidationMessage('Please write a review (at least 10 characters)')
            return false
          }

          return { rating, comment }
        },
      })

      if (formValues) {
        this.submitReview(formValues.rating, formValues.comment)
      }
    },
    submitReview(rating, comment) {
      const newReview = {
        id: Date.now().toString(),
        productId: this.productId,
        productName: this.productName,
        userId: this.authStore.user.id,
        authorName: this.authStore.user.name,
        authorAvatar: this.authStore.user.name.charAt(0).toUpperCase(),
        rating: rating,
        comment: comment,
        date: new Date().toISOString(),
        status: 'pending',
        helpfulCount: 0,
      }

      // Save to localStorage
      const allReviews = JSON.parse(localStorage.getItem('productReviews') || '[]')
      allReviews.push(newReview)
      localStorage.setItem('productReviews', JSON.stringify(allReviews))

      // Reload reviews
      this.loadReviews()

      // Show success message
      Swal.fire({
        icon: 'success',
        title: 'Review Submitted!',
        html: `
          <p>Thank you for your review!</p>
          <p style="color: #666; font-size: 14px; margin-top: 10px;">
            Your review will be visible after admin approval.
          </p>
        `,
        confirmButtonText: 'OK',
        confirmButtonColor: '#0071e3',
        timer: 3000,
        timerProgressBar: true,
      })
    },
    markHelpful(reviewId) {
      const allReviews = JSON.parse(localStorage.getItem('productReviews') || '[]')
      const reviewIndex = allReviews.findIndex((r) => r.id === reviewId)

      if (reviewIndex !== -1) {
        allReviews[reviewIndex].helpfulCount = (allReviews[reviewIndex].helpfulCount || 0) + 1
        localStorage.setItem('productReviews', JSON.stringify(allReviews))
        this.loadReviews()
      }
    },
  },
}
</script>

<style scoped>
.product-rating-section {
  background: #f9f9f9;
  border-radius: 20px;
  padding: 40px;
  margin: 40px 0;
}

/* Rating Summary */
.rating-summary {
  display: grid;
  grid-template-columns: 1fr 2fr;
  gap: 40px;
  margin-bottom: 40px;
  background: white;
  padding: 30px;
  border-radius: 16px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.average-rating {
  text-align: center;
  border-right: 2px solid #f0f0f0;
  padding-right: 40px;
}

.rating-number {
  font-size: 4rem;
  font-weight: 700;
  color: #0071e3;
  margin-bottom: 10px;
}

.stars-large {
  margin-bottom: 10px;
}

.stars-large .star {
  font-size: 2rem;
  color: #ddd;
  margin: 0 2px;
}

.stars-large .star.filled {
  color: #ffd700;
}

.total-reviews {
  color: #86868b;
  font-size: 1rem;
}

.rating-breakdown {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.rating-bar-item {
  display: flex;
  align-items: center;
  gap: 12px;
}

.rating-label {
  min-width: 40px;
  font-size: 0.9rem;
  color: #666;
}

.rating-bar {
  flex: 1;
  height: 8px;
  background: #f0f0f0;
  border-radius: 4px;
  overflow: hidden;
}

.rating-bar-fill {
  height: 100%;
  background: linear-gradient(135deg, #ffd700, #ffa500);
  transition: width 0.3s ease;
}

.rating-count {
  min-width: 30px;
  text-align: right;
  font-size: 0.9rem;
  color: #86868b;
}

/* Write Review Section */
.write-review-section {
  text-align: center;
  margin: 30px 0;
}

.write-review-btn {
  background: linear-gradient(135deg, #0071e3, #005bb5);
  color: white;
  border: none;
  padding: 16px 40px;
  border-radius: 30px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  display: inline-flex;
  align-items: center;
  gap: 10px;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(0, 113, 227, 0.3);
}

.write-review-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(0, 113, 227, 0.4);
}

.write-review-btn .icon {
  font-size: 1.2rem;
}

/* User Existing Review */
.user-existing-review {
  margin-bottom: 30px;
}

.review-notice {
  display: flex;
  align-items: center;
  gap: 10px;
  background: #e8f5e9;
  color: #2e7d32;
  padding: 12px 20px;
  border-radius: 10px;
  margin-bottom: 15px;
  font-weight: 600;
}

.user-review {
  border: 2px solid #0071e3;
  position: relative;
}

/* Reviews List */
.reviews-title {
  font-size: 1.8rem;
  font-weight: 700;
  color: #1d1d1f;
  margin-bottom: 30px;
}

.no-reviews {
  text-align: center;
  padding: 60px 20px;
  background: white;
  border-radius: 16px;
}

.no-reviews .icon {
  font-size: 4rem;
  display: block;
  margin-bottom: 20px;
  opacity: 0.5;
}

.no-reviews p {
  color: #86868b;
  font-size: 1.1rem;
}

.review-card {
  background: white;
  border-radius: 16px;
  padding: 25px;
  margin-bottom: 20px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.review-card:hover {
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
}

.review-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.review-author {
  display: flex;
  align-items: center;
  gap: 12px;
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

.author-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.author-name {
  font-weight: 600;
  color: #1d1d1f;
  font-size: 1rem;
}

.review-date {
  color: #86868b;
  font-size: 0.85rem;
}

.review-rating .star {
  font-size: 1.2rem;
  color: #ddd;
  margin: 0 2px;
}

.review-rating .star.filled {
  color: #ffd700;
}

.review-content {
  margin-bottom: 15px;
}

.review-content p {
  color: #333;
  line-height: 1.6;
  font-size: 0.95rem;
}

.review-status {
  margin-top: 15px;
}

.status-badge {
  display: inline-block;
  padding: 6px 16px;
  border-radius: 20px;
  font-size: 0.85rem;
  font-weight: 600;
}

.status-badge.approved {
  background: #e8f5e9;
  color: #2e7d32;
}

.status-badge.pending {
  background: #fff8e1;
  color: #f57c00;
}

.review-helpful {
  margin-top: 15px;
  padding-top: 15px;
  border-top: 1px solid #f0f0f0;
}

.helpful-btn {
  background: #f5f5f7;
  border: none;
  padding: 8px 16px;
  border-radius: 20px;
  cursor: pointer;
  display: inline-flex;
  align-items: center;
  gap: 8px;
  font-size: 0.9rem;
  color: #666;
  transition: all 0.3s ease;
}

.helpful-btn:hover {
  background: #e5e5e7;
  transform: scale(1.05);
}

/* Responsive */
@media (max-width: 768px) {
  .product-rating-section {
    padding: 20px;
  }

  .rating-summary {
    grid-template-columns: 1fr;
    gap: 20px;
  }

  .average-rating {
    border-right: none;
    border-bottom: 2px solid #f0f0f0;
    padding-right: 0;
    padding-bottom: 20px;
  }

  .rating-number {
    font-size: 3rem;
  }

  .reviews-title {
    font-size: 1.5rem;
  }

  .review-card {
    padding: 20px;
  }

  .review-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 15px;
  }
}
</style>
