<template>
  <ProductPage
    product-type="AirPods"
    :page-config="pageConfig"
    :products="audioProducts"
    :sections="productSections"
  />
</template>

<script>
import ProductPage from '../components/product/ProductPage.vue'

export default {
  name: 'AirPodsView',
  components: {
    ProductPage,
  },
  data() {
    return {
      pageConfig: {
        title: 'AirPods',
        subtitle: "Magic like you've never heard.",
        emoji: '🎧',
      },
      productSections: [
        {
          key: 'airpods-4',
          title: 'AirPods 4',
          subtitle: 'Updated fit. Optimal comfort.',
          series: 'AirPods 4',
        },
        {
          key: 'airpods-pro',
          title: 'AirPods Pro',
          subtitle: 'Adaptive Audio. Now playing.',
          series: 'AirPods Pro',
        },
        {
          key: 'earpods',
          title: 'EarPods',
          subtitle: 'Simple. Affordable. Essential.',
          series: 'EarPods',
        },
      ],
      audioProducts: [
        // AirPods 4
        {
          id: 1,
          name: 'AirPods 4',
          series: 'AirPods 4',
          price: '$179',
          description: 'Updated fit. Optimal comfort.',
          colors: ['#ffffff'],
          badge: 'New',
          category: 'AirPods 4',
        },
        {
          id: 2,
          name: 'AirPods 4 with ANC',
          series: 'AirPods 4',
          price: '$229',
          description: 'Active Noise Cancellation included.',
          colors: ['#ffffff'],
          badge: 'ANC',
          category: 'AirPods 4',
        },
        // AirPods Pro
        {
          id: 3,
          name: 'AirPods Pro (2nd generation)',
          series: 'AirPods Pro',
          price: '$249',
          description: 'Adaptive Audio. Now playing.',
          colors: ['#ffffff'],
          badge: 'Pro',
          category: 'AirPods Pro',
        },
        // EarPods
        {
          id: 4,
          name: 'EarPods (USB-C)',
          series: 'EarPods',
          price: '$19',
          description: 'Universal compatibility.',
          colors: ['#ffffff'],
          category: 'EarPods',
        },
        {
          id: 5,
          name: 'EarPods (Lightning)',
          series: 'EarPods',
          price: '$19',
          description: 'For iPhone and iPad.',
          colors: ['#ffffff'],
          category: 'EarPods',
        },
        {
          id: 6,
          name: 'EarPods (3.5mm)',
          series: 'EarPods',
          price: '$19',
          description: 'Classic wired connection.',
          colors: ['#ffffff'],
          category: 'EarPods',
        },
      ],
    }
  },
}
</script>
