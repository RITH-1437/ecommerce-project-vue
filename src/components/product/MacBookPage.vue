<template>
  <div class="macbook-page">
    <AppHeader @search="handleSearch" />

    <main class="main-content">
      <div class="container">
        <!-- Page Hero -->
        <section class="page-hero">
          <div class="hero-content">
            <h1 class="page-title">
              <span class="gradient-text">MacBook</span>
            </h1>
            <p class="page-subtitle">Supercharged for pros.</p>
          </div>
          <div class="hero-image">
            <div class="macbook-showcase">
              <div class="macbook-stack">
                <div class="macbook macbook-1">💻</div>
                <div class="macbook macbook-2">💻</div>
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

        <!-- MacBook Pro Section -->
        <section class="product-section" v-if="filteredMacBookPro.length > 0">
          <div class="section-header">
            <h2 class="section-title">MacBook Pro</h2>
            <p class="section-subtitle">Mind-blowing. Head-turning.</p>
          </div>
          <div class="products-grid">
            <ProductCard
              v-for="macbook in filteredMacBookPro"
              :key="macbook.id"
              :product="macbook"
              @product-click="viewProduct"
              @buy-now="buyNow"
            />
          </div>
        </section>

        <!-- MacBook Air Section -->
        <section class="product-section" v-if="filteredMacBookAir.length > 0">
          <div class="section-header">
            <h2 class="section-title">MacBook Air</h2>
            <p class="section-subtitle">Designed to go places.</p>
          </div>
          <div class="products-grid">
            <ProductCard
              v-for="macbook in filteredMacBookAir"
              :key="macbook.id"
              :product="macbook"
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
  name: 'MacBookPage',
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
      categories: ['All', 'MacBook Pro', 'MacBook Air'],
      macbooks: [
        // MacBook Pro
        {
          id: 1,
          name: 'MacBook Pro 16-inch M5',
          series: 'MacBook Pro',
          price: '$1,899',
          description: 'The most powerful MacBook Pro ever.',
          colors: ['#2c2c2e', '#e8e8ed'],
          badge: 'New',
          category: 'MacBook Pro',
        },
        {
          id: 2,
          name: 'MacBook Pro 14-inch M5',
          series: 'MacBook Pro',
          price: '$1,699',
          description: 'Supercharged for pros.',
          colors: ['#2c2c2e', '#e8e8ed'],
          badge: 'New',
          category: 'MacBook Pro',
        },

        // MacBook Air
        {
          id: 3,
          name: 'MacBook Air 15-inch M4',
          series: 'MacBook Air',
          price: '$1,399',
          description: 'Impressively big. Impossibly thin.',
          colors: ['#2c2c2e', '#e8e8ed', '#c9b037', '#8b4513'],
          category: 'MacBook Air',
        },
        {
          id: 4,
          name: 'MacBook Air 13-inch M4',
          series: 'MacBook Air',
          price: '$1,099',
          description: 'Designed to go places.',
          colors: ['#2c2c2e', '#e8e8ed', '#c9b037', '#8b4513'],
          category: 'MacBook Air',
        },
      ],
    }
  },
  computed: {
    filteredProducts() {
      let filtered = this.macbooks

      if (this.searchQuery) {
        filtered = filtered.filter(
          (macbook) =>
            macbook.name.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
            macbook.description.toLowerCase().includes(this.searchQuery.toLowerCase()),
        )
      }

      if (this.activeCategory !== 'All') {
        filtered = filtered.filter((macbook) => macbook.category === this.activeCategory)
      }

      return this.sortProducts(filtered)
    },

    filteredMacBookPro() {
      return this.filteredProducts.filter((macbook) => macbook.series === 'MacBook Pro')
    },

    filteredMacBookAir() {
      return this.filteredProducts.filter((macbook) => macbook.series === 'MacBook Air')
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
.macbook-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #1e3c72 0%, #2a5298 100%);
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
  background: linear-gradient(135deg, #74b9ff 0%, #0984e3 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
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

.macbook-showcase {
  position: relative;
  width: 500px;
  height: 300px;
}

.macbook-stack {
  position: relative;
  width: 100%;
  height: 100%;
}

.macbook {
  position: absolute;
  width: 200px;
  height: 140px;
  background: linear-gradient(135deg, #74b9ff 0%, #0984e3 100%);
  border-radius: 15px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 60px;
  color: white;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  animation: float 6s ease-in-out infinite;
}

.macbook-1 {
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 2;
  animation-delay: 0s;
}

.macbook-2 {
  top: 60%;
  left: 60%;
  transform: translate(-50%, -50%) rotate(5deg);
  z-index: 1;
  opacity: 0.7;
  animation-delay: -3s;
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
  background: #74b9ff;
  color: white;
  border-color: #74b9ff;
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

  .macbook-showcase {
    width: 300px;
    height: 200px;
  }

  .macbook {
    width: 120px;
    height: 80px;
    font-size: 30px;
  }
}
</style>
