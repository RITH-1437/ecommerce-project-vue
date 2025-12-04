<template>
  <div v-if="!isAdminPage">
    <!-- Contact Popup -->
    <div class="contact-popup" :class="{ 'popup-visible': showContactPopup }">
      <div class="popup-content">
        <div class="popup-header">
          <div class="contact-icon">
            <svg viewBox="0 0 24 24" width="20" height="20" fill="currentColor">
              <path
                d="M20 4H4c-1.1 0-2 .9-2 2v12c0 1.1.9 2 2 2h16c1.1 0 2-.9 2-2V6c0-1.1-.9-2-2-2zm0 4l-8 5-8-5V6l8 5 8-5v2z"
              />
            </svg>
          </div>
          <h4>Contact Us</h4>
          <button class="close-btn" @click="closeContactPopup">×</button>
        </div>
        
        <!-- Contact Form or Success Message -->
        <div class="popup-body">
          <div v-if="!submitSuccess" class="contact-form">
            <div class="form-group">
              <label for="contact-name">Full Name</label>
              <input
                id="contact-name"
                v-model="contactForm.name"
                type="text"
                placeholder="Enter your name"
                class="form-input"
                :class="{ error: errors.name }"
              />
              <span v-if="errors.name" class="error-text">{{ errors.name }}</span>
            </div>

            <div class="form-group">
              <label for="contact-email">Email Address</label>
              <input
                id="contact-email"
                v-model="contactForm.email"
                type="email"
                placeholder="your@email.com"
                class="form-input"
                :class="{ error: errors.email }"
              />
              <span v-if="errors.email" class="error-text">{{ errors.email }}</span>
            </div>

            <div class="form-group">
              <label for="contact-subject">Subject</label>
              <select
                id="contact-subject"
                v-model="contactForm.subject"
                class="form-input"
                :class="{ error: errors.subject }"
              >
                <option value="">Select a topic...</option>
                <option value="order">Order Inquiry</option>
                <option value="product">Product Question</option>
                <option value="support">Technical Support</option>
                <option value="refund">Refund Request</option>
                <option value="feedback">Feedback</option>
                <option value="other">Other</option>
              </select>
              <span v-if="errors.subject" class="error-text">{{ errors.subject }}</span>
            </div>

            <div class="form-group">
              <label for="contact-message">Message</label>
              <textarea
                id="contact-message"
                v-model="contactForm.message"
                placeholder="How can we help you?"
                rows="4"
                class="form-textarea"
                :class="{ error: errors.message }"
              ></textarea>
              <span v-if="errors.message" class="error-text">{{ errors.message }}</span>
            </div>

            <button @click="submitContact" class="submit-btn" :disabled="isSubmitting">
              <span v-if="!isSubmitting" class="btn-icon">📤</span>
              <span v-else class="btn-icon spinner">⌛</span>
              <span>{{ isSubmitting ? 'Sending...' : 'Send Message' }}</span>
            </button>
          </div>

          <div v-else class="success-message">
            <div class="success-icon">✅</div>
            <h3>Message Sent!</h3>
            <p>Thank you for contacting us. We'll get back to you within 24 hours.</p>
            <button @click="resetForm" class="new-message-btn">Send Another Message</button>
          </div>
        </div>
      </div>
    </div>

    <!-- Contact Toggle Button -->
    <button
      class="contact-toggle-btn"
      @click="toggleContactPopup"
      :class="{ active: showContactPopup }"
    >
      <svg viewBox="0 0 24 24" width="18" height="18" fill="currentColor">
        <path
          d="M20 4H4c-1.1 0-2 .9-2 2v12c0 1.1.9 2 2 2h16c1.1 0 2-.9 2-2V6c0-1.1-.9-2-2-2zm0 4l-8 5-8-5V6l8 5 8-5v2z"
        />
      </svg>
      <span v-if="unreadCount > 0" class="notification-badge">{{ unreadCount }}</span>
    </button>
  </div>
</template>

<script>
import { useContactStore } from '@/stores/contact'
import { useAuthStore } from '@/stores/auth'
import Swal from 'sweetalert2'

export default {
  name: 'ContactPopup',
  setup() {
    const contactStore = useContactStore()
    const authStore = useAuthStore()

    return {
      contactStore,
      authStore,
    }
  },
  data() {
    return {
      showContactPopup: false,
      submitSuccess: false,
      isSubmitting: false,
      unreadCount: 0,
      contactForm: {
        name: '',
        email: '',
        subject: '',
        message: '',
      },
      errors: {
        name: '',
        email: '',
        subject: '',
        message: '',
      },
    }
  },
  computed: {
    isAdminPage() {
      const currentPath = this.$route.path
      return currentPath.includes('/dashboard') || currentPath.includes('/admin')
    },
  },
  mounted() {
    // Pre-fill user info if logged in
    if (this.authStore.isLoggedIn) {
      this.contactForm.name = this.authStore.user?.name || ''
      this.contactForm.email = this.authStore.user?.email || ''
    }
  },
  methods: {
    toggleContactPopup() {
      this.showContactPopup = !this.showContactPopup
      if (this.showContactPopup) {
        // Pre-fill user info when opening
        if (this.authStore.isLoggedIn && !this.contactForm.name) {
          this.contactForm.name = this.authStore.user?.name || ''
          this.contactForm.email = this.authStore.user?.email || ''
        }
      }
    },
    closeContactPopup() {
      this.showContactPopup = false
    },
    validateForm() {
      let isValid = true
      this.errors = { name: '', email: '', subject: '', message: '' }

      if (!this.contactForm.name.trim()) {
        this.errors.name = 'Name is required'
        isValid = false
      }

      if (!this.contactForm.email.trim()) {
        this.errors.email = 'Email is required'
        isValid = false
      } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(this.contactForm.email)) {
        this.errors.email = 'Invalid email format'
        isValid = false
      }

      if (!this.contactForm.subject) {
        this.errors.subject = 'Please select a subject'
        isValid = false
      }

      if (!this.contactForm.message.trim()) {
        this.errors.message = 'Message is required'
        isValid = false
      } else if (this.contactForm.message.trim().length < 10) {
        this.errors.message = 'Message must be at least 10 characters'
        isValid = false
      }

      return isValid
    },
    async submitContact() {
      if (!this.validateForm()) {
        Swal.fire({
          icon: 'error',
          title: 'Validation Error',
          text: 'Please fill in all required fields correctly.',
          confirmButtonColor: '#ff3b30',
        })
        return
      }

      this.isSubmitting = true

      try {
        // Simulate API call
        await new Promise((resolve) => setTimeout(resolve, 1500))

        // Add contact message to store
        await this.contactStore.addContactMessage({
          name: this.contactForm.name,
          email: this.contactForm.email,
          subject: this.contactForm.subject,
          message: this.contactForm.message,
          userId: this.authStore.user?.id || null,
        })

        this.submitSuccess = true
      } catch (error) {
        console.error('Contact submission error:', error)
        Swal.fire({
          icon: 'error',
          title: 'Submission Failed',
          text: 'Something went wrong. Please try again.',
          confirmButtonColor: '#007aff',
        })
      } finally {
        this.isSubmitting = false
      }
    },
    resetForm() {
      this.contactForm = {
        name: this.authStore.user?.name || '',
        email: this.authStore.user?.email || '',
        subject: '',
        message: '',
      }
      this.errors = { name: '', email: '', subject: '', message: '' }
      this.submitSuccess = false
    },
  },
}
</script>

<style scoped>
/* Contact Toggle Button */
.contact-toggle-btn {
  position: fixed;
  right: 20px;
  top: 50%;
  transform: translateY(-50%);
  background: linear-gradient(135deg, #007aff, #0051d5);
  color: white;
  border: none;
  width: 50px;
  height: 50px;
  border-radius: 50%;
  font-size: 1.2rem;
  cursor: pointer;
  box-shadow: 0 4px 15px rgba(0, 122, 255, 0.3);
  transition: all 0.3s ease;
  z-index: 1000;
  display: flex;
  align-items: center;
  justify-content: center;
}

.contact-toggle-btn:hover {
  transform: translateY(-50%) scale(1.1);
  box-shadow: 0 6px 20px rgba(0, 122, 255, 0.4);
}

.contact-toggle-btn.active {
  background: linear-gradient(135deg, #0051d5, #003d9e);
}

.notification-badge {
  position: absolute;
  top: -4px;
  right: -4px;
  background: #ff3b30;
  color: white;
  border-radius: 12px;
  width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.7rem;
  font-weight: 700;
  box-shadow: 0 2px 8px rgba(255, 59, 48, 0.4);
  border: 2px solid white;
}

/* Contact Popup */
.contact-popup {
  position: fixed;
  right: -420px;
  top: 50%;
  transform: translateY(-50%);
  width: 380px;
  max-height: 90vh;
  background: white;
  border-radius: 15px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  z-index: 999;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.contact-popup.popup-visible {
  right: 80px;
}

.popup-content {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.popup-header {
  background: linear-gradient(135deg, #007aff, #0051d5);
  color: white;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 12px;
  position: relative;
  flex-shrink: 0;
}

.contact-icon {
  font-size: 1.5rem;
}

.popup-header h4 {
  margin: 0;
  flex-grow: 1;
  font-size: 1.1rem;
  font-weight: 600;
}

.close-btn {
  background: none;
  border: none;
  color: white;
  font-size: 1.5rem;
  cursor: pointer;
  padding: 0;
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  transition: background-color 0.3s ease;
}

.close-btn:hover {
  background: rgba(255, 255, 255, 0.2);
}

.popup-body {
  padding: 25px;
  overflow-y: auto;
  flex: 1;
}

/* Contact Form */
.contact-form {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.form-group label {
  font-size: 0.85rem;
  font-weight: 600;
  color: #333;
}

.form-input,
.form-textarea {
  padding: 12px;
  border: 2px solid #e9ecef;
  border-radius: 10px;
  font-size: 0.9rem;
  transition: all 0.3s ease;
  font-family: inherit;
}

.form-input:focus,
.form-textarea:focus {
  outline: none;
  border-color: #007aff;
  box-shadow: 0 0 0 4px rgba(0, 122, 255, 0.1);
}

.form-input.error,
.form-textarea.error {
  border-color: #ff3b30;
}

.form-textarea {
  resize: vertical;
  min-height: 100px;
}

.error-text {
  color: #ff3b30;
  font-size: 0.75rem;
  margin-top: 2px;
}

.submit-btn {
  background: linear-gradient(135deg, #007aff, #0051d5);
  color: white;
  padding: 14px 20px;
  border: none;
  border-radius: 25px;
  font-size: 0.95rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  margin-top: 8px;
}

.submit-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(0, 122, 255, 0.4);
}

.submit-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.btn-icon {
  font-size: 1.1rem;
}

.spinner {
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

/* Success Message */
.success-message {
  text-align: center;
  padding: 20px 0;
}

.success-icon {
  font-size: 4rem;
  margin-bottom: 16px;
  animation: scaleIn 0.5s ease-out;
}

@keyframes scaleIn {
  from {
    transform: scale(0);
  }
  to {
    transform: scale(1);
  }
}

.success-message h3 {
  color: #34c759;
  margin: 0 0 12px 0;
  font-size: 1.3rem;
}

.success-message p {
  color: #666;
  margin: 0 0 24px 0;
  line-height: 1.6;
}

.new-message-btn {
  background: #f8f9fa;
  color: #007aff;
  padding: 12px 24px;
  border: 2px solid #007aff;
  border-radius: 25px;
  font-size: 0.9rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.new-message-btn:hover {
  background: #007aff;
  color: white;
  transform: translateY(-2px);
}

/* Responsive Design */
@media (max-width: 768px) {
  .contact-popup {
    width: 340px;
    right: -360px;
  }

  .contact-popup.popup-visible {
    right: 70px;
  }

  .contact-toggle-btn {
    right: 15px;
    width: 45px;
    height: 45px;
    font-size: 1.1rem;
  }
}

@media (max-width: 480px) {
  .contact-popup {
    width: 300px;
    right: -320px;
    max-height: 85vh;
  }

  .contact-popup.popup-visible {
    right: 60px;
  }

  .contact-toggle-btn {
    right: 10px;
    width: 40px;
    height: 40px;
    font-size: 1rem;
  }

  .popup-header {
    padding: 15px;
  }

  .popup-body {
    padding: 20px;
  }

  .form-input,
  .form-textarea {
    padding: 10px;
    font-size: 0.85rem;
  }

  .submit-btn {
    padding: 12px 18px;
    font-size: 0.9rem;
  }
}
</style>
