<template>
  <div class="iphone-page">
    <AppHeader @search="handleSearch" />

    <main class="main-content">
      <div class="container">
        <!-- Page Hero -->
        <section class="page-hero">
          <div class="hero-content">
            <h1 class="page-title">
              <span class="gradient-text">iPhone</span>
            </h1>
            <p class="page-subtitle">
              The most advanced iPhone lineup ever. Built for Apple Intelligence.
            </p>
          </div>
          <div class="hero-image">
            <div class="phone-showcase">
              <div class="phone-stack">
                <div class="phone phone-1">📱</div>
                <div class="phone phone-2">📱</div>
                <div class="phone phone-3">📱</div>
              </div>
            </div>
          </div>
        </section>

        <!-- Filter Bar -->
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
              <option value="newest">Newest First</option>
            </select>
          </div>
        </section>

        <!-- iPhone 17 Series -->
        <section class="product-section" v-if="filteredIPhone17.length > 0">
          <div class="section-header">
            <h2 class="section-title">iPhone 17 Models</h2>
            <p class="section-subtitle">The latest and most powerful iPhone experience</p>
          </div>
          <div class="products-grid">
            <ProductCard
              v-for="phone in filteredIPhone17"
              :key="phone.id"
              :product="phone"
              @product-click="viewProduct"
              @buy-now="buyNow"
            />
          </div>
        </section>

        <!-- iPhone 16 Series -->
        <section class="product-section" v-if="filteredIPhone16.length > 0">
          <div class="section-header">
            <h2 class="section-title">iPhone 16 Models</h2>
            <p class="section-subtitle">Powerful performance meets incredible value</p>
          </div>
          <div class="products-grid">
            <ProductCard
              v-for="phone in filteredIPhone16"
              :key="phone.id"
              :product="phone"
              @product-click="viewProduct"
              @buy-now="buyNow"
            />
          </div>
        </section>

        <!-- iPhone Air -->
        <section class="product-section" v-if="filteredIPhoneAir.length > 0">
          <div class="section-header">
            <h2 class="section-title">iPhone Air</h2>
            <p class="section-subtitle">Ultra-thin design with premium features</p>
          </div>
          <div class="products-grid">
            <ProductCard
              v-for="phone in filteredIPhoneAir"
              :key="phone.id"
              :product="phone"
              @product-click="viewProduct"
              @buy-now="buyNow"
            />
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
  name: 'iPhonePage',
  components: {
    AppHeader,
    AppFooter,
    ProductCard,
  },
  data() {
    return {
      searchQuery: '',
      activeCategory: 'All',
      sortBy: 'name',
      categories: ['All', 'iPhone 17', 'iPhone 16', 'iPhone Air'],
      iphones: [
        // iPhone 17 Series
        {
          id: 1,
          name: 'iPhone 17 Pro Max',
          series: 'iPhone 17',
          price: '$1,299',
          originalPrice: null,
          description: 'Our most advanced Pro camera system. A17 Pro chip.',
          colors: ['#4a4a4a', '#c9b037', '#e8e8e8', '#8b4513'],
          badge: 'New',
          category: 'iPhone 17',
        },
        {
          id: 2,
          name: 'iPhone 17 Pro',
          series: 'iPhone 17',
          price: '$1,099',
          originalPrice: null,
          description: 'Pro camera system. A17 Pro chip. Titanium design.',
          colors: ['#4a4a4a', '#c9b037', '#e8e8e8', '#8b4513'],
          badge: 'New',
          category: 'iPhone 17',
        },
        {
          id: 3,
          name: 'iPhone 17',
          series: 'iPhone 17',
          price: '$899',
          originalPrice: null,
          description: 'Advanced dual-camera system. A17 chip. All-day battery.',
          colors: ['#ff69b4', '#87ceeb', '#98fb98', '#ffd700', '#000000'],
          badge: 'New',
          category: 'iPhone 17',
        },

        // iPhone Air
        {
          id: 4,
          name: 'iPhone Air',
          series: 'iPhone Air',
          price: '$999',
          originalPrice: null,
          description: 'Ultra-thin design. A16 Bionic chip. Premium materials.',
          colors: ['#e6f3ff', '#fff0f5', '#f0fff0'],
          badge: 'Coming Soon',
          category: 'iPhone Air',
        },

        // iPhone 16 Series
        {
          id: 5,
          name: 'iPhone 16 Plus',
          series: 'iPhone 16',
          price: '$899',
          originalPrice: '$949',
          description: 'Large 6.7-inch display. Advanced camera features.',
          colors: ['#000080', '#4169e1', '#ffffff'],
          category: 'iPhone 16',
        },
        {
          id: 6,
          name: 'iPhone 16',
          series: 'iPhone 16',
          price: '$799',
          originalPrice: '$849',
          description: 'Perfect balance of features and performance.',
          colors: ['#000080', '#4169e1', '#ffffff'],
          category: 'iPhone 16',
        },
        {
          id: 7,
          name: 'iPhone 16e',
          series: 'iPhone 16',
          price: '$699',
          originalPrice: '$749',
          description: 'Essential features at an incredible value.',
          colors: ['#000000', '#ffffff', '#c0c0c0'],
          category: 'iPhone 16',
        },
      ],
    }
  },
  computed: {
    filteredProducts() {
      let filtered = this.iphones

      // Filter by search
      if (this.searchQuery) {
        filtered = filtered.filter(
          (phone) =>
            phone.name.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
            phone.description.toLowerCase().includes(this.searchQuery.toLowerCase()),
        )
      }

      // Filter by category
      if (this.activeCategory !== 'All') {
        filtered = filtered.filter((phone) => phone.category === this.activeCategory)
      }

      // Sort products
      return this.sortProducts(filtered)
    },

    filteredIPhone17() {
      return this.filteredProducts.filter((phone) => phone.series === 'iPhone 17')
    },

    filteredIPhone16() {
      return this.filteredProducts.filter((phone) => phone.series === 'iPhone 16')
    },

    filteredIPhoneAir() {
      return this.filteredProducts.filter((phone) => phone.series === 'iPhone Air')
    },
  },
  methods: {
    handleSearch(query) {
      this.searchQuery = query
    },

    sortProducts(products) {
      const sorted = [...products]

      switch (this.sortBy) {
        case 'price-low':
          return sorted.sort((a, b) => this.getPrice(a.price) - this.getPrice(b.price))
        case 'price-high':
          return sorted.sort((a, b) => this.getPrice(b.price) - this.getPrice(a.price))
        case 'newest':
          return sorted.sort((a, b) => b.id - a.id)
        default:
          return sorted.sort((a, b) => a.name.localeCompare(b.name))
      }
    },

    getPrice(priceString) {
      return parseInt(priceString.replace(/[^0-9]/g, ''))
    },

    viewProduct(product) {
      console.log('View product:', product)
      // Navigate to product detail page
    },

    buyNow(product) {
      console.log('Buy now:', product)
      // Add to cart or navigate to purchase
    },
  },
}
</script>

<style scoped>
.iphone-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
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
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.page-subtitle {
  font-size: 24px;
  color: #86868b;
  font-weight: 400;
  line-height: 1.4;
}

.hero-image {
  display: flex;
  justify-content: center;
  align-items: center;
}

.phone-showcase {
  position: relative;
  width: 300px;
  height: 400px;
}

.phone-stack {
  position: relative;
  width: 100%;
  height: 100%;
}

.phone {
  position: absolute;
  width: 120px;
  height: 250px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 25px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 40px;
  color: white;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.2);
  animation: float 6s ease-in-out infinite;
}

.phone-1 {
  top: 0;
  left: 50%;
  transform: translateX(-50%) rotate(-5deg);
  z-index: 3;
  animation-delay: 0s;
}

.phone-2 {
  top: 50px;
  left: 20%;
  transform: rotate(-15deg);
  z-index: 2;
  opacity: 0.8;
  animation-delay: -2s;
}

.phone-3 {
  top: 50px;
  right: 20%;
  transform: rotate(15deg);
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
    transform: translateY(-20px) rotate(var(--rotate, 0deg));
  }
}

/* Filter Section */
.filter-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 50px;
  padding: 30px;
  background: white;
  border-radius: 20px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.1);
}

.filter-tabs {
  display: flex;
  gap: 8px;
}

.filter-tab {
  padding: 12px 24px;
  border: 2px solid #f5f5f7;
  background: white;
  border-radius: 25px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.filter-tab:hover,
.filter-tab.active {
  background: #0071e3;
  color: white;
  border-color: #0071e3;
}

.sort-select {
  padding: 12px 20px;
  border: 2px solid #f5f5f7;
  border-radius: 25px;
  font-weight: 500;
  outline: none;
  cursor: pointer;
  background: white;
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
  color: #1d1d1f;
  margin-bottom: 12px;
}

.section-subtitle {
  font-size: 20px;
  color: #86868b;
  font-weight: 400;
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
  gap: 30px;
}

/* Responsive Design */
@media (max-width: 1024px) {
  .page-hero {
    grid-template-columns: 1fr;
    gap: 40px;
    text-align: center;
    padding: 60px 0;
  }

  .page-title {
    font-size: 56px;
  }

  .page-subtitle {
    font-size: 20px;
  }
}

@media (max-width: 768px) {
  .page-title {
    font-size: 40px;
  }

  .page-subtitle {
    font-size: 18px;
  }

  .filter-section {
    flex-direction: column;
    gap: 20px;
  }

  .filter-tabs {
    flex-wrap: wrap;
    justify-content: center;
  }

  .section-title {
    font-size: 36px;
  }

  .products-grid {
    grid-template-columns: 1fr;
    gap: 20px;
  }

  .phone-showcase {
    width: 200px;
    height: 300px;
  }

  .phone {
    width: 80px;
    height: 160px;
    font-size: 24px;
  }
}
</style>
