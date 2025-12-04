<template>
  <div :class="`${productType.toLowerCase()}-page`" class="product-page-root">
    <AppHeader @search="handleSearch" />

    <main class="main-content">
      <div class="container">
        <!-- Page Hero -->
        <section class="page-hero">
          <div class="hero-content">
            <h1 class="page-title">
              <span class="gradient-text">{{ pageConfig.title }}</span>
            </h1>
            <p class="page-subtitle">{{ pageConfig.subtitle }}</p>
          </div>
          <div class="hero-image">
            <div class="product-showcase">
              <div class="product-stack">
                <div
                  v-for="(item, index) in 3"
                  :key="index"
                  :class="`product product-${index + 1}`"
                  v-html="pageConfig.emoji"
                ></div>
              </div>
            </div>
          </div>
        </section>

        <!-- Modern Filter Section -->
        <section class="modern-filter-section">
          <div class="filter-container">
            <div class="filter-content">
              <!-- Main Row: Categories, Sort, and Clear All -->
              <div class="filter-main-row">
                <!-- Category Pills -->
                <div class="category-section">
                  <label class="section-label">Categories</label>
                  <div class="category-pills">
                    <button
                      v-for="category in categories"
                      :key="category"
                      @click="activeCategory = category"
                      :class="['category-pill', { active: activeCategory === category }]"
                    >
                      <span class="pill-text">{{ category }}</span>
                      <span v-if="getCategoryCount(category) > 0" class="pill-count">
                        {{ getCategoryCount(category) }}
                      </span>
                    </button>
                  </div>
                </div>

                <!-- Sort Dropdown -->
                <div class="sort-section">
                  <label class="section-label">Sort By</label>
                  <div class="sort-dropdown-wrapper">
                    <select v-model="sortBy" class="modern-sort-select">
                      <option value="name">📝 Name (A-Z)</option>
                      <option value="price-low">💰 Price: Low to High</option>
                      <option value="price-high">💎 Price: High to Low</option>
                    </select>
                    <div class="dropdown-arrow">▼</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </section>

        <!-- Dynamic Product Sections -->
        <section v-for="section in visibleSections" :key="section.key" class="product-section">
          <div class="section-header">
            <h2 class="section-title">{{ section.title }}</h2>
            <p class="section-subtitle">{{ section.subtitle }}</p>
          </div>
          <div class="products-carousel-container">
            <button
              class="carousel-btn prev-btn"
              @click="scrollCarousel(section.key, -1)"
              aria-label="Previous"
            >
              <svg viewBox="0 0 24 24" fill="currentColor">
                <path d="M15.41 7.41L14 6l-6 6 6 6 1.41-1.41L10.83 12z" />
              </svg>
            </button>
            <div class="products-carousel" :ref="`carousel-${section.key}`">
              <div class="products-track">
                <div
                  v-for="product in section.products"
                  :key="product.id"
                  class="product-circle-wrapper"
                >
                  <ProductCard :product="product" @product-click="viewProduct" @buy-now="buyNow" />
                </div>
              </div>
            </div>
            <button
              class="carousel-btn next-btn"
              @click="scrollCarousel(section.key, 1)"
              aria-label="Next"
            >
              <svg viewBox="0 0 24 24" fill="currentColor">
                <path d="M8.59 16.59L10 18l6-6-6-6-1.41 1.41L13.17 12z" />
              </svg>
            </button>
          </div>
        </section>

        <!-- Fallback when no products -->
        <section v-if="hasNoProducts" class="no-products">
          <div class="section-header">
            <h2 class="section-title">No Products Available</h2>
            <p class="section-subtitle">Please check back later or try a different category.</p>
          </div>
        </section>
      </div>
    </main>

    <AppFooter />
  </div>
</template>

<script>
import AppHeader from '../layout/AppHeader.vue'
import AppFooter from '../layout/AppFooter.vue'
import ProductCard from './ProductCard.vue'

export default {
  name: 'ProductPage',
  components: {
    AppHeader,
    AppFooter,
    ProductCard,
  },
  props: {
    productType: {
      type: String,
      default: 'product',
    },
    pageConfig: {
      type: Object,
      default: () => ({ title: 'Products', subtitle: 'Loading...', emoji: '📦' }),
    },
    products: {
      type: Array,
      default: () => [],
    },
    sections: {
      type: Array,
      default: () => [],
    },
  },
  data() {
    return {
      searchQuery: '',
      activeCategory: 'All',
      sortBy: 'name',
    }
  },
  mounted() {
    console.log('ProductPage mounted:', {
      productType: this.productType,
      products: this.products?.length || 0,
      sections: this.sections?.length || 0,
      pageConfig: this.pageConfig,
    })
  },
  computed: {
    categories() {
      if (!this.products || !Array.isArray(this.products)) return ['All']
      const uniqueCategories = new Set(['All'])
      this.products.forEach((product) => {
        if (product.category) {
          uniqueCategories.add(product.category)
        }
      })
      return Array.from(uniqueCategories)
    },

    filteredProducts() {
      if (!this.products || !Array.isArray(this.products)) return []
      let filtered = this.products

      if (this.searchQuery) {
        filtered = filtered.filter(
          (product) =>
            product.name.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
            product.description.toLowerCase().includes(this.searchQuery.toLowerCase()),
        )
      }

      if (this.activeCategory !== 'All') {
        filtered = filtered.filter((product) => product.category === this.activeCategory)
      }

      return this.sortProducts(filtered)
    },

    productSections() {
      if (!this.sections || !Array.isArray(this.sections)) return []
      return this.sections.map((section) => ({
        ...section,
        products:
          this.filteredProducts.filter((product) => product.series === section.series) || [],
      }))
    },

    visibleSections() {
      return this.productSections.filter(
        (section) => section.products && section.products.length > 0,
      )
    },

    hasNoProducts() {
      return (
        this.productSections.length === 0 ||
        this.productSections.every((s) => !s.products || s.products.length === 0)
      )
    },

    hasActiveFilters() {
      return this.activeCategory !== 'All' || this.sortBy !== 'name'
    },
  },
  methods: {
    handleSearch(query) {
      this.searchQuery = query
    },

    sortProducts(products) {
      if (!products || !Array.isArray(products)) return []
      const sorted = [...products]

      switch (this.sortBy) {
        case 'price-low':
          return sorted.sort((a, b) => this.getPrice(a.price) - this.getPrice(b.price))
        case 'price-high':
          return sorted.sort((a, b) => this.getPrice(b.price) - this.getPrice(a.price))
        default:
          return sorted.sort((a, b) => a.name.localeCompare(b.name))
      }
    },

    getPrice(priceString) {
      return parseInt(priceString.replace(/[^0-9]/g, ''))
    },

    viewProduct(product) {
      // Save product to localStorage
      localStorage.setItem(`product_${product.id}`, JSON.stringify(product))
      // Navigate to product detail page
      this.$router.push({
        name: 'ProductDetail',
        params: {
          type: this.productType.toLowerCase(),
          id: product.id,
        },
      })
    },

    buyNow(product) {
      console.log('Buy now:', product)
    },

    scrollCarousel(sectionKey, direction) {
      const carousel = this.$refs[`carousel-${sectionKey}`]
      if (carousel && carousel[0]) {
        const scrollAmount = 380 * direction
        carousel[0].scrollBy({
          left: scrollAmount,
          behavior: 'smooth',
        })
      }
    },

    getCategoryCount(category) {
      if (category === 'All') {
        return this.products?.length || 0
      }
      return this.products?.filter((product) => product.category === category).length || 0
    },

    clearAllFilters() {
      this.activeCategory = 'All'
      this.sortBy = 'name'
    },
  },
}
</script>

<style scoped>
/* Base styles that work for all product types */
.iphone-page,
.ipad-page,
.macbook-page,
.watch-page {
  min-height: 100vh;
}

.iphone-page {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.ipad-page {
  background: linear-gradient(135deg, #ff6b35 0%, #f7931e 100%);
}

.macbook-page {
  background: linear-gradient(135deg, #2c3e50 0%, #3498db 100%);
}

.watch-page {
  background: linear-gradient(135deg, #ff6b6b 0%, #ee5a52 100%);
}

.airpods-page {
  background: linear-gradient(135deg, #2c3e50 0%, #34495e 100%);
}

.main-content {
  padding: 0;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

/* Page Hero */
.page-hero {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 60px;
  align-items: center;
  padding: 80px 0;
  min-height: 70vh;
}

.hero-content {
  text-align: left;
}

.page-title {
  font-size: 72px;
  font-weight: 900;
  margin-bottom: 20px;
  line-height: 1.1;
}

.gradient-text {
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  color: #ffffff; /* Fallback color */
  display: inline-block;
}

.iphone-page .gradient-text {
  background: linear-gradient(135deg, #ffffff 0%, #e0e0e0 100%);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.ipad-page .gradient-text {
  background: linear-gradient(135deg, #ffffff 0%, #f8f9fa 100%);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.macbook-page .gradient-text {
  background: linear-gradient(135deg, #ffffff 0%, #bdc3c7 100%);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.watch-page .gradient-text {
  background: linear-gradient(135deg, #ffffff 0%, #f8f9fa 100%);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.airpods-page .gradient-text {
  background: linear-gradient(135deg, #ffffff 0%, #f8f9fa 100%);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.page-subtitle {
  font-size: 24px;
  color: #ffffff;
  font-weight: 400;
  line-height: 1.4;
}

.hero-image {
  display: flex;
  justify-content: center;
  align-items: center;
}

.product-showcase {
  position: relative;
  width: 400px;
  height: 300px;
}

.product-stack {
  position: relative;
  width: 100%;
  height: 100%;
}

.product {
  position: absolute;
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 50px;
  color: rgb(255, 255, 255);
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  animation: float 6s ease-in-out infinite;
}

.iphone-page .product {
  width: 120px;
  height: 200px;
  background: linear-gradient(135deg, #007aff 0%, #5856d6 100%);
}

.ipad-page .product {
  width: 160px;
  height: 220px;
  background: linear-gradient(135deg, #ff6b6b 0%, #ffd83d7a 100%);
}

.macbook-page .product {
  width: 180px;
  height: 120px;
  background: linear-gradient(135deg, #3498db 0%, #2c3e50 100%);
}

.watch-page .product {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  background: linear-gradient(135deg, #ff6b6b 0%, #ee5a52 100%);
}

.airpods-page .product {
  width: 120px;
  height: 120px;
  border-radius: 20px;
  background: linear-gradient(135deg, #b0b0b0 10%, #f8f8f8 100%);
  border: 2px solid rgba(255, 255, 255, 0.8);
}

.product-1 {
  top: 0;
  left: 50%;
  transform: translateX(-50%);
  z-index: 3;
  animation-delay: 0s;
}

.product-2 {
  top: 30px;
  left: 10%;
  transform: rotate(-10deg);
  z-index: 2;
  opacity: 0.8;
  animation-delay: -2s;
}

.product-3 {
  top: 30px;
  right: 10%;
  transform: rotate(10deg);
  z-index: 1;
  opacity: 0.6;
  animation-delay: -4s;
}

@keyframes float {
  0%,
  100% {
    transform: translateY(0px) rotate(var(--rotate, 0deg));
  }
  50% {
    transform: translateY(-15px) rotate(var(--rotate, 0deg));
  }
}

/* Modern Filter Section */
.modern-filter-section {
  margin-bottom: 60px;
}

.filter-container {
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.95), rgba(255, 255, 255, 0.98));
  border-radius: 24px;
  box-shadow:
    0 20px 60px rgba(0, 0, 0, 0.1),
    0 8px 32px rgba(0, 0, 0, 0.08),
    inset 0 1px 0 rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  overflow: hidden;
  position: relative;
}

.filter-container::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: linear-gradient(90deg, var(--active-color), #667eea, #764ba2);
}

/* .filter-header {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 24px 32px 20px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.05);
} */

.filter-content {
  padding: 24px 32px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.filter-main-row {
  display: flex;
  gap: 40px;
  align-items: flex-start;
}

.category-section {
  flex: 1;
  min-width: 0;
}

.category-section .section-label {
  display: block;
  font-size: 14px;
  font-weight: 600;
  color: #666;
  margin-bottom: 12px;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.sort-section {
  width: 220px;
  flex-shrink: 0;
}

.category-pills {
  display: flex;
  flex-wrap: nowrap;
  gap: 8px;
  overflow-x: auto;
  scrollbar-width: none;
  -ms-overflow-style: none;
}

.category-pills::-webkit-scrollbar {
  display: none;
}

.sort-section {
  flex-shrink: 0;
  width: 220px;
  margin-left: auto;
}

.category-pill {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 10px 16px;
  border: 2px solid rgba(0, 0, 0, 0.1);
  background: rgba(255, 255, 255, 0.8);
  border-radius: 20px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  color: #666;
  position: relative;
  overflow: hidden;
}

.category-pill::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.4), transparent);
  transition: left 0.5s;
}

.category-pill:hover::before {
  left: 100%;
}

.category-pill:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
  border-color: rgba(0, 0, 0, 0.2);
}

.category-pill.active {
  color: rgb(0, 250, 88);
  border-color: var(--active-color);
  background: var(--active-color);
  background-color: #1c24be;
  box-shadow: 0 4px 20px rgba(0, 113, 227, 0.3);
  transform: translateY(-1px);
}

.pill-text {
  font-size: 14px;
  white-space: nowrap;
}

.pill-count {
  background: rgba(255, 255, 255, 0.2);
  color: inherit;
  padding: 2px 6px;
  border-radius: 10px;
  font-size: 12px;
  font-weight: 600;
  min-width: 18px;
  text-align: center;
}

.category-pill.active .pill-count {
  background: rgba(255, 255, 255, 0.3);
}

.sort-section .section-label {
  display: block;
  font-size: 14px;
  font-weight: 600;
  color: #666;
  margin-bottom: 12px;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.sort-dropdown-wrapper {
  position: relative;
  max-width: 280px;
}

.modern-sort-select {
  width: 100%;
  padding: 14px 20px;
  border: 2px solid rgba(0, 0, 0, 0.1);
  border-radius: 16px;
  font-size: 14px;
  font-weight: 500;
  outline: none;
  cursor: pointer;
  background: rgba(255, 255, 255, 0.9);
  transition: all 0.3s ease;
  appearance: none;
  color: #1d1d1f;
}

.modern-sort-select:hover {
  border-color: rgba(0, 0, 0, 0.2);
  background: rgba(255, 255, 255, 1);
}

.modern-sort-select:focus {
  border-color: var(--active-color);
  box-shadow: 0 0 0 3px rgba(0, 113, 227, 0.1);
}

.dropdown-arrow {
  position: absolute;
  right: 16px;
  top: 50%;
  transform: translateY(-50%);
  color: #666;
  font-size: 12px;
  pointer-events: none;
}

/* Responsive Design */
@media (max-width: 1024px) {
  .filter-main-row {
    gap: 30px;
  }

  .sort-section {
    width: 200px;
  }
}

@media (max-width: 768px) {
  .filter-main-row {
    flex-direction: column;
    gap: 24px;
  }

  .category-section {
    flex: none;
  }

  .sort-section {
    width: 220px;
    align-self: flex-start;
  }

  .filter-container {
    margin: 0 -10px;
  }

  .filter-content {
    padding: 20px 24px;
  }
}

@media (max-width: 480px) {
  .filter-content {
    padding: 16px 20px;
  }

  .filter-main-row {
    gap: 16px;
  }

  .category-pills {
    gap: 6px;
    flex-wrap: wrap;
    overflow-x: visible;
  }

  .category-pill {
    padding: 8px 12px;
    font-size: 13px;
  }

  .sort-section {
    width: 180px;
  }

  .modern-sort-select {
    padding: 12px 16px;
    font-size: 13px;
  }
}

/* Product Sections */
.product-section {
  margin-bottom: 80px;
}

.section-header {
  text-align: center;
  margin-bottom: 50px;
}

.section-title {
  font-size: 48px;
  font-weight: 700;
  color: #ffffff;
  margin-bottom: 12px;
}

.section-subtitle {
  font-size: 20px;
  color: rgba(255, 255, 255, 0.8);
  font-weight: 400;
}

/* Products Carousel - Modern Circular Slide */
.products-carousel-container {
  position: relative;
  margin: 40px 0;
  padding: 0 60px;
}

.products-carousel {
  overflow-x: auto;
  overflow-y: hidden;
  scroll-behavior: smooth;
  scrollbar-width: none;
  -ms-overflow-style: none;
  border-radius: 20px;
}

.products-carousel::-webkit-scrollbar {
  display: none;
}

.products-track {
  display: flex;
  gap: 30px;
  padding: 20px 10px;
}

.product-circle-wrapper {
  flex: 0 0 auto;
  width: 320px;
  position: relative;
  animation: slideIn 0.6s ease-out backwards;
}

.product-circle-wrapper:nth-child(1) {
  animation-delay: 0.1s;
}
.product-circle-wrapper:nth-child(2) {
  animation-delay: 0.2s;
}
.product-circle-wrapper:nth-child(3) {
  animation-delay: 0.3s;
}
.product-circle-wrapper:nth-child(4) {
  animation-delay: 0.4s;
}
.product-circle-wrapper:nth-child(5) {
  animation-delay: 0.5s;
}

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateX(50px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}
.carousel-btn {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  width: 48px;
  height: 48px;
  border-radius: 50%;
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.95), rgba(255, 255, 255, 0.85));
  border: 2px solid rgba(255, 255, 255, 0.5);
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 10;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow:
    0 4px 12px rgba(0, 0, 0, 0.15),
    0 0 0 1px rgba(0, 0, 0, 0.05);
  backdrop-filter: blur(10px);
}

.carousel-btn:hover {
  transform: translateY(-50%) scale(1.1);
  background: linear-gradient(135deg, #ffffff, #f8f8f8);
  box-shadow:
    0 6px 20px rgba(0, 0, 0, 0.2),
    0 0 0 2px rgba(0, 102, 204, 0.3);
}

.carousel-btn:active {
  transform: translateY(-50%) scale(0.95);
}

.carousel-btn svg {
  width: 24px;
  height: 24px;
  color: #1d1d1f;
  transition: color 0.3s ease;
}

.carousel-btn:hover svg {
  color: #0066cc;
}

.prev-btn {
  left: 0;
}

.next-btn {
  right: 0;
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
  gap: 30px;
}

/* No products fallback */
.no-products {
  margin-bottom: 80px;
  text-align: center;
  padding: 60px 20px;
}

.no-products .section-title {
  color: rgba(255, 255, 255, 0.9);
}

.no-products .section-subtitle {
  color: rgba(255, 255, 255, 0.7);
}
</style>
