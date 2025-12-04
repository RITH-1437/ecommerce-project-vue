<template>
  <div class="receipt-page">
    <main class="receipt-main">
      <div class="container">
        <!-- Colorful error banner (always visible when there's an error) -->
        <transition name="fade-slide">
          <div v-if="errorMessage" class="error-banner" role="alert">
            <div class="error-banner__left">
              <span class="error-banner__icon">⚠️</span>
              <span class="error-banner__text">{{ errorMessage }}</span>
            </div>
            <button class="error-banner__close" @click="clearError" aria-label="Dismiss error">
              ✖
            </button>
          </div>
        </transition>

        <!-- Receipt Content -->
        <div v-if="orderData" class="receipt-content">
          <!-- Success Message with Modern Animation -->
          <div class="success-section">
            <div class="success-animation">
              <div class="checkmark-circle">
                <svg class="checkmark" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 52 52">
                  <circle class="checkmark__circle" cx="26" cy="26" r="25" fill="none" />
                  <path class="checkmark__check" fill="none" d="M14.1 27.2l7.1 7.2 16.7-16.8" />
                </svg>
              </div>
              <div class="confetti">
                <div class="confetti-piece" style="--rotation: 45deg; --delay: 0s"></div>
                <div class="confetti-piece" style="--rotation: 135deg; --delay: 0.1s"></div>
                <div class="confetti-piece" style="--rotation: 225deg; --delay: 0.2s"></div>
                <div class="confetti-piece" style="--rotation: 315deg; --delay: 0.3s"></div>
                <div class="confetti-piece" style="--rotation: 90deg; --delay: 0.15s"></div>
                <div class="confetti-piece" style="--rotation: 180deg; --delay: 0.25s"></div>
              </div>
            </div>
            <div class="success-content">
              <h2 class="success-title">
                <span class="gradient-text">Payment Successful!</span>
                <span class="celebration-emoji">🎉</span>
              </h2>
              <p class="success-subtitle">
                Your order is confirmed and being prepared for delivery
              </p>
              <div class="order-badge">
                <span class="badge-icon">📋</span>
                <span class="badge-label">Order ID:</span>
                <span class="badge-number">#{{ orderData.orderNumber }}</span>
              </div>
            </div>
          </div>

          <!-- Receipt Card with Modern Design -->
          <div class="receipt-card" id="receipt-card">
            <!-- Premium Header with Glassmorphism -->
            <div class="receipt-header">
              <div class="brand-section">
                <div class="brand-logo">
                  <div class="logo-circle">
                    <svg class="apple-logo" viewBox="0 0 24 24" fill="currentColor">
                      <path
                        d="M17.05 20.28c-.98.95-2.05.8-3.08.35-1.09-.46-2.09-.48-3.24 0-1.44.62-2.2.44-3.06-.35C2.79 15.25 3.51 7.59 9.05 7.31c1.35.07 2.29.74 3.08.8 1.18-.24 2.31-.93 3.57-.84 1.51.12 2.65.72 3.4 1.8-3.12 1.87-2.38 5.98.48 7.13-.57 1.5-1.31 2.99-2.54 4.09l.01-.01zM12.03 7.25c-.15-2.23 1.66-4.07 3.74-4.25.29 2.58-2.34 4.5-3.74 4.25z"
                      />
                    </svg>
                  </div>
                  <div class="brand-text">
                    <h3 class="store-name">Apple Store</h3>
                    <p class="store-tagline">Premium Electronics</p>
                    <p class="store-location">📍 Phnom Penh, Cambodia</p>
                  </div>
                </div>
                <div class="receipt-meta">
                  <div class="qr-code-container">
                    <div class="qr-code">
                      <div class="qr-pattern"></div>
                    </div>
                    <span class="qr-text">Scan Receipt</span>
                  </div>
                  <div class="receipt-stamp">
                    <svg class="stamp-icon" viewBox="0 0 24 24" fill="currentColor">
                      <path d="M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41z" />
                    </svg>
                    <span class="stamp-text">VERIFIED</span>
                  </div>
                </div>
              </div>
            </div>

            <!-- Order Summary Cards -->
            <div class="info-grid">
              <div class="info-card order-number-card">
                <div class="info-icon">
                  <svg viewBox="0 0 24 24" fill="currentColor">
                    <path
                      d="M19 3H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm-5 14h-4v-2h4v2zm3-4H7v-2h10v2zm0-4H7V7h10v2z"
                    />
                  </svg>
                </div>
                <div class="info-content">
                  <span class="info-label">Order ID</span>
                  <span class="info-value">{{ orderData.orderNumber }}</span>
                </div>
              </div>
              <div class="info-card date-card">
                <div class="info-icon">
                  <svg viewBox="0 0 24 24" fill="currentColor">
                    <path
                      d="M19 4h-1V2h-2v2H8V2H6v2H5c-1.11 0-1.99.9-1.99 2L3 20c0 1.1.89 2 2 2h14c1.1 0 2-.9 2-2V6c0-1.1-.9-2-2-2zm0 16H5V10h14v10zM5 8V6h14v2H5zm2 4h10v2H7z"
                    />
                  </svg>
                </div>
                <div class="info-content">
                  <span class="info-label">Date</span>
                  <span class="info-value">{{ formatShortDate(orderData.orderDate) }}</span>
                </div>
              </div>
              <div class="info-card payment-card">
                <div class="info-icon">
                  <svg viewBox="0 0 24 24" fill="currentColor">
                    <path
                      d="M20 4H4c-1.11 0-1.99.89-1.99 2L2 18c0 1.1.89 2 2 2h16c1.1 0 2-.9 2-2V6c0-1.1-.9-2-2-2zm0 14H4v-6h16v6zm0-10H4V6h16v2z"
                    />
                  </svg>
                </div>
                <div class="info-content">
                  <span class="info-label">Payment</span>
                  <span class="info-value">{{ formatPaymentMethod(orderData.paymentMethod) }}</span>
                </div>
              </div>
            </div>

            <!-- Billing Information -->
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
                      <span class="item-total">${{ computeItemTotal(item) }}</span>
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
                  <span class="summary-value">${{ safeNumber(orderData.totals?.subtotal) }}</span>
                </div>
                <div class="summary-row">
                  <span class="summary-label">Shipping</span>
                  <span
                    class="summary-value shipping-free"
                    v-if="Number(orderData.totals?.shipping || 0) === 0"
                    >Free 🚚</span
                  >
                  <span class="summary-value" v-else
                    >${{ safeNumber(orderData.totals?.shipping) }}</span
                  >
                </div>
                <div class="summary-row">
                  <span class="summary-label">Tax</span>
                  <span class="summary-value">${{ safeNumber(orderData.totals?.tax) }}</span>
                </div>
                <div class="summary-divider"></div>
                <div class="summary-row total-row">
                  <span class="summary-label total-label">Total Paid</span>
                  <span class="summary-value total-value"
                    >${{ safeNumber(orderData.totals?.total) }}</span
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

        <!-- No Order Found (also shows error detail if present) -->
        <div v-else class="no-order">
          <div class="no-order-icon">📋</div>
          <h2>No Order Found</h2>
          <p>We couldn't find any recent order information.</p>
          <p v-if="errorMessage" class="no-order-error">Details: {{ errorMessage }}</p>
          <router-link to="/" class="home-btn">Back to Store</router-link>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
import Swal from 'sweetalert2'

export default {
  name: 'CheckoutReceiptView',
  data() {
    return {
      orderData: null,
      errorMessage: '',
    }
  },
  computed: {
    estimatedDelivery() {
      try {
        const base = this.orderData?.orderDate ? new Date(this.orderData.orderDate) : new Date()
        const validBase = isNaN(base.getTime()) ? new Date() : base
        const deliveryDate = new Date(validBase.getTime() + 5 * 24 * 60 * 60 * 1000)
        return deliveryDate.toLocaleDateString('en-US', {
          weekday: 'long',
          year: 'numeric',
          month: 'long',
          day: 'numeric',
        })
      } catch (e) {
        return ''
      }
    },
  },
  methods: {
    clearError() {
      this.errorMessage = ''
    },
    safeNumber(val) {
      const n = Number(val)
      return Number.isFinite(n) ? n.toFixed(2) : '0.00'
    },
    parsePrice(p) {
      if (typeof p === 'number') return Number.isFinite(p) ? p : 0
      if (typeof p === 'string') {
        const n = parseFloat(p.replace('$', '').replace(',', ''))
        return Number.isFinite(n) ? n : 0
      }
      return 0
    },
    computeItemTotal(item) {
      const price = this.parsePrice(item?.price)
      const qty = Number(item?.quantity || 0)
      const total = (Number.isFinite(qty) ? qty : 0) * price
      return total.toFixed(2)
    },
    formatDate(dateString) {
      try {
        const date = new Date(dateString)
        if (isNaN(date.getTime())) return ''
        return date.toLocaleDateString('en-US', {
          year: 'numeric',
          month: 'long',
          day: 'numeric',
          hour: '2-digit',
          minute: '2-digit',
        })
      } catch (e) {
        return ''
      }
    },
    formatShortDate(dateString) {
      try {
        const date = new Date(dateString)
        if (isNaN(date.getTime())) return ''
        return date.toLocaleDateString('en-US', {
          month: 'short',
          day: 'numeric',
          year: 'numeric',
        })
      } catch (e) {
        return ''
      }
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
      try {
        if (!this.orderData) {
          Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'No order data available to print.',
            confirmButtonColor: '#0066cc',
          })
          return
        }

        const printContent = document.getElementById('receipt-card')
        if (!printContent) {
          Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Receipt content not found.',
            confirmButtonColor: '#0066cc',
          })
          return
        }

        const printWindow = window.open('', '_blank')
        if (!printWindow) {
          Swal.fire({
            icon: 'error',
            title: 'Popup Blocked',
            text: 'Please allow popups to print the receipt.',
            confirmButtonColor: '#0066cc',
          })
          return
        }

        const styles =
          '@media print{@page{margin:0.5cm}}body{font-family:-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,sans-serif;margin:0;padding:20px;color:#1d1d1f}' +
          '.receipt-card{max-width:800px;margin:0 auto;border:2px solid #e5e5e7;border-radius:12px;padding:40px}' +
          '.receipt-header{margin-bottom:30px;padding-bottom:20px;border-bottom:2px solid #e5e5e7}' +
          '.brand-section{display:flex;justify-content:space-between;align-items:flex-start}' +
          '.brand-logo{display:flex;gap:15px;align-items:center}' +
          '.logo-circle{width:60px;height:60px;background:linear-gradient(135deg,#0066cc,#0077ed);border-radius:50%;display:flex;align-items:center;justify-content:center}' +
          '.apple-logo{width:35px;height:35px;color:white}' +
          '.store-name{font-size:24px;font-weight:700;margin:0 0 5px 0}' +
          '.store-tagline,.store-location{font-size:14px;color:#6e6e73;margin:0}' +
          '.info-grid{display:grid;grid-template-columns:repeat(2,1fr);gap:20px;margin-bottom:30px}' +
          '.info-card{display:flex;gap:12px;padding:15px;background:#f5f5f7;border-radius:10px}' +
          '.info-icon{width:40px;height:40px;background:white;border-radius:8px;display:flex;align-items:center;justify-content:center}' +
          '.info-icon svg{width:24px;height:24px;color:#0066cc}' +
          '.info-label{display:block;font-size:12px;color:#6e6e73}' +
          '.info-value{display:block;font-size:16px;font-weight:600}' +
          '.items-section{margin-bottom:30px}' +
          '.section-title{font-size:18px;font-weight:600;margin:0 0 15px 0}' +
          '.item-row{display:flex;gap:15px;padding:15px 0;border-bottom:1px solid #f5f5f7}' +
          '.item-icon{font-size:32px;width:50px;height:50px;background:#f5f5f7;border-radius:8px;display:flex;align-items:center;justify-content:center}' +
          '.item-details{flex:1}' +
          '.item-name{font-weight:600;margin:0}' +
          '.item-quantity{min-width:80px;text-align:center}' +
          '.item-price{font-weight:600;min-width:100px;text-align:right}' +
          '.totals-section{background:#f5f5f7;border-radius:10px;padding:20px;margin-bottom:30px}' +
          '.total-row{display:flex;justify-content:space-between;padding:8px 0}' +
          '.grand-total{border-top:2px solid #d2d2d7;margin-top:10px;padding-top:10px;font-weight:700;color:#0066cc}' +
          '.receipt-footer{text-align:center;padding-top:20px;border-top:2px solid #e5e5e7;color:#6e6e73;font-size:13px}' +
          '.action-buttons,.qr-code-container,.confetti,.success-animation{display:none!important}' +
          '@media print{.action-buttons,.qr-code-container,.confetti,.success-animation{display:none!important}}'

        const html =
          '<!DOCTYPE html><html><head><title>Order Receipt - ' +
          this.orderData.orderNumber +
          '</title><style>' +
          styles +
          '</style></head><body>' +
          printContent.outerHTML +
          '<script>window.onload=function(){window.print()}</' +
          'script></body></html>'

        printWindow.document.write(html)
        printWindow.document.close()
      } catch (err) {
        this.errorMessage = 'Failed to prepare the receipt for printing.'
        Swal.fire({
          icon: 'error',
          title: 'Print Error',
          text: 'An unexpected error occurred while preparing the receipt.',
          confirmButtonColor: '#0066cc',
        })
      }
    },
    downloadReceipt() {
      try {
        if (!this.orderData) {
          Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'No order data available to download.',
            confirmButtonColor: '#0066cc',
          })
          return
        }

        // Create professional receipt content with better formatting
        const border = '═'.repeat(68)
        const line = '─'.repeat(68)
        const doubleLine = '═'.repeat(68)

        // Safely get shipping address (fallback to billing if not available)
        const shippingAddr = this.orderData.shippingAddress || this.orderData.billingAddress

        // Format items with better alignment
        const itemsBlock = (this.orderData.items || [])
          .map((item, index) => {
            const price = this.parsePrice(item?.price)
            const qty = Number(item?.quantity || 0)
            const total = (Number.isFinite(qty) ? qty : 0) * price
            const name = (item?.name || 'Unknown Item').substring(0, 50)
            const qtyStr = `${qty} × ${item?.price || '$0.00'}`
            const totalStr = `$${total.toFixed(2)}`

            return `  ${index + 1}. ${name}
     Qty: ${qtyStr.padEnd(20)} Total: ${totalStr.padStart(10)}`
          })
          .join('\n' + line + '\n')

        // Format totals with alignment
        const subtotal = this.safeNumber(this.orderData.totals?.subtotal)
        const shipping =
          Number(this.orderData.totals?.shipping || 0) === 0
            ? 'FREE ✓'
            : '$' + this.safeNumber(this.orderData.totals?.shipping)
        const tax = this.safeNumber(this.orderData.totals?.tax)
        const total = this.safeNumber(this.orderData.totals?.total)

        const content = `
╔${border}╗
║                                                                    ║
║                           🍎 APPLE STORE 🍎                         ║
║                         Premium Electronics                        ║
║                      📍 Phnom Penh, Cambodia                       ║
║                                                                    ║
╚${border}╝

${doubleLine}
                              RECEIPT
${doubleLine}

  Order Number:     ${(this.orderData.orderNumber || '').padEnd(40)}
  Date:             ${this.formatDate(this.orderData.orderDate)}
  Payment Method:   ${this.formatPaymentMethod(this.orderData.paymentMethod)}

${doubleLine}
                          ITEMS PURCHASED
${doubleLine}

${itemsBlock}

${doubleLine}
                          ORDER SUMMARY
${doubleLine}

  Subtotal:                                           $${subtotal.padStart(10)}
  Shipping:                                            ${String(shipping).padStart(10)}
  Tax:                                                 $${tax.padStart(10)}
  ${line}
  TOTAL PAID:                                         $${total.padStart(10)}

${doubleLine}
                         BILLING ADDRESS
${doubleLine}

  ${(this.orderData.billingAddress?.fullName || '').padEnd(60)}
  ${(this.orderData.billingAddress?.street || '').padEnd(60)}
  ${(
    (this.orderData.billingAddress?.city || '') +
    ', ' +
    (this.orderData.billingAddress?.state || '') +
    ' ' +
    (this.orderData.billingAddress?.zip || '')
  ).padEnd(60)}
  ${this.getCountryName(this.orderData.billingAddress?.country || '').padEnd(60)}

${
  shippingAddr !== this.orderData.billingAddress
    ? `${doubleLine}
                        SHIPPING ADDRESS
${doubleLine}

  ${(shippingAddr?.fullName || '').padEnd(60)}
  ${(shippingAddr?.street || '').padEnd(60)}
  ${(
    (shippingAddr?.city || '') +
    ', ' +
    (shippingAddr?.state || '') +
    ' ' +
    (shippingAddr?.zip || '')
  ).padEnd(60)}
  ${this.getCountryName(shippingAddr?.country || '').padEnd(60)}

`
    : ''
}${doubleLine}
                      CONTACT INFORMATION
${doubleLine}

                 Thank you for choosing Apple Store!

                        🍎 Apple Store Premium
                    Russian Conf Norodom Boulevard
                       Phnom Penh 120404, Cambodia

                     📞 Phone: +855 966 273 314
                     📧 Email: support@applestore.com
                     🌐 Web: www.applestore.com

${doubleLine}

  This is a computer-generated receipt.
  For any inquiries, please contact our support team.

  Receipt generated: ${new Date().toLocaleString()}

${doubleLine}
`

        // Create and download the file with better naming
        const timestamp = new Date().toISOString().split('T')[0]
        const filename = `AppleStore_Receipt_${this.orderData.orderNumber}_${timestamp}.txt`

        const file = new Blob([content], { type: 'text/plain;charset=utf-8' })
        const element = document.createElement('a')
        element.href = URL.createObjectURL(file)
        element.download = filename
        element.style.display = 'none'
        document.body.appendChild(element)
        element.click()

        // Clean up
        setTimeout(() => {
          document.body.removeChild(element)
          URL.revokeObjectURL(element.href)
        }, 100)

        // Show SweetAlert confirmation with download info
        Swal.fire({
          icon: 'success',
          title: '📄 Download Successful!',
          html: `
            <div style="text-align: left; padding: 10px;">
              <p style="margin: 10px 0;">Receipt has been downloaded:</p>
              <p style="background: #f5f5f7; padding: 10px; border-radius: 8px; font-family: monospace; font-size: 0.9em; word-break: break-all;">
                <strong>${filename}</strong>
              </p>
              <p style="margin-top: 15px; color: #6e6e73; font-size: 0.9em;">
                ✓ Order: <strong>#${this.orderData.orderNumber}</strong><br>
                ✓ Format: Plain Text (.txt)
              </p>
            </div>
          `,
          confirmButtonText: 'OK',
          confirmButtonColor: '#0066cc',
          timer: 4000,
          timerProgressBar: true,
          showClass: {
            popup: 'animate__animated animate__fadeInDown',
          },
          hideClass: {
            popup: 'animate__animated animate__fadeOutUp',
          },
        })
      } catch (err) {
        console.error('Download error:', err)
        this.errorMessage = 'Failed to generate the receipt file.'
        Swal.fire({
          icon: 'error',
          title: 'Download Failed',
          html: `
            <p>Unable to download the receipt.</p>
            <p style="color: #6e6e73; font-size: 0.9em; margin-top: 10px;">
              ${err.message || 'Please try again or contact support.'}
            </p>
          `,
          confirmButtonColor: '#0066cc',
        })
      }
    },
  },
  mounted() {
    try {
      // Load order data from sessionStorage (set by payment page)
      const savedOrder = sessionStorage.getItem('orderData')
      if (savedOrder) {
        try {
          this.orderData = JSON.parse(savedOrder)
          // Also save to localStorage for backup
          localStorage.setItem('lastOrder', savedOrder)
        } catch (e) {
          this.errorMessage = 'Stored order data is corrupted. Please complete checkout again.'
        }
      } else {
        // Fallback to localStorage
        const backupOrder = localStorage.getItem('lastOrder')
        if (backupOrder) {
          try {
            this.orderData = JSON.parse(backupOrder)
          } catch (e) {
            this.errorMessage = 'Backup order data is corrupted. Please complete checkout again.'
          }
        }
      }
    } catch (err) {
      this.errorMessage = 'Failed to load order information due to an unexpected error.'
      Swal.fire({
        icon: 'error',
        title: 'Load Error',
        text: 'We could not load your receipt due to an unexpected error.',
        confirmButtonColor: '#0066cc',
      })
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

/* Colorful error banner */
.error-banner {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 16px;
  background: linear-gradient(135deg, #ff3b30, #ff9f0a);
  color: #ffffff;
  padding: 14px 18px;
  border-radius: 12px;
  box-shadow: 0 8px 24px rgba(255, 59, 48, 0.25);
  border: 1px solid rgba(255, 255, 255, 0.15);
  margin: 18px 0 6px 0;
}
.error-banner__left {
  display: flex;
  align-items: center;
  gap: 10px;
}
.error-banner__icon {
  font-size: 20px;
  filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.25));
}
.error-banner__text {
  font-weight: 700;
  letter-spacing: 0.2px;
}
.error-banner__close {
  appearance: none;
  border: none;
  border-radius: 8px;
  padding: 6px 10px;
  cursor: pointer;
  color: #ffffff;
  background: rgba(255, 255, 255, 0.2);
  transition:
    background 0.25s ease,
    transform 0.15s ease;
}
.error-banner__close:hover {
  background: rgba(255, 255, 255, 0.35);
}
.error-banner__close:active {
  transform: scale(0.98);
}

.fade-slide-enter-active,
.fade-slide-leave-active {
  transition:
    opacity 0.25s ease,
    transform 0.25s ease;
}
.fade-slide-enter-from,
.fade-slide-leave-to {
  opacity: 0;
  transform: translateY(-6px);
}

.receipt-main {
  padding: 20px 0 40px 0;
}

.receipt-content {
  max-width: 700px;
  margin: 0 auto;
}

/* Success Section */
.success-section {
  text-align: center;
  background: linear-gradient(135deg, #f8f9ff 0%, #ffffff 50%, #f0fff4 100%);
  border-radius: 24px;
  padding: 60px;
  margin-bottom: 40px;
  box-shadow:
    0 8px 32px rgba(0, 102, 204, 0.1),
    0 0 0 1px rgba(0, 102, 204, 0.05);
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

/* Modern SVG Checkmark Animation */
.success-animation {
  position: relative;
  display: inline-block;
  margin-bottom: 30px;
}

.checkmark {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  display: block;
  stroke-width: 3;
  stroke: #34c759;
  stroke-miterlimit: 10;
  box-shadow:
    inset 0 0 0 #34c759,
    0 4px 20px rgba(52, 199, 89, 0.3);
  animation:
    checkmark-fill 0.4s ease-in-out 0.4s forwards,
    checkmark-scale 0.3s ease-in-out 0.9s both;
}

.checkmark__circle {
  stroke-dasharray: 166;
  stroke-dashoffset: 166;
  stroke-width: 3;
  stroke-miterlimit: 10;
  stroke: #34c759;
  fill: none;
  animation: checkmark-stroke 0.6s cubic-bezier(0.65, 0, 0.45, 1) forwards;
}

.checkmark__check {
  transform-origin: 50% 50%;
  stroke-dasharray: 48;
  stroke-dashoffset: 48;
  stroke: #34c759;
  animation: checkmark-stroke 0.3s cubic-bezier(0.65, 0, 0.45, 1) 0.8s forwards;
}

@keyframes checkmark-stroke {
  100% {
    stroke-dashoffset: 0;
  }
}
@keyframes checkmark-scale {
  0%,
  100% {
    transform: none;
  }
  50% {
    transform: scale3d(1.1, 1.1, 1);
  }
}
@keyframes checkmark-fill {
  100% {
    box-shadow:
      inset 0 0 0 60px rgba(52, 199, 89, 0.1),
      0 4px 20px rgba(52, 199, 89, 0.3);
  }
}

/* Confetti Animation */
.confetti {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 10;
}

.confetti-piece {
  position: absolute;
  width: 10px;
  height: 10px;
  background: linear-gradient(135deg, #0066cc, #34c759);
  top: 0;
  left: 50%;
  opacity: 0;
  animation: confetti-fall 2s cubic-bezier(0.25, 0.46, 0.45, 0.94) forwards;
}

.confetti-piece:nth-child(1) {
  left: calc(50% - 60px);
  animation-delay: var(--delay, 0s);
  transform: rotate(var(--rotation, 0deg));
  background: #0066cc;
}
.confetti-piece:nth-child(2) {
  left: calc(50% - 30px);
  animation-delay: var(--delay, 0.1s);
  transform: rotate(var(--rotation, 45deg));
  background: #34c759;
}
.confetti-piece:nth-child(3) {
  left: 50%;
  animation-delay: var(--delay, 0.2s);
  transform: rotate(var(--rotation, 90deg));
  background: #ff6b6b;
}
.confetti-piece:nth-child(4) {
  left: calc(50% + 30px);
  animation-delay: var(--delay, 0.15s);
  transform: rotate(var(--rotation, 135deg));
  background: #ffa500;
}
.confetti-piece:nth-child(5) {
  left: calc(50% + 60px);
  animation-delay: var(--delay, 0.25s);
  transform: rotate(var(--rotation, 180deg));
  background: #9b59b6;
}
.confetti-piece:nth-child(6) {
  left: calc(50% + 90px);
  animation-delay: var(--delay, 0.3s);
  transform: rotate(var(--rotation, 225deg));
  background: #3498db;
}

@keyframes confetti-fall {
  0% {
    opacity: 1;
    transform: translateY(0) rotate(0deg);
  }
  100% {
    opacity: 0;
    transform: translateY(200px) rotate(360deg);
  }
}

/* Receipt Card - Premium Glassmorphism Design */
.receipt-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 24px;
  padding: 48px;
  margin-bottom: 40px;
  box-shadow:
    0 8px 32px rgba(0, 0, 0, 0.08),
    0 0 0 1px rgba(0, 0, 0, 0.04),
    inset 0 1px 0 rgba(255, 255, 255, 0.8);
  border: 1px solid rgba(0, 0, 0, 0.06);
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
}

.receipt-card:hover {
  transform: translateY(-4px);
  box-shadow:
    0 16px 48px rgba(0, 0, 0, 0.12),
    0 0 0 1px rgba(0, 102, 204, 0.1),
    inset 0 1px 0 rgba(255, 255, 255, 0.9);
}

.receipt-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: linear-gradient(90deg, #0066cc 0%, #34c759 50%, #ff6b6b 100%);
  border-radius: 24px 24px 0 0;
}

/* Premium Receipt Header */
.receipt-header {
  margin-bottom: 32px;
  padding-bottom: 24px;
  border-bottom: 2px solid rgba(0, 0, 0, 0.05);
}
.brand-section {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 20px;
}
.brand-logo {
  display: flex;
  align-items: center;
  gap: 20px;
}
.logo-circle {
  width: 80px;
  height: 80px;
  background: linear-gradient(135deg, #f5f5f7 0%, #e8e8ed 100%);
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow:
    0 4px 12px rgba(0, 0, 0, 0.1),
    inset 0 1px 0 rgba(255, 255, 255, 0.5);
  transition: all 0.3s ease;
}
.logo-circle:hover {
  transform: rotate(-5deg) scale(1.05);
}
.apple-logo {
  width: 40px;
  height: 40px;
  color: #1d1d1f;
  transition: color 0.3s ease;
}
.logo-circle:hover .apple-logo {
  color: #0066cc;
}
.brand-text {
  display: flex;
  flex-direction: column;
  gap: 4px;
}
.store-name {
  font-size: 1.8rem;
  font-weight: 700;
  color: #1d1d1f;
  margin: 0;
  letter-spacing: -0.5px;
}
.store-tagline {
  font-size: 0.9rem;
  color: #86868b;
  margin: 0;
  font-weight: 500;
}
.store-location {
  font-size: 0.85rem;
  color: #0066cc;
  margin: 0;
  font-weight: 500;
}

.receipt-meta {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 16px;
}
.qr-code-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
}
.qr-code {
  width: 80px;
  height: 80px;
  background: linear-gradient(135deg, #1d1d1f 0%, #2d2d2f 100%);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
  position: relative;
  overflow: hidden;
  transition: transform 0.3s ease;
}
.qr-code:hover {
  transform: scale(1.05);
}
.qr-pattern {
  width: 60px;
  height: 60px;
  background-image:
    linear-gradient(white 2px, transparent 2px), linear-gradient(90deg, white 2px, transparent 2px);
  background-size: 10px 10px;
  background-position: center;
  border-radius: 4px;
}
.qr-text {
  font-size: 0.75rem;
  color: #86868b;
  font-weight: 500;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.receipt-stamp {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  background: linear-gradient(135deg, rgba(52, 199, 89, 0.1), rgba(48, 209, 88, 0.1));
  border: 1.5px solid rgba(52, 199, 89, 0.3);
  border-radius: 20px;
  animation: stamp-appear 0.6s cubic-bezier(0.68, -0.55, 0.265, 1.55) 0.5s both;
}
@keyframes stamp-appear {
  0% {
    transform: scale(0) rotate(-180deg);
    opacity: 0;
  }
  100% {
    transform: scale(1) rotate(0deg);
    opacity: 1;
  }
}
.stamp-icon {
  width: 18px;
  height: 18px;
  color: #34c759;
}
.stamp-text {
  font-size: 0.75rem;
  font-weight: 700;
  color: #34c759;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

/* Modern Info Grid Cards */
.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin: 32px 0;
}
.info-card {
  background: linear-gradient(135deg, #ffffff 0%, #f8f9ff 100%);
  border-radius: 16px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 16px;
  box-shadow:
    0 4px 12px rgba(0, 0, 0, 0.06),
    0 0 0 1px rgba(0, 0, 0, 0.04);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
}
.info-card::before {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 3px;
  background: linear-gradient(90deg, #0066cc, #34c759);
  transform: scaleX(0);
  transition: transform 0.3s ease;
}
.info-card:hover {
  transform: translateY(-4px);
  box-shadow:
    0 8px 24px rgba(0, 0, 0, 0.1),
    0 0 0 1px rgba(0, 102, 204, 0.2);
}
.info-card:hover::before {
  transform: scaleX(1);
}
.order-number-card {
  background: linear-gradient(135deg, #e3f2fd 0%, #bbdefb 100%);
}
.date-card {
  background: linear-gradient(135deg, #f3e5f5 0%, #e1bee7 100%);
}
.payment-card {
  background: linear-gradient(135deg, #e8f5e9 0%, #c8e6c9 100%);
}
.status-card {
  background: linear-gradient(135deg, #fff3e0 0%, #ffe0b2 100%);
}
.info-icon {
  width: 48px;
  height: 48px;
  min-width: 48px;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow:
    0 4px 8px rgba(0, 0, 0, 0.08),
    inset 0 1px 0 rgba(255, 255, 255, 0.8);
  transition: all 0.3s ease;
}
.info-icon svg {
  width: 24px;
  height: 24px;
  color: #0066cc;
  transition: all 0.3s ease;
}
.order-number-card .info-icon svg {
  color: #1976d2;
}
.date-card .info-icon svg {
  color: #7b1fa2;
}
.payment-card .info-icon svg {
  color: #388e3c;
}
.status-card .info-icon svg {
  color: #f57c00;
}
.info-card:hover .info-icon {
  transform: scale(1.1) rotate(5deg);
}
.info-card:hover .info-icon svg {
  transform: scale(1.2);
}

.info-content {
  display: flex;
  flex-direction: column;
  gap: 4px;
  flex: 1;
}
.info-label {
  font-size: 0.75rem;
  color: #86868b;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}
.info-value {
  font-size: 1rem;
  color: #1d1d1f;
  font-weight: 700;
  letter-spacing: -0.3px;
}

/* Divider */
.receipt-divider {
  border: none;
  height: 1px;
  background: linear-gradient(90deg, transparent, #e5e5e7 20%, #e5e5e7 80%, transparent);
  margin: 32px 0;
}

/* Items Section - Modern Card Layout */
.items-section {
  margin: 32px 0;
}
.items-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}
.item-card {
  display: flex;
  align-items: center;
  gap: 16px;
  background: linear-gradient(135deg, #fafafa 0%, #f5f5f7 100%);
  padding: 16px;
  border-radius: 12px;
  transition: all 0.3s ease;
  border: 1px solid rgba(0, 0, 0, 0.04);
}
.item-card:hover {
  transform: translateX(4px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}
.item-visual {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}
.item-icon {
  width: 56px;
  height: 56px;
  background: white;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.4rem;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}
.quantity-badge {
  position: absolute;
  top: -6px;
  right: -6px;
  background: linear-gradient(135deg, #0066cc, #0077ed);
  color: white;
  font-size: 0.75rem;
  font-weight: 700;
  padding: 4px 8px;
  border-radius: 10px;
  box-shadow: 0 2px 6px rgba(0, 102, 204, 0.3);
  min-width: 24px;
  text-align: center;
}
.item-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 6px;
}
.item-title {
  font-size: 1rem;
  font-weight: 600;
  color: #1d1d1f;
  margin: 0;
}
.item-price-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 16px;
}
.unit-price {
  font-size: 0.85rem;
  color: #86868b;
  font-weight: 500;
}
.item-total {
  font-size: 1.1rem;
  font-weight: 700;
  color: #0066cc;
}

/* Summary Section - Modern Card Design */
.summary-section {
  margin: 32px 0;
}
.summary-card {
  background: linear-gradient(135deg, #ffffff 0%, #f8f9ff 100%);
  border-radius: 16px;
  padding: 24px;
  box-shadow:
    0 4px 12px rgba(0, 0, 0, 0.06),
    0 0 0 1px rgba(0, 102, 204, 0.08);
}
.summary-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 0;
}
.summary-label {
  font-size: 0.95rem;
  color: #86868b;
  font-weight: 500;
}
.summary-value {
  font-size: 1rem;
  color: #1d1d1f;
  font-weight: 600;
}
.shipping-free {
  color: #34c759;
  font-weight: 700;
}
.summary-divider {
  height: 1px;
  background: linear-gradient(90deg, transparent, #e5e5e7 20%, #e5e5e7 80%, transparent);
  margin: 12px 0;
}
.total-row {
  padding: 16px 0 0 0;
}
.total-label {
  font-size: 1.2rem;
  color: #1d1d1f;
  font-weight: 700;
}
.total-value {
  font-size: 1.5rem;
  color: #0066cc;
  font-weight: 800;
  letter-spacing: -0.5px;
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

/* Action Buttons - Enhanced Modern Design */
.action-buttons {
  display: grid !important;
  grid-template-columns: repeat(3, 1fr);
  gap: 25px;
  margin-bottom: 40px;
  visibility: visible !important;
  opacity: 1 !important;
  position: relative;
  z-index: 10;
}

@media screen {
  .action-buttons {
    display: grid !important;
  }
}

.print-btn,
.download-btn,
.continue-btn {
  display: flex !important;
  align-items: center;
  justify-content: center;
  gap: 12px;
  padding: 18px 28px;
  border-radius: 16px;
  text-decoration: none;
  font-weight: 700;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  border: none;
  cursor: pointer;
  font-size: 1rem;
  position: relative;
  overflow: hidden;
  letter-spacing: 0.3px;
  visibility: visible !important;
  opacity: 1 !important;
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
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.3), transparent);
  transition: left 0.6s ease;
}
.print-btn:hover::before,
.download-btn:hover::before,
.continue-btn:hover::before {
  left: 100%;
}
.print-btn {
  background: linear-gradient(135deg, #0066cc 0%, #0077ed 50%, #0088ff 100%);
  color: white;
  box-shadow:
    0 4px 16px rgba(0, 102, 204, 0.35),
    0 0 0 1px rgba(0, 102, 204, 0.1);
}
.print-btn:hover {
  transform: translateY(-4px) scale(1.03);
  box-shadow:
    0 10px 30px rgba(0, 102, 204, 0.45),
    0 0 0 1px rgba(0, 102, 204, 0.2);
}
.print-btn:active {
  transform: translateY(-2px) scale(1.01);
}
.download-btn {
  background: linear-gradient(135deg, #34c759 0%, #30d158 50%, #28cd50 100%);
  color: white;
  box-shadow:
    0 4px 16px rgba(52, 199, 89, 0.35),
    0 0 0 1px rgba(52, 199, 89, 0.1);
}
.download-btn:hover {
  transform: translateY(-4px) scale(1.03);
  box-shadow:
    0 10px 30px rgba(52, 199, 89, 0.45),
    0 0 0 1px rgba(52, 199, 89, 0.2);
}
.download-btn:active {
  transform: translateY(-2px) scale(1.01);
}
.continue-btn {
  background: linear-gradient(135deg, #ff6b6b 0%, #ee5a24 50%, #ff4757 100%);
  color: white;
  box-shadow:
    0 4px 16px rgba(255, 107, 107, 0.35),
    0 0 0 1px rgba(255, 107, 107, 0.1);
}
.continue-btn:hover {
  transform: translateY(-4px) scale(1.03);
  box-shadow:
    0 10px 30px rgba(255, 107, 107, 0.45),
    0 0 0 1px rgba(255, 107, 107, 0.2);
}
.continue-btn:active {
  transform: translateY(-2px) scale(1.01);
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
  font-size: 2.2rem;
  background: linear-gradient(135deg, #0066cc, #0077ed);
  color: white;
  padding: 18px;
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
  margin-bottom: 10px;
}
.no-order-error {
  color: #d91e18;
  font-weight: 600;
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
  .receipt-card {
    padding: 25px;
  }
  .action-buttons {
    grid-template-columns: 1fr;
    gap: 15px;
  }
}

@media print {
  .action-buttons,
  .delivery-info,
  .confetti,
  .error-banner {
    display: none !important;
  }
  .receipt-page {
    background: white;
    padding: 0;
    margin: 0;
  }
  .receipt-main {
    padding: 20px;
  }
  .success-section {
    background: white;
    border: 2px solid #e5e5e7;
    box-shadow: none;
    page-break-after: avoid;
    padding: 30px;
  }
  .success-section::before {
    display: none;
  }
  .checkmark {
    width: 80px;
    height: 80px;
    box-shadow: none;
    border: 2px solid #34c759;
  }
  .receipt-card {
    box-shadow: none;
    border: 2px solid #e5e5e7;
    page-break-inside: avoid;
    margin-bottom: 0;
  }
  .receipt-card::before {
    display: none;
  }
  .receipt-header {
    border-bottom: 3px solid #1d1d1f;
  }
  .logo-circle {
    box-shadow: none;
    border: 2px solid #e5e5e7;
  }
  .qr-code {
    box-shadow: none;
    border: 2px solid #1d1d1f;
  }
  .receipt-stamp {
    border: 2px solid #34c759;
    background: white;
  }
  .info-card {
    box-shadow: none;
    border: 1px solid #e5e5e7;
    page-break-inside: avoid;
  }
  .info-card::before {
    display: none;
  }
  .order-number {
    box-shadow: none;
    border: 2px solid #0066cc;
  }
  .items-table {
    page-break-inside: avoid;
  }
  .table-row {
    page-break-inside: avoid;
  }
  .receipt-footer {
    page-break-before: avoid;
    border-top: 3px solid #1d1d1f;
  }
  .qr-pattern {
    filter: contrast(1.2);
  }
  * {
    -webkit-print-color-adjust: exact;
    print-color-adjust: exact;
  }
  .success-section,
  .receipt-card {
    page-break-after: auto;
  }
  h2,
  h3,
  h4 {
    page-break-after: avoid;
  }
}
</style>
