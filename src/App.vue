<template>
  <div id="app">
    <router-view />
    <LocationPopup />
    <ContactPopup />
  </div>
</template>

<script>
import LocationPopup from '@/components/global/LocationPopup.vue'
import ContactPopup from '@/components/global/ContactPopup.vue'

export default {
  name: 'App',
  components: {
    LocationPopup,
    ContactPopup,
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
</style>
