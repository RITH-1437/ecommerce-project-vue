<template>
  <ProductPage
    product-type="iPad"
    :page-config="pageConfig"
    :products="ipads"
    :sections="productSections"
  />
</template>

<script>
import ProductPage from '../components/product/ProductPage.vue'

export default {
  name: 'IPadView',
  components: {
    ProductPage,
  },
  data() {
    return {
      pageConfig: {
        title: 'iPad',
        subtitle: 'Lovable. Drawable. Magical.',
        emoji: '💻',
      },
      productSections: [
        {
          key: 'ipad-pro',
          title: 'iPad Pro',
          subtitle: 'Supercharged by M4 chip',
          series: 'iPad Pro',
        },
        {
          key: 'ipad-air',
          title: 'iPad Air',
          subtitle: 'Light. Bright. Full of might.',
          series: 'iPad Air',
        },
        {
          key: 'ipad-11th',
          title: 'iPad 11th Generation',
          subtitle: 'The colorful, all-screen iPad for the things you do most.',
          series: 'iPad 11th Gen',
        },
        {
          key: 'ipad-mini',
          title: 'iPad mini',
          subtitle: 'Mega power. Mini size.',
          series: 'iPad mini',
        },
      ],
      ipads: [
        // iPad Pro
        {
          id: 1,
          name: 'iPad Pro 13-inch M4',
          series: 'iPad Pro',
          price: '$1,299',
          description: 'The ultimate iPad experience with M4 chip.',
          colors: ['#2c2c2e', '#e8e8ed', '#c7c7cc'],
          category: 'iPad Pro',
        },
        {
          id: 2,
          name: 'iPad Pro 11-inch M4',
          series: 'iPad Pro',
          price: '$999',
          description: 'Pro performance in a compact size.',
          colors: ['#2c2c2e', '#e8e8ed', '#c7c7cc'],
          category: 'iPad Pro',
        },
        // iPad Air
        {
          id: 3,
          name: 'iPad Air 13-inch M3',
          series: 'iPad Air',
          price: '$1,099',
          description: 'Serious performance. Surprisingly portable.',
          colors: ['#e8e8ed', '#f1c2b0', '#a7c0cd', '#d9d9d9'],
          category: 'iPad Air',
        },
        {
          id: 4,
          name: 'iPad Air 11-inch M3',
          series: 'iPad Air',
          price: '$899',
          description: 'Light. Bright. Full of might.',
          colors: ['#e8e8ed', '#f1c2b0', '#a7c0cd', '#d9d9d9'],
          category: 'iPad Air',
        },
        // iPad 11th Generation
        {
          id: 5,
          name: 'iPad 11th (A16)',
          series: 'iPad 11th Gen',
          price: '$1,299',
          description: 'The colorful, all-screen iPad.',
          colors: ['#87ceeb', '#ffb6c1', '#98fb98', '#ffd700'],
          category: 'iPad 11th Gen',
        },
        // iPad mini
        {
          id: 6,
          name: 'iPad mini (A17 Pro)',
          series: 'iPad mini',
          price: '$799',
          description: 'Mega power. Mini size.',
          colors: ['#e8e8ed', '#f1c2b0', '#a7c0cd', '#d9d9d9'],
          category: 'iPad mini',
        },
      ],
    }
  },
}
</script>
