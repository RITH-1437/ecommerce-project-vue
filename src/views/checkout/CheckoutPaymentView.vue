<template>
  <div class="checkout-payment">
    <div class="checkout-container">
      <!-- Progress Bar -->
      <div class="progress-bar">
        <div class="progress-step completed">
          <div class="step-circle">✓</div>
          <span>Cart</span>
        </div>
        <div class="progress-line completed"></div>
        <div class="progress-step active">
          <div class="step-circle">2</div>
          <span>Payment</span>
        </div>
        <div class="progress-line"></div>
        <div class="progress-step">
          <div class="step-circle">3</div>
          <span>Receipt</span>
        </div>
      </div>

      <div class="payment-content">
        <div class="payment-form-section">
          <h2>Payment Details</h2>

          <!-- Payment Methods -->
          <div class="payment-methods">
            <h3>Choose Payment Method</h3>
            <div class="payment-options">
              <label class="payment-option" :class="{ active: paymentMethod === 'card' }">
                <input type="radio" v-model="paymentMethod" value="card" />
                <div class="option-content">
                  <span class="option-icon">💳</span>
                  <span class="option-text">Credit/Debit Card</span>
                </div>
              </label>

              <label class="payment-option" :class="{ active: paymentMethod === 'paypal' }">
                <input type="radio" v-model="paymentMethod" value="paypal" />
                <div class="option-content">
                  <span class="option-icon">🔵</span>
                  <span class="option-text">PayPal</span>
                </div>
              </label>

              <label class="payment-option" :class="{ active: paymentMethod === 'applepay' }">
                <input type="radio" v-model="paymentMethod" value="applepay" />
                <div class="option-content">
                  <span class="option-icon">🍎</span>
                  <span class="option-text">Apple Pay</span>
                </div>
              </label>
            </div>
          </div>

          <!-- Card Details Form (shown when card is selected) -->
          <div v-if="paymentMethod === 'card'" class="card-form">
            <div class="form-group">
              <label>Card Number</label>
              <input
                type="text"
                v-model="cardDetails.number"
                placeholder="1234 5678 9012 3456"
                @input="formatCardNumber"
                maxlength="19"
              />
            </div>

            <div class="form-row">
              <div class="form-group">
                <label>Expiry Date</label>
                <input
                  type="text"
                  v-model="cardDetails.expiry"
                  placeholder="MM/YY"
                  @input="formatExpiry"
                  maxlength="5"
                />
              </div>

              <div class="form-group">
                <label>CVV</label>
                <input type="text" v-model="cardDetails.cvv" placeholder="123" maxlength="4" />
              </div>
            </div>

            <div class="form-group">
              <label>Cardholder Name</label>
              <input type="text" v-model="cardDetails.name" placeholder="John Doe" />
            </div>
          </div>

          <!-- Billing Address -->
          <div class="billing-address">
            <h3>Billing Address</h3>
            <div class="form-group">
              <label>Full Name</label>
              <input type="text" v-model="billingAddress.fullName" placeholder="John Doe" />
            </div>

            <div class="form-group">
              <label>Street Address</label>
              <input type="text" v-model="billingAddress.street" placeholder="123 Main Street" />
            </div>

            <div class="form-row">
              <div class="form-group">
                <label>City</label>
                <input type="text" v-model="billingAddress.city" placeholder="New York" />
              </div>

              <div class="form-group">
                <label>State</label>
                <input type="text" v-model="billingAddress.state" placeholder="NY" />
              </div>

              <div class="form-group">
                <label>ZIP Code</label>
                <input type="text" v-model="billingAddress.zip" placeholder="10001" />
              </div>
            </div>

            <div class="form-group">
              <label>Country</label>
              <select v-model="billingAddress.country">
                <option value="US">United States</option>
                <option value="CA">Canada</option>
                <option value="UK">United Kingdom</option>
                <option value="AU">Australia</option>
                <option value="DE">Germany</option>
                <option value="FR">France</option>
                <option value="IT">Italy</option>
                <option value="ES">Spain</option>
                <option value="NL">Netherlands</option>
                <option value="JP">Japan</option>
                <option value="KR">South Korea</option>
                <option value="SG">Singapore</option>
                <option value="IN">India</option>
                <option value="KH">Cambodia</option>
                <option value="BR">Brazil</option>
                <option value="MX">Mexico</option>
              </select>
            </div>
          </div>
        </div>

        <!-- Order Summary Sidebar -->
        <div class="order-summary-section">
          <div class="order-summary">
            <h3>Order Summary</h3>

            <div class="cart-items">
              <div v-for="item in cartItems" :key="item.id" class="cart-item">
                <div class="item-info">
                  <span class="item-emoji">{{ item.image }}</span>
                  <div class="item-details">
                    <h4>{{ item.name }}</h4>
                    <p class="item-price">{{ item.price }} × {{ item.quantity }}</p>
                  </div>
                </div>
                <div class="item-total">
                  {{ formatPrice(parsePrice(item.price) * item.quantity) }}
                </div>
              </div>
            </div>

            <div class="order-totals">
              <div class="total-row">
                <span>Subtotal</span>
                <span>{{ formatPrice(subtotal) }}</span>
              </div>
              <div class="total-row">
                <span>Shipping</span>
                <span>{{ shipping === 0 ? 'FREE' : formatPrice(shipping) }}</span>
              </div>
              <div class="total-row">
                <span>Tax</span>
                <span>{{ formatPrice(tax) }}</span>
              </div>
              <div class="total-row total">
                <span>Total</span>
                <span>{{ formatPrice(total) }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Navigation Buttons -->
      <div class="checkout-navigation">
        <button class="nav-button secondary" @click="goBackToCart">← Back to Cart</button>
        <button class="nav-button primary" @click="processPayment" :disabled="!isFormValid">
          {{ paymentMethod === 'card' ? 'Pay Now' : `Pay with ${getPaymentMethodName()}` }}
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import { useCartStore } from '@/stores/counter'
import { computed, ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

export default {
  name: 'CheckoutPaymentView',
  setup() {
    const cartStore = useCartStore()
    const router = useRouter()

    const paymentMethod = ref('card')
    const cardDetails = ref({
      number: '',
      expiry: '',
      cvv: '',
      name: '',
    })

    const billingAddress = ref({
      fullName: '',
      street: '',
      city: '',
      state: '',
      zip: '',
      country: 'US',
    })

    // Computed properties
    const cartItems = computed(() => cartStore.items)
    const subtotal = computed(() => cartStore.totalPrice)
    const shipping = computed(() => (subtotal.value > 100 ? 0 : 9.99))
    const tax = computed(() => subtotal.value * 0.08875) // NY tax rate
    const total = computed(() => subtotal.value + shipping.value + tax.value)

    const isFormValid = computed(() => {
      if (paymentMethod.value === 'card') {
        return (
          cardDetails.value.number &&
          cardDetails.value.expiry &&
          cardDetails.value.cvv &&
          cardDetails.value.name &&
          billingAddress.value.fullName &&
          billingAddress.value.street &&
          billingAddress.value.city &&
          billingAddress.value.state &&
          billingAddress.value.zip
        )
      }
      return (
        billingAddress.value.fullName &&
        billingAddress.value.street &&
        billingAddress.value.city &&
        billingAddress.value.state &&
        billingAddress.value.zip
      )
    })

    // Methods
    const parsePrice = (priceString) => {
      return parseFloat(priceString.replace('$', '').replace(',', ''))
    }

    const formatPrice = (price) => {
      return `$${price.toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,')}`
    }

    const formatCardNumber = () => {
      // Remove all non-digits
      let value = cardDetails.value.number.replace(/\s+/g, '').replace(/[^0-9]/gi, '')

      // Add spaces every 4 digits
      let matches = value.match(/\d{4,16}/g)
      let match = (matches && matches[0]) || ''
      let parts = []

      for (let i = 0, len = match.length; i < len; i += 4) {
        parts.push(match.substring(i, i + 4))
      }

      if (parts.length) {
        cardDetails.value.number = parts.join(' ')
      } else {
        cardDetails.value.number = value
      }
    }

    const formatExpiry = () => {
      let value = cardDetails.value.expiry.replace(/\D/g, '')
      if (value.length >= 2) {
        value = value.substring(0, 2) + '/' + value.substring(2, 4)
      }
      cardDetails.value.expiry = value
    }

    const getPaymentMethodName = () => {
      const names = {
        paypal: 'PayPal',
        applepay: 'Apple Pay',
        card: 'Card',
      }
      return names[paymentMethod.value] || 'Card'
    }

    const goBackToCart = () => {
      router.push('/checkout/cart')
    }

    const processPayment = () => {
      if (!isFormValid.value) {
        alert('Please fill in all required fields')
        return
      }

      // Simulate payment processing
      const orderData = {
        items: cartItems.value,
        paymentMethod: paymentMethod.value,
        billingAddress: billingAddress.value,
        totals: {
          subtotal: subtotal.value,
          shipping: shipping.value,
          tax: tax.value,
          total: total.value,
        },
        orderDate: new Date().toISOString(),
        orderNumber: 'APL' + Date.now().toString().slice(-8),
      }

      // Store order data in sessionStorage for receipt page
      sessionStorage.setItem('orderData', JSON.stringify(orderData))

      // Clear the cart
      cartStore.clearCart()

      // Navigate to receipt
      router.push('/checkout/receipt')
    }

    // Check if cart is empty on mount
    onMounted(() => {
      if (cartStore.items.length === 0) {
        router.push('/checkout/cart')
      }
    })

    return {
      paymentMethod,
      cardDetails,
      billingAddress,
      cartItems,
      subtotal,
      shipping,
      tax,
      total,
      isFormValid,
      parsePrice,
      formatPrice,
      formatCardNumber,
      formatExpiry,
      getPaymentMethodName,
      goBackToCart,
      processPayment,
    }
  },
}
</script>

<style scoped>
.checkout-payment {
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  padding: 2rem 1rem;
}

.checkout-container {
  max-width: 1200px;
  margin: 0 auto;
}

/* Progress Bar */
.progress-bar {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 3rem;
  background: white;
  border-radius: 15px;
  padding: 2rem;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
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

/* Payment Content */
.payment-content {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 2rem;
  margin-bottom: 2rem;
}

.payment-form-section {
  background: white;
  border-radius: 15px;
  padding: 2rem;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.payment-form-section h2 {
  margin: 0 0 2rem 0;
  color: #333;
  font-size: 1.5rem;
}

.payment-form-section h3 {
  margin: 2rem 0 1rem 0;
  color: #333;
  font-size: 1.2rem;
}

/* Payment Methods */
.payment-methods {
  margin-bottom: 2rem;
}

.payment-options {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.payment-option {
  display: flex;
  align-items: center;
  padding: 1rem;
  border: 2px solid #e9ecef;
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.payment-option:hover {
  border-color: #007aff;
  background: #f8f9ff;
}

.payment-option.active {
  border-color: #007aff;
  background: #f8f9ff;
}

.payment-option input[type='radio'] {
  margin-right: 1rem;
}

.option-content {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.option-icon {
  font-size: 1.5rem;
}

.option-text {
  font-weight: 500;
  color: #333;
}

/* Form Styles */
.card-form {
  margin-top: 1.5rem;
}

.form-group {
  margin-bottom: 1.5rem;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 500;
  color: #333;
}

.form-group input,
.form-group select {
  width: 100%;
  padding: 0.75rem;
  border: 2px solid #e9ecef;
  border-radius: 8px;
  font-size: 1rem;
  transition: border-color 0.3s ease;
}

.form-group input:focus,
.form-group select:focus {
  outline: none;
  border-color: #007aff;
}

/* Order Summary */
.order-summary-section {
  position: sticky;
  top: 2rem;
  height: fit-content;
}

.order-summary {
  background: white;
  border-radius: 15px;
  padding: 2rem;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.order-summary h3 {
  margin: 0 0 1.5rem 0;
  color: #333;
  font-size: 1.3rem;
}

.cart-items {
  margin-bottom: 1.5rem;
}

.cart-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem 0;
  border-bottom: 1px solid #f1f1f1;
}

.cart-item:last-child {
  border-bottom: none;
}

.item-info {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.item-emoji {
  font-size: 2rem;
}

.item-details h4 {
  margin: 0 0 0.25rem 0;
  font-size: 1rem;
  color: #333;
}

.item-details .item-price {
  margin: 0;
  font-size: 0.875rem;
  color: #666;
}

.item-total {
  font-weight: 600;
  color: #007aff;
}

.order-totals {
  border-top: 2px solid #f1f1f1;
  padding-top: 1rem;
}

.total-row {
  display: flex;
  justify-content: space-between;
  margin-bottom: 0.5rem;
  color: #666;
}

.total-row.total {
  font-size: 1.2rem;
  font-weight: 700;
  color: #333;
  border-top: 1px solid #e9ecef;
  padding-top: 0.5rem;
  margin-top: 0.5rem;
}

/* Navigation */
.checkout-navigation {
  display: flex;
  justify-content: space-between;
  gap: 1rem;
}

.nav-button {
  padding: 1rem 2rem;
  border: none;
  border-radius: 10px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  min-width: 150px;
}

.nav-button.secondary {
  background: #6c757d;
  color: white;
}

.nav-button.secondary:hover {
  background: #5a6268;
  transform: translateY(-2px);
}

.nav-button.primary {
  background: linear-gradient(135deg, #007aff 0%, #0051d0 100%);
  color: white;
}

.nav-button.primary:hover:not(:disabled) {
  background: linear-gradient(135deg, #0051d0 0%, #003a9b 100%);
  transform: translateY(-2px);
}

.nav-button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  transform: none;
}

/* Responsive Design */
@media (max-width: 768px) {
  .checkout-payment {
    padding: 1rem;
  }

  .progress-bar {
    padding: 1rem;
  }

  .progress-line {
    margin: 0 1rem;
  }

  .payment-content {
    grid-template-columns: 1fr;
    gap: 1rem;
  }

  .form-row {
    grid-template-columns: 1fr;
  }

  .checkout-navigation {
    flex-direction: column;
  }

  .nav-button {
    min-width: auto;
  }
}
</style>
