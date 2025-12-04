<template>
  <div class="receipt-page">
    <!-- Receipt Content -->
    <main class="receipt-main">
      <div class="container">
        <div v-if="orderData" class="receipt-content">
          <!-- Success Message -->
          <div class="success-section">
            <div class="success-animation">
              <div class="checkmark-circle">
                <div class="checkmark">✓</div>
              </div>
            </div>
            <div class="success-content">
              <h2 class="success-title">Payment Successful! 🎉</h2>
              <p class="success-subtitle">Your order is confirmed and being prepared</p>
              <div class="order-badge">
                <span class="badge-label">Order</span>
                <span class="badge-number">#{{ orderData.orderNumber }}</span>
              </div>
            </div>
          </div>

          <!-- Receipt Card -->
          <div class="receipt-card" id="receipt-card">
            <div class="receipt-brand">
              <div class="brand-logo">
                <div class="logo-circle">🍎</div>
                <div class="brand-text">
                  <h3 class="store-name">Apple Store</h3>
                  <p class="store-location">Phnom Penh, Cambodia</p>
                </div>
              </div>
              <div class="receipt-qr">
                <div class="qr-code">📱</div>
                <span class="qr-text">Scan for details</span>
              </div>
            </div>

            <!-- Order Summary Cards -->
            <div class="info-grid">
              <div class="info-card">
                <div class="info-icon">📋</div>
                <div class="info-content">
                  <span class="info-label">Order ID</span>
                  <span class="info-value">{{ orderData.orderNumber }}</span>
                </div>
              </div>
              <div class="info-card">
                <div class="info-icon">📅</div>
                <div class="info-content">
                  <span class="info-label">Date</span>
                  <span class="info-value">{{ formatShortDate(orderData.orderDate) }}</span>
                </div>
              </div>
              <div class="info-card">
                <div class="info-icon">💳</div>
                <div class="info-content">
                  <span class="info-label">Payment</span>
                  <span class="info-value">{{ formatPaymentMethod(orderData.paymentMethod) }}</span>
                </div>
              </div>
            </div>

            <!-- Shipping Information -->
            <div class="shipping-info">
              <h4>Billing Address</h4>
              <div class="address">
                <p>{{ orderData.billingAddress.fullName }}</p>
                <p>{{ orderData.billingAddress.street }}</p>
                <p>
                  {{ orderData.billingAddress.city }}, {{ orderData.billingAddress.state }}
                  {{ orderData.billingAddress.zip }}
                </p>
                <p>{{ getCountryName(orderData.billingAddress.country) }}</p>
              </div>
            </div>

            <hr class="receipt-divider" />

            <!-- Items Section -->
            <div class="items-section">
              <h4 class="section-title">🛍️ Your Items</h4>
              <div class="items-list">
                <div v-for="item in orderData.items" :key="item.id" class="item-card">
                  <div class="item-visual">
                    <div class="item-icon">{{ item.image || '📱' }}</div>
                    <div class="quantity-badge">{{ item.quantity }}×</div>
                  </div>
                  <div class="item-info">
                    <h5 class="item-title">{{ item.name }}</h5>
                    <div class="item-price-info">
                      <span class="unit-price">{{ item.price }} each</span>
                      <span class="item-total"
                        >${{
                          (
                            parseFloat(item.price.replace('$', '').replace(',', '')) * item.quantity
                          ).toFixed(2)
                        }}</span
                      >
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <!-- Order Summary -->
            <div class="summary-section">
              <h4 class="section-title">💰 Payment Summary</h4>
              <div class="summary-card">
                <div class="summary-row">
                  <span class="summary-label">Subtotal</span>
                  <span class="summary-value">${{ orderData.totals.subtotal.toFixed(2) }}</span>
                </div>
                <div class="summary-row">
                  <span class="summary-label">Shipping</span>
                  <span class="summary-value shipping-free" v-if="orderData.totals.shipping === 0"
                    >Free 🚚</span
                  >
                  <span class="summary-value" v-else
                    >${{ orderData.totals.shipping.toFixed(2) }}</span
                  >
                </div>
                <div class="summary-row">
                  <span class="summary-label">Tax</span>
                  <span class="summary-value">${{ orderData.totals.tax.toFixed(2) }}</span>
                </div>
                <div class="summary-divider"></div>
                <div class="summary-row total-row">
                  <span class="summary-label total-label">Total Paid</span>
                  <span class="summary-value total-value"
                    >${{ orderData.totals.total.toFixed(2) }}</span
                  >
                </div>
              </div>
            </div>

            <!-- Order Notes -->
            <div v-if="orderData.notes" class="order-notes">
              <hr class="receipt-divider" />
              <h4>Order Notes</h4>
              <p>{{ orderData.notes }}</p>
            </div>

            <!-- Footer -->
            <div class="receipt-footer">
              <p>Thank you for choosing Apple Store!</p>
              <p>For support, contact us at support@applestore.com</p>
            </div>
          </div>

          <!-- Action Buttons -->
          <div class="action-buttons">
            <button @click="printReceipt" class="print-btn">
              <span class="btn-icon">🖨️</span>
              Print Receipt
            </button>
            <button @click="downloadReceipt" class="download-btn">
              <span class="btn-icon">📄</span>
              Download PDF
            </button>
            <router-link to="/" class="continue-btn">
              <span class="btn-icon">🛍️</span>
              Continue Shopping
            </router-link>
          </div>

          <!-- Estimated Delivery -->
          <div class="delivery-info">
            <div class="delivery-card">
              <div class="delivery-icon">🚚</div>
              <div class="delivery-details">
                <h4>Estimated Delivery</h4>
                <p class="delivery-date">{{ estimatedDelivery }}</p>
                <p class="delivery-note">
                  We'll send you tracking information via email once your order ships.
                </p>
              </div>
            </div>
          </div>
        </div>

        <!-- No Order Found -->
        <div v-else class="no-order">
          <div class="no-order-icon">📋</div>
          <h2>No Order Found</h2>
          <p>We couldn't find any recent order information.</p>
          <router-link to="/" class="home-btn">Back to Store</router-link>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
export default {
  name: 'CheckoutReceiptView',
  data() {
    return {
      orderData: null,
    }
  },
  computed: {
    estimatedDelivery() {
      if (!this.orderData) return ''
      const orderDate = new Date(this.orderData.orderDate)
      const deliveryDate = new Date(orderDate.getTime() + 5 * 24 * 60 * 60 * 1000) // 5 days
      return deliveryDate.toLocaleDateString('en-US', {
        weekday: 'long',
        year: 'numeric',
        month: 'long',
        day: 'numeric',
      })
    },
  },
  methods: {
    formatDate(dateString) {
      const date = new Date(dateString)
      return date.toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
      })
    },
    formatShortDate(dateString) {
      const date = new Date(dateString)
      return date.toLocaleDateString('en-US', {
        month: 'short',
        day: 'numeric',
        year: 'numeric',
      })
    },
    formatPaymentMethod(method) {
      const methods = {
        card: 'Credit Card',
        paypal: 'PayPal',
        applepay: 'Apple Pay',
      }
      return methods[method] || 'Credit Card'
    },
    getCountryName(countryCode) {
      const countries = {
        US: 'United States',
        CA: 'Canada',
        UK: 'United Kingdom',
        AU: 'Australia',
        DE: 'Germany',
        FR: 'France',
        IT: 'Italy',
        ES: 'Spain',
        NL: 'Netherlands',
        JP: 'Japan',
        KR: 'South Korea',
        SG: 'Singapore',
        IN: 'India',
        KH: 'Cambodia',
        BR: 'Brazil',
        MX: 'Mexico',
      }
      return countries[countryCode] || countryCode
    },
    printReceipt() {
      const printContent = document.getElementById('receipt-card')
      const printWindow = window.open('', '_blank')

      printWindow.document.write(`
        <!DOCTYPE html>
        <html>
        <head>
          <title>Order Receipt - ${this.orderData.orderNumber}</title>
          <style>
            body { 
              font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; 
              margin: 20px;
              color: #333;
            }
            .receipt-card { 
              max-width: 600px; 
              margin: 0 auto;
              border: 1px solid #ddd;
              padding: 20px;
            }
            .receipt-header-content { 
              display: flex; 
              justify-content: space-between; 
              align-items: center; 
              margin-bottom: 20px;
            }
            .store-info h3 { margin: 0 0 10px 0; }
            .receipt-divider { 
              border: none; 
              height: 1px; 
              background: #ddd; 
              margin: 20px 0; 
            }
            .detail-row, .total-row { 
              display: flex; 
              justify-content: space-between; 
              margin-bottom: 8px; 
            }
            .table-header, .table-row { 
              display: grid; 
              grid-template-columns: 2fr 1fr 1fr 1fr; 
              gap: 10px; 
              padding: 8px 0;
              border-bottom: 1px solid #eee;
            }
            .table-header { font-weight: bold; }
            .item-info { display: flex; align-items: center; gap: 10px; }
            .item-image { width: 40px; height: 40px; background: #f5f5f5; border-radius: 4px; display: flex; align-items: center; justify-content: center; }
            .item-image img { width: 100%; height: 100%; object-fit: contain; }
            .grand-total { font-weight: bold; font-size: 1.1rem; }
            h4 { margin: 15px 0 10px 0; }
            .logo { font-size: 2rem; }
          </style>
        </head>
        <body>
          ${printContent.outerHTML}
        </body>
        </html>
      `)

      printWindow.document.close()
      printWindow.print()
      printWindow.close()
    },
    downloadReceipt() {
      // Create detailed receipt content
      const content = `APPLE STORE RECEIPT\n${'='.repeat(50)}\n\nOrder Number: ${this.orderData.orderNumber}\nDate: ${this.formatDate(this.orderData.orderDate)}\nPayment Method: ${this.formatPaymentMethod(this.orderData.paymentMethod)}\n\n${'='.repeat(50)}\nITEMS PURCHASED\n${'='.repeat(50)}\n\n${this.orderData.items
        .map((item) => {
          const itemTotal = (
            parseFloat(item.price.replace('$', '').replace(',', '')) * item.quantity
          ).toFixed(2)
          return `${item.name}\nQuantity: ${item.quantity} x ${item.price} = $${itemTotal}\n${'-'.repeat(30)}`
        })
        .join(
          '\n',
        )}\n\n${'='.repeat(50)}\nORDER SUMMARY\n${'='.repeat(50)}\n\nSubtotal: $${this.orderData.totals.subtotal.toFixed(2)}\nShipping: ${this.orderData.totals.shipping === 0 ? 'Free' : '$' + this.orderData.totals.shipping.toFixed(2)}\nTax: $${this.orderData.totals.tax.toFixed(2)}\nTOTAL: $${this.orderData.totals.total.toFixed(2)}\n\n${'='.repeat(50)}\nBILLING ADDRESS\n${'='.repeat(50)}\n\n${this.orderData.billingAddress.fullName}\n${this.orderData.billingAddress.street}\n${this.orderData.billingAddress.city}, ${this.orderData.billingAddress.state} ${this.orderData.billingAddress.zip}\n${this.getCountryName(this.orderData.billingAddress.country)}\n\n${'='.repeat(50)}\n\nThank you for choosing Apple Store!\n\nApple Store\nRussian Conf Norodom Boulevard\nPhnom Penh 120404, Cambodia\nPhone: +855 966 273 314\n\nFor support: support@applestore.com`

      const file = new Blob([content], { type: 'text/plain' })
      const element = document.createElement('a')
      element.href = URL.createObjectURL(file)
      element.download = `Apple-Store-Receipt-${this.orderData.orderNumber}.txt`
      document.body.appendChild(element)
      element.click()
      document.body.removeChild(element)

      // Show confirmation
      alert('Receipt downloaded successfully!')
    },
  },
  mounted() {
    // Load order data from sessionStorage (set by payment page)
    const savedOrder = sessionStorage.getItem('orderData')
    if (savedOrder) {
      this.orderData = JSON.parse(savedOrder)
      // Also save to localStorage for backup
      localStorage.setItem('lastOrder', savedOrder)
    } else {
      // Fallback to localStorage
      const backupOrder = localStorage.getItem('lastOrder')
      if (backupOrder) {
        this.orderData = JSON.parse(backupOrder)
      }
    }
  },
}
</script>

<style scoped>
.receipt-page {
  min-height: 100vh;
  background: #f5f5f7;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
}

.container {
  max-width: 900px;
  margin: 0 auto;
  padding: 0 20px;
}

.receipt-main {
  padding: 40px 0;
}

.receipt-content {
  max-width: 700px;
  margin: 0 auto;
}

/* Success Section */
.success-section {
  text-align: center;
  background: linear-gradient(135deg, #f8f9ff, #ffffff);
  border-radius: 24px;
  padding: 50px;
  margin-bottom: 40px;
  box-shadow: 0 8px 32px rgba(0, 102, 204, 0.1);
  border: 1px solid rgba(0, 102, 204, 0.1);
  position: relative;
  overflow: hidden;
}

.success-section::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(0, 102, 204, 0.05), transparent);
  animation: shimmer 3s infinite;
}

@keyframes shimmer {
  0% {
    left: -100%;
  }
  100% {
    left: 100%;
  }
}

.success-icon {
  font-size: 5rem;
  margin-bottom: 25px;
  background: linear-gradient(135deg, #34c759, #30d158);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  filter: drop-shadow(0 2px 4px rgba(52, 199, 89, 0.3));
  animation: bounce 1s ease-out;
}

@keyframes bounce {
  0%,
  20%,
  53%,
  80%,
  100% {
    transform: translateY(0);
  }
  40%,
  43% {
    transform: translateY(-15px);
  }
  70% {
    transform: translateY(-7px);
  }
}

.success-section h2 {
  color: #1d1d1f;
  margin-bottom: 15px;
  font-size: 2.2rem;
  font-weight: 700;
  letter-spacing: -0.5px;
  position: relative;
  z-index: 1;
}

.success-section p {
  color: #86868b;
  margin-bottom: 25px;
  font-size: 1.2rem;
  font-weight: 500;
  position: relative;
  z-index: 1;
}

.order-number {
  background: linear-gradient(135deg, #0066cc, #0077ed);
  color: white;
  padding: 15px 25px;
  border-radius: 30px;
  display: inline-block;
  font-size: 1.1rem;
  font-weight: 600;
  box-shadow: 0 4px 16px rgba(0, 102, 204, 0.3);
  position: relative;
  z-index: 1;
}

/* Receipt Card */
.receipt-card {
  background: white;
  border-radius: 24px;
  padding: 45px;
  margin-bottom: 40px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.08);
  border: 1px solid rgba(0, 0, 0, 0.06);
  transition:
    transform 0.3s ease,
    box-shadow 0.3s ease;
  position: relative;
}

.receipt-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.12);
}

.receipt-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: linear-gradient(135deg, #0066cc, #34c759, #ff6b6b);
  border-radius: 24px 24px 0 0;
}

.receipt-header-content {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 20px;
}

.store-info h3 {
  color: #1d1d1f;
  margin: 0 0 10px 0;
  font-size: 1.5rem;
}

.store-info p {
  color: #86868b;
  margin: 5px 0;
  line-height: 1.4;
}

.receipt-logo .logo {
  font-size: 3rem;
}

.receipt-divider {
  border: none;
  height: 2px;
  background: #e5e5e7;
  margin: 25px 0;
}

/* Order Details */
.order-details,
.order-total {
  margin-bottom: 20px;
}

.detail-row,
.total-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
  padding: 8px 0;
}

.detail-row .label,
.total-row span:first-child {
  color: #86868b;
  font-weight: 500;
}

.detail-row .value,
.total-row span:last-child {
  color: #1d1d1f;
  font-weight: 600;
}

.total-row.grand-total {
  border-top: 2px solid #e5e5e7;
  margin-top: 15px;
  padding-top: 15px;
  font-size: 1.2rem;
}

.total-row.grand-total span {
  color: #1d1d1f;
  font-weight: 700;
}

/* Shipping Info */
.shipping-info h4,
.order-items h4,
.order-notes h4 {
  color: #1d1d1f;
  margin: 0 0 15px 0;
  font-size: 1.1rem;
}

.address p {
  margin: 5px 0;
  color: #1d1d1f;
  line-height: 1.4;
}

/* Order Items */
.items-table {
  margin-top: 15px;
}

.table-header,
.table-row {
  display: grid;
  grid-template-columns: 2fr 80px 100px 100px;
  gap: 20px;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #f5f5f7;
}

.table-header {
  font-weight: 600;
  color: #86868b;
  font-size: 0.9rem;
}

.table-row:last-child {
  border-bottom: none;
}

.item-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.item-image {
  width: 50px;
  height: 50px;
  background: #f5f5f7;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.item-image img {
  width: 100%;
  height: 100%;
  object-fit: contain;
  border-radius: 8px;
}

.placeholder {
  font-size: 1.5rem;
  opacity: 0.5;
}

.item-details {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.item-name {
  font-weight: 500;
  color: #1d1d1f;
}

.item-desc {
  font-size: 0.85rem;
  color: #86868b;
}

.quantity,
.price,
.total {
  font-weight: 500;
  color: #1d1d1f;
  text-align: center;
}

/* Order Notes */
.order-notes p {
  color: #1d1d1f;
  line-height: 1.5;
  background: #f5f5f7;
  padding: 15px;
  border-radius: 8px;
}

/* Receipt Footer */
.receipt-footer {
  text-align: center;
  margin-top: 30px;
  padding-top: 20px;
  border-top: 2px solid #e5e5e7;
}

.receipt-footer p {
  color: #86868b;
  margin: 5px 0;
}

/* Action Buttons */
.action-buttons {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 25px;
  margin-bottom: 40px;
}

.print-btn,
.download-btn,
.continue-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  padding: 16px 24px;
  border-radius: 16px;
  text-decoration: none;
  font-weight: 600;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border: none;
  cursor: pointer;
  font-size: 1rem;
  position: relative;
  overflow: hidden;
}

.print-btn::before,
.download-btn::before,
.continue-btn::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
  transition: left 0.5s;
}

.print-btn:hover::before,
.download-btn:hover::before,
.continue-btn:hover::before {
  left: 100%;
}

.print-btn {
  background: linear-gradient(135deg, #0066cc, #0077ed);
  color: white;
  box-shadow: 0 4px 16px rgba(0, 102, 204, 0.3);
}

.print-btn:hover {
  transform: translateY(-3px) scale(1.02);
  box-shadow: 0 8px 24px rgba(0, 102, 204, 0.4);
}

.download-btn {
  background: linear-gradient(135deg, #34c759, #30d158);
  color: white;
  box-shadow: 0 4px 16px rgba(52, 199, 89, 0.3);
}

.download-btn:hover {
  transform: translateY(-3px) scale(1.02);
  box-shadow: 0 8px 24px rgba(52, 199, 89, 0.4);
}

.continue-btn {
  background: linear-gradient(135deg, #ff6b6b, #ee5a24);
  color: white;
  box-shadow: 0 4px 16px rgba(255, 107, 107, 0.3);
}

.continue-btn:hover {
  transform: translateY(-3px) scale(1.02);
  box-shadow: 0 8px 24px rgba(255, 107, 107, 0.4);
}

.btn-icon {
  font-size: 1.2rem;
}

/* Delivery Info */
.delivery-info {
  background: linear-gradient(135deg, #ffffff, #f8f9ff);
  border-radius: 24px;
  padding: 35px;
  box-shadow: 0 8px 32px rgba(0, 102, 204, 0.08);
  border: 1px solid rgba(0, 102, 204, 0.1);
  transition:
    transform 0.3s ease,
    box-shadow 0.3s ease;
}

.delivery-info:hover {
  transform: translateY(-2px);
  box-shadow: 0 12px 40px rgba(0, 102, 204, 0.12);
}

.delivery-card {
  display: flex;
  align-items: center;
  gap: 20px;
}

.delivery-icon {
  font-size: 3.5rem;
  background: linear-gradient(135deg, #0066cc, #0077ed);
  color: white;
  padding: 25px;
  border-radius: 50%;
  box-shadow: 0 8px 24px rgba(0, 102, 204, 0.3);
  display: flex;
  align-items: center;
  justify-content: center;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0%,
  100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.05);
  }
}

.delivery-details h4 {
  color: #1d1d1f;
  margin: 0 0 10px 0;
  font-size: 1.3rem;
}

.delivery-date {
  color: #0066cc;
  font-weight: 600;
  font-size: 1.1rem;
  margin: 0 0 10px 0;
}

.delivery-note {
  color: #86868b;
  margin: 0;
  line-height: 1.4;
}

/* No Order */
.no-order {
  text-align: center;
  background: white;
  border-radius: 20px;
  padding: 60px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
}

.no-order-icon {
  font-size: 4rem;
  margin-bottom: 20px;
  opacity: 0.5;
}

.no-order h2 {
  color: #1d1d1f;
  margin-bottom: 10px;
}

.no-order p {
  color: #86868b;
  margin-bottom: 30px;
}

.home-btn {
  display: inline-block;
  background: #0066cc;
  color: white;
  padding: 12px 24px;
  border-radius: 25px;
  text-decoration: none;
  font-weight: 600;
  transition: background 0.3s ease;
}

.home-btn:hover {
  background: #0077ed;
}

/* Responsive Design */
@media (max-width: 768px) {
  .receipt-header .container {
    flex-direction: column;
    gap: 15px;
    text-align: center;
  }

  .receipt-title {
    font-size: 1.5rem;
  }

  .progress-bar {
    padding: 1rem;
  }

  .progress-line {
    margin: 0 1rem;
  }

  .receipt-card {
    padding: 25px;
  }

  .receipt-header-content {
    flex-direction: column;
    text-align: center;
    gap: 20px;
  }

  .action-buttons {
    grid-template-columns: 1fr;
    gap: 15px;
  }

  .table-header,
  .table-row {
    grid-template-columns: 1fr;
    gap: 10px;
  }

  .table-header span:not(:first-child),
  .table-row span:not(:first-child) {
    display: none;
  }

  .item-info {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }

  .delivery-card {
    flex-direction: column;
    text-align: center;
  }
}

@media print {
  .action-buttons,
  .delivery-info {
    display: none;
  }

  .receipt-page {
    background: white;
  }

  .receipt-card {
    box-shadow: none;
    border: 1px solid #ddd;
  }
}
</style>
