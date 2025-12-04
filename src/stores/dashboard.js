import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useDashboardStore = defineStore('dashboard', () => {
  // Loading states
  const isLoading = ref(false)
  const lastUpdated = ref(null)

  // Dashboard data
  const stats = ref({
    totalSales: { value: 0, change: 0, trend: 'up' },
    totalOrders: { value: 0, change: 0, trend: 'up' },
    newCustomers: { value: 0, change: 0, trend: 'up' },
    productInventory: { value: 0, change: 0, trend: 'down' },
    revenue: { value: 0, change: 0, trend: 'up' },
    avgOrderValue: { value: 0, change: 0, trend: 'up' },
  })

  const recentOrders = ref([])
  const lowStockProducts = ref([])
  const salesChart = ref({
    labels: [],
    datasets: [],
  })
  const topProducts = ref([])

  // Computed properties
  const totalRevenue = computed(() => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
    }).format(stats.value.totalSales.value)
  })

  const todayOrders = computed(() => {
    const today = new Date().toISOString().split('T')[0]
    return recentOrders.value.filter((order) => order.date.startsWith(today)).length
  })

  // API simulation functions (replace with real API calls)
  const fetchDashboardStats = async () => {
    isLoading.value = true
    try {
      // Simulate API call delay
      await new Promise((resolve) => setTimeout(resolve, 1000))

      // Fallback data - replace with actual API call
      stats.value = {
        totalSales: { value: 245680, change: 12.5, trend: 'up' },
        totalOrders: { value: 1248, change: 8.2, trend: 'up' },
        newCustomers: { value: 186, change: -2.1, trend: 'down' },
        productInventory: { value: 2847, change: -5.3, trend: 'down' },
        revenue: { value: 98750, change: 15.8, trend: 'up' },
        avgOrderValue: { value: 196.9, change: 4.2, trend: 'up' },
      }

      lastUpdated.value = new Date()
    } catch (error) {
      console.error('Failed to fetch dashboard stats:', error)
    } finally {
      isLoading.value = false
    }
  }

  const fetchRecentOrders = async () => {
    try {
      // Simulate API call - replace with real endpoint
      await new Promise((resolve) => setTimeout(resolve, 800))

      recentOrders.value = [
        {
          id: '#ORD-2025-001',
          customer: { name: 'Sarah Johnson', email: 'sarah.j@email.com', avatar: '👩🏻‍💼' },
          date: '2025-12-02T14:30:00Z',
          status: 'processing',
          total: 1299.99,
          items: ['iPhone 15 Pro', 'AirPods Pro'],
          priority: 'high',
        },
        {
          id: '#ORD-2025-002',
          customer: { name: 'Michael Chen', email: 'mike.chen@email.com', avatar: '👨🏻‍💻' },
          date: '2025-12-02T13:15:00Z',
          status: 'shipped',
          total: 2499.99,
          items: ['MacBook Pro 16"', 'Magic Mouse'],
          priority: 'medium',
        },
        {
          id: '#ORD-2025-003',
          customer: { name: 'Emily Rodriguez', email: 'emily.r@email.com', avatar: '👩🏽‍🎨' },
          date: '2025-12-02T12:45:00Z',
          status: 'delivered',
          total: 899.99,
          items: ['iPad Air', 'Apple Pencil'],
          priority: 'low',
        },
        {
          id: '#ORD-2025-004',
          customer: { name: 'David Kim', email: 'david.kim@email.com', avatar: '👨🏻‍🔬' },
          date: '2025-12-02T11:20:00Z',
          status: 'cancelled',
          total: 599.99,
          items: ['Apple Watch Series 9'],
          priority: 'low',
        },
        {
          id: '#ORD-2025-005',
          customer: { name: 'Lisa Wang', email: 'lisa.wang@email.com', avatar: '👩🏻‍🚀' },
          date: '2025-12-02T10:10:00Z',
          status: 'shipped',
          total: 3999.99,
          items: ['Mac Studio', 'Studio Display'],
          priority: 'high',
        },
      ]
    } catch (error) {
      console.error('Failed to fetch recent orders:', error)
    }
  }

  const fetchLowStockProducts = async () => {
    try {
      await new Promise((resolve) => setTimeout(resolve, 600))

      // All products - will filter based on new logic
      const allProducts = [
        {
          id: 'PROD-001',
          name: 'iPhone 15 Pro Max',
          category: 'iPhone',
          currentStock: 8,
          minStock: 50,
          price: 1199.99,
          image: '📱',
        },
        {
          id: 'PROD-002',
          name: 'AirPods Pro (2nd Gen)',
          category: 'AirPod',
          currentStock: 15,
          minStock: 100,
          price: 249.99,
          image: '🎧',
        },
        {
          id: 'PROD-003',
          name: 'MacBook Air 15"',
          category: 'MacBook',
          currentStock: 5,
          minStock: 25,
          price: 1299.99,
          image: '💻',
        },
        {
          id: 'PROD-004',
          name: 'Apple Watch Ultra 2',
          category: 'Watch',
          currentStock: 18,
          minStock: 40,
          price: 799.99,
          image: '⌚',
        },
        {
          id: 'PROD-005',
          name: 'iPad Pro 12.9"',
          category: 'iPad',
          currentStock: 12,
          minStock: 30,
          price: 1099.99,
          image: '📱',
        },
      ]

      // Filter products based on new logic: only show if stock < 20
      lowStockProducts.value = allProducts
        .filter((product) => product.currentStock < 20)
        .map((product) => {
          // Determine urgency based on current stock
          let urgency = 'fine'
          if (product.currentStock < 10) {
            urgency = 'critical'
          } else if (product.currentStock < 20) {
            urgency = 'low'
          }

          return {
            ...product,
            urgency,
          }
        })
    } catch (error) {
      console.error('Failed to fetch low stock products:', error)
    }
  }

  const fetchSalesChart = async () => {
    try {
      await new Promise((resolve) => setTimeout(resolve, 700))

      const last7Days = Array.from({ length: 7 }, (_, i) => {
        const date = new Date()
        date.setDate(date.getDate() - (6 - i))
        return date.toLocaleDateString('en-US', { weekday: 'short' })
      })

      salesChart.value = {
        labels: last7Days,
        datasets: [
          {
            label: 'Sales',
            data: [12400, 19800, 15600, 22100, 18900, 25300, 28700],
            borderColor: '#007AFF',
            backgroundColor: 'rgba(0, 122, 255, 0.1)',
            tension: 0.4,
          },
          {
            label: 'Orders',
            data: [68, 89, 72, 95, 84, 108, 124],
            borderColor: '#34C759',
            backgroundColor: 'rgba(52, 199, 89, 0.1)',
            tension: 0.4,
          },
        ],
      }
    } catch (error) {
      console.error('Failed to fetch sales chart:', error)
    }
  }

  const fetchTopProducts = async () => {
    try {
      await new Promise((resolve) => setTimeout(resolve, 500))

      topProducts.value = [
        {
          id: 'PROD-101',
          name: 'iPhone 15 Pro',
          sales: 342,
          revenue: 341580,
          growth: 12.5,
          image: '📱',
          category: 'Smartphones',
        },
        {
          id: 'PROD-102',
          name: 'MacBook Pro 14"',
          sales: 89,
          revenue: 178911,
          growth: 8.2,
          image: '💻',
          category: 'Laptops',
        },
        {
          id: 'PROD-103',
          name: 'AirPods Pro',
          sales: 567,
          revenue: 141750,
          growth: 15.8,
          image: '🎧',
          category: 'Audio',
        },
        {
          id: 'PROD-104',
          name: 'iPad Pro 12.9"',
          sales: 123,
          revenue: 135270,
          growth: -2.1,
          image: '📱',
          category: 'Tablets',
        },
      ]
    } catch (error) {
      console.error('Failed to fetch top products:', error)
    }
  }

  // Load all dashboard data
  const loadDashboardData = async () => {
    await Promise.all([
      fetchDashboardStats(),
      fetchRecentOrders(),
      fetchLowStockProducts(),
      fetchSalesChart(),
      fetchTopProducts(),
    ])
  }

  // Refresh specific data
  const refreshStats = async () => {
    await fetchDashboardStats()
  }

  const refreshOrders = async () => {
    await fetchRecentOrders()
  }

  return {
    // State
    isLoading,
    lastUpdated,
    stats,
    recentOrders,
    lowStockProducts,
    salesChart,
    topProducts,

    // Computed
    totalRevenue,
    todayOrders,

    // Actions
    loadDashboardData,
    refreshStats,
    refreshOrders,
    fetchDashboardStats,
    fetchRecentOrders,
    fetchLowStockProducts,
    fetchSalesChart,
    fetchTopProducts,
  }
})
