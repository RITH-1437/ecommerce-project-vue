<template>
  <div class="product-card" @click="$emit('product-click', product)">
    <div class="product-image-container">
      <div class="product-image">
        <img v-if="product.image" :src="product.image" :alt="product.name" />
        <div v-else class="placeholder-image">📱</div>
      </div>
      <div v-if="product.badge && showBadge" class="product-badge">{{ product.badge }}</div>
    </div>

    <div class="product-info">
      <h3 class="product-name">{{ product.name }}</h3>
      <p class="product-description" v-if="product.description && showDescription">
        {{ product.description }}
      </p>
      <div class="product-pricing">
        <span class="product-price">{{ product.price }}</span>
        <span v-if="product.originalPrice" class="original-price">{{ product.originalPrice }}</span>
      </div>
      <div class="product-colors" v-if="product.colors && showColors">
        <div
          v-for="color in product.colors"
          :key="color"
          class="color-option"
          :style="{ backgroundColor: color }"
          :title="color"
        ></div>
      </div>
      <button class="buy-button" @click.stop="addToCart">
        <span>{{ buttonText }}</span>
        <span class="arrow">→</span>
      </button>
    </div>
  </div>
</template>

<script>
import { useCartStore } from '@/stores/counter.js'

export default {
  name: 'ProductCard',
  props: {
    product: {
      type: Object,
      required: true,
    },
    showColors: {
      type: Boolean,
      default: true,
    },
    showDescription: {
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
    buttonText: {
      type: String,
      default: 'Add to Cart',
    },
    showBadge: {
      type: Boolean,
      default: true,
    },
  },
  emits: ['product-click'],
  setup() {
    const cartStore = useCartStore()

    return {
      cartStore,
    }
  },
  methods: {
    addToCart() {
      this.cartStore.addItem(this.product)
      // Just add to cart without redirecting
    },
  },
}
</script>

<style scoped>
.product-card {
  background: white;
  border-radius: 20px;
  padding: 24px;
  box-shadow: 0 4px 30px rgba(0, 0, 0, 0.08);
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.product-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15);
}

.product-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 3px;
  background: linear-gradient(90deg, #0071e3, #00c7be);
  transform: scaleX(0);
  transition: transform 0.3s ease;
}

.product-card:hover::before {
  transform: scaleX(1);
}

.product-image-container {
  position: relative;
  text-align: center;
  margin-bottom: 20px;
}

.product-image {
  width: 100%;
  height: 200px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  border-radius: 16px;
  margin-bottom: 16px;
}

.product-image img {
  width: 100%;
  height: 100%;
  object-fit: contain;
  border-radius: 16px;
}

.placeholder-image {
  font-size: 60px;
  opacity: 0.7;
}

.product-badge {
  position: absolute;
  top: 12px;
  right: 12px;
  background: linear-gradient(135deg, #ff6b6b, #ee5a24);
  color: white;
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 600;
}

.product-info {
  text-align: center;
}

.product-name {
  font-size: 20px;
  font-weight: 700;
  color: #1d1d1f;
  margin-bottom: 8px;
  line-height: 1.3;
}

.product-description {
  font-size: 14px;
  color: #86868b;
  margin-bottom: 12px;
  line-height: 1.4;
}

.product-pricing {
  margin-bottom: 16px;
}

.product-price {
  font-size: 24px;
  font-weight: 700;
  color: #0071e3;
  margin-right: 8px;
}

.original-price {
  font-size: 16px;
  color: #86868b;
  text-decoration: line-through;
}

.product-colors {
  display: flex;
  justify-content: center;
  gap: 8px;
  margin-bottom: 20px;
}

.color-option {
  width: 20px;
  height: 20px;
  border-radius: 50%;
  border: 2px solid #f5f5f7;
  cursor: pointer;
  transition: transform 0.2s ease;
}

.color-option:hover {
  transform: scale(1.2);
  border-color: #0071e3;
}

.buy-button {
  width: 100%;
  background: linear-gradient(135deg, #0071e3 0%, #005bb5 100%);
  color: white;
  border: none;
  padding: 14px 24px;
  border-radius: 25px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.buy-button:hover {
  background: linear-gradient(135deg, #005bb5 0%, #0071e3 100%);
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(0, 113, 227, 0.4);
}

.arrow {
  transition: transform 0.3s ease;
}

.buy-button:hover .arrow {
  transform: translateX(4px);
}

@media (max-width: 768px) {
  .product-card {
    padding: 20px;
  }

  .product-image {
    height: 150px;
  }

  .product-name {
    font-size: 18px;
  }

  .product-price {
    font-size: 20px;
  }
}
</style>
