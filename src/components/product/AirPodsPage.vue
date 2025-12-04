<template>
  <div class="airpods-page">
    <AppHeader @search="handleSearch" />

    <main class="main-content">
      <div class="container">
        <!-- Page Hero -->
        <section class="page-hero">
          <div class="hero-content">
            <h1 class="page-title">
              <span class="gradient-text">AirPods</span>
            </h1>
            <p class="page-subtitle">Magic like you've never heard.</p>
          </div>
          <div class="hero-image">
            <div class="airpods-showcase">
              <div class="airpods-stack">
                <div class="airpods airpods-1">🎧</div>
                <div class="airpods airpods-2">🎧</div>
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

        <!-- AirPods 4 Section -->
        <section class="product-section" v-if="filteredAirPods4.length > 0">
          <div class="section-header">
            <h2 class="section-title">AirPods 4</h2>
            <p class="section-subtitle">Updated fit. Optimal comfort.</p>
          </div>
          <div class="products-grid">
            <ProductCard
              v-for="airpod in filteredAirPods4"
              :key="airpod.id"
              :product="airpod"
              @product-click="viewProduct"
              @buy-now="buyNow"
            />
          </div>
        </section>

        <!-- AirPods Pro 3 Section -->
        <section class="product-section" v-if="filteredAirPodsPro.length > 0">
          <div class="section-header">
            <h2 class="section-title">AirPods Pro 3</h2>
            <p class="section-subtitle">Adaptive Audio. Now playing.</p>
          </div>
          <div class="products-grid">
            <ProductCard
              v-for="airpod in filteredAirPodsPro"
              :key="airpod.id"
              :product="airpod"
              @product-click="viewProduct"
              @buy-now="buyNow"
            />
          </div>
        </section>

        <!-- EarPods Section -->
        <section class="product-section" v-if="filteredEarPods.length > 0">
          <div class="section-header">
            <h2 class="section-title">EarPods</h2>
            <p class="section-subtitle">Simple. Affordable. Essential.</p>
          </div>
          <div class="products-grid">
            <ProductCard
              v-for="earpod in filteredEarPods"
              :key="earpod.id"
              :product="earpod"
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
  name: 'AirPodsPage',
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
      categories: ['All', 'AirPods 4', 'AirPods Pro', 'EarPods'],
      audioProducts: [
        // AirPods 4
        {
          id: 1,
          name: 'AirPods 4',
          series: 'AirPods 4',
          price: '$399',
          description: 'Updated fit. Optimal comfort.',
          colors: ['#ffffff'],
          badge: 'New',
          category: 'AirPods 4',
        },
        {
          id: 2,
          name: 'AirPods 4 ANC',
          series: 'AirPods 4',
          price: '$399',
          description: 'Active Noise Cancellation included.',
          colors: ['#ffffff'],
          badge: 'ANC',
          category: 'AirPods 4',
        },

        // AirPods Pro 3
        {
          id: 3,
          name: 'AirPods Pro 3',
          series: 'AirPods Pro',
          price: '$399',
          description: 'Adaptive Audio. Now playing.',
          colors: ['#ffffff'],
          badge: 'Pro',
          category: 'AirPods Pro',
        },

        // EarPods
        {
          id: 4,
          name: 'USB-C',
          series: 'EarPods',
          price: '$19',
          description: 'Universal compatibility.',
          colors: ['#ffffff'],
          category: 'EarPods',
        },
        {
          id: 5,
          name: 'Lightning Connector',
          series: 'EarPods',
          price: '$19',
          description: 'For iPhone and iPad.',
          colors: ['#ffffff'],
          category: 'EarPods',
        },
        {
          id: 6,
          name: '3.5mm Headphone Plug',
          series: 'EarPods',
          price: '$19',
          description: 'Classic wired connection.',
          colors: ['#ffffff'],
          category: 'EarPods',
        },
      ],
    }
  },
  computed: {
    filteredProducts() {
      let filtered = this.audioProducts

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

    filteredAirPods4() {
      return this.filteredProducts.filter((product) => product.series === 'AirPods 4')
    },

    filteredAirPodsPro() {
      return this.filteredProducts.filter((product) => product.series === 'AirPods Pro')
    },

    filteredEarPods() {
      return this.filteredProducts.filter((product) => product.series === 'EarPods')
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
        default:
          return sorted.sort((a, b) => a.name.localeCompare(b.name))
      }
    },

    getPrice(priceString) {
      return parseInt(priceString.replace(/[^0-9]/g, ''))
    },

    viewProduct(product) {
      console.log('View product:', product)
    },

    buyNow(product) {
      console.log('Buy now:', product)
    },
  },
}
</script>

<style scoped>
.airpods-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
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
  color: #1d1d1f;
  font-weight: 400;
  line-height: 1.4;
}

.hero-image {
  display: flex;
  justify-content: center;
  align-items: center;
}

.airpods-showcase {
  position: relative;
  width: 300px;
  height: 200px;
}

.airpods-stack {
  position: relative;
  width: 100%;
  height: 100%;
}

.airpods {
  position: absolute;
  width: 120px;
  height: 120px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 50px;
  color: #adadad;
  box-shadow:
    0 15px 40px rgba(102, 126, 234, 0.4),
    0 5px 15px rgba(0, 0, 0, 0.2),
    inset 0 1px 2px rgba(255, 255, 255, 0.2);
  animation: float 5s ease-in-out infinite;
  border: 2px solid rgba(255, 255, 255, 0.3);
  filter: drop-shadow(0 4px 8px rgba(0, 0, 0, 0.3));
  text-shadow:
    0 2px 4px rgba(0, 0, 0, 0.5),
    0 0 20px rgba(255, 0, 0, 0.4);
  font-family: 'Apple Color Emoji', 'Segoe UI Emoji', 'Segoe UI Symbol', 'Noto Color Emoji', sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

.airpods-1 {
  top: 40%;
  left: 30%;
  transform: translate(-50%, -50%);
  z-index: 2;
  animation-delay: 0s;
}

.airpods-2 {
  top: 50%;
  right: 30%;
  transform: translate(50%, -50%) rotate(15deg);
  z-index: 1;
  opacity: 0.8;
  animation-delay: -2.5s;
}

@keyframes float {
  0%,
  100% {
    transform: translate(-50%, -50%) rotate(var(--rotate, 0deg));
  }
  50% {
    transform: translate(-50%, -60%) rotate(var(--rotate, 0deg));
  }
}

/* Filter Section */
.filter-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 50px;
  padding: 30px;
  background: rgba(255, 255, 255, 0.9);
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
  background: rgba(255, 255, 255, 0.3);
  border-radius: 25px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  color: #1d1d1f;
}

.filter-tab:hover,
.filter-tab.active {
  background: #667eea;
  color: white;
  border-color: #667eea;
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

  .airpods-showcase {
    width: 200px;
    height: 150px;
  }

  .airpods {
    width: 80px;
    height: 80px;
    font-size: 25px;
  }
}
</style>
