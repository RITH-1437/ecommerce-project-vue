<template>
  <div class="watch-page">
    <AppHeader @search="handleSearch" />

    <main class="main-content">
      <div class="container">
        <!-- Page Hero -->
        <section class="page-hero">
          <div class="hero-content">
            <h1 class="page-title">
              <span class="gradient-text">Apple Watch</span>
            </h1>
            <p class="page-subtitle">A healthy leap ahead.</p>
          </div>
          <div class="hero-image">
            <div class="watch-showcase">
              <div class="watch-stack">
                <div class="watch watch-1">⌚</div>
                <div class="watch watch-2">⌚</div>
                <div class="watch watch-3">⌚</div>
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

        <!-- Apple Watch Series 11 Section -->
        <section class="product-section" v-if="filteredSeries11.length > 0">
          <div class="section-header">
            <h2 class="section-title">Apple Watch Series 11</h2>
            <p class="section-subtitle">The most advanced Apple Watch yet.</p>
          </div>
          <div class="products-grid">
            <ProductCard
              v-for="watch in filteredSeries11"
              :key="watch.id"
              :product="watch"
              @product-click="viewProduct"
              @buy-now="buyNow"
            />
          </div>
        </section>

        <!-- Apple Watch SE 3 Section -->
        <section class="product-section" v-if="filteredSE3.length > 0">
          <div class="section-header">
            <h2 class="section-title">Apple Watch SE 3</h2>
            <p class="section-subtitle">A great deal to love.</p>
          </div>
          <div class="products-grid">
            <ProductCard
              v-for="watch in filteredSE3"
              :key="watch.id"
              :product="watch"
              @product-click="viewProduct"
              @buy-now="buyNow"
            />
          </div>
        </section>

        <!-- Apple Watch Ultra 3 Section -->
        <section class="product-section" v-if="filteredUltra3.length > 0">
          <div class="section-header">
            <h2 class="section-title">Apple Watch Ultra 3</h2>
            <p class="section-subtitle">The most rugged and capable Apple Watch.</p>
          </div>
          <div class="products-grid">
            <ProductCard
              v-for="watch in filteredUltra3"
              :key="watch.id"
              :product="watch"
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
  name: 'WatchPage',
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
      categories: ['All', 'Series 11', 'SE 3', 'Ultra 3'],
      watches: [
        // Apple Watch Series 11
        {
          id: 1,
          name: 'Watch Series 11',
          series: 'Series 11',
          price: '$399',
          description: 'The most advanced Apple Watch yet.',
          colors: ['#000000', '#c9b037', '#e8e8ed', '#ff69b4'],
          badge: 'New',
          category: 'Series 11',
        },
        {
          id: 2,
          name: 'Watch Hermès Series 11',
          series: 'Series 11',
          price: '$399',
          description: 'Luxury meets innovation.',
          colors: ['#8b4513', '#000000'],
          badge: 'Hermès',
          category: 'Series 11',
        },

        // Apple Watch SE 3
        {
          id: 3,
          name: 'Watch SE 3',
          series: 'SE 3',
          price: '$399',
          description: 'A great deal to love.',
          colors: ['#ff6b6b', '#ffffff', '#000000'],
          category: 'SE 3',
        },

        // Apple Watch Ultra 3
        {
          id: 4,
          name: 'Watch Ultra 3',
          series: 'Ultra 3',
          price: '$399',
          description: 'The most rugged and capable.',
          colors: ['#2c2c2e', '#ffffff'],
          badge: 'Ultra',
          category: 'Ultra 3',
        },
        {
          id: 5,
          name: 'Watch Hermès Ultra 3',
          series: 'Ultra 3',
          price: '$399',
          description: 'Ultimate luxury and durability.',
          colors: ['#8b4513', '#000000'],
          badge: 'Hermès Ultra',
          category: 'Ultra 3',
        },
      ],
    }
  },
  computed: {
    filteredProducts() {
      let filtered = this.watches

      if (this.searchQuery) {
        filtered = filtered.filter(
          (watch) =>
            watch.name.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
            watch.description.toLowerCase().includes(this.searchQuery.toLowerCase()),
        )
      }

      if (this.activeCategory !== 'All') {
        filtered = filtered.filter((watch) => watch.category === this.activeCategory)
      }

      return this.sortProducts(filtered)
    },

    filteredSeries11() {
      return this.filteredProducts.filter((watch) => watch.series === 'Series 11')
    },

    filteredSE3() {
      return this.filteredProducts.filter((watch) => watch.series === 'SE 3')
    },

    filteredUltra3() {
      return this.filteredProducts.filter((watch) => watch.series === 'Ultra 3')
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
.watch-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
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
  background: linear-gradient(135deg, #ff9a9e 0%, #fecfef 100%);
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

.watch-showcase {
  position: relative;
  width: 300px;
  height: 300px;
}

.watch-stack {
  position: relative;
  width: 100%;
  height: 100%;
}

.watch {
  position: absolute;
  width: 80px;
  height: 80px;
  background: linear-gradient(135deg, #ff9a9e 0%, #fecfef 100%);
  border-radius: 15px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 30px;
  color: white;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
  animation: float 4s ease-in-out infinite;
}

.watch-1 {
  top: 40%;
  left: 45%;
  transform: translate(-50%, -50%);
  z-index: 3;
  animation-delay: 0s;
}

.watch-2 {
  top: 30%;
  left: 20%;
  transform: translate(-50%, -50%) rotate(-15deg);
  z-index: 2;
  opacity: 0.8;
  animation-delay: -1.5s;
}

.watch-3 {
  top: 60%;
  right: 20%;
  transform: translate(50%, -50%) rotate(15deg);
  z-index: 1;
  opacity: 0.6;
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
  background: #ff9a9e;
  color: white;
  border-color: #ff9a9e;
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

  .watch-showcase {
    width: 200px;
    height: 200px;
  }

  .watch {
    width: 60px;
    height: 60px;
    font-size: 20px;
  }
}
</style>
