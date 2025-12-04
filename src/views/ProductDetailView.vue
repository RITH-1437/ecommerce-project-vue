<template>
  <div class="product-detail-page">
    <AppHeader />

    <main class="main-content">
      <div class="container">
        <!-- Back Button -->
        <router-link :to="backRoute" class="back-link">
          <span class="back-icon"><i class="fas fa-arrow-left"></i></span>
          <span>Back to Products</span>
        </router-link>

        <div v-if="product" class="product-detail-container">
          <!-- Product Main Section -->
          <div class="product-main">
            <div class="product-image-section">
              <div class="product-badge" v-if="product.badge">{{ product.badge }}</div>
              <div class="product-image-large">
                <img v-if="product.image" :src="product.image" :alt="product.name" />
                <div v-else class="placeholder-icon"><i class="fas fa-mobile-alt"></i></div>
              </div>
              <div v-if="product.colors" class="color-options">
                <div class="color-label">Available Colors:</div>
                <div class="colors-grid">
                  <div
                    v-for="color in product.colors"
                    :key="color"
                    class="color-circle"
                    :style="{ backgroundColor: color }"
                    :title="color"
                  ></div>
                </div>
              </div>
            </div>

            <div class="product-info-section">
              <h1 class="product-title">{{ product.name }}</h1>
              <p class="product-description">{{ product.description }}</p>

              <div class="product-price-section">
                <div class="price-main">{{ product.price }}</div>
                <div v-if="product.originalPrice" class="price-original">
                  {{ product.originalPrice }}
                </div>
              </div>

              <div v-if="product.features" class="product-features">
                <h3>Key Features:</h3>
                <ul>
                  <li v-for="(feature, index) in product.features" :key="index">
                    {{ feature }}
                  </li>
                </ul>
              </div>

              <div v-if="product.specs" class="product-specs">
                <h3>Specifications:</h3>
                <div class="specs-grid">
                  <div v-for="(value, key) in product.specs" :key="key" class="spec-item">
                    <span class="spec-label">{{ key }}:</span>
                    <span class="spec-value">{{ value }}</span>
                  </div>
                </div>
              </div>

              <div class="product-actions">
                <button @click="addToCart" class="btn-add-cart">
                  <span>🛒</span>
                  <span>Add to Cart</span>
                </button>
                <button @click="buyNow" class="btn-buy-now">
                  <span>Buy Now</span>
                </button>
              </div>
            </div>
          </div>

          <!-- Product Rating Section -->
          <ProductRating :product-id="product.id" :product-name="product.name" />
        </div>

        <div v-else class="product-not-found">
          <div class="not-found-icon"><i class="fas fa-search"></i></div>
          <h2>Product Not Found</h2>
          <p>The product you're looking for doesn't exist or has been removed.</p>
          <router-link to="/" class="btn-home">Go to Home</router-link>
        </div>
      </div>
    </main>

    <AppFooter />
  </div>
</template>

<script>
import AppHeader from '@/components/layout/AppHeader.vue'
import AppFooter from '@/components/layout/AppFooter.vue'
import ProductRating from '@/components/product/ProductRating.vue'
import { useCartStore } from '@/stores/counter.js'
import Swal from 'sweetalert2'

export default {
  name: 'ProductDetailView',
  components: {
    AppHeader,
    AppFooter,
    ProductRating,
  },
  setup() {
    const cartStore = useCartStore()
    return { cartStore }
  },
  data() {
    return {
      product: null,
      backRoute: '/',
    }
  },
  mounted() {
    this.loadProduct()
  },
  methods: {
    loadProduct() {
      // Get product data from route params or localStorage
      const productId = this.$route.params.id
      const productType = this.$route.params.type

      // Try to get from route state first
      if (this.$route.params.product) {
        this.product = this.$route.params.product
        this.backRoute = this.$route.params.backRoute || `/${productType || ''}`
        return
      }

      // Try to load from localStorage (temporary solution)
      const savedProduct = localStorage.getItem(`product_${productId}`)
      if (savedProduct) {
        this.product = JSON.parse(savedProduct)
        this.backRoute = `/${productType || ''}`
        return
      }

      // Fallback: redirect to home
      this.$router.push('/')
    },
    addToCart() {
      this.cartStore.addItem(this.product)
      Swal.fire({
        icon: 'success',
        title: 'Added to Cart!',
        html: `
          <p><strong>${this.product.name}</strong> has been added to your cart.</p>
        `,
        confirmButtonText: 'Continue Shopping',
        showCancelButton: true,
        cancelButtonText: 'View Cart',
        confirmButtonColor: '#0071e3',
        cancelButtonColor: '#6c757d',
      }).then((result) => {
        if (!result.isConfirmed) {
          this.$router.push('/checkout/cart')
        }
      })
    },
    buyNow() {
      this.cartStore.addItem(this.product)
      this.$router.push('/checkout/cart')
    },
  },
}
</script>

<style scoped>
.product-detail-page {
  background: #f5f5f7;
  min-height: 100vh;
}

.main-content {
  padding: 40px 0;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

/* Back Link */
.back-link {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  color: #0071e3;
  text-decoration: none;
  font-weight: 600;
  margin-bottom: 30px;
  transition: all 0.3s ease;
}

.back-link:hover {
  gap: 12px;
  color: #005bb5;
}

.back-icon {
  font-size: 1.2rem;
}

/* Product Detail Container */
.product-detail-container {
  background: white;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.product-main {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 60px;
  padding: 60px;
}

/* Product Image Section */
.product-image-section {
  position: relative;
}

.product-badge {
  position: absolute;
  top: 20px;
  right: 20px;
  background: linear-gradient(135deg, #ff6b6b, #ee5a24);
  color: white;
  padding: 8px 20px;
  border-radius: 25px;
  font-weight: 600;
  font-size: 0.9rem;
  z-index: 10;
}

.product-image-large {
  width: 100%;
  aspect-ratio: 1;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 30px;
}

.product-image-large img {
  width: 80%;
  height: 80%;
  object-fit: contain;
}

.placeholder-icon {
  font-size: 10rem;
  opacity: 0.8;
}

.color-options {
  text-align: center;
}

.color-label {
  font-weight: 600;
  color: #666;
  margin-bottom: 15px;
}

.colors-grid {
  display: flex;
  justify-content: center;
  gap: 12px;
  flex-wrap: wrap;
}

.color-circle {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  border: 3px solid #f0f0f0;
  cursor: pointer;
  transition: all 0.3s ease;
}

.color-circle:hover {
  transform: scale(1.2);
  border-color: #0071e3;
}

/* Product Info Section */
.product-info-section {
  display: flex;
  flex-direction: column;
  gap: 25px;
}

.product-title {
  font-size: 2.5rem;
  font-weight: 700;
  color: #1d1d1f;
  margin: 0;
  line-height: 1.2;
}

.product-description {
  font-size: 1.1rem;
  color: #666;
  line-height: 1.6;
  margin: 0;
}

.product-price-section {
  display: flex;
  align-items: center;
  gap: 15px;
}

.price-main {
  font-size: 2rem;
  font-weight: 700;
  color: #0071e3;
}

.price-original {
  font-size: 1.3rem;
  color: #86868b;
  text-decoration: line-through;
}

.product-features h3,
.product-specs h3 {
  font-size: 1.3rem;
  font-weight: 600;
  color: #1d1d1f;
  margin: 0 0 15px 0;
}

.product-features ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.product-features li {
  padding: 10px 0;
  padding-left: 30px;
  position: relative;
  color: #333;
  line-height: 1.6;
}

.product-features li::before {
  content: '✓';
  position: absolute;
  left: 0;
  color: #28a745;
  font-weight: 700;
  font-size: 1.2rem;
}

.specs-grid {
  display: grid;
  grid-template-columns: 1fr;
  gap: 12px;
}

.spec-item {
  display: flex;
  justify-content: space-between;
  padding: 12px;
  background: #f9f9f9;
  border-radius: 8px;
}

.spec-label {
  font-weight: 600;
  color: #666;
}

.spec-value {
  color: #333;
}

/* Product Actions */
.product-actions {
  display: flex;
  gap: 15px;
  margin-top: 10px;
}

.btn-add-cart,
.btn-buy-now {
  flex: 1;
  padding: 18px 30px;
  border: none;
  border-radius: 12px;
  font-size: 1.1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
}

.btn-add-cart {
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: white;
}

.btn-add-cart:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.4);
}

.btn-buy-now {
  background: linear-gradient(135deg, #0071e3, #005bb5);
  color: white;
}

.btn-buy-now:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(0, 113, 227, 0.4);
}

/* Product Not Found */
.product-not-found {
  text-align: center;
  padding: 100px 20px;
  background: white;
  border-radius: 20px;
}

.not-found-icon {
  font-size: 6rem;
  margin-bottom: 20px;
  opacity: 0.5;
}

.product-not-found h2 {
  font-size: 2rem;
  color: #1d1d1f;
  margin-bottom: 15px;
}

.product-not-found p {
  font-size: 1.1rem;
  color: #666;
  margin-bottom: 30px;
}

.btn-home {
  display: inline-block;
  padding: 15px 40px;
  background: #0071e3;
  color: white;
  text-decoration: none;
  border-radius: 30px;
  font-weight: 600;
  transition: all 0.3s ease;
}

.btn-home:hover {
  background: #005bb5;
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(0, 113, 227, 0.3);
}

/* Responsive */
@media (max-width: 1024px) {
  .product-main {
    grid-template-columns: 1fr;
    gap: 40px;
    padding: 40px;
  }

  .product-title {
    font-size: 2rem;
  }
}

@media (max-width: 768px) {
  .main-content {
    padding: 20px 0;
  }

  .product-main {
    padding: 30px 20px;
  }

  .product-title {
    font-size: 1.8rem;
  }

  .price-main {
    font-size: 1.5rem;
  }

  .product-actions {
    flex-direction: column;
  }

  .placeholder-icon {
    font-size: 6rem;
  }
}
</style>
