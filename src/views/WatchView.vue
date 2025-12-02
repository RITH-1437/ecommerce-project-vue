<template>
  <ProductPage
    product-type="Watch"
    :page-config="pageConfig"
    :products="watches"
    :sections="productSections"
  />
</template>

<script>
import ProductPage from '../components/product/ProductPage.vue'

export default {
  name: 'WatchView',
  components: {
    ProductPage,
  },
  data() {
    return {
      pageConfig: {
        title: 'Apple Watch',
        subtitle: 'A healthy leap ahead.',
        emoji: '⌚',
      },
      productSections: [
        {
          key: 'watch-series',
          title: 'Apple Watch Series',
          subtitle: 'The ultimate device for a healthy life.',
          series: 'Apple Watch Series',
        },
        {
          key: 'watch-ultra',
          title: 'Apple Watch Ultra',
          subtitle: 'The most rugged and capable Apple Watch.',
          series: 'Apple Watch Ultra',
        },
        {
          key: 'watch-se',
          title: 'Apple Watch SE',
          subtitle: 'All the essentials. Light on price.',
          series: 'Apple Watch SE',
        },
        {
          key: 'watch-hermes',
          title: 'Apple Watch Hermès',
          subtitle: 'Unique bands crafted by Hermès.',
          series: 'Apple Watch Hermès',
        },
      ],
      watches: [
        // Apple Watch Series
        {
          id: 1,
          name: 'Apple Watch Series 10',
          series: 'Apple Watch Series',
          price: '$399',
          description: 'The ultimate device for a healthy life.',
          colors: ['#2c2c2e', '#e8e8ed', '#ff6347', '#87ceeb', '#ffd700'],
          category: 'Apple Watch Series',
        },
        {
          id: 2,
          name: 'Apple Watch Series 9',
          series: 'Apple Watch Series',
          price: '$329',
          description: 'Powerful health insights. Incredible value.',
          colors: ['#2c2c2e', '#e8e8ed', '#ff6347', '#87ceeb'],
          category: 'Apple Watch Series',
        },
        // Apple Watch Ultra
        {
          id: 3,
          name: 'Apple Watch Ultra 2',
          series: 'Apple Watch Ultra',
          price: '$799',
          description: 'The most rugged and capable Apple Watch.',
          colors: ['#2c2c2e', '#ffa500'],
          category: 'Apple Watch Ultra',
        },
        // Apple Watch SE
        {
          id: 4,
          name: 'Apple Watch SE',
          series: 'Apple Watch SE',
          price: '$249',
          description: 'All the essentials. Light on price.',
          colors: ['#2c2c2e', '#e8e8ed', '#87ceeb'],
          category: 'Apple Watch SE',
        },
        // Apple Watch Hermès
        {
          id: 5,
          name: 'Apple Watch Hermès Series 10',
          series: 'Apple Watch Hermès',
          price: '$1,299',
          description: 'Unique bands crafted by Hermès.',
          colors: ['#8b4513', '#2c2c2e', '#ffd700'],
          category: 'Apple Watch Hermès',
        },
      ],
    }
  },
}
</script>
