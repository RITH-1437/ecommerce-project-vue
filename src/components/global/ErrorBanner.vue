<template>
  <transition name="fade-slide">
    <div v-if="visible && message" class="error-banner" role="alert">
      <div class="error-banner__left">
        <span class="error-banner__icon">⚠️</span>
        <span class="error-banner__text">{{ message }}</span>
      </div>
      <button class="error-banner__close" @click="dismiss" aria-label="Dismiss error">✖</button>
    </div>
  </transition>
</template>

<script setup>
import { computed } from 'vue'
import { storeToRefs } from 'pinia'
import { useErrorStore } from '@/stores/error.js'

const store = useErrorStore()
const { showBanner: visible, latest } = storeToRefs(store)

const message = computed(() => latest.value?.message || '')
function dismiss() {
  store.clearBanner()
}
</script>

<style scoped>
.error-banner {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 16px;
  background: linear-gradient(135deg, #ff3b30, #ff9f0a);
  color: #ffffff;
  padding: 14px 18px;
  border-radius: 12px;
  box-shadow: 0 8px 24px rgba(255, 59, 48, 0.25);
  border: 1px solid rgba(255, 255, 255, 0.15);
  margin: 12px 0;
}
.error-banner__left { display: flex; align-items: center; gap: 10px; }
.error-banner__icon { font-size: 20px; filter: drop-shadow(0 2px 4px rgba(0,0,0,0.25)); }
.error-banner__text { font-weight: 700; letter-spacing: 0.2px; }
.error-banner__close { appearance: none; border: none; border-radius: 8px; padding: 6px 10px; cursor: pointer; color: #ffffff; background: rgba(255, 255, 255, 0.2); transition: background 0.25s ease, transform 0.15s ease; }
.error-banner__close:hover { background: rgba(255, 255, 255, 0.35); }
.error-banner__close:active { transform: scale(0.98); }
.fade-slide-enter-active, .fade-slide-leave-active { transition: opacity 0.25s ease, transform 0.25s ease; }
.fade-slide-enter-from, .fade-slide-leave-to { opacity: 0; transform: translateY(-6px); }
</style>
