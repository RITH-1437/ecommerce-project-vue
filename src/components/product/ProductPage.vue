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

        <!-- Filter Section -->
        <section class="filter-section">
          <div class="filter-tabs">
            <button
              v-for="category in categories"
              :key="category"
              @click="activeCategory = category"
              :class="['filter-tab', { active: activeCategory === category }]"
            >
              {{ category }}
            </button>
          </div>
          <div class="sort-options">
            <select v-model="sortBy" class="sort-select">
              <option value="name">Sort by Name</option>
              <option value="price-low">Price: Low to High</option>
              <option value="price-high">Price: High to Low</option>
            </select>
          </div>
        </section>

        <!-- Dynamic Product Sections -->
        <section v-for="section in visibleSections" :key="section.key" class="product-section">
          <div class="section-header">
            <h2 class="section-title">{{ section.title }}</h2>
            <p class="section-subtitle">{{ section.subtitle }}</p>
          </div>
          <div class="products-grid">
            <ProductCard
              v-for="product in section.products"
              :key="product.id"
              :product="product"
              @product-click="viewProduct"
              @buy-now="buyNow"
            />
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
  color: white;
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
  background: linear-gradient(135deg, #ff6b6b 0%, #ffd93d 100%);
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
  background: linear-gradient(135deg, #ffffff 0%, #f8f8f8 100%);
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

/* Filter Section */
.filter-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 50px;
  padding: 30px;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 20px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(20px);
}

.filter-tabs {
  display: flex;
  gap: 8px;
}

.filter-tab {
  padding: 12px 24px;
  border: 2px solid rgba(255, 255, 255, 0.2);
  background: rgba(255, 255, 255, 0.1);
  border-radius: 25px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  color: #1d1d1f;
}

.filter-tab:hover,
.filter-tab.active {
  color: white;
  border-color: var(--active-color);
  background: var(--active-color);
}

.iphone-page {
  --active-color: #007aff;
}

.ipad-page {
  --active-color: #ff6b6b;
}

.macbook-page {
  --active-color: #3498db;
}

.watch-page {
  --active-color: #ff6b6b;
}

.airpods-page {
  --active-color: #667eea;
}

.sort-select {
  padding: 12px 20px;
  border: 2px solid rgba(255, 255, 255, 0.2);
  border-radius: 25px;
  font-weight: 500;
  outline: none;
  cursor: pointer;
  background: rgba(255, 255, 255, 0.9);
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
