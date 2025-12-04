<template>
  <footer class="app-footer">
    <div class="container">
      <div class="footer-content">
        <div class="footer-section">
          <h3>Products</h3>
          <ul>
            <li><router-link to="/iphone">iPhone</router-link></li>
            <li><router-link to="/ipad">iPad</router-link></li>
            <li><router-link to="/macbook">Mac</router-link></li>
            <li><router-link to="/watch">Watch</router-link></li>
            <li><router-link to="/airpods">AirPods</router-link></li>
            <li><a href="#">Accessories</a></li>
          </ul>
        </div>
        <div class="footer-section">
          <h3>Follow us</h3>
          <div class="social-links">
            <a href="https://youtube.com/@applestore" class="social-link" target="_blank">
              <span class="social-icon youtube-icon">
                <svg viewBox="0 0 24 24" width="16" height="16" fill="currentColor">
                  <path
                    d="M23.498 6.186a3.016 3.016 0 0 0-2.122-2.136C19.505 3.545 12 3.545 12 3.545s-7.505 0-9.377.505A3.017 3.017 0 0 0 .502 6.186C0 8.07 0 12 0 12s0 3.93.502 5.814a3.016 3.016 0 0 0 2.122 2.136c1.871.505 9.376.505 9.376.505s7.505 0 9.377-.505a3.015 3.015 0 0 0 2.122-2.136C24 15.93 24 12 24 12s0-3.93-.502-5.814zM9.545 15.568V8.432L15.818 12l-6.273 3.568z"
                  />
                </svg>
              </span>
              <span>YouTube</span>
            </a>
            <a href="https://facebook.com/applestore" class="social-link" target="_blank">
              <span class="social-icon facebook-icon">
                <svg viewBox="0 0 24 24" width="16" height="16" fill="currentColor">
                  <path
                    d="M24 12.073c0-6.627-5.373-12-12-12s-12 5.373-12 12c0 5.99 4.388 10.954 10.125 11.854v-8.385H7.078v-3.47h3.047V9.43c0-3.007 1.792-4.669 4.533-4.669 1.312 0 2.686.235 2.686.235v2.953H15.83c-1.491 0-1.956.925-1.956 1.874v2.25h3.328l-.532 3.47h-2.796v8.385C19.612 23.027 24 18.062 24 12.073z"
                  />
                </svg>
              </span>
              <span>Facebook</span>
            </a>
            <a href="https://instagram.com/applestore" class="social-link" target="_blank">
              <span class="social-icon instagram-icon">
                <svg viewBox="0 0 24 24" width="16" height="16" fill="currentColor">
                  <path
                    d="M12 2.163c3.204 0 3.584.012 4.85.07 3.252.148 4.771 1.691 4.919 4.919.058 1.265.069 1.645.069 4.849 0 3.205-.012 3.584-.069 4.849-.149 3.225-1.664 4.771-4.919 4.919-1.266.058-1.644.07-4.85.07-3.204 0-3.584-.012-4.849-.07-3.26-.149-4.771-1.699-4.919-4.92-.058-1.265-.07-1.644-.07-4.849 0-3.204.013-3.583.07-4.849.149-3.227 1.664-4.771 4.919-4.919 1.266-.057 1.645-.069 4.849-.069zm0-2.163c-3.259 0-3.667.014-4.947.072-4.358.2-6.78 2.618-6.98 6.98-.059 1.281-.073 1.689-.073 4.948 0 3.259.014 3.668.072 4.948.2 4.358 2.618 6.78 6.98 6.98 1.281.058 1.689.072 4.948.072 3.259 0 3.668-.014 4.948-.072 4.354-.2 6.782-2.618 6.979-6.98.059-1.28.073-1.689.073-4.948 0-3.259-.014-3.667-.072-4.947-.196-4.354-2.617-6.78-6.979-6.98-1.281-.059-1.69-.073-4.949-.073zm0 5.838c-3.403 0-6.162 2.759-6.162 6.162s2.759 6.163 6.162 6.163 6.162-2.759 6.162-6.163c0-3.403-2.759-6.162-6.162-6.162zm0 10.162c-2.209 0-4-1.79-4-4 0-2.209 1.791-4 4-4s4 1.791 4 4c0 2.21-1.791 4-4 4zm6.406-11.845c-.796 0-1.441.645-1.441 1.44s.645 1.44 1.441 1.44c.795 0 1.439-.645 1.439-1.44s-.644-1.44-1.439-1.44z"
                  />
                </svg>
              </span>
              <span>Instagram</span>
            </a>
            <a href="https://t.me/applestore" class="social-link" target="_blank">
              <span class="social-icon telegram-icon">
                <svg viewBox="0 0 24 24" width="16" height="16" fill="currentColor">
                  <path
                    d="M11.944 0A12 12 0 0 0 0 12a12 12 0 0 0 12 12 12 12 0 0 0 12-12A12 12 0 0 0 12 0a12 12 0 0 0-.056 0zm4.962 7.224c.1-.002.321.023.465.14a.506.506 0 0 1 .171.325c.016.093.036.306.02.472-.18 1.898-.962 6.502-1.36 8.627-.168.9-.499 1.201-.82 1.23-.696.065-1.225-.46-1.9-.902-1.056-.693-1.653-1.124-2.678-1.8-1.185-.78-.417-1.21.258-1.91.177-.184 3.247-2.977 3.307-3.23.007-.032.014-.15-.056-.212s-.174-.041-.249-.024c-.106.024-1.793 1.14-5.061 3.345-.48.33-.913.49-1.302.48-.428-.008-1.252-.241-1.865-.44-.752-.245-1.349-.374-1.297-.789.027-.216.325-.437.893-.663 3.498-1.524 5.83-2.529 6.998-3.014 3.332-1.386 4.025-1.627 4.476-1.635z"
                  />
                </svg>
              </span>
              <span>Telegram</span>
            </a>
          </div>
        </div>
        <div class="footer-section">
          <h3>Contacts</h3>
          <div class="contact-info">
            <p class="contact-item">
              <span class="contact-icon">📞</span>
              <a href="tel:0966273314">0966273314</a>
            </p>
            <p class="contact-item">
              <span class="contact-icon">📧</span>
              <a href="mailto:applestore@info.com">applestore@info.com</a>
            </p>
            <p class="contact-item">
              <span class="contact-icon">💬</span>
              <a href="https://wa.me/855966273314" target="_blank">WhatsApp: +855 966 273 314</a>
            </p>
          </div>
        </div>
      </div>
      <div class="footer-bottom">
        <p>&copy; {{ currentYear }} Apple Store Cambodia. All rights reserved.</p>
      </div>
    </div>
  </footer>
</template>

<script>
export default {
  name: 'AppFooter',
  computed: {
    currentYear() {
      return new Date().getFullYear()
    },
  },
}
</script>

<style scoped>
.app-footer {
  background: #1d1d1f;
  color: white;
  margin-top: 60px;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.footer-content {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 30px;
  padding: 35px 0 20px;
}

.footer-section h3 {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 12px;
  color: white;
}

.footer-section ul {
  list-style: none;
  padding: 0;
}

.footer-section li {
  margin-bottom: 6px;
}

.footer-section a {
  color: #a1a1a6;
  text-decoration: none;
  transition: color 0.3s ease;
  font-size: 13px;
}

.footer-section a:hover {
  color: #0071e3;
}

.footer-section p {
  color: #a1a1a6;
  margin: 6px 0;
  font-size: 13px;
}

.footer-section p a {
  color: #0071e3;
  text-decoration: none;
}

.footer-section p a:hover {
  text-decoration: underline;
}

.footer-bottom {
  border-top: 1px solid #424245;
  padding: 18px 0;
  text-align: center;
}

.footer-bottom p {
  color: #a1a1a6;
  margin: 0;
  font-size: 12px;
}

/* Social Links Styling */
.social-links {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.social-link {
  display: flex;
  align-items: center;
  gap: 10px;
  color: #a1a1a6;
  text-decoration: none;
  transition: all 0.3s ease;
  padding: 6px 0;
  font-size: 13px;
}

.social-link:hover {
  color: #0071e3;
  transform: translateX(5px);
}

.social-icon {
  font-size: 16px;
  width: 24px;
  height: 24px;
  text-align: center;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 5px;
  font-weight: bold;
  transition: all 0.3s ease;
}

.youtube-icon {
  background: #ff0000;
  color: white;
  font-size: 14px;
}

.facebook-icon {
  background: #1877f2;
  color: white;
  font-family: Arial, sans-serif;
  font-size: 16px;
}

.instagram-icon {
  background: linear-gradient(
    45deg,
    #f09433 0%,
    #e6683c 25%,
    #dc2743 50%,
    #cc2366 75%,
    #bc1888 100%
  );
  color: white;
  font-size: 16px;
}

.telegram-icon {
  background: #0088cc;
  color: white;
  font-size: 14px;
}

.social-link:hover .youtube-icon {
  background: #cc0000;
  transform: scale(1.1);
}

.social-link:hover .facebook-icon {
  background: #166fe5;
  transform: scale(1.1);
}

.social-link:hover .instagram-icon {
  transform: scale(1.1);
  filter: brightness(1.1);
}

.social-link:hover .telegram-icon {
  background: #0077b5;
  transform: scale(1.1);
}

/* Contact Info Styling */
.contact-info {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.contact-item {
  display: flex;
  align-items: center;
  gap: 10px;
  color: #a1a1a6;
  margin: 0;
  padding: 6px 0;
  font-size: 13px;
}

.contact-item a {
  color: #0071e3;
  text-decoration: none;
  transition: color 0.3s ease;
}

.contact-item a:hover {
  color: white;
  text-decoration: underline;
}

.contact-icon {
  font-size: 16px;
  width: 20px;
  text-align: center;
}

@media (max-width: 768px) {
  .footer-content {
    grid-template-columns: 1fr;
    gap: 30px;
    text-align: center;
  }

  .social-links,
  .contact-info {
    align-items: center;
  }
}
</style>
