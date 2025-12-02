<template>
  <div class="product-grid" :class="[`grid-${columns}`, `theme-${theme}`, { compact: compact }]">
    <ProductCard
      v-for="product in displayProducts"
      :key="product.id"
      :product="product"
      :show-colors="showProductColors"
      :show-description="showProductDescription"
      :card-size="cardSize"
      :theme="theme"
      :button-text="buyButtonText"
      :show-badge="showBadges"
      @product-click="handleProductClick"
      @buy-now="handleBuyNow"
    />
  </div>
  <div v-if="showPagination && totalPages > 1" class="pagination">
    <button
      v-for="page in totalPages"
      :key="page"
      @click="currentPage = page"
      :class="{ active: currentPage === page }"
      class="page-button"
    >
      {{ page }}
    </button>
  </div>
</template>

<script>
import ProductCard from './ProductCard.vue'

export default {
  name: 'ProductGrid',
  components: {
    ProductCard,
  },
  props: {
    products: {
      type: Array,
      required: true,
      default: () => [],
    },
    columns: {
      type: [Number, String],
      default: 3,
      validator: (value) => [1, 2, 3, 4, 5, 6].includes(Number(value)),
    },
    itemsPerPage: {
      type: Number,
      default: 12,
    },
    showPagination: {
      type: Boolean,
      default: false,
    },
    showProductColors: {
      type: Boolean,
      default: true,
    },
    showProductDescription: {
      type: Boolean,
      default: true,
    },
    showBadges: {
      type: Boolean,
      default: true,
    },
    cardSize: {
      type: String,
      default: 'medium',
      validator: (value) => ['small', 'medium', 'large'].includes(value),
    },
    theme: {
      type: String,
      default: 'light',
      validator: (value) => ['light', 'dark'].includes(value),
    },
    buyButtonText: {
      type: String,
      default: 'Buy Now',
    },
    compact: {
      type: Boolean,
      default: false,
    },
    sortBy: {
      type: String,
      default: 'name',
      validator: (value) => ['name', 'price', 'newest', 'popular'].includes(value),
    },
    filterBy: {
      type: Object,
      default: () => ({}),
    },
  },
  data() {
    return {
      currentPage: 1,
    }
  },
  computed: {
    filteredProducts() {
      let filtered = [...this.products]

      // Apply filters
      Object.keys(this.filterBy).forEach((key) => {
        if (this.filterBy[key] && this.filterBy[key] !== '') {
          filtered = filtered.filter((product) => {
            const productValue = product[key]
            const filterValue = this.filterBy[key]

            if (typeof productValue === 'string') {
              return productValue.toLowerCase().includes(filterValue.toLowerCase())
            }
            return productValue === filterValue
          })
        }
      })

      // Apply sorting
      filtered.sort((a, b) => {
        switch (this.sortBy) {
          case 'price':
            return a.price - b.price
          case 'newest':
            return new Date(b.releaseDate || 0) - new Date(a.releaseDate || 0)
          case 'popular':
            return (b.rating || 0) - (a.rating || 0)
          default:
            return a.name.localeCompare(b.name)
        }
      })

      return filtered
    },
    totalPages() {
      return Math.ceil(this.filteredProducts.length / this.itemsPerPage)
    },
    displayProducts() {
      if (!this.showPagination) return this.filteredProducts

      const start = (this.currentPage - 1) * this.itemsPerPage
      const end = start + this.itemsPerPage
      return this.filteredProducts.slice(start, end)
    },
  },
  methods: {
    handleProductClick(product) {
      this.$emit('product-click', product)
    },
    handleBuyNow(product) {
      this.$emit('buy-now', product)
    },
  },
  emits: ['product-click', 'buy-now'],
  watch: {
    products() {
      this.currentPage = 1
    },
    filterBy: {
      handler() {
        this.currentPage = 1
      },
      deep: true,
    },
  },
}
</script>

<style scoped>
.product-grid {
  display: grid;
  gap: 24px;
  padding: 20px 0;
  transition: all 0.3s ease;
}

.grid-1 {
  grid-template-columns: 1fr;
}
.grid-2 {
  grid-template-columns: repeat(2, 1fr);
}
.grid-3 {
  grid-template-columns: repeat(3, 1fr);
}
.grid-4 {
  grid-template-columns: repeat(4, 1fr);
}
.grid-5 {
  grid-template-columns: repeat(5, 1fr);
}
.grid-6 {
  grid-template-columns: repeat(6, 1fr);
}

.compact {
  gap: 16px;
}

.theme-dark {
  background: #1a1a1a;
  border-radius: 12px;
  padding: 20px;
}

.pagination {
  display: flex;
  justify-content: center;
  gap: 8px;
  margin-top: 32px;
}

.page-button {
  padding: 8px 16px;
  border: 2px solid #007aff;
  background: white;
  color: #007aff;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-weight: 500;
}

.page-button:hover {
  background: #007aff;
  color: white;
  transform: translateY(-1px);
}

.page-button.active {
  background: #007aff;
  color: white;
}

@media (max-width: 1024px) {
  .grid-4,
  .grid-5,
  .grid-6 {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (max-width: 768px) {
  .grid-3,
  .grid-4,
  .grid-5,
  .grid-6 {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 480px) {
  .grid-2,
  .grid-3,
  .grid-4,
  .grid-5,
  .grid-6 {
    grid-template-columns: 1fr;
  }
}
</style>
