<template>
  <div class="checkout-page">
    <!-- Progress Bar Card -->
    <div class="progress-section">
      <div class="container">
        <div class="progress-card">
          <div class="progress-step active">
            <div class="step-circle">1</div>
            <span>Cart</span>
          </div>
          <div class="progress-line"></div>
          <div class="progress-step">
            <div class="step-circle">2</div>
            <span>Payment</span>
          </div>
          <div class="progress-line"></div>
          <div class="progress-step">
            <div class="step-circle">3</div>
            <span>Receipt</span>
          </div>
        </div>
      </div>
    </div>

    <!-- Cart Content -->
    <main class="checkout-main">
      <div class="container">
        <div class="cart-layout">
          <!-- Cart Items -->
          <div class="cart-section">
            <div v-if="cartStore.items.length === 0" class="empty-cart">
              <div class="empty-cart-icon">🛒</div>
              <h2>Your cart is empty</h2>
              <p>Add some amazing Apple products to get started!</p>
              <router-link to="/" class="continue-shopping-btn">Continue Shopping</router-link>
            </div>

            <div v-else class="cart-items">
              <div v-for="item in cartStore.items" :key="item.id" class="cart-item">
                <div class="item-image">
                  <img v-if="item.image" :src="item.image" :alt="item.name" />
                  <div v-else class="placeholder">📱</div>
                </div>

                <div class="item-details">
                  <h3 class="item-name">{{ item.name }}</h3>
                  <p class="item-description" v-if="item.description">{{ item.description }}</p>
                  <div class="item-colors" v-if="item.colors && item.colors.length > 0">
                    <span>Color:</span>
                    <div class="color-options">
                      <div
                        v-for="color in item.colors"
                        :key="color"
                        class="color-dot"
                        :class="{ selected: item.selectedColor === color }"
                        :style="{ backgroundColor: color }"
                        @click="selectColor(item.id, color)"
                        :title="getColorName(color)"
                      ></div>
                    </div>
                  </div>
                  <button @click="showItemDetails(item)" class="learn-more-btn">Details</button>
                </div>

                <div class="item-quantity">
                  <button
                    @click="decreaseQuantity(item.id)"
                    class="quantity-btn"
                    :disabled="item.quantity <= 1"
                  >
                    −
                  </button>
                  <span class="quantity">{{ item.quantity }}</span>
                  <button @click="increaseQuantity(item.id)" class="quantity-btn">+</button>
                </div>

                <div class="item-price">
                  <span class="price">{{ item.price }}</span>
                  <button @click="removeItem(item.id)" class="remove-btn">🗑️</button>
                </div>
              </div>
            </div>
          </div>

          <!-- Order Summary -->
          <div v-if="cartStore.items.length > 0" class="summary-section">
            <div class="order-summary">
              <h3>Order Summary</h3>

              <!-- Coupon Section -->
              <div class="coupon-section">
                <h4>Promo Code</h4>
                <div class="coupon-input-group">
                  <input
                    type="text"
                    v-model="couponCode"
                    placeholder="Enter promo code"
                    class="coupon-input"
                    @keyup.enter="applyCoupon"
                  />
                  <button
                    @click="applyCoupon"
                    class="apply-coupon-btn"
                    :disabled="!couponCode.trim()"
                  >
                    Apply
                  </button>
                </div>
                <div v-if="appliedCoupon" class="applied-coupon">
                  <span class="coupon-name">{{ appliedCoupon.code }}</span>
                  <span class="coupon-discount">-{{ appliedCoupon.discount }}%</span>
                  <button @click="removeCoupon" class="remove-coupon">×</button>
                </div>
              </div>

              <div class="summary-row">
                <span>Subtotal ({{ cartStore.itemCount }} items)</span>
                <span>${{ cartStore.totalPrice.toFixed(2) }}</span>
              </div>

              <div class="summary-row">
                <span>Shipping</span>
                <span>Free</span>
              </div>

              <div v-if="appliedCoupon" class="summary-row discount">
                <span>Discount ({{ appliedCoupon.code }})</span>
                <span>-${{ discountAmount.toFixed(2) }}</span>
              </div>

              <div class="summary-row">
                <span>Tax</span>
                <span>${{ taxAmount.toFixed(2) }}</span>
              </div>

              <hr class="summary-divider" />

              <div class="summary-row total">
                <span>Total</span>
                <span>${{ finalTotal.toFixed(2) }}</span>
              </div>

              <button @click="proceedToPayment" class="checkout-btn">
                Proceed to Payment
                <span class="arrow">→</span>
              </button>

              <div class="security-info">
                <span class="security-icon">🔒</span>
                <span>Secure checkout with 256-bit SSL encryption</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>

    <!-- Item Details Modal -->
    <div v-if="showModal" class="modal-overlay" @click="closeModal">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h2>{{ selectedItem?.name }}</h2>
          <button @click="closeModal" class="close-btn">×</button>
        </div>
        <div class="modal-body">
          <div class="modal-image">
            <img v-if="selectedItem?.image" :src="selectedItem.image" :alt="selectedItem.name" />
            <div v-else class="placeholder">{{ selectedItem?.image || '📱' }}</div>
          </div>
          <div class="modal-details">
            <p class="modal-price">{{ selectedItem?.price }}</p>
            <div class="modal-specs">
              <h4>Product Details:</h4>
              <ul>
                <li><strong>Model:</strong> {{ selectedItem?.name }}</li>
                <li><strong>Price:</strong> {{ selectedItem?.price }}</li>
                <li v-if="selectedItem?.rating">
                  <strong>Rating:</strong> {{ selectedItem.rating }}/5 ({{ selectedItem.reviews }}
                  reviews)
                </li>
                <li v-if="selectedItem?.colors">
                  <strong>Available Colors:</strong> {{ selectedItem.colors?.length }} options
                </li>
                <li v-if="selectedItem?.badge"><strong>Badge:</strong> {{ selectedItem.badge }}</li>
              </ul>
            </div>
            <div class="modal-description">
              <p>{{ getItemDescription(selectedItem) }}</p>
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <button @click="closeModal" class="modal-btn secondary">Close</button>
          <button @click="addMoreToCart(selectedItem)" class="modal-btn primary">
            Add Another to Cart
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { useCartStore } from '@/stores/counter.js'
import Swal from 'sweetalert2'

export default {
  name: 'CheckoutCartView',
  setup() {
    const cartStore = useCartStore()

    return {
      cartStore,
    }
  },
  data() {
    return {
      couponCode: '',
      appliedCoupon: null,
      showModal: false,
      selectedItem: null,
      availableCoupons: {
        'Miss-me': { code: 'Miss-me', discount: 10, description: 'Miss Me 10% Off' },
        'Love-you': { code: 'Love-you', discount: 15, description: 'Love You 15% Off' },
        'Kiss-one': { code: 'Kiss-one', discount: 20, description: 'Kiss One 20% Off' },
      },
    }
  },
  computed: {
    subtotalAfterDiscount() {
      return this.appliedCoupon
        ? this.cartStore.totalPrice * (1 - this.appliedCoupon.discount / 100)
        : this.cartStore.totalPrice
    },
    discountAmount() {
      return this.appliedCoupon
        ? this.cartStore.totalPrice * (this.appliedCoupon.discount / 100)
        : 0
    },
    taxAmount() {
      return this.subtotalAfterDiscount * 0.1
    },
    finalTotal() {
      return this.subtotalAfterDiscount + this.taxAmount
    },
  },
  methods: {
    increaseQuantity(productId) {
      const item = this.cartStore.items.find((item) => item.id === productId)
      if (item) {
        this.cartStore.updateQuantity(productId, item.quantity + 1)
      }
    },
    decreaseQuantity(productId) {
      const item = this.cartStore.items.find((item) => item.id === productId)
      if (item && item.quantity > 1) {
        this.cartStore.updateQuantity(productId, item.quantity - 1)
      }
    },
    removeItem(productId) {
      const item = this.cartStore.items.find((i) => i.id === productId)
      if (!item) return

      Swal.fire({
        title: 'Remove Item?',
        html: `
          <div style="text-align: left; padding: 10px;">
            <p style="margin-bottom: 15px;">Are you sure you want to remove this item from your cart?</p>
            <div style="background: #f8f9fa; padding: 15px; border-radius: 8px; display: flex; align-items: center; gap: 12px;">
              <div style="font-size: 2rem;">${item.image || '📱'}</div>
              <div>
                <h4 style="margin: 0; font-size: 1rem; color: #212529;">${item.name}</h4>
                <p style="margin: 5px 0 0 0; color: #6c757d; font-size: 0.9rem;">${item.price} × ${item.quantity}</p>
              </div>
            </div>
          </div>
        `,
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#dc3545',
        cancelButtonColor: '#6c757d',
        confirmButtonText: '🗑️ Remove',
        cancelButtonText: 'Keep it',
      }).then((result) => {
        if (result.isConfirmed) {
          this.cartStore.removeItem(productId)
          Swal.fire({
            icon: 'success',
            title: 'Removed!',
            text: 'Item has been removed from your cart.',
            timer: 1500,
            showConfirmButton: false,
          })
        }
      })
    },
    applyCoupon() {
      const code = this.couponCode.trim()
      if (!code) {
        Swal.fire({
          icon: 'warning',
          title: 'Empty Code',
          text: 'Please enter a coupon code.',
          confirmButtonColor: '#0066cc',
          confirmButtonText: 'OK',
        })
        return
      }

      const coupon = this.availableCoupons[code]
      if (coupon) {
        this.appliedCoupon = coupon
        this.couponCode = ''
        Swal.fire({
          icon: 'success',
          title: 'Coupon Applied!',
          html: `
            <div style="text-align: center;">
              <p style="font-size: 1.1rem; margin: 10px 0;">🎉 <strong>${coupon.code}</strong></p>
              <p style="color: #28a745; font-weight: 600; font-size: 1.2rem;">${coupon.discount}% OFF</p>
              <p style="color: #86868b; font-size: 0.9rem;">Discount applied to your order</p>
            </div>
          `,
          confirmButtonColor: '#28a745',
          confirmButtonText: 'Great!',
          timer: 2000,
        })
      } else {
        Swal.fire({
          icon: 'error',
          title: 'Invalid Coupon Code',
          html: `
            <div style="text-align: center;">
              <p style="color: #ff4d4f; font-size: 1.1rem; margin: 15px 0;">❌ Code "<strong>${code}</strong>" is not valid</p>
              <p style="color: #86868b; font-size: 0.95rem;">Please check your code and try again.</p>
            </div>
          `,
          confirmButtonColor: '#ff4d4f',
          confirmButtonText: 'Try Again',
        })
        this.couponCode = ''
      }
    },
    showItemDetails(item) {
      this.selectedItem = item
      this.showModal = true
    },
    closeModal() {
      this.showModal = false
      this.selectedItem = null
    },
    addMoreToCart(item) {
      this.cartStore.addItem(item)
      this.closeModal()
    },
    getItemDescription(item) {
      if (!item) return ''

      const descriptions = {
        'iPhone 15 Pro Max':
          'The most advanced iPhone yet, featuring titanium design, A17 Pro chip, and professional camera system.',
        'iPhone 15':
          'All-new iPhone with Dynamic Island, 48MP Main camera, and USB-C connectivity.',
        'MacBook Air M3':
          'Supercharged by the M3 chip, featuring up to 18 hours of battery life and stunning Liquid Retina display.',
        'MacBook Pro':
          'Pro performance with M3 chip, advanced thermal design, and studio-quality microphones.',
        'AirPods Pro (2nd gen)':
          'Personalized Spatial Audio with dynamic head tracking and Adaptive Transparency.',
        'Apple Watch Series 9':
          'Advanced health and fitness features with the revolutionary S9 chip and Double Tap gesture.',
        'iPad Pro 12.9"':
          'The ultimate iPad experience with M2 chip, Liquid Retina XDR display, and Apple Pencil support.',
      }

      return (
        descriptions[item.name] ||
        'Premium Apple product with cutting-edge technology and exceptional build quality.'
      )
    },
    removeCoupon() {
      this.appliedCoupon = null
      this.couponCode = ''
    },
    selectColor(productId, color) {
      const item = this.cartStore.items.find((i) => i.id === productId)
      if (item) {
        item.selectedColor = color
        Swal.fire({
          icon: 'success',
          title: 'Color Updated!',
          text: `Color changed to ${this.getColorName(color)}`,
          timer: 1500,
          showConfirmButton: false,
        })
      }
    },
    getColorName(color) {
      const colorNames = {
        '#000000': 'Black',
        '#1d1d1f': 'Space Black',
        '#4a4a4a': 'Space Gray',
        '#8e8e93': 'Gray',
        '#c0c0c0': 'Silver',
        '#ffffff': 'White',
        '#f5f5f7': 'Starlight',
        '#faf0e6': 'Light',
        '#e8d7c3': 'Gold',
        '#fad7a0': 'Desert Gold',
        '#b8a88f': 'Natural Titanium',
        '#d4c5b9': 'Sand',
        '#e1f5ff': 'Sky Blue',
        '#007aff': 'Blue',
        '#003d7a': 'Pacific Blue',
        '#1c3d5a': 'Deep Blue',
        '#7d98a1': 'Blue Titanium',
        '#006d5b': 'Alpine Green',
        '#004d3c': 'Midnight Green',
        '#405e54': 'Green',
        '#c7a27c': 'Beige',
        '#f9e5c9': 'Cream',
        '#ffc0cb': 'Pink',
        '#ffb3c1': 'Rose',
        '#e8b4b8': 'Rose Gold',
        '#d4af37': 'Gold',
        '#ff9500': 'Orange',
        '#ff3b30': 'Red',
        '#c41e3a': 'Deep Red',
        '#8b0000': 'Dark Red',
        '#800020': 'Burgundy',
        '#6e3667': 'Purple',
        '#5e17eb': 'Deep Purple',
        '#4a0e4e': 'Violet',
      }
      return colorNames[color.toLowerCase()] || 'Custom'
    },
    proceedToPayment() {
      // Store coupon data for payment page
      if (this.appliedCoupon) {
        sessionStorage.setItem('appliedCoupon', JSON.stringify(this.appliedCoupon))
      }
      this.$router.push('/checkout/payment')
    },
  },
}
</script>

<style scoped>
.checkout-page {
  min-height: 100vh;
  background: #f5f5f7;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
}

.checkout-header {
  background: white;
  border-bottom: 1px solid #d2d2d7;
  padding: 20px 0;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.checkout-header .container {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.back-link {
  color: #0066cc;
  text-decoration: none;
  font-weight: 500;
  transition: color 0.3s ease;
}

.back-link:hover {
  color: #0077ed;
}

.checkout-title {
  font-size: 2rem;
  font-weight: 700;
  color: #1d1d1f;
  margin: 0;
}

/* Progress Section */
.progress-section {
  background: #f5f5f7;
  padding: 40px 0;
}

.progress-card {
  display: flex;
  align-items: center;
  justify-content: center;
  background: white;
  border-radius: 20px;
  padding: 2rem;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.08);
  border: 1px solid rgba(0, 0, 0, 0.06);
}

.progress-step {
  display: flex;
  flex-direction: column;
  align-items: center;
  color: #999;
  transition: all 0.3s ease;
}

.progress-step.completed {
  color: #28a745;
}

.progress-step.active {
  color: #007aff;
}

.step-circle {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: #e9ecef;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  margin-bottom: 0.5rem;
  transition: all 0.3s ease;
}

.progress-step.completed .step-circle {
  background: #28a745;
  color: white;
}

.progress-step.active .step-circle {
  background: #007aff;
  color: white;
}

.progress-line {
  flex: 1;
  height: 3px;
  background: #e9ecef;
  margin: 0 2rem;
  border-radius: 2px;
  transition: all 0.3s ease;
}

.progress-line.completed {
  background: #28a745;
}

.checkout-main {
  padding: 40px 0;
}

.cart-layout {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 40px;
  align-items: start;
}

/* Empty Cart */
.empty-cart {
  text-align: center;
  padding: 80px 20px;
  background: white;
  border-radius: 20px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
}

.empty-cart-icon {
  font-size: 4rem;
  margin-bottom: 20px;
}

.empty-cart h2 {
  color: #1d1d1f;
  margin-bottom: 10px;
}

.empty-cart p {
  color: #86868b;
  margin-bottom: 30px;
}

.continue-shopping-btn {
  display: inline-block;
  background: #0066cc;
  color: white;
  padding: 12px 24px;
  border-radius: 25px;
  text-decoration: none;
  font-weight: 600;
  transition: background 0.3s ease;
}

.continue-shopping-btn:hover {
  background: #0077ed;
}

/* Cart Items */
.cart-items {
  background: white;
  border-radius: 20px;
  padding: 30px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
}

.cart-item {
  display: grid;
  grid-template-columns: 100px 1fr auto auto;
  gap: 20px;
  align-items: center;
  padding: 20px 0;
  border-bottom: 1px solid #f5f5f7;
}

.cart-item:last-child {
  border-bottom: none;
}

.item-image {
  width: 80px;
  height: 80px;
  background: #f5f5f7;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.item-image img {
  width: 100%;
  height: 100%;
  object-fit: contain;
  border-radius: 12px;
}

.placeholder {
  font-size: 2rem;
  opacity: 0.5;
}

.item-details h3 {
  color: #1d1d1f;
  margin-bottom: 5px;
  font-size: 1.1rem;
}

.item-description {
  color: #86868b;
  font-size: 0.9rem;
  margin-bottom: 10px;
}

.item-colors {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 0.9rem;
  color: #86868b;
}

.color-options {
  display: flex;
  gap: 4px;
}

.color-dot {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  border: 2px solid #e5e5e7;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
}

.color-dot:hover {
  transform: scale(1.15);
  border-color: #007aff;
  box-shadow: 0 2px 8px rgba(0, 122, 255, 0.3);
}

.color-dot.selected {
  border: 3px solid #007aff;
  box-shadow: 0 0 0 2px rgba(0, 122, 255, 0.2);
  transform: scale(1.1);
}

.color-dot.selected::after {
  content: '✓';
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  color: white;
  font-size: 12px;
  font-weight: bold;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.5);
}

.item-quantity {
  display: flex;
  align-items: center;
  gap: 10px;
  background: #f5f5f7;
  padding: 8px;
  border-radius: 8px;
}

.quantity-btn {
  width: 32px;
  height: 32px;
  border: none;
  background: white;
  border-radius: 6px;
  font-size: 1.2rem;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: background 0.3s ease;
}

.quantity-btn:hover:not(:disabled) {
  background: #e5e5e7;
}

.quantity-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.quantity {
  min-width: 30px;
  text-align: center;
  font-weight: 600;
}

.item-price {
  text-align: right;
}

.price {
  font-size: 1.2rem;
  font-weight: 600;
  color: #0066cc;
  display: block;
  margin-bottom: 10px;
}

.remove-btn {
  background: none;
  border: none;
  font-size: 1.2rem;
  cursor: pointer;
  padding: 8px;
  border-radius: 6px;
  transition: background 0.3s ease;
}

.remove-btn:hover {
  background: #ffe6e6;
}

/* Order Summary */
.summary-section {
  position: sticky;
  top: 20px;
}

.order-summary {
  background: white;
  border-radius: 20px;
  padding: 30px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
}

.order-summary h3 {
  color: #1d1d1f;
  margin-bottom: 20px;
  font-size: 1.3rem;
}

.summary-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
  color: #86868b;
}

.summary-row.total {
  color: #1d1d1f;
  font-weight: 700;
  font-size: 1.2rem;
}

.summary-divider {
  border: none;
  height: 1px;
  background: #e5e5e7;
  margin: 20px 0;
}

.checkout-btn {
  width: 100%;
  background: linear-gradient(135deg, #0066cc, #0077ed);
  color: white;
  border: none;
  padding: 16px 24px;
  border-radius: 25px;
  font-size: 1.1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  margin-bottom: 20px;
}

.checkout-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(0, 102, 204, 0.4);
}

.arrow {
  transition: transform 0.3s ease;
}

.checkout-btn:hover .arrow {
  transform: translateX(4px);
}

.security-info {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  color: #86868b;
  font-size: 0.9rem;
}

.security-icon {
  color: #34c759;
}

/* Coupon Styles */
.coupon-section {
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid #f5f5f7;
}

.coupon-section h4 {
  margin: 0 0 10px 0;
  color: #1d1d1f;
  font-size: 1rem;
}

.coupon-input-group {
  display: flex;
  gap: 8px;
  margin-bottom: 10px;
}

.coupon-input {
  flex: 1;
  padding: 10px 12px;
  border: 2px solid #e5e5e7;
  border-radius: 8px;
  font-size: 14px;
  outline: none;
  transition: border-color 0.3s ease;
}

.coupon-input:focus {
  border-color: #0066cc;
}

.apply-coupon-btn {
  background: #0066cc;
  color: white;
  border: none;
  padding: 10px 16px;
  border-radius: 8px;
  font-weight: 600;
  cursor: pointer;
  transition: background 0.3s ease;
  white-space: nowrap;
}

.apply-coupon-btn:hover:not(:disabled) {
  background: #0077ed;
}

.apply-coupon-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.applied-coupon {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  background: #e6f7ff;
  border: 1px solid #91d5ff;
  border-radius: 8px;
  font-size: 14px;
}

.coupon-name {
  font-weight: 600;
  color: #0066cc;
}

.coupon-discount {
  color: #28a745;
  font-weight: 600;
}

.remove-coupon {
  background: none;
  border: none;
  color: #ff4d4f;
  font-size: 16px;
  cursor: pointer;
  padding: 2px;
  line-height: 1;
}

.coupon-error {
  color: #ff4d4f;
  font-size: 12px;
  margin-top: 5px;
}

.summary-row.discount {
  color: #28a745;
  font-weight: 600;
}

/* Learn More Button */
.learn-more-btn {
  background: #0066cc;
  color: white;
  border: none;
  padding: 6px 12px;
  border-radius: 6px;
  font-size: 12px;
  font-weight: 500;
  cursor: pointer;
  transition: background 0.3s ease;
  margin-top: 8px;
}

.learn-more-btn:hover {
  background: #0077ed;
}

/* Modal Styles */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  backdrop-filter: blur(4px);
}

.modal-content {
  background: white;
  border-radius: 20px;
  max-width: 600px;
  max-height: 80vh;
  width: 90%;
  overflow-y: auto;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 30px;
  border-bottom: 1px solid #f5f5f7;
}

.modal-header h2 {
  margin: 0;
  color: #1d1d1f;
  font-size: 1.5rem;
}

.close-btn {
  background: none;
  border: none;
  font-size: 24px;
  color: #86868b;
  cursor: pointer;
  padding: 0;
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  transition: background 0.3s ease;
}

.close-btn:hover {
  background: #f5f5f7;
}

.modal-body {
  padding: 30px;
  display: grid;
  grid-template-columns: 200px 1fr;
  gap: 30px;
  align-items: start;
}

.modal-image {
  width: 100%;
  height: 200px;
  background: #f5f5f7;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.modal-image img {
  width: 100%;
  height: 100%;
  object-fit: contain;
  border-radius: 16px;
}

.modal-image .placeholder {
  font-size: 4rem;
  opacity: 0.5;
}

.modal-details {
  flex: 1;
}

.modal-price {
  font-size: 2rem;
  font-weight: 700;
  color: #0066cc;
  margin: 0 0 20px 0;
}

.modal-specs h4 {
  color: #1d1d1f;
  margin: 0 0 10px 0;
  font-size: 1.1rem;
}

.modal-specs ul {
  list-style: none;
  padding: 0;
  margin: 0 0 20px 0;
}

.modal-specs li {
  padding: 5px 0;
  color: #86868b;
  font-size: 14px;
}

.modal-specs strong {
  color: #1d1d1f;
}

.modal-description {
  color: #86868b;
  line-height: 1.6;
}

.modal-footer {
  display: flex;
  gap: 15px;
  padding: 20px 30px;
  border-top: 1px solid #f5f5f7;
  justify-content: flex-end;
}

.modal-btn {
  padding: 12px 24px;
  border-radius: 25px;
  font-weight: 600;
  cursor: pointer;
  border: none;
  transition: all 0.3s ease;
}

.modal-btn.secondary {
  background: #f5f5f7;
  color: #86868b;
}

.modal-btn.secondary:hover {
  background: #e5e5e7;
  color: #1d1d1f;
}

.modal-btn.primary {
  background: linear-gradient(135deg, #0066cc, #0077ed);
  color: white;
}

.modal-btn.primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(0, 102, 204, 0.4);
}

/* Responsive Design */
@media (max-width: 768px) {
  .checkout-header .container {
    flex-direction: column;
    gap: 15px;
    text-align: center;
  }

  .cart-layout {
    grid-template-columns: 1fr;
    gap: 20px;
  }

  .cart-item {
    grid-template-columns: 80px 1fr;
    gap: 15px;
  }

  .item-quantity,
  .item-price {
    grid-column: 2;
    justify-self: end;
  }

  .item-price {
    margin-top: 10px;
  }

  .progress-section {
    padding: 20px 0;
  }

  .progress-card {
    padding: 1.5rem;
  }

  .progress-line {
    margin: 0 1rem;
  }

  .coupon-input-group {
    flex-direction: column;
  }

  .apply-coupon-btn {
    width: 100%;
  }

  .checkout-title {
    font-size: 1.5rem;
  }

  .modal-content {
    width: 95%;
    max-height: 90vh;
  }

  .modal-body {
    grid-template-columns: 1fr;
    gap: 20px;
    padding: 20px;
  }

  .modal-image {
    height: 150px;
  }

  .modal-footer {
    flex-direction: column;
    padding: 15px 20px;
  }

  .modal-btn {
    width: 100%;
  }
}
</style>
