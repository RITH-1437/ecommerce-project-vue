<template>
  <div class="props-demo">
    <h2>Advanced Props Usage Examples</h2>

    <!-- Example 1: Basic ProductCard with minimal props -->
    <section class="demo-section">
      <h3>1. Basic Product Card (Minimal Props)</h3>
      <ProductCard :product="sampleProducts[0]" />
    </section>

    <!-- Example 2: Customized ProductCard with all props -->
    <section class="demo-section">
      <h3>2. Fully Customized Product Card</h3>
      <ProductCard
        :product="sampleProducts[1]"
        :show-colors="false"
        :show-description="true"
        card-size="large"
        theme="dark"
        button-text="Add to Wishlist"
        :show-badge="true"
        @product-click="handleProductClick"
        @buy-now="handleBuyNow"
      />
    </section>

    <!-- Example 3: ProductGrid with various configurations -->
    <section class="demo-section">
      <h3>3. Product Grid Configurations</h3>

      <div class="grid-controls">
        <label>
          Columns:
          <select v-model="gridConfig.columns">
            <option v-for="n in 6" :key="n" :value="n">{{ n }}</option>
          </select>
        </label>

        <label>
          <input type="checkbox" v-model="gridConfig.showPagination" />
          Show Pagination
        </label>

        <label>
          <input type="checkbox" v-model="gridConfig.compact" />
          Compact Mode
        </label>

        <label>
          Theme:
          <select v-model="gridConfig.theme">
            <option value="light">Light</option>
            <option value="dark">Dark</option>
          </select>
        </label>
      </div>

      <ProductGrid
        :products="sampleProducts"
        :columns="gridConfig.columns"
        :items-per-page="4"
        :show-pagination="gridConfig.showPagination"
        :show-product-colors="gridConfig.showColors"
        :show-product-description="gridConfig.showDescriptions"
        :show-badges="gridConfig.showBadges"
        card-size="medium"
        :theme="gridConfig.theme"
        button-text="Quick Buy"
        :compact="gridConfig.compact"
        sort-by="price"
        @product-click="handleProductClick"
        @buy-now="handleBuyNow"
      />
    </section>

    <!-- Example 4: Header variations -->
    <section class="demo-section">
      <h3>4. Header Component Variations</h3>

      <div class="header-demo">
        <h4>Header with Search Only</h4>
        <AppHeader
          :show-search="true"
          :show-profile="false"
          :show-cart="false"
          logo-text="Apple Demo"
          search-placeholder="Search demo..."
        />
      </div>

      <div class="header-demo">
        <h4>Dark Theme Header</h4>
        <AppHeader
          :show-search="true"
          :show-profile="true"
          :show-cart="true"
          logo-text="Dark Apple"
          search-placeholder="Dark search..."
          theme="dark"
        />
      </div>
    </section>

    <!-- Example 5: Props validation demo -->
    <section class="demo-section">
      <h3>5. Props Validation Examples</h3>
      <div class="validation-demo">
        <h4>Valid Props:</h4>
        <pre><code>{{ JSON.stringify(validProps, null, 2) }}</code></pre>

        <h4>Validation Errors:</h4>
        <ul v-if="validationErrors.length">
          <li v-for="error in validationErrors" :key="error" class="error">
            {{ error }}
          </li>
        </ul>
        <p v-else class="success">✅ All props are valid!</p>
      </div>
    </section>
  </div>
</template>

<script>
import ProductCard from './ProductCard.vue'
import ProductGrid from './ProductGrid.vue'
import AppHeader from '../layout/AppHeader.vue'
import { usePropsValidation } from '../../composables/useProps.js'
import Swal from 'sweetalert2'

export default {
  name: 'PropsDemo',
  components: {
    ProductCard,
    ProductGrid,
    AppHeader,
  },
  setup() {
    // Demo props validation
    const propDefinitions = {
      required: { type: String, required: true },
      withDefault: { type: Number, default: 42 },
      validated: {
        type: String,
        validator: (value) => ['small', 'medium', 'large'].includes(value),
      },
    }

    const testProps = {
      required: 'Hello',
      withDefault: undefined, // Will use default
      validated: 'medium',
    }

    const { validatedProps, validationErrors } = usePropsValidation(propDefinitions, testProps)

    return {
      validProps: validatedProps,
      validationErrors,
    }
  },
  data() {
    return {
      gridConfig: {
        columns: 3,
        showPagination: true,
        compact: false,
        theme: 'light',
        showColors: true,
        showDescriptions: true,
        showBadges: true,
      },
      sampleProducts: [
        {
          id: 1,
          name: 'iPhone 17 Pro',
          price: 999,
          description: 'The latest iPhone with advanced features',
          colors: ['Black', 'White', 'Gold'],
          badge: 'New',
          rating: 4.9,
          image: '📱',
        },
        {
          id: 2,
          name: 'MacBook Pro M4',
          price: 1999,
          description: 'Professional laptop with M4 chip',
          colors: ['Space Gray', 'Silver'],
          badge: 'Pro',
          rating: 4.8,
          image: '💻',
        },
        {
          id: 3,
          name: 'Apple Watch Ultra',
          price: 799,
          description: 'Ultimate smartwatch for adventurers',
          colors: ['Natural', 'Blue', 'Orange'],
          badge: 'Ultra',
          rating: 4.7,
          image: '⌚',
        },
        {
          id: 4,
          name: 'AirPods Max',
          price: 549,
          description: 'Premium over-ear headphones',
          colors: ['Silver', 'Space Gray', 'Green', 'Blue', 'Pink'],
          rating: 4.6,
          image: '🎧',
        },
      ],
    }
  },
  methods: {
    handleProductClick(product) {
      console.log('Demo: Product clicked:', product.name)
    },
    handleBuyNow(product) {
      Swal.fire({
        icon: 'info',
        title: 'Demo Mode',
        text: `Buying ${product.name} for $${product.price}`,
        timer: 2000,
        showConfirmButton: false,
      })
    },
  },
}
</script>

<style scoped>
.props-demo {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 20px;
}

.demo-section {
  margin-bottom: 60px;
  padding: 30px;
  background: white;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.demo-section h3 {
  color: #1d1d1f;
  margin-bottom: 20px;
  font-size: 1.5rem;
  font-weight: 600;
}

.grid-controls {
  display: flex;
  gap: 20px;
  margin-bottom: 30px;
  padding: 20px;
  background: #f5f5f7;
  border-radius: 12px;
  flex-wrap: wrap;
}

.grid-controls label {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 500;
  color: #1d1d1f;
}

.grid-controls select,
.grid-controls input[type='checkbox'] {
  margin-left: 8px;
}

.header-demo {
  margin: 20px 0;
  padding: 20px;
  background: #f9f9f9;
  border-radius: 12px;
}

.header-demo h4 {
  margin: 0 0 15px 0;
  color: #666;
  font-size: 1rem;
}

.validation-demo {
  padding: 20px;
  background: #f8f9fa;
  border-radius: 12px;
}

.validation-demo pre {
  background: #e9ecef;
  padding: 15px;
  border-radius: 8px;
  overflow-x: auto;
  font-size: 0.9rem;
}

.validation-demo .error {
  color: #dc3545;
  margin: 5px 0;
}

.validation-demo .success {
  color: #28a745;
  font-weight: 500;
  margin: 10px 0;
}

@media (max-width: 768px) {
  .grid-controls {
    flex-direction: column;
    gap: 15px;
  }

  .demo-section {
    padding: 20px;
    margin-bottom: 40px;
  }
}
</style>
