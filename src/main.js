import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'
import './assets/styles/global.css'

const app = createApp(App)
const pinia = createPinia()

app.use(pinia)
app.use(router)

// Initialize auth store after Pinia is set up
import { useAuthStore } from './stores/auth.js'
const authStore = useAuthStore()
// Auth store automatically initializes from localStorage

app.mount('#app')
