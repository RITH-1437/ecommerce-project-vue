<template>
  <div id="app">
    <div class="global-banner-container">
      <ErrorBanner />
    </div>
    <router-view />
    <LocationPopup />
    <ContactPopup />
  </div>
</template>

<script>
import LocationPopup from '@/components/global/LocationPopup.vue'
import ContactPopup from '@/components/global/ContactPopup.vue'
import ErrorBanner from '@/components/global/ErrorBanner.vue'

export default {
  name: 'App',
  components: {
    LocationPopup,
    ContactPopup,
    ErrorBanner,
  },
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

#app {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

.global-banner-container {
  position: sticky;
  top: 0;
  z-index: 1000;
  padding: 0;
}
</style>
