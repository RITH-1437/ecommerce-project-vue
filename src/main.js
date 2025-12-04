import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'
import './assets/styles/global.css'

const app = createApp(App)
const pinia = createPinia()

app.use(pinia)
app.use(router)

// Global error capture
import { useErrorStore } from './stores/error.js'
app.config.errorHandler = (err, instance, info) => {
  try {
    const store = useErrorStore()
    store.capture(err, info)
  } catch (e) {
    console.error('Global error handler failed', e)
  }
}
app.config.warnHandler = (msg, instance, trace) => {
  try {
    const store = useErrorStore()
    store.capture(new Error(String(msg)), 'Vue warn', { trace })
  } catch (e) {
    console.warn('Global warn handler failed', e)
  }
}

// Initialize auth store after Pinia is set up
import { useAuthStore } from './stores/auth.js'
const authStore = useAuthStore()
// Auth store automatically initializes from localStorage

app.mount('#app')
