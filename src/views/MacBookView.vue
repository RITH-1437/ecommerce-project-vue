<template>
  <ProductPage
    product-type="MacBook"
    :page-config="pageConfig"
    :products="macbooks"
    :sections="productSections"
  />
</template>

<script>
import ProductPage from '../components/product/ProductPage.vue'

export default {
  name: 'MacBookView',
  components: {
    ProductPage,
  },
  data() {
    return {
      pageConfig: {
        title: 'MacBook',
        subtitle: 'Supercharged for pros.',
        emoji: '<i class="fas fa-laptop"></i>',
      },
      productSections: [
        {
          key: 'macbook-pro',
          title: 'MacBook Pro',
          subtitle: 'Mind-blowing. Head-turning.',
          series: 'MacBook Pro',
        },
        {
          key: 'macbook-air',
          title: 'MacBook Air',
          subtitle: 'Lean. Mean. M-series machine.',
          series: 'MacBook Air',
        },
      ],
      macbooks: [
        // MacBook Pro
        {
          id: 1,
          name: 'MacBook Pro 16-inch M4 Pro',
          series: 'MacBook Pro',
          price: '$2,499',
          description: 'The most powerful MacBook Pro ever.',
          colors: ['#2c2c2e', '#e8e8ed'],
          category: 'MacBook Pro',
        },
        {
          id: 2,
          name: 'MacBook Pro 14-inch M4',
          series: 'MacBook Pro',
          price: '$1,999',
          description: 'Supercharged by M4 chip.',
          colors: ['#2c2c2e', '#e8e8ed'],
          category: 'MacBook Pro',
        },
        {
          id: 3,
          name: 'MacBook Pro 13-inch M3',
          series: 'MacBook Pro',
          price: '$1,599',
          description: 'Pro performance, compact design.',
          colors: ['#2c2c2e', '#e8e8ed'],
          category: 'MacBook Pro',
        },
        // MacBook Air
        {
          id: 4,
          name: 'MacBook Air 15-inch M3',
          series: 'MacBook Air',
          price: '$1,299',
          description: 'Impressively big. Impossibly thin.',
          colors: ['#2c2c2e', '#e8e8ed', '#c7c7cc', '#ffd700'],
          category: 'MacBook Air',
        },
        {
          id: 5,
          name: 'MacBook Air 13-inch M3',
          series: 'MacBook Air',
          price: '$1,099',
          description: 'Lean. Mean. M3 machine.',
          colors: ['#2c2c2e', '#e8e8ed', '#c7c7cc', '#ffd700'],
          category: 'MacBook Air',
        },
        {
          id: 6,
          name: 'MacBook Air 13-inch M2',
          series: 'MacBook Air',
          price: '$999',
          description: 'Strikingly thin design powered by M2.',
          colors: ['#2c2c2e', '#e8e8ed', '#c7c7cc', '#ffd700'],
          category: 'MacBook Air',
        },
      ],
    }
  },
}
</script>
