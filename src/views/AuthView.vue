<template>
  <div class="auth-page">
    <div class="auth-container">
      <div class="auth-header">
        <router-link to="/" class="back-link">
          <span class="back-icon">←</span>
          <span>Back to Store</span>
        </router-link>
        <h1 class="auth-title">Apple Store</h1>
      </div>

      <div class="auth-form-container">
        <!-- Google Configuration Warning (only show if enabled but not configured) -->
        <div v-if="isGoogleEnabled && !isGoogleConfigured" class="warning-message">
          <span class="warning-icon">⚙️</span>
          <span
            >Google OAuth not configured. Please set up your Google Client ID in the .env file for
            full functionality.</span
          >
        </div>

        <!-- Error Display (only show if enabled or auth error) -->
        <div v-if="authStore.error || (isGoogleEnabled && googleError)" class="error-message">
          <span class="error-icon">⚠️</span>
          <span>{{ authStore.error || googleError }}</span>
          <button @click="clearAllErrors" class="error-close">×</button>
        </div>

        <!-- Success Message -->
        <div v-if="successMessage" class="success-message">
          <span class="success-icon">✅</span>
          <span>{{ successMessage }}</span>
        </div>

        <div class="auth-tabs">
          <button
            class="auth-tab"
            :class="{ active: authMode === 'login' }"
            @click="authMode = 'login'"
          >
            Sign In
          </button>
          <button
            class="auth-tab"
            :class="{ active: authMode === 'register' }"
            @click="authMode = 'register'"
          >
            Create Account
          </button>
        </div>

        <!-- Login Form -->
        <form v-if="authMode === 'login'" @submit.prevent="handleLogin" class="auth-form">
          <h2 class="form-title">Sign in to your account</h2>
          <p class="form-subtitle">Welcome back! Please enter your details.</p>

          <!-- Google Sign-In Button (only show if enabled) -->
          <button
            v-if="isGoogleEnabled"
            type="button"
            class="auth-button social google"
            @click="handleGoogleSignIn"
            :disabled="isSigningIn || authStore.isLoading"
            :class="{ 'not-configured': !isGoogleConfigured }"
          >
            <span class="social-icon">🔍</span>
            <span v-if="isSigningIn">Signing in with Google...</span>
            <span v-else-if="!isGoogleConfigured">Google OAuth (Not Configured)</span>
            <span v-else>Continue with Google</span>
          </button>

          <!-- Alternative Google Sign-In (if first method fails) -->
          <button
            v-if="isGoogleEnabled && googleError && isGoogleConfigured"
            type="button"
            class="auth-button social google-alt"
            @click="handleAlternativeGoogleSignIn"
            :disabled="isSigningIn || authStore.isLoading"
          >
            <span class="social-icon">🔄</span>
            <span>Try Google Sign-In Again</span>
          </button>
          <div v-if="isGoogleEnabled" class="auth-divider">
            <span>or sign in with email</span>
          </div>

          <div class="form-group">
            <label for="email">Email</label>
            <input
              id="email"
              type="email"
              v-model="loginForm.email"
              placeholder="Enter your email"
              required
              :disabled="authStore.isLoading"
            />
          </div>

          <div class="form-group">
            <label for="password">Password</label>
            <input
              id="password"
              type="password"
              v-model="loginForm.password"
              placeholder="Enter your password"
              required
              :disabled="authStore.isLoading"
              @input="validateLoginPassword"
            />
            <div v-if="loginPasswordWarning" class="password-warning">
              <span class="warning-icon">⚠️</span>
              <span>{{ loginPasswordWarning }}</span>
            </div>
          </div>

          <div class="form-group">
            <label for="loginRole">Role</label>
            <select
              id="loginRole"
              v-model="loginForm.role"
              class="role-select"
              required
              :disabled="authStore.isLoading"
            >
              <option value="">Select your role</option>
              <option value="customer">Customer</option>
              <option value="employee">Employee</option>
              <option value="admin">Admin</option>
            </select>
          </div>

          <div class="form-options">
            <label class="checkbox-container">
              <input
                type="checkbox"
                v-model="loginForm.rememberMe"
                :disabled="authStore.isLoading"
              />
              <span class="checkmark"></span>
              Remember me
            </label>
            <a href="#" class="forgot-password">Forgot password?</a>
          </div>

          <button type="submit" class="auth-button primary" :disabled="authStore.isLoading">
            <span v-if="authStore.isLoading">Signing In...</span>
            <span v-else>Sign In</span>
          </button>

          <!-- Quick Login Buttons -->
          <div class="quick-login-buttons">
            <button
              @click="quickUserLogin()"
              class="quick-user-btn"
              :disabled="authStore.isLoading"
            >
              <span class="user-icon">👤</span>
              <span>Demo User</span>
            </button>
            <button @click="quickLogin()" class="demo-login-btn" :disabled="authStore.isLoading">
              <span class="demo-icon">🚀</span>
              <span>Demo Admin</span>
            </button>
          </div>
        </form>

        <!-- Register Form -->
        <form v-if="authMode === 'register'" @submit.prevent="handleRegister" class="auth-form">
          <h2 class="form-title">Create your account</h2>
          <p class="form-subtitle">Join Apple Store to access exclusive features.</p>

          <!-- Google Sign-Up Button (only show if enabled) -->
          <button
            v-if="isGoogleEnabled"
            type="button"
            class="auth-button social google"
            @click="handleGoogleSignIn"
            :disabled="isSigningIn || authStore.isLoading"
            :class="{ 'not-configured': !isGoogleConfigured }"
          >
            <span class="social-icon">🔍</span>
            <span v-if="isSigningIn">Creating account...</span>
            <span v-else-if="!isGoogleConfigured">Google OAuth (Not Configured)</span>
            <span v-else>Continue with Google</span>
          </button>

          <div v-if="isGoogleEnabled" class="auth-divider">
            <span>or create account with email</span>
          </div>

          <div class="form-row">
            <div class="form-group">
              <label for="firstName">First Name</label>
              <input
                id="firstName"
                type="text"
                v-model="registerForm.firstName"
                placeholder="First name"
                required
                :disabled="authStore.isLoading"
              />
            </div>
            <div class="form-group">
              <label for="lastName">Last Name</label>
              <input
                id="lastName"
                type="text"
                v-model="registerForm.lastName"
                placeholder="Last name"
                required
                :disabled="authStore.isLoading"
              />
            </div>
          </div>

          <div class="form-group">
            <label for="registerEmail">Email</label>
            <input
              id="registerEmail"
              type="email"
              v-model="registerForm.email"
              placeholder="Enter your email"
              required
              :disabled="authStore.isLoading"
            />
          </div>

          <div class="form-group">
            <label for="registerPassword">Password</label>
            <input
              id="registerPassword"
              type="password"
              v-model="registerForm.password"
              placeholder="Create a password (min 8 chars, include special symbol)"
              required
              :disabled="authStore.isLoading"
              @input="validateRegisterPassword"
              :class="{ 'weak-password': passwordStrength === 'weak' }"
            />
            <div v-if="passwordStrength" class="password-strength" :class="passwordStrength">
              <div class="strength-bar">
                <div class="strength-fill" :style="{ width: strengthPercentage }"></div>
              </div>
              <div class="strength-text">
                <span class="strength-icon">{{ strengthIcon }}</span>
                <span>{{ strengthMessage }}</span>
              </div>
            </div>
            <div class="password-requirements">
              <div class="requirement" :class="{ met: hasMinLength }">
                <span>{{ hasMinLength ? '✓' : '○' }}</span> At least 8 characters
              </div>
              <div class="requirement" :class="{ met: hasSpecialChar }">
                <span>{{ hasSpecialChar ? '✓' : '○' }}</span> Contains special symbol (!@#$%^&*)
              </div>
            </div>
          </div>

          <div class="form-group">
            <label for="confirmPassword">Confirm Password</label>
            <input
              id="confirmPassword"
              type="password"
              v-model="registerForm.confirmPassword"
              placeholder="Confirm your password"
              required
              :disabled="authStore.isLoading"
            />
          </div>

          <label class="checkbox-container">
            <input
              type="checkbox"
              v-model="registerForm.agreeToTerms"
              required
              :disabled="authStore.isLoading"
            />
            <span class="checkmark"></span>
            I agree to the <a href="#">Terms of Service</a> and <a href="#">Privacy Policy</a>
          </label>

          <button type="submit" class="auth-button primary" :disabled="authStore.isLoading">
            <span v-if="authStore.isLoading">Creating Account...</span>
            <span v-else>Create Account</span>
          </button>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import { useAuthStore } from '@/stores/auth.js'
import { useDemoStore } from '@/stores/demo.js'
import { useGoogleAuth } from '@/composables/useGoogleAuth.js'
import Swal from 'sweetalert2'

export default {
  name: 'AuthView',
  setup() {
    const authStore = useAuthStore()
    const demoStore = useDemoStore()
    const {
      isSigningIn,
      signInWithGoogle,
      googleError,
      isGoogleEnabled,
      isGoogleConfigured,
      triggerGoogleSignIn,
    } = useGoogleAuth()

    return {
      authStore,
      demoStore,
      isSigningIn,
      signInWithGoogle,
      googleError,
      isGoogleEnabled,
      isGoogleConfigured,
      triggerGoogleSignIn,
    }
  },
  data() {
    return {
      authMode: 'login',
      successMessage: '',
      loginPasswordWarning: '',
      passwordStrength: '',
      hasMinLength: false,
      hasSpecialChar: false,
      loginForm: {
        email: '',
        password: '',
        role: '',
        rememberMe: false,
      },
      registerForm: {
        firstName: '',
        lastName: '',
        email: '',
        password: '',
        confirmPassword: '',
        role: '',
        agreeToTerms: false,
      },
    }
  },
  watch: {
    // Redirect to appropriate page after successful login
    'authStore.isLoggedIn'(isLoggedIn) {
      if (isLoggedIn) {
        // Check for redirect query parameter
        const redirectPath =
          this.$route.query.redirect || (this.authStore.isAdmin ? '/admin/overview' : '/')

        Swal.fire({
          icon: 'success',
          title: 'Welcome Back!',
          html: `
            <div style="text-align: center;">
              <p style="font-size: 16px; color: #333; margin: 10px 0;">
                Successfully signed in as <strong>${this.authStore.user?.name || 'User'}</strong>
              </p>
              <p style="font-size: 14px; color: #666;">
                Role: <span style="color: #0071e3; font-weight: 600;">${this.authStore.user?.role || 'Customer'}</span>
              </p>
            </div>
          `,
          confirmButtonText: 'Continue',
          confirmButtonColor: '#0071e3',
          timer: 2500,
          timerProgressBar: true,
          showClass: {
            popup: 'animate__animated animate__fadeInDown',
          },
          hideClass: {
            popup: 'animate__animated animate__fadeOutUp',
          },
        }).then(() => {
          this.$router.push(redirectPath)
        })
      }
    },
  },
  computed: {
    strengthPercentage() {
      if (this.passwordStrength === 'weak') return '33%'
      if (this.passwordStrength === 'medium') return '66%'
      if (this.passwordStrength === 'strong') return '100%'
      return '0%'
    },
    strengthIcon() {
      if (this.passwordStrength === 'weak') return '⚠️'
      if (this.passwordStrength === 'medium') return '🔸'
      if (this.passwordStrength === 'strong') return '✅'
      return ''
    },
    strengthMessage() {
      if (this.passwordStrength === 'weak')
        return 'Weak Password - Add more characters and special symbols'
      if (this.passwordStrength === 'medium')
        return 'Medium Password - Consider adding more special characters'
      if (this.passwordStrength === 'strong') return 'Strong Password'
      return ''
    },
  },
  methods: {
    validateLoginPassword() {
      const password = this.loginForm.password
      const hasSpecial = /[!@#$%^&*(),.?":{}|<>]/.test(password)

      if (password.length < 8 && !hasSpecial) {
        this.loginPasswordWarning = 'Weak Password: Less than 8 characters and no special symbols'
      } else if (password.length < 8) {
        this.loginPasswordWarning = 'Weak Password: Less than 8 characters'
      } else if (!hasSpecial) {
        this.loginPasswordWarning = 'Weak Password: No special symbols'
      } else {
        this.loginPasswordWarning = ''
      }
    },

    validateRegisterPassword() {
      const password = this.registerForm.password
      this.hasMinLength = password.length >= 8
      this.hasSpecialChar = /[!@#$%^&*(),.?":{}|<>]/.test(password)

      if (password.length === 0) {
        this.passwordStrength = ''
        return
      }

      if (password.length < 8 || !this.hasSpecialChar) {
        this.passwordStrength = 'weak'
      } else if (password.length >= 8 && this.hasSpecialChar) {
        const hasNumber = /[0-9]/.test(password)
        const hasUpper = /[A-Z]/.test(password)
        const hasLower = /[a-z]/.test(password)

        if (hasNumber && hasUpper && hasLower && password.length >= 12) {
          this.passwordStrength = 'strong'
        } else {
          this.passwordStrength = 'medium'
        }
      }
    },

    async handleLogin() {
      this.authStore.clearError()
      this.authStore.setLoading(true)

      try {
        // Validate form
        if (!this.loginForm.email || !this.loginForm.password || !this.loginForm.role) {
          throw new Error('Please fill in all required fields')
        }

        // Simulate API call delay
        await new Promise((resolve) => setTimeout(resolve, 1000))

        // Create user data
        const userData = {
          id: Date.now().toString(),
          name: this.loginForm.email.split('@')[0],
          email: this.loginForm.email,
          role: this.loginForm.role,
          provider: 'email',
          loginTime: new Date().toISOString(),
        }

        // Login through store
        this.authStore.login(userData)
      } catch (error) {
        this.authStore.setError(error.message || 'Login failed. Please try again.')
      } finally {
        this.authStore.setLoading(false)
      }
    },

    async handleRegister() {
      this.authStore.clearError()
      this.authStore.setLoading(true)

      try {
        // Validate form
        if (
          !this.registerForm.firstName ||
          !this.registerForm.lastName ||
          !this.registerForm.email ||
          !this.registerForm.password ||
          !this.registerForm.agreeToTerms
        ) {
          throw new Error('Please fill in all required fields and accept the terms')
        }

        if (this.registerForm.password !== this.registerForm.confirmPassword) {
          throw new Error('Passwords do not match')
        }

        if (this.registerForm.password.length < 8) {
          throw new Error('Password must be at least 8 characters long')
        }

        const hasSpecialChar = /[!@#$%^&*(),.?":{}|<>]/.test(this.registerForm.password)
        if (!hasSpecialChar) {
          throw new Error('Password must contain at least one special symbol (!@#$%^&*)')
        }

        if (this.passwordStrength === 'weak') {
          throw new Error(
            'Password is too weak. Please use a stronger password with at least 8 characters and special symbols',
          )
        }

        // Simulate API call delay
        await new Promise((resolve) => setTimeout(resolve, 1200))

        // Create user data - default to customer role
        const userData = {
          id: Date.now().toString(),
          name: `${this.registerForm.firstName} ${this.registerForm.lastName}`,
          email: this.registerForm.email,
          role: 'customer', // Always start as customer
          provider: 'email',
          firstName: this.registerForm.firstName,
          lastName: this.registerForm.lastName,
          loginTime: new Date().toISOString(),
        }

        // Register through store
        this.authStore.login(userData)

        // Show success alert after registration
        await Swal.fire({
          icon: 'success',
          title: 'Account Created!',
          html: `
            <div style="text-align: center;">
              <p style="font-size: 16px; color: #333; margin: 10px 0;">
                Welcome to Apple Store, <strong>${userData.name}</strong>! 🎉
              </p>
              <p style="font-size: 14px; color: #666;">
                Your account has been successfully created as a Customer.
              </p>
              <p style="font-size: 12px; color: #999; margin-top: 8px;">
                Admins can promote your account to higher roles later.
              </p>
            </div>
          `,
          confirmButtonText: 'Get Started',
          confirmButtonColor: '#0071e3',
          timer: 3000,
          timerProgressBar: true,
          showClass: {
            popup: 'animate__animated animate__fadeInDown',
          },
          hideClass: {
            popup: 'animate__animated animate__fadeOutUp',
          },
        })
      } catch (error) {
        this.authStore.setError(error.message || 'Registration failed. Please try again.')
      } finally {
        this.authStore.setLoading(false)
      }
    },

    async handleGoogleSignIn() {
      try {
        console.log('Starting Google Sign-In process...')
        this.clearAllErrors()

        if (!this.isGoogleConfigured) {
          this.authStore.setError(
            'Google OAuth is not configured. Please check the setup instructions.',
          )
          return
        }

        await this.signInWithGoogle()
      } catch (error) {
        console.error('Google Sign-In failed:', error)
        const errorMessage = error.message || 'Google Sign-In failed. Please try again.'
        this.authStore.setError(errorMessage)
      }
    },

    async handleAlternativeGoogleSignIn() {
      try {
        console.log('Trying alternative Google Sign-In method...')
        this.clearAllErrors()
        await this.triggerGoogleSignIn()
      } catch (error) {
        console.error('Alternative Google Sign-In failed:', error)
        this.authStore.setError('Google Sign-In failed. Please try the email/password option.')
      }
    },

    clearAllErrors() {
      this.authStore.clearError()
      // Note: googleError is from composable and is reactive
    },

    showSuccessMessage(message) {
      this.successMessage = message
      setTimeout(() => {
        this.successMessage = ''
      }, 3000)
    },

    async quickUserLogin() {
      this.authStore.clearError()
      this.authStore.setLoading(true)

      try {
        // Simulate quick login delay
        await new Promise((resolve) => setTimeout(resolve, 500))

        // Create demo customer user data
        const userData = {
          id: 'quick-user-001',
          name: 'Quick User',
          email: 'user@quicklogin.com',
          role: 'customer',
          provider: 'quick',
          firstName: 'Quick',
          lastName: 'User',
          loginTime: new Date().toISOString(),
        }

        // Login through store
        this.authStore.login(userData)

        // Show success message
        await Swal.fire({
          icon: 'success',
          title: `Welcome, ${userData.name}!`,
          html: `
            <div style="text-align: center;">
              <p style="font-size: 16px; color: #333; margin: 10px 0;">
                Successfully logged in as <strong>Customer</strong>
              </p>
              <p style="font-size: 14px; color: #666;">
                This is a quick login account for testing purposes.
              </p>
            </div>
          `,
          confirmButtonText: 'Get Started',
          confirmButtonColor: '#0071e3',
          timer: 2000,
          timerProgressBar: true,
          showClass: {
            popup: 'animate__animated animate__fadeInDown',
          },
          hideClass: {
            popup: 'animate__animated animate__fadeOutUp',
          },
        })
      } catch (error) {
        this.authStore.setError(error.message || 'Quick user login failed. Please try again.')
      } finally {
        this.authStore.setLoading(false)
      }
    },

    async quickLogin() {
      this.authStore.clearError()
      this.authStore.setLoading(true)

      try {
        // Simulate quick login delay
        await new Promise((resolve) => setTimeout(resolve, 500))

        // Create demo admin user data
        const userData = {
          id: 'demo-admin-001',
          name: 'Demo Admin',
          email: 'admin@demo.com',
          role: 'admin',
          provider: 'demo',
          firstName: 'Demo',
          lastName: 'Admin',
          loginTime: new Date().toISOString(),
        }

        // Login through store
        this.authStore.login(userData)

        // Show success message
        await Swal.fire({
          icon: 'success',
          title: `Welcome, ${userData.name}!`,
          html: `
            <div style="text-align: center;">
              <p style="font-size: 16px; color: #333; margin: 10px 0;">
                Successfully logged in as <strong>Admin</strong>
              </p>
              <p style="font-size: 14px; color: #666;">
                This is a demo account for testing purposes.
              </p>
            </div>
          `,
          confirmButtonText: 'Get Started',
          confirmButtonColor: '#0071e3',
          timer: 2000,
          timerProgressBar: true,
          showClass: {
            popup: 'animate__animated animate__fadeInDown',
          },
          hideClass: {
            popup: 'animate__animated animate__fadeOutUp',
          },
        })
      } catch (error) {
        this.authStore.setError(error.message || 'Demo admin login failed. Please try again.')
      } finally {
        this.authStore.setLoading(false)
      }
    },

    resetForms() {
      this.loginForm = {
        email: '',
        password: '',
        role: '',
        rememberMe: false,
      }
      this.registerForm = {
        firstName: '',
        lastName: '',
        email: '',
        password: '',
        confirmPassword: '',
        role: '',
        agreeToTerms: false,
      }
    },
  },

  beforeUnmount() {
    // Clear any pending operations
    this.authStore.setLoading(false)
  },
}
</script>

<style scoped>
.auth-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
}

.auth-container {
  width: 100%;
  max-width: 500px;
  background: white;
  border-radius: 20px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.auth-header {
  background: #f8f9fa;
  padding: 15px 30px 30px 30px;
  text-align: center;
  border-bottom: 1px solid #f0f0f0;
}

.back-link {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  color: #666;
  text-decoration: none;
  font-size: 14px;
  margin-bottom: 10px;
  transition: color 0.3s ease;
}

.back-link:hover {
  color: #0071e3;
}

.back-icon {
  font-size: 16px;
}

.auth-title {
  font-size: 28px;
  font-weight: 700;
  color: #1d1d1f;
  margin: 0;
}

.auth-form-container {
  padding: 20px 40px 40px 40px;
}

/* Demo Admin Login Button */
.demo-login-btn {
  flex: 1;
  padding: 16px 20px;
  border: 2px solid #00eaff;
  background: linear-gradient(135deg, #0b89af, #03e6ff );
  color: white;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 700;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  box-shadow: 0 4px 12px rgba(220, 53, 69, 0.3);
}

.demo-login-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(220, 53, 69, 0.4);
  background: linear-gradient(135deg, #01b7ff, #00c6dc);
}

.demo-login-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  transform: none;
}

.demo-icon {
  font-size: 20px;
  filter: drop-shadow(0 1px 2px rgba(0, 0, 0, 0.2));
}

/* Quick User Login Button */
.quick-user-btn {
  flex: 1;
  padding: 16px 20px;
  border: 2px solid #28a745;
  background: linear-gradient(135deg, #28a745, #20c997);
  color: white;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 700;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  box-shadow: 0 4px 12px rgba(40, 167, 69, 0.3);
}

.quick-user-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(40, 167, 69, 0.4);
  background: linear-gradient(135deg, #20c997, #17a2b8);
}

.quick-user-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  transform: none;
}

.user-icon {
  font-size: 20px;
  filter: drop-shadow(0 1px 2px rgba(0, 0, 0, 0.2));
}

/* Quick Login Buttons */
.quick-login-buttons {
  display: flex;
  gap: 12px;
  margin-bottom: 20px;
}

.quick-login-btn {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  padding: 16px 12px;
  border: 2px solid #e5e5e7;
  border-radius: 12px;
  background: white;
  color: #1d1d1f;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 14px;
}

.quick-login-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.quick-login-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  transform: none;
}

.quick-login-btn.admin {
  border-color: #ffebee;
  background: linear-gradient(135deg, #ffebee 0%, #ffffff 100%);
}

.quick-login-btn.admin:hover:not(:disabled) {
  border-color: #d32f2f;
  background: linear-gradient(135deg, #ffebee 0%, #fce4ec 100%);
}

.quick-login-btn.employee {
  border-color: #f3e5f5;
  background: linear-gradient(135deg, #f3e5f5 0%, #ffffff 100%);
}

.quick-login-btn.employee:hover:not(:disabled) {
  border-color: #7b1fa2;
  background: linear-gradient(135deg, #f3e5f5 0%, #e1bee7 100%);
}

.quick-login-btn.customer {
  border-color: #e8f5e9;
  background: linear-gradient(135deg, #e8f5e9 0%, #ffffff 100%);
}

.quick-login-btn.customer:hover:not(:disabled) {
  border-color: #2e7d32;
  background: linear-gradient(135deg, #e8f5e9 0%, #c8e6c9 100%);
}

.quick-icon {
  font-size: 24px;
}

.quick-login-note {
  font-size: 12px;
  color: #86868b;
  margin: 0;
  font-weight: 500;
}

.auth-tabs {
  display: flex;
  background: #f5f5f7;
  border-radius: 12px;
  padding: 4px;
  margin-bottom: 30px;
}

.auth-tab {
  flex: 1;
  padding: 12px;
  border: none;
  background: transparent;
  border-radius: 8px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  color: #666;
}

.auth-tab.active {
  background: white;
  color: #1d1d1f;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.auth-form {
  animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.form-title {
  font-size: 24px;
  font-weight: 600;
  color: #1d1d1f;
  margin-bottom: 8px;
}

.form-subtitle {
  color: #666;
  margin-bottom: 30px;
  font-size: 14px;
}

.form-row {
  display: flex;
  gap: 16px;
}

.form-group {
  flex: 1;
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  font-weight: 500;
  color: #1d1d1f;
  margin-bottom: 8px;
  font-size: 14px;
}

.form-group input,
.form-group select {
  width: 100%;
  padding: 14px 16px;
  border: 2px solid #f0f0f0;
  border-radius: 12px;
  font-size: 16px;
  transition: all 0.3s ease;
  box-sizing: border-box;
  background: white;
}

.form-group input:focus,
.form-group select:focus {
  outline: none;
  border-color: #0071e3;
  box-shadow: 0 0 0 4px rgba(0, 113, 227, 0.1);
}

.role-select {
  appearance: none;
  background-image: url('data:image/svg+xml;charset=US-ASCII,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 4 5"><path fill="%23666" d="M2 0L0 2h4zm0 5L0 3h4z"/></svg>');
  background-repeat: no-repeat;
  background-position: right 16px center;
  background-size: 12px;
  cursor: pointer;
}

.role-select option {
  padding: 10px;
  font-size: 16px;
}

.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.checkbox-container {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  font-size: 14px;
  color: #666;
}

.checkbox-container input {
  width: auto !important;
  margin: 0;
}

.forgot-password {
  color: #0071e3;
  text-decoration: none;
  font-size: 14px;
}

.forgot-password:hover {
  text-decoration: underline;
}

.auth-button {
  width: 100%;
  padding: 16px;
  border: none;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  margin-bottom: 16px;
}

.auth-button.primary {
  background: #0071e3;
  color: white;
}

.auth-button.primary:hover {
  background: #0056b3;
  transform: translateY(-1px);
}

.auth-button.social {
  background: white;
  color: #1d1d1f;
  border: 2px solid #f0f0f0;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
}

.auth-button.social:hover {
  border-color: #d0d0d0;
  transform: translateY(-1px);
}

.social-icon {
  font-size: 18px;
}

.auth-divider {
  text-align: center;
  margin: 24px 0;
  position: relative;
  color: #666;
  font-size: 14px;
}

.auth-divider::before {
  content: '';
  position: absolute;
  top: 50%;
  left: 0;
  right: 0;
  height: 1px;
  background: #f0f0f0;
}

.auth-divider span {
  background: white;
  padding: 0 16px;
  position: relative;
}

.checkbox-container a {
  color: #0071e3;
  text-decoration: none;
}

.checkbox-container a:hover {
  text-decoration: underline;
}

/* Error, Success, and Warning Messages */
.error-message,
.success-message,
.warning-message {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 12px 16px;
  border-radius: 12px;
  margin-bottom: 20px;
  font-size: 14px;
  font-weight: 500;
}

.error-message {
  background: #fef2f2;
  color: #dc2626;
  border: 1px solid #fecaca;
}

.success-message {
  background: #f0fdf4;
  color: #16a34a;
  border: 1px solid #bbf7d0;
}

.warning-message {
  background: #fef3c7;
  color: #d97706;
  border: 1px solid #fcd34d;
}

.warning-icon {
  font-size: 16px;
}

.error-close {
  margin-left: auto;
  background: none;
  border: none;
  color: #dc2626;
  cursor: pointer;
  font-size: 18px;
  padding: 0;
  width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.error-close:hover {
  background: rgba(220, 38, 38, 0.1);
  border-radius: 50%;
}

.error-icon,
.success-icon {
  font-size: 16px;
}

/* Enhanced Google Button */
.auth-button.social.google {
  background: #ffffff;
  color: #1d1d1f;
  border: 2px solid #dadce0;
  position: relative;
  overflow: hidden;
}

.auth-button.social.google:hover:not(:disabled) {
  border-color: #1a73e8;
  box-shadow: 0 2px 10px rgba(26, 115, 232, 0.15);
}

.auth-button.social.google:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  transform: none;
}

.auth-button.social.google .social-icon {
  font-size: 18px;
  background: linear-gradient(45deg, #4285f4, #34a853, #fbbc05, #ea4335);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

/* Not configured Google button */
.auth-button.social.google.not-configured {
  background: #f5f5f7;
  color: #86868b;
  border-color: #e5e5e7;
  cursor: not-allowed;
}

.auth-button.social.google.not-configured:hover {
  border-color: #e5e5e7;
  box-shadow: none;
  transform: none;
}

.auth-button.social.google.not-configured .social-icon {
  background: #86868b;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

/* Alternative Google button */
.auth-button.social.google-alt {
  background: #fff8e1;
  color: #f57c00;
  border: 2px solid #ffcc02;
}

.auth-button.social.google-alt:hover:not(:disabled) {
  border-color: #ff9800;
  box-shadow: 0 2px 10px rgba(255, 152, 0, 0.15);
}

.auth-button.social.google-alt .social-icon {
  color: #ff9800;
}

/* Loading States */
.auth-button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  transform: none;
}

.form-group input:disabled,
.form-group select:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  background-color: #f9f9f9;
}

/* Enhanced Divider */
.auth-divider {
  position: relative;
  text-align: center;
  margin: 20px 0;
  color: #86868b;
  font-size: 13px;
}

.auth-divider::before {
  content: '';
  position: absolute;
  top: 50%;
  left: 0;
  right: 0;
  height: 1px;
  background: linear-gradient(to right, transparent, #e5e5e7, transparent);
}

.auth-divider span {
  background: white;
  padding: 0 20px;
  position: relative;
  font-weight: 500;
}

/* Improved Form Focus States */
.form-group input:focus,
.form-group select:focus {
  border-color: #1a73e8;
  box-shadow: 0 0 0 4px rgba(26, 115, 232, 0.1);
}

/* Password Validation Styles */
.password-warning {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-top: 8px;
  padding: 10px 12px;
  background: #fff3cd;
  border: 1px solid #ffc107;
  border-radius: 8px;
  font-size: 13px;
  color: #856404;
  animation: slideDown 0.3s ease-out;
}

.password-warning .warning-icon {
  font-size: 16px;
}

.password-strength {
  margin-top: 10px;
  padding: 12px;
  border-radius: 8px;
  background: #f8f9fa;
  border: 1px solid #e9ecef;
}

.password-strength.weak {
  background: #fff3cd;
  border-color: #ffc107;
}

.password-strength.medium {
  background: #cfe2ff;
  border-color: #0d6efd;
}

.password-strength.strong {
  background: #d1e7dd;
  border-color: #198754;
}

.strength-bar {
  height: 6px;
  background: #e9ecef;
  border-radius: 3px;
  overflow: hidden;
  margin-bottom: 8px;
}

.strength-fill {
  height: 100%;
  border-radius: 3px;
  transition: all 0.3s ease;
}

.weak .strength-fill {
  background: #ffc107;
  width: 33%;
}

.medium .strength-fill {
  background: #0d6efd;
  width: 66%;
}

.strong .strength-fill {
  background: #198754;
  width: 100%;
}

.strength-text {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  font-weight: 600;
}

.weak .strength-text {
  color: #856404;
}

.medium .strength-text {
  color: #084298;
}

.strong .strength-text {
  color: #0f5132;
}

.strength-icon {
  font-size: 14px;
}

.password-requirements {
  margin-top: 8px;
  padding: 10px;
  background: #f8f9fa;
  border-radius: 8px;
  font-size: 13px;
}

.requirement {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 4px 0;
  color: #6c757d;
  transition: color 0.3s ease;
}

.requirement.met {
  color: #198754;
  font-weight: 600;
}

.requirement span:first-child {
  font-weight: bold;
  font-size: 16px;
}

.weak-password {
  border-color: #ffc107 !important;
  background: #fffbf0 !important;
}

.weak-password:focus {
  border-color: #ffc107 !important;
  box-shadow: 0 0 0 4px rgba(255, 193, 7, 0.15) !important;
}

@keyframes slideDown {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* Animation for form transitions */
@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateX(20px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

.auth-form {
  animation: slideIn 0.3s ease-out;
}

@media (max-width: 768px) {
  .auth-page {
    padding: 10px;
  }

  .auth-form-container {
    padding: 30px 20px;
  }

  .form-row {
    flex-direction: column;
    gap: 0;
  }

  .error-message,
  .success-message {
    padding: 10px 12px;
    font-size: 13px;
  }
}
</style>
