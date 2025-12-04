<template>
  <div class="landing-page">
    <!-- App Header -->
    <AppHeader @search="handleSearch" />

    <!-- Modern Hero Section -->
    <section class="hero-banner">
      <div class="hero-background">
        <div class="hero-gradient"></div>
        <div class="floating-elements">
          <div class="float-element" v-for="n in 6" :key="n" :style="getFloatingStyle(n)"></div>
        </div>
      </div>
      <div class="hero-content">
        <div class="hero-text">
          <h1 class="hero-title">
            <span class="text-gradient">Apple Store</span><br />
            <span class="hero-subtitle">Experience Innovation</span>
          </h1>
          <p class="hero-description">
            Discover the latest Apple products with cutting-edge technology, elegant design, and
            unmatched performance.
          </p>
          <div class="hero-actions">
            <button class="cta-primary" @click="scrollToProducts">Explore Products</button>
          </div>
        </div>
        <div class="hero-visual">
          <div class="product-stack">
            <div class="product-float iphone" :class="{ animated: isVisible }">
              <div class="device-frame">
                <div class="device-screen"><i class="fas fa-mobile-alt"></i></div>
              </div>
            </div>
            <div class="product-float ipad" :class="{ animated: isVisible }">
              <div class="device-frame">
                <div class="device-screen"><i class="fas fa-laptop"></i></div>
              </div>
            </div>
            <div class="product-float watch" :class="{ animated: isVisible }">
              <div class="device-frame">
                <div class="device-screen"><i class="fas fa-clock"></i></div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Main Content -->
    <main class="main-content">
      <div class="container">
        <!-- Popular Products Slider -->
        <section class="popular-products-section">
          <div class="section-header">
            <h2 class="section-title">Popular Products on Trend</h2>
            <p class="section-subtitle">
              Discover our top-rated products loved by customers worldwide
            </p>
          </div>
          <div class="slider-container">
            <button
              class="slider-btn prev-btn"
              @click="previousSlide"
              :disabled="currentSlide === 0"
            >
              ‹
            </button>
            <div class="products-slider">
              <div
                class="slider-track"
                :style="{ transform: `translateX(-${currentSlide * 33.333}%)` }"
              >
                <div v-for="product in popularProducts" :key="product.id" class="product-slide">
                  <div class="product-card-popular">
                    <div class="product-badge-popular">{{ product.badge }}</div>
                    <div class="product-image-popular">
                      <div class="product-icon">{{ product.image }}</div>
                    </div>
                    <div class="product-info">
                      <h3 class="product-name">{{ product.name }}</h3>
                      <div class="product-rating">
                        <div class="stars">
                          <span
                            v-for="star in 5"
                            :key="star"
                            class="star"
                            :class="{ filled: star <= Math.floor(product.rating) }"
                          >
                            ★
                          </span>
                        </div>
                        <span class="rating-text"
                          >{{ product.rating }} ({{ product.reviews }} reviews)</span
                        >
                      </div>
                      <div class="product-price">{{ product.price }}</div>
                      <div class="product-actions">
                        <button @click="showProductDetails(product)" class="product-btn-learn">
                          Learn More
                        </button>
                        <button @click="addToCartAndNavigate(product)" class="product-btn-cart">
                          🛒 Add to Cart
                        </button>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <button
              class="slider-btn next-btn"
              @click="nextSlide"
              :disabled="currentSlide >= popularProducts.length - 3"
            >
              ›
            </button>
          </div>
          <div class="slider-dots">
            <button
              v-for="(dot, index) in Math.ceil(popularProducts.length / 3)"
              :key="index"
              class="dot"
              :class="{ active: Math.floor(currentSlide) === index }"
              @click="goToSlide(index)"
            ></button>
          </div>
        </section>

        <!-- Featured Categories -->
        <section class="featured-categories" ref="categoriesSection">
          <h2 class="section-title">Shop by Category</h2>
          <div class="categories-grid">
            <router-link
              v-for="category in categories"
              :key="category.id"
              :to="category.route"
              class="category-card"
              :class="category.class"
            >
              <div class="category-icon">{{ category.icon }}</div>
              <h3 class="category-name">{{ category.name }}</h3>
              <p class="category-description">{{ category.description }}</p>
              <div class="category-overlay">
                <span class="explore-text">Explore →</span>
              </div>
            </router-link>
          </div>
        </section>

        <!-- Product Highlights -->
        <section class="product-highlights">
          <div class="section-header">
            <h2 class="section-title">Latest Products</h2>
            <p class="section-subtitle">Discover our newest innovations</p>
          </div>
          <div class="highlights-grid">
            <div class="highlight-card featured">
              <div class="highlight-image">
                <div class="product-badge">New</div>
                <div class="product-visual">📱</div>
              </div>
              <div class="highlight-content">
                <h3>iPhone 17 Pro</h3>
                <p>The most advanced iPhone ever created</p>
                <div class="price-tag">From $999</div>
                <button class="product-btn" @click="showProductDetails(highlightProducts.iphone17)">
                  Learn More
                </button>
              </div>
            </div>
            <div class="highlight-card">
              <div class="highlight-image">
                <div class="product-badge">Pro</div>
                <div class="product-visual">💻</div>
              </div>
              <div class="highlight-content">
                <h3>MacBook Pro M4</h3>
                <p>Supercharged by Apple Silicon</p>
                <div class="price-tag">From $1599</div>
                <button
                  class="product-btn"
                  @click="showProductDetails(highlightProducts.macbookPro)"
                >
                  Learn More
                </button>
              </div>
            </div>
            <div class="highlight-card">
              <div class="highlight-image">
                <div class="product-badge">Ultra</div>
                <div class="product-visual">⌚</div>
              </div>
              <div class="highlight-content">
                <h3>Apple Watch Ultra 3</h3>
                <p>Built for extreme adventures</p>
                <div class="price-tag">From $799</div>
                <button
                  class="product-btn"
                  @click="showProductDetails(highlightProducts.watchUltra)"
                >
                  Learn More
                </button>
              </div>
            </div>
          </div>
        </section>

        <!-- Stats Section -->
        <section class="stats-section">
          <div class="stats-container">
            <div class="stat-item" v-for="stat in stats" :key="stat.id">
              <div class="stat-number" ref="statNumbers">{{ animatedStats[stat.id] }}</div>
              <div class="stat-label">{{ stat.label }}</div>
            </div>
          </div>
        </section>

        <!-- Testimonials -->
        <section class="testimonials-section">
          <h2 class="section-title">What Our Customers Say</h2>
          <div class="testimonials-carousel">
            <div
              class="testimonial-track"
              :style="{ transform: `translateX(-${currentTestimonial * 100}%)` }"
            >
              <div
                class="testimonial-card"
                v-for="testimonial in testimonials"
                :key="testimonial.id"
              >
                <div class="testimonial-content">
                  <div class="stars">
                    <span v-for="n in 5" :key="n" class="star">⭐</span>
                  </div>
                  <p class="testimonial-text">"{{ testimonial.text }}"</p>
                  <div class="testimonial-author">
                    <div class="author-avatar">{{ testimonial.avatar }}</div>
                    <div class="author-info">
                      <div class="author-name">{{ testimonial.name }}</div>
                      <div class="author-title">{{ testimonial.title }}</div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="carousel-controls">
            <button
              v-for="(testimonial, index) in testimonials"
              :key="index"
              @click="currentTestimonial = index"
              :class="{ active: currentTestimonial === index }"
              class="carousel-dot"
            ></button>
          </div>
        </section>

        <!-- Newsletter Signup -->
        <section class="newsletter-section">
          <div class="newsletter-card">
            <div class="newsletter-content">
              <h2>Stay in the Loop</h2>
              <p>Get the latest updates on new products and exclusive offers</p>
              <div class="newsletter-form">
                <input
                  type="email"
                  placeholder="Enter your email"
                  v-model="newsletterEmail"
                  class="newsletter-input"
                />
                <button @click="subscribeNewsletter" class="newsletter-btn">Subscribe</button>
              </div>
            </div>
            <div class="newsletter-visual">
              <div class="notification-icon">🔔</div>
            </div>
          </div>
        </section>
      </div>
    </main>

    <!-- Footer -->
    <AppFooter />

    <!-- Product Details Modal -->
    <div v-if="showModal" class="modal-overlay" @click="closeModal">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h2>{{ selectedProduct?.name }}</h2>
          <button @click="closeModal" class="close-btn">×</button>
        </div>
        <div class="modal-body">
          <div class="modal-image">
            <div class="product-icon-large">{{ selectedProduct?.image }}</div>
          </div>
          <div class="modal-details">
            <p class="modal-price">{{ selectedProduct?.price }}</p>
            <div class="modal-specs">
              <h4>Product Details:</h4>
              <ul>
                <li><strong>Model:</strong> {{ selectedProduct?.name }}</li>
                <li><strong>Price:</strong> {{ selectedProduct?.price }}</li>
                <li v-if="selectedProduct?.rating">
                  <strong>Rating:</strong> {{ selectedProduct.rating }}/5 ({{
                    selectedProduct.reviews
                  }}
                  reviews)
                </li>
                <li v-if="selectedProduct?.badge">
                  <strong>Badge:</strong> {{ selectedProduct.badge }}
                </li>
                <li v-if="selectedProduct?.features">
                  <strong>Key Features:</strong>
                  <ul class="feature-list">
                    <li v-for="feature in selectedProduct.features" :key="feature">
                      {{ feature }}
                    </li>
                  </ul>
                </li>
              </ul>
            </div>
            <div class="modal-description">
              <p>{{ selectedProduct?.description }}</p>
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <button @click="closeModal" class="modal-btn secondary">Close</button>
          <router-link
            v-if="selectedProduct?.route"
            :to="selectedProduct.route"
            @click="closeModal"
            class="modal-btn primary"
          >
            View Full Details
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import AppHeader from '@/components/layout/AppHeader.vue'
import AppFooter from '@/components/layout/AppFooter.vue'

export default {
  name: 'HomeView',
  components: {
    AppHeader,
    AppFooter,
  },
  data() {
    return {
      isVisible: false,
      currentTestimonial: 0,
      newsletterEmail: '',
      showModal: false,
      selectedProduct: null,
      animatedStats: {
        customers: 0,
        products: 0,
        countries: 0,
        satisfaction: 0,
      },
      highlightProducts: {
        iphone17: {
          id: 'iphone17',
          name: 'iPhone 17 Pro',
          image: '📱',
          price: 'From $999',
          route: '/iphone',
          description:
            'The most advanced iPhone ever created with revolutionary A18 Bionic chip, titanium design, and advanced camera system.',
          features: [
            'A18 Bionic Chip',
            'Titanium Design',
            'Pro Camera System',
            '120Hz ProMotion Display',
            'Action Button',
          ],
        },
        macbookPro: {
          id: 'macbookPro',
          name: 'MacBook Pro M4',
          image: '💻',
          price: 'From $1599',
          route: '/macbook',
          description:
            'Supercharged by Apple Silicon M4 chip with incredible performance and all-day battery life.',
          features: [
            'M4 Chip',
            'Liquid Retina XDR Display',
            '22-hour Battery Life',
            'Advanced Thermal Design',
            'Studio-Quality Mics',
          ],
        },
        watchUltra: {
          id: 'watchUltra',
          name: 'Apple Watch Ultra 3',
          image: '⌚',
          price: 'From $799',
          route: '/watch',
          description:
            'Built for extreme adventures with rugged titanium design and advanced health monitoring.',
          features: [
            'Titanium Case',
            'Action Button',
            'Precision GPS',
            '36-hour Battery Life',
            'Water Resistant to 100m',
          ],
        },
      },
      popularProducts: [
        {
          id: 1,
          name: 'iPhone 15 Pro Max',
          image: '📱',
          price: '$1199',
          rating: 4.9,
          reviews: 2847,
          badge: 'Best Seller',
          route: '/iphone',
        },
        {
          id: 2,
          name: 'MacBook Air M3',
          image: '💻',
          price: '$1299',
          rating: 4.8,
          reviews: 1523,
          badge: "Editor's Choice",
          route: '/macbook',
        },
        {
          id: 3,
          name: 'AirPods Pro (2nd gen)',
          image: '🎧',
          price: '$249',
          rating: 4.7,
          reviews: 3421,
          badge: 'Top Rated',
          route: '/airpods',
        },
        {
          id: 4,
          name: 'Apple Watch Series 9',
          image: '⌚',
          price: '$399',
          rating: 4.8,
          reviews: 1876,
          badge: 'Popular',
          route: '/watch',
        },
        {
          id: 5,
          name: 'iPad Pro 12.9"',
          image: '📟',
          price: '$1099',
          rating: 4.6,
          reviews: 892,
          badge: 'Pro Choice',
          route: '/ipad',
        },
        {
          id: 6,
          name: 'iPhone 15',
          image: '📱',
          price: '$799',
          rating: 4.7,
          reviews: 2156,
          badge: 'New',
          route: '/iphone',
        },
      ],
      currentSlide: 0,
      categories: [
        {
          id: 'iphone',
          name: 'iPhone',
          icon: '📱',
          description: 'The ultimate smartphone experience',
          route: '/iphone',
          class: 'iphone-category',
        },
        {
          id: 'ipad',
          name: 'iPad',
          icon: '💻',
          description: 'Versatile tablet for work and creativity',
          route: '/ipad',
          class: 'ipad-category',
        },
        {
          id: 'macbook',
          name: 'MacBook',
          icon: '💻',
          description: 'Professional laptops for every need',
          route: '/macbook',
          class: 'macbook-category',
        },
        {
          id: 'watch',
          name: 'Apple Watch',
          icon: '⌚',
          description: 'Your health and fitness companion',
          route: '/watch',
          class: 'watch-category',
        },
        {
          id: 'airpods',
          name: 'AirPods',
          icon: '🎧',
          description: 'Wireless audio perfection',
          route: '/airpods',
          class: 'airpods-category',
        },
      ],
      stats: [
        { id: 'customers', value: 250000, label: 'Happy Customers' },
        { id: 'products', value: 150, label: 'Products Available' },
        { id: 'countries', value: 75, label: 'Countries Served' },
        { id: 'satisfaction', value: 99, label: 'Satisfaction Rate' },
      ],
      testimonials: [
        {
          id: 1,
          text: 'Amazing service and quality products. My iPhone has been perfect for over 2 years now!',
          name: 'Sarah Johnson',
          title: 'Creative Designer',
          avatar: '👩‍💼',
        },
        {
          id: 2,
          text: 'The MacBook Pro has transformed my workflow. Incredible performance and battery life.',
          name: 'Michael Chen',
          title: 'Software Engineer',
          avatar: '👨‍💻',
        },
        {
          id: 3,
          text: 'Best customer service ever! They helped me choose the perfect iPad for my studies.',
          name: 'Emily Rodriguez',
          title: 'Medical Student',
          avatar: '👩‍⚕️',
        },
      ],
    }
  },
  mounted() {
    this.initAnimations()
    this.startTestimonialRotation()
    this.animateStats()
  },
  beforeUnmount() {
    if (this.testimonialInterval) {
      clearInterval(this.testimonialInterval)
    }
  },
  methods: {
    handleSearch(query) {
      console.log('Search query:', query)
      // Add search functionality here if needed
    },
    initAnimations() {
      setTimeout(() => {
        this.isVisible = true
      }, 500)
    },
    getFloatingStyle(index) {
      const positions = [
        { top: '10%', left: '10%', delay: '0s' },
        { top: '20%', right: '15%', delay: '0.5s' },
        { top: '60%', left: '5%', delay: '1s' },
        { top: '70%', right: '10%', delay: '1.5s' },
        { top: '40%', left: '20%', delay: '2s' },
        { top: '30%', right: '25%', delay: '2.5s' },
      ]
      return {
        ...positions[index - 1],
        animationDelay: positions[index - 1].delay,
      }
    },
    scrollToProducts() {
      this.$refs.categoriesSection?.scrollIntoView({ behavior: 'smooth' })
    },
    nextSlide() {
      if (this.currentSlide < this.popularProducts.length - 3) {
        this.currentSlide++
      }
    },
    previousSlide() {
      if (this.currentSlide > 0) {
        this.currentSlide--
      }
    },
    goToSlide(index) {
      this.currentSlide = index
    },
    startTestimonialRotation() {
      this.testimonialInterval = setInterval(() => {
        this.currentTestimonial = (this.currentTestimonial + 1) % this.testimonials.length
      }, 5000)
    },
    animateStats() {
      this.stats.forEach((stat, index) => {
        if (stat.value) {
          this.animateNumber(stat.id, stat.value, index * 200)
        }
      })
    },
    animateNumber(key, target, delay = 0) {
      setTimeout(() => {
        const duration = 2000
        const steps = 60
        const increment = target / steps
        let current = 0

        const timer = setInterval(() => {
          current += increment
          if (current >= target) {
            this.animatedStats[key] = target
            clearInterval(timer)
          } else {
            this.animatedStats[key] = Math.floor(current)
          }
        }, duration / steps)
      }, delay)
    },
    subscribeNewsletter() {
      if (this.newsletterEmail) {
        console.log('Newsletter subscription:', this.newsletterEmail)
        this.newsletterEmail = ''
        alert('Thank you for subscribing!')
      }
    },
    showProductDetails(product) {
      // Add descriptions to popular products if not present
      if (product && !product.description) {
        const descriptions = {
          'iPhone 15 Pro Max':
            'The most advanced iPhone yet, featuring titanium design, A17 Pro chip, and professional camera system with 5x Telephoto zoom.',
          'iPhone 15':
            'All-new iPhone with Dynamic Island, 48MP Main camera, and USB-C connectivity for a premium experience.',
          'MacBook Air M3':
            'Supercharged by the M3 chip, featuring up to 18 hours of battery life and stunning 13.6-inch Liquid Retina display.',
          'AirPods Pro (2nd gen)':
            'Personalized Spatial Audio with dynamic head tracking, Adaptive Transparency, and up to 6 hours of listening time.',
          'Apple Watch Series 9':
            'Advanced health and fitness features with the revolutionary S9 chip, Double Tap gesture, and Precision Finding.',
          'iPad Pro 12.9"':
            'The ultimate iPad experience with M2 chip, Liquid Retina XDR display, and support for Apple Pencil Pro.',
        }
        product.description =
          descriptions[product.name] ||
          'Premium Apple product with cutting-edge technology and exceptional build quality.'

        // Add features based on product type
        const features = {
          'iPhone 15 Pro Max': [
            'A17 Pro Chip',
            'Titanium Design',
            '5x Telephoto Camera',
            'Action Button',
            'USB-C',
          ],
          'iPhone 15': [
            'A16 Bionic Chip',
            'Dynamic Island',
            '48MP Main Camera',
            'USB-C',
            'Ceramic Shield',
          ],
          'MacBook Air M3': [
            'M3 Chip',
            'Liquid Retina Display',
            '18-hour Battery',
            'MagSafe Charging',
            'Silent Design',
          ],
          'AirPods Pro (2nd gen)': [
            'H2 Chip',
            'Active Noise Cancellation',
            'Spatial Audio',
            'MagSafe Charging',
            'Sweat Resistant',
          ],
          'Apple Watch Series 9': [
            'S9 Chip',
            'Double Tap',
            'Always-On Display',
            'Blood Oxygen',
            'ECG App',
          ],
          'iPad Pro 12.9"': [
            'M2 Chip',
            'Liquid Retina XDR',
            'Apple Pencil Pro',
            '12MP Cameras',
            '5G Connectivity',
          ],
        }
        product.features = features[product.name] || [
          'Premium Design',
          'Advanced Technology',
          'Exceptional Performance',
        ]
      }

      this.selectedProduct = product
      this.showModal = true
    },
    closeModal() {
      this.showModal = false
      this.selectedProduct = null
    },
    addToCartAndNavigate(product) {
      // Import cart store
      const { useCartStore } = require('@/stores/counter.js')
      const cartStore = useCartStore()

      // Add to cart
      cartStore.addItem(product)

      // Show success notification
      alert(`${product.name} added to cart!`)

      // Navigate to product page
      if (product.route) {
        this.$router.push(product.route)
      }
    },
  },
}
</script>

<style scoped>
/* Modern Variables */
:root {
  --primary-gradient: linear-gradient(135deg, #007aff, #0051d5);
  --hero-gradient: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  --card-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  --hover-shadow: 0 20px 40px rgba(0, 0, 0, 0.15);
  --text-primary: #1d1d1f;
  --text-secondary: #666;
  --background-card: rgba(255, 255, 255, 0.95);
  --border-radius: 20px;
  --transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
}

.landing-page {
  min-height: 100vh;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
}

/* Hero Banner */
.hero-banner {
  position: relative;
  height: 100vh;
  display: flex;
  align-items: center;
  overflow: hidden;
  background: var(--hero-gradient);
}

.hero-background {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 1;
}

.hero-gradient {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.9) 0%, rgba(118, 75, 162, 0.9) 100%);
}

.floating-elements {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
}

.float-element {
  position: absolute;
  width: 60px;
  height: 60px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 50%;
  animation: float 6s ease-in-out infinite;
  backdrop-filter: blur(10px);
}

@keyframes float {
  0%,
  100% {
    transform: translateY(0px) rotate(0deg);
  }
  50% {
    transform: translateY(-20px) rotate(180deg);
  }
}

.hero-content {
  position: relative;
  z-index: 2;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 60px;
  align-items: center;
}

.hero-text {
  color: white;
}

.hero-title {
  font-size: 4rem;
  font-weight: 800;
  margin-bottom: 20px;
  line-height: 1.1;
}

.text-gradient {
  background: linear-gradient(135deg, #ffffff, #f0f8ff);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.hero-subtitle {
  font-weight: 300;
  opacity: 0.9;
}

.hero-description {
  font-size: 1.2rem;
  line-height: 1.6;
  margin-bottom: 30px;
  opacity: 0.8;
}

.hero-actions {
  display: flex;
  gap: 20px;
}

.cta-primary,
.cta-secondary {
  padding: 15px 30px;
  border-radius: 50px;
  font-weight: 600;
  font-size: 1.1rem;
  transition: var(--transition);
  cursor: pointer;
  border: none;
}

.cta-primary {
  background: white;
  color: #667eea;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
}

.cta-primary:hover {
  transform: translateY(-3px);
  box-shadow: 0 15px 40px rgba(0, 0, 0, 0.3);
}

.cta-secondary {
  background: transparent;
  color: white;
  border: 2px solid rgba(255, 255, 255, 0.3);
}

.cta-secondary:hover {
  background: rgba(255, 255, 255, 0.1);
  border-color: rgba(255, 255, 255, 0.6);
}

/* Hero Visual */
.hero-visual {
  display: flex;
  justify-content: center;
  align-items: center;
}

.product-stack {
  position: relative;
  width: 300px;
  height: 300px;
}

.product-float {
  position: absolute;
  transition: all 0.8s cubic-bezier(0.25, 0.8, 0.25, 1);
  opacity: 0;
  transform: translateY(50px) scale(0.8);
}

.product-float.animated {
  opacity: 1;
  transform: translateY(0) scale(1);
}

.product-float.iphone {
  top: 0;
  left: 50%;
  transform: translateX(-50%);
  animation-delay: 0.2s;
}

.product-float.ipad {
  bottom: 20px;
  left: 0;
  animation-delay: 0.4s;
}

.product-float.watch {
  bottom: 20px;
  right: 0;
  animation-delay: 0.6s;
}

.device-frame {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 15px;
  padding: 20px;
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.device-screen {
  font-size: 2rem;
  text-align: center;
}

/* Container */
.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

/* Main Content */
.main-content {
  background: #fafafa;
  padding: 80px 0;
}

/* Featured Categories */
.featured-categories {
  margin-bottom: 100px;
}

.section-title {
  font-size: 3rem;
  font-weight: 800;
  text-align: center;
  margin-bottom: 20px;
  color: var(--text-primary);
  background: linear-gradient(135deg, #667eea, #764ba2);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.section-subtitle {
  text-align: center;
  font-size: 1.2rem;
  color: var(--text-secondary);
  margin-bottom: 50px;
}

.categories-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 30px;
  margin-top: 50px;
}

.category-card {
  position: relative;
  background: var(--background-card);
  border-radius: var(--border-radius);
  padding: 40px 30px;
  text-align: center;
  text-decoration: none;
  color: var(--text-primary);
  box-shadow: var(--card-shadow);
  transition: var(--transition);
  overflow: hidden;
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.category-card:hover {
  transform: translateY(-10px);
  box-shadow: var(--hover-shadow);
}

.category-icon {
  font-size: 4rem;
  margin-bottom: 20px;
  display: block;
  animation: bounce 2s infinite;
}

@keyframes bounce {
  0%,
  20%,
  50%,
  80%,
  100% {
    transform: translateY(0);
  }
  40% {
    transform: translateY(-10px);
  }
  60% {
    transform: translateY(-5px);
  }
}

.category-name {
  font-size: 1.5rem;
  font-weight: 700;
  margin-bottom: 10px;
}

.category-description {
  color: var(--text-secondary);
  margin-bottom: 20px;
  line-height: 1.6;
}

.category-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: var(--primary-gradient);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: var(--transition);
}

.category-card:hover .category-overlay {
  opacity: 0.95;
}

.explore-text {
  color: white;
  font-size: 1.2rem;
  font-weight: 600;
}

/* Product Highlights */
.product-highlights {
  margin-bottom: 100px;
}

.section-header {
  text-align: center;
  margin-bottom: 60px;
}

.highlights-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(350px, 1fr));
  gap: 40px;
}

.highlight-card {
  background: white;
  border-radius: var(--border-radius);
  overflow: hidden;
  box-shadow: var(--card-shadow);
  transition: var(--transition);
}

.highlight-card:hover {
  transform: translateY(-5px);
  box-shadow: var(--hover-shadow);
}

.highlight-card.featured {
  grid-column: span 1;
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: white;
}

.highlight-image {
  position: relative;
  height: 200px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #f5f5f7, #e8e8ed);
}

.featured .highlight-image {
  background: rgba(255, 255, 255, 0.1);
}

.product-badge {
  position: absolute;
  top: 15px;
  right: 15px;
  background: #ff6b6b;
  color: white;
  padding: 5px 15px;
  border-radius: 20px;
  font-size: 0.8rem;
  font-weight: 600;
}

.product-visual {
  font-size: 4rem;
  opacity: 0.8;
}

.highlight-content {
  padding: 30px;
}

.highlight-content h3 {
  font-size: 1.5rem;
  font-weight: 700;
  margin-bottom: 10px;
}

.highlight-content p {
  color: var(--text-secondary);
  margin-bottom: 20px;
  line-height: 1.6;
}

.featured .highlight-content p {
  color: rgba(255, 255, 255, 0.8);
}

.price-tag {
  font-size: 1.2rem;
  font-weight: 700;
  color: #007aff;
  margin-bottom: 20px;
}

.featured .price-tag {
  color: white;
}

.product-btn {
  background: #1a1a1a;
  color: white;
  border: none;
  padding: 12px 25px;
  border-radius: 25px;
  font-weight: 600;
  cursor: pointer;
  transition: var(--transition);
}

.featured .product-btn {
  background: #ff6b35;
  color: white;
}

.product-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 10px 25px rgba(26, 26, 26, 0.4);
}

.featured .product-btn:hover {
  background: #e55a2b;
  box-shadow: 0 10px 25px rgba(255, 107, 53, 0.4);
}

/* Stats Section */
.stats-section {
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: white;
  padding: 80px 0;
  margin: 100px 0;
}

.stats-container {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 40px;
  text-align: center;
}

.stat-item {
  padding: 20px;
}

.stat-number {
  font-size: 3rem;
  font-weight: 800;
  margin-bottom: 10px;
  background: linear-gradient(135deg, #ffffff, #f0f8ff);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.stat-label {
  font-size: 1.1rem;
  opacity: 0.9;
}

/* Testimonials */
.testimonials-section {
  margin-bottom: 100px;
  text-align: center;
}

.testimonials-carousel {
  overflow: hidden;
  border-radius: var(--border-radius);
  margin: 50px 0;
}

.testimonial-track {
  display: flex;
  transition: transform 0.5s ease;
}

.testimonial-card {
  min-width: 100%;
  padding: 40px;
  background: white;
  box-shadow: var(--card-shadow);
}

.testimonial-content {
  max-width: 600px;
  margin: 0 auto;
}

.stars {
  margin-bottom: 20px;
}

.star {
  font-size: 1.2rem;
  margin: 0 2px;
}

.testimonial-text {
  font-size: 1.3rem;
  line-height: 1.6;
  margin-bottom: 30px;
  color: var(--text-primary);
  font-style: italic;
}

.testimonial-author {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 15px;
}

.author-avatar {
  font-size: 2rem;
}

.author-info {
  text-align: left;
}

.author-name {
  font-weight: 700;
  color: var(--text-primary);
}

.author-title {
  color: var(--text-secondary);
  font-size: 0.9rem;
}

.carousel-controls {
  display: flex;
  justify-content: center;
  gap: 10px;
  margin-top: 30px;
}

.carousel-dot {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  border: none;
  background: #ddd;
  cursor: pointer;
  transition: var(--transition);
}

.carousel-dot.active {
  background: var(--primary-gradient);
  transform: scale(1.2);
}

/* Newsletter */
.newsletter-section {
  margin-bottom: 50px;
}

.newsletter-card {
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: white;
  border-radius: var(--border-radius);
  padding: 60px 40px;
  display: grid;
  grid-template-columns: 1fr auto;
  gap: 40px;
  align-items: center;
}

.newsletter-content h2 {
  font-size: 2.5rem;
  font-weight: 800;
  margin-bottom: 15px;
}

.newsletter-content p {
  font-size: 1.2rem;
  opacity: 0.9;
  margin-bottom: 30px;
}

.newsletter-form {
  display: flex;
  gap: 15px;
}

.newsletter-input {
  flex: 1;
  padding: 15px 20px;
  border: none;
  border-radius: 25px;
  font-size: 1rem;
  outline: none;
}

.newsletter-btn {
  background: white;
  color: #667eea;
  border: none;
  padding: 15px 30px;
  border-radius: 25px;
  font-weight: 600;
  cursor: pointer;
  transition: var(--transition);
}

.newsletter-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 10px 25px rgba(255, 255, 255, 0.3);
}

.newsletter-visual {
  display: flex;
  justify-content: center;
  align-items: center;
}

.notification-icon {
  font-size: 4rem;
  opacity: 0.8;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.1);
  }
  100% {
    transform: scale(1);
  }
}

/* Popular Products Slider */
.popular-products-section {
  margin: 80px 0;
}

.popular-products-section .section-header {
  text-align: center;
  margin-bottom: 50px;
}

.popular-products-section .section-title {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  font-size: 2.5rem;
  font-weight: 700;
  margin-bottom: 15px;
}

.popular-products-section .section-subtitle {
  color: var(--text-secondary);
  font-size: 1.1rem;
  max-width: 600px;
  margin: 0 auto;
}

.slider-container {
  position: relative;
  overflow: hidden;
  padding: 0 60px;
}

.products-slider {
  overflow: hidden;
  border-radius: 20px;
}

.slider-track {
  display: flex;
  transition: transform 0.4s ease-in-out;
}

.product-slide {
  min-width: 33.333%;
  padding: 0 15px;
}

.product-card-popular {
  background: white;
  border-radius: 20px;
  padding: 30px 25px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
  height: 400px;
  display: flex;
  flex-direction: column;
}

.product-card-popular:hover {
  transform: translateY(-10px);
  box-shadow: 0 20px 50px rgba(0, 0, 0, 0.15);
}

.product-badge-popular {
  position: absolute;
  top: 20px;
  right: 20px;
  background: linear-gradient(135deg, #ff6b6b, #ee5a24);
  color: white;
  padding: 5px 12px;
  border-radius: 15px;
  font-size: 0.8rem;
  font-weight: 600;
  z-index: 2;
}

.product-image-popular {
  text-align: center;
  margin-bottom: 25px;
  position: relative;
}

.product-icon {
  font-size: 4rem;
  margin: 20px 0;
  filter: drop-shadow(0 5px 15px rgba(0, 0, 0, 0.1));
}

.product-info {
  flex-grow: 1;
  display: flex;
  flex-direction: column;
}

.product-name {
  font-size: 1.3rem;
  font-weight: 700;
  color: var(--text-primary);
  margin-bottom: 15px;
  text-align: center;
}

.product-rating {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  margin-bottom: 15px;
}

.stars {
  display: flex;
  gap: 3px;
}

.star {
  color: #ddd;
  font-size: 1.1rem;
  transition: color 0.2s ease;
}

.star.filled {
  color: #ffd700;
}

.rating-text {
  color: var(--text-secondary);
  font-size: 0.9rem;
}

.product-price {
  font-size: 1.5rem;
  font-weight: 700;
  color: var(--primary-color);
  text-align: center;
  margin-bottom: 20px;
}

.product-btn-popular {
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: white;
  padding: 12px 25px;
  border: none;
  border-radius: 25px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  text-decoration: none;
  text-align: center;
  margin-top: auto;
}

.product-btn-popular:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.3);
  color: white;
}

.slider-btn {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  background: white;
  border: none;
  width: 50px;
  height: 50px;
  border-radius: 50%;
  font-size: 1.5rem;
  color: var(--primary-color);
  cursor: pointer;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  z-index: 10;
}

.slider-btn:hover:not(:disabled) {
  background: var(--primary-color);
  color: white;
  transform: translateY(-50%) scale(1.1);
}

.slider-btn:disabled {
  opacity: 0.3;
  cursor: not-allowed;
}

.prev-btn {
  left: 10px;
}

.next-btn {
  right: 10px;
}

.slider-dots {
  display: flex;
  justify-content: center;
  gap: 10px;
  margin-top: 40px;
}

.dot {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  border: none;
  background: #ddd;
  cursor: pointer;
  transition: all 0.3s ease;
}

.dot.active {
  background: linear-gradient(135deg, #667eea, #764ba2);
  transform: scale(1.3);
}

/* Responsive Design */
@media (max-width: 1024px) {
  .hero-content {
    grid-template-columns: 1fr;
    text-align: center;
    gap: 40px;
  }

  .hero-title {
    font-size: 3rem;
  }

  .slider-container {
    padding: 0 40px;
  }

  .product-slide {
    min-width: 50%;
  }
}

@media (max-width: 768px) {
  .hero-title {
    font-size: 2.5rem;
  }

  .hero-actions {
    justify-content: center;
    flex-wrap: wrap;
  }

  .categories-grid {
    grid-template-columns: 1fr;
  }

  .highlights-grid {
    grid-template-columns: 1fr;
  }

  .newsletter-card {
    grid-template-columns: 1fr;
    text-align: center;
  }

  .newsletter-form {
    flex-direction: column;
  }

  .stats-container {
    grid-template-columns: repeat(2, 1fr);
  }

  .popular-products-section .section-title {
    font-size: 2rem;
  }

  .slider-container {
    padding: 0 20px;
  }

  .product-slide {
    min-width: 100%;
  }

  .product-card-popular {
    height: 350px;
    padding: 25px 20px;
  }

  .slider-btn {
    width: 40px;
    height: 40px;
    font-size: 1.2rem;
  }

  .prev-btn {
    left: 5px;
  }

  .next-btn {
    right: 5px;
  }
}

@media (max-width: 480px) {
  .container {
    padding: 0 15px;
  }

  .hero-banner {
    height: 80vh;
  }

  .hero-title {
    font-size: 2rem;
  }

  .section-title {
    font-size: 2rem;
  }

  .stats-container {
    grid-template-columns: 1fr;
  }

  .testimonial-card {
    padding: 20px;
  }

  .newsletter-card {
    padding: 40px 20px;
  }

  .popular-products-section {
    margin: 60px 0;
  }

  .popular-products-section .section-title {
    font-size: 1.8rem;
  }

  .product-card-popular {
    height: 320px;
    padding: 20px 15px;
  }

  .product-name {
    font-size: 1.1rem;
  }

  .product-price {
    font-size: 1.3rem;
  }

  .slider-btn {
    width: 35px;
    height: 35px;
    font-size: 1rem;
  }

  .modal-content {
    width: 95%;
    max-height: 90vh;
  }

  .modal-body {
    grid-template-columns: 1fr;
    gap: 20px;
    padding: 20px;
  }

  .modal-image {
    height: 150px;
  }

  .modal-footer {
    flex-direction: column;
    padding: 15px 20px;
  }

  .modal-btn {
    width: 100%;
  }

  .product-actions {
    flex-direction: column;
    gap: 8px;
  }
}

/* Learn More Button Styles */
.product-actions {
  display: flex;
  gap: 10px;
  margin-top: 15px;
}

.product-btn-learn {
  background: #f5f5f7;
  color: #007aff;
  border: 2px solid #007aff;
  padding: 8px 16px;
  border-radius: 20px;
  font-weight: 600;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s ease;
  text-decoration: none;
  display: inline-block;
  text-align: center;
}

.product-btn-learn:hover {
  background: #007aff;
  color: white;
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(0, 122, 255, 0.3);
}

.product-btn-cart {
  background: linear-gradient(135deg, #34c759, #30d158);
  color: white;
  padding: 8px 16px;
  border: none;
  border-radius: 20px;
  font-weight: 600;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s ease;
  text-decoration: none;
  display: inline-block;
  text-align: center;
}

.product-btn-cart:hover {
  background: linear-gradient(135deg, #30d158, #28cd4f);
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(52, 199, 89, 0.4);
}

/* Modal Styles */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  backdrop-filter: blur(4px);
}

.modal-content {
  background: white;
  border-radius: 20px;
  max-width: 600px;
  max-height: 80vh;
  width: 90%;
  overflow-y: auto;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 30px;
  border-bottom: 1px solid #f5f5f7;
}

.modal-header h2 {
  margin: 0;
  color: #1d1d1f;
  font-size: 1.5rem;
}

.close-btn {
  background: none;
  border: none;
  font-size: 24px;
  color: #86868b;
  cursor: pointer;
  padding: 0;
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  transition: background 0.3s ease;
}

.close-btn:hover {
  background: #f5f5f7;
}

.modal-body {
  padding: 30px;
  display: grid;
  grid-template-columns: 200px 1fr;
  gap: 30px;
  align-items: start;
}

.modal-image {
  width: 100%;
  height: 200px;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.product-icon-large {
  font-size: 4rem;
  opacity: 0.8;
}

.modal-details {
  flex: 1;
}

.modal-price {
  font-size: 2rem;
  font-weight: 700;
  color: #007aff;
  margin: 0 0 20px 0;
}

.modal-specs h4 {
  color: #1d1d1f;
  margin: 0 0 10px 0;
  font-size: 1.1rem;
}

.modal-specs ul {
  list-style: none;
  padding: 0;
  margin: 0 0 20px 0;
}

.modal-specs li {
  padding: 5px 0;
  color: #86868b;
  font-size: 14px;
}

.modal-specs strong {
  color: #1d1d1f;
}

.feature-list {
  margin-left: 10px;
  margin-top: 5px;
}

.feature-list li {
  font-size: 13px;
  color: #007aff;
  position: relative;
}

.feature-list li:before {
  content: '•';
  color: #007aff;
  margin-right: 8px;
}

.modal-description {
  color: #86868b;
  line-height: 1.6;
}

.modal-footer {
  display: flex;
  gap: 15px;
  padding: 20px 30px;
  border-top: 1px solid #f5f5f7;
  justify-content: flex-end;
}

.modal-btn {
  padding: 12px 24px;
  border-radius: 25px;
  font-weight: 600;
  cursor: pointer;
  border: none;
  transition: all 0.3s ease;
  text-decoration: none;
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

.modal-btn.secondary {
  background: #f5f5f7;
  color: #86868b;
}

.modal-btn.secondary:hover {
  background: #e5e5e7;
  color: #1d1d1f;
}

.modal-btn.primary {
  background: linear-gradient(135deg, #007aff, #0051d5);
  color: white;
}

.modal-btn.primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(0, 122, 255, 0.4);
}
</style>
