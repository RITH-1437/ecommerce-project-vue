<template>
  <div v-if="!isAdminPage">
    <!-- Location Popup -->
    <div class="location-popup" :class="{ 'popup-visible': showLocationPopup }">
      <div class="popup-content">
        <div class="popup-header">
          <div class="location-icon">
            <svg viewBox="0 0 24 24" width="20" height="20" fill="currentColor">
              <path
                d="M12 2C8.13 2 5 5.13 5 9c0 5.25 7 13 7 13s7-7.75 7-13c0-3.87-3.13-7-7-7zm0 9.5c-1.38 0-2.5-1.12-2.5-2.5s1.12-2.5 2.5-2.5 2.5 1.12 2.5 2.5-1.12 2.5-2.5 2.5z"
              />
            </svg>
          </div>
          <h4>Our Location</h4>
          <button class="close-btn" @click="closeLocationPopup">×</button>
        </div>
        <div class="popup-body">
          <p class="location-address">
            Russian Conf Norodom Boulevard,<br />
            Phnom Penh 120404, Cambodia
          </p>
          <div class="popup-actions">
            <a
              href="https://maps.google.com/?q=Russian+Conf+Norodom+Boulevard+Phnom+Penh+120404+Cambodia"
              target="_blank"
              class="map-btn"
            >
              <svg viewBox="0 0 24 24" width="16" height="16" fill="currentColor">
                <path
                  d="M12 2C8.13 2 5 5.13 5 9c0 5.25 7 13 7 13s7-7.75 7-13c0-3.87-3.13-7-7-7zm0 9.5c-1.38 0-2.5-1.12-2.5-2.5s1.12-2.5 2.5-2.5 2.5 1.12 2.5 2.5-1.12 2.5-2.5 2.5z"
                />
              </svg>
              View on Maps
            </a>
          </div>
        </div>
      </div>
    </div>

    <!-- Location Toggle Button -->
    <button
      class="location-toggle-btn"
      @click="toggleLocationPopup"
      :class="{ active: showLocationPopup }"
    >
      <svg viewBox="0 0 24 24" width="18" height="18" fill="currentColor">
        <path
          d="M12 2C8.13 2 5 5.13 5 9c0 5.25 7 13 7 13s7-7.75 7-13c0-3.87-3.13-7-7-7zm0 9.5c-1.38 0-2.5-1.12-2.5-2.5s1.12-2.5 2.5-2.5 2.5 1.12 2.5 2.5-1.12 2.5-2.5 2.5z"
        />
      </svg>
    </button>
  </div>
</template>

<script>
export default {
  name: 'LocationPopup',
  data() {
    return {
      showLocationPopup: false,
    }
  },
  computed: {
    isAdminPage() {
      const currentPath = this.$route.path
      return currentPath.includes('/dashboard') || currentPath.includes('/admin')
    },
  },
  methods: {
    toggleLocationPopup() {
      this.showLocationPopup = !this.showLocationPopup
    },
    closeLocationPopup() {
      this.showLocationPopup = false
    },
  },
}
</script>

<style scoped>
/* Location Popup */
.location-toggle-btn {
  position: fixed;
  left: 20px;
  top: 50%;
  transform: translateY(-50%);
  background: linear-gradient(135deg, #28a745, #20c997);
  color: white;
  border: none;
  width: 50px;
  height: 50px;
  border-radius: 50%;
  font-size: 1.2rem;
  cursor: pointer;
  box-shadow: 0 4px 15px rgba(40, 167, 69, 0.3);
  transition: all 0.3s ease;
  z-index: 1000;
}

.location-toggle-btn:hover {
  transform: translateY(-50%) scale(1.1);
  box-shadow: 0 6px 20px rgba(40, 167, 69, 0.4);
}

.location-toggle-btn.active {
  background: linear-gradient(135deg, #1e7e34, #17a2b8);
}

.location-popup {
  position: fixed;
  left: -350px;
  top: 50%;
  transform: translateY(-50%);
  width: 320px;
  background: white;
  border-radius: 15px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  z-index: 999;
  overflow: hidden;
}

.location-popup.popup-visible {
  left: 80px;
}

.popup-content {
  padding: 0;
}

.popup-header {
  background: linear-gradient(135deg, #28a745, #20c997);
  color: white;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 12px;
  position: relative;
}

.location-icon {
  font-size: 1.5rem;
}

.popup-header h4 {
  margin: 0;
  flex-grow: 1;
  font-size: 1.1rem;
  font-weight: 600;
}

.close-btn {
  background: none;
  border: none;
  color: white;
  font-size: 1.5rem;
  cursor: pointer;
  padding: 0;
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  transition: background-color 0.3s ease;
}

.close-btn:hover {
  background: rgba(255, 255, 255, 0.2);
}

.popup-body {
  padding: 25px;
}

.location-address {
  color: #333;
  line-height: 1.6;
  margin: 0 0 20px 0;
  font-size: 0.95rem;
}

.popup-actions {
  display: flex;
  justify-content: center;
}

.map-btn {
  background: linear-gradient(135deg, #28a745, #20c997);
  color: white;
  padding: 10px 20px;
  border-radius: 25px;
  text-decoration: none;
  font-size: 0.9rem;
  font-weight: 600;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 8px;
}

.map-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(40, 167, 69, 0.4);
  color: white;
}

/* Responsive Design */
@media (max-width: 768px) {
  .location-popup {
    width: 280px;
    left: -300px;
  }

  .location-popup.popup-visible {
    left: 70px;
  }

  .location-toggle-btn {
    left: 15px;
    width: 45px;
    height: 45px;
    font-size: 1.1rem;
  }
}

@media (max-width: 480px) {
  .location-popup {
    width: 260px;
    left: -280px;
  }

  .location-popup.popup-visible {
    left: 60px;
  }

  .location-toggle-btn {
    left: 10px;
    width: 40px;
    height: 40px;
    font-size: 1rem;
  }

  .popup-header {
    padding: 15px;
  }

  .popup-body {
    padding: 20px;
  }
}
</style>
