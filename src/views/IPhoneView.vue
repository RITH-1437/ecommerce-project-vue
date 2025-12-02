<template>
  <ProductPage
    product-type="iPhone"
    :page-config="pageConfig"
    :products="iphones"
    :sections="productSections"
  />
</template>

<script>
import ProductPage from '../components/product/ProductPage.vue'

export default {
  name: 'IPhoneView',
  components: {
    ProductPage,
  },
  data() {
    return {
      pageConfig: {
        title: 'iPhone',
        subtitle: 'Designed to be loved.',
        emoji: '📱',
      },
      productSections: [
        {
          key: 'iphone-pro',
          title: 'iPhone Pro',
          subtitle: 'Forged in titanium',
          series: 'iPhone Pro',
        },
        {
          key: 'iphone-plus',
          title: 'iPhone Plus',
          subtitle: 'Bigger and better',
          series: 'iPhone Plus',
        },
        {
          key: 'iphone',
          title: 'iPhone',
          subtitle: 'So impressive. So intuitive. So iPhone.',
          series: 'iPhone',
        },
        {
          key: 'iphone-se',
          title: 'iPhone SE',
          subtitle: 'Love the power. Love the price.',
          series: 'iPhone SE',
        },
      ],
      iphones: [
        // iPhone Pro
        {
          id: 1,
          name: 'iPhone 15 Pro Max',
          series: 'iPhone Pro',
          price: '$1,199',
          description: 'The ultimate iPhone experience with titanium design.',
          colors: ['#2c2c2e', '#e8e8ed', '#c7c7cc', '#ff6347'],
          category: 'iPhone Pro',
        },
        {
          id: 2,
          name: 'iPhone 15 Pro',
          series: 'iPhone Pro',
          price: '$999',
          description: 'Pro camera system with advanced features.',
          colors: ['#2c2c2e', '#e8e8ed', '#c7c7cc', '#ff6347'],
          category: 'iPhone Pro',
        },
        // iPhone Plus
        {
          id: 3,
          name: 'iPhone 15 Plus',
          series: 'iPhone Plus',
          price: '$899',
          description: 'Larger screen for immersive experience.',
          colors: ['#ffb6c1', '#ffd700', '#98fb98', '#87ceeb', '#2c2c2e'],
          category: 'iPhone Plus',
        },
        // iPhone
        {
          id: 4,
          name: 'iPhone 15',
          series: 'iPhone',
          price: '$799',
          description: 'Powerful performance in a beautiful design.',
          colors: ['#ffb6c1', '#ffd700', '#98fb98', '#87ceeb', '#2c2c2e'],
          category: 'iPhone',
        },
        {
          id: 5,
          name: 'iPhone 14',
          series: 'iPhone',
          price: '$699',
          description: 'Essential iPhone experience.',
          colors: ['#ffb6c1', '#ffd700', '#98fb98', '#87ceeb', '#2c2c2e'],
          category: 'iPhone',
        },
        // iPhone SE
        {
          id: 6,
          name: 'iPhone SE',
          series: 'iPhone SE',
          price: '$429',
          description: 'Love the power. Love the price.',
          colors: ['#2c2c2e', '#e8e8ed', '#ff0000'],
          category: 'iPhone SE',
        },
      ],
    }
  },
}
</script>
