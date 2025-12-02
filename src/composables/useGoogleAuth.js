import { ref, onMounted } from 'vue'
import { useAuthStore } from '@/stores/auth.js'

export function useGoogleAuth() {
  const authStore = useAuthStore()
  const isGoogleLoaded = ref(false)
  const isSigningIn = ref(false)
  const googleError = ref(null)

  // Google OAuth Configuration
  const GOOGLE_OAUTH_ENABLED = import.meta.env.VITE_GOOGLE_OAUTH_ENABLED === 'true'
  const GOOGLE_CLIENT_ID = import.meta.env.VITE_GOOGLE_CLIENT_ID

  // Check if Google OAuth is enabled and properly configured
  const isGoogleConfigured = () => {
    if (!GOOGLE_OAUTH_ENABLED) {
      return false
    }

    return (
      GOOGLE_CLIENT_ID &&
      GOOGLE_CLIENT_ID !== 'your-google-client-id.googleusercontent.com' &&
      GOOGLE_CLIENT_ID !== 'YOUR_REAL_CLIENT_ID_HERE.apps.googleusercontent.com' &&
      GOOGLE_CLIENT_ID !==
        '1087013467001-9dq8p7vqkd0l3j5v0s2l7p6r7h5k8m2n.apps.googleusercontent.com' &&
      GOOGLE_CLIENT_ID.includes('.googleusercontent.com') &&
      GOOGLE_CLIENT_ID.length > 50 // Real client IDs are much longer
    )
  }

  // Initialize Google Sign-In
  const initializeGoogleSignIn = () => {
    return new Promise((resolve) => {
      // Check if Google OAuth is enabled
      if (!GOOGLE_OAUTH_ENABLED) {
        console.log(
          'Google OAuth is disabled. Set VITE_GOOGLE_OAUTH_ENABLED=true in .env to enable.',
        )
        resolve(false)
        return
      }

      // Check if Google OAuth is properly configured
      if (!isGoogleConfigured()) {
        const errorMsg =
          'Google OAuth Error: Please configure your Google Client ID in the .env file. Follow the setup guide in document/GOOGLE_OAUTH_SETUP.md'
        console.warn(errorMsg)
        googleError.value = errorMsg
        resolve(false)
        return
      }

      console.log('Initializing Google Sign-In with Client ID:', GOOGLE_CLIENT_ID)

      if (window.google?.accounts?.id) {
        try {
          window.google.accounts.id.initialize({
            client_id: GOOGLE_CLIENT_ID,
            callback: handleGoogleResponse,
            auto_select: false,
            cancel_on_tap_outside: true,
            use_fedcm_for_prompt: false,
          })
          isGoogleLoaded.value = true
          googleError.value = null
          console.log('Google Sign-In initialized successfully')
          resolve(true)
        } catch (error) {
          console.error('Error initializing Google Sign-In:', error)
          googleError.value = 'Failed to initialize Google Sign-In'
          resolve(false)
        }
      } else {
        // Load Google Sign-In SDK
        const script = document.createElement('script')
        script.src = 'https://accounts.google.com/gsi/client'
        script.async = true
        script.defer = true

        script.onload = () => {
          console.log('Google SDK loaded, initializing...')
          setTimeout(() => {
            try {
              if (window.google?.accounts?.id) {
                window.google.accounts.id.initialize({
                  client_id: GOOGLE_CLIENT_ID,
                  callback: handleGoogleResponse,
                  auto_select: false,
                  cancel_on_tap_outside: true,
                  use_fedcm_for_prompt: false,
                })
                isGoogleLoaded.value = true
                googleError.value = null
                console.log('Google Sign-In initialized successfully')
                resolve(true)
              } else {
                throw new Error('Google accounts API not available')
              }
            } catch (error) {
              console.error('Error initializing Google Sign-In after SDK load:', error)
              googleError.value = 'Failed to initialize Google Sign-In'
              resolve(false)
            }
          }, 500)
        }

        script.onerror = (error) => {
          console.error('Failed to load Google Sign-In SDK:', error)
          googleError.value = 'Failed to load Google Sign-In SDK'
          resolve(false)
        }

        document.head.appendChild(script)
      }
    })
  }

  // Handle Google Sign-In Response
  const handleGoogleResponse = async (response) => {
    try {
      isSigningIn.value = true

      // Decode the JWT token
      const payload = JSON.parse(atob(response.credential.split('.')[1]))

      const userData = {
        id: payload.sub,
        name: payload.name,
        email: payload.email,
        picture: payload.picture,
        given_name: payload.given_name,
        family_name: payload.family_name,
        provider: 'google',
        isLoggedIn: true,
        role: 'customer', // Default role for Google sign-in
      }

      // Store user data using auth store
      authStore.login(userData)

      console.log('Google Sign-In successful:', userData)
    } catch (error) {
      console.error('Google Sign-In error:', error)
      throw error
    } finally {
      isSigningIn.value = false
    }
  }

  // Sign in with Google
  const signInWithGoogle = async () => {
    try {
      console.log('Attempting Google Sign-In...')

      if (!isGoogleConfigured()) {
        throw new Error('Google OAuth is not configured. Please check your environment variables.')
      }

      if (!isGoogleLoaded.value) {
        console.log('Google SDK not loaded, initializing...')
        const initialized = await initializeGoogleSignIn()
        if (!initialized) {
          throw new Error('Failed to initialize Google Sign-In')
        }
      }

      if (window.google?.accounts?.id) {
        console.log('Showing Google Sign-In prompt...')
        window.google.accounts.id.prompt((notification) => {
          console.log('Google prompt notification:', notification)
          if (notification.isNotDisplayed()) {
            console.warn(
              'Google Sign-In prompt not displayed:',
              notification.getNotDisplayedReason(),
            )
            // Try alternative method
            triggerGoogleSignIn()
          } else if (notification.isSkippedMoment()) {
            console.warn('Google Sign-In prompt skipped:', notification.getSkippedReason())
          }
        })
      } else {
        throw new Error('Google Sign-In not available')
      }
    } catch (error) {
      console.error('Google Sign-In error:', error)
      googleError.value = error.message
      throw error
    }
  }

  // Alternative Google Sign-In trigger
  const triggerGoogleSignIn = () => {
    try {
      if (window.google?.accounts?.id) {
        // Create a temporary button and click it
        const tempDiv = document.createElement('div')
        tempDiv.style.position = 'fixed'
        tempDiv.style.top = '-1000px'
        tempDiv.style.left = '-1000px'
        document.body.appendChild(tempDiv)

        window.google.accounts.id.renderButton(tempDiv, {
          theme: 'outline',
          size: 'large',
          width: 250,
        })

        // Trigger click programmatically
        setTimeout(() => {
          const button = tempDiv.querySelector('div[role="button"]')
          if (button) {
            button.click()
          }
          document.body.removeChild(tempDiv)
        }, 100)
      }
    } catch (error) {
      console.error('Error in triggerGoogleSignIn:', error)
    }
  }

  // Sign in with Google using popup (alternative method)
  const signInWithGooglePopup = async () => {
    return signInWithGoogle()
  } // Sign out
  const signOut = () => {
    if (window.google) {
      window.google.accounts.id.disableAutoSelect()
    }
    authStore.logout()
  }

  onMounted(async () => {
    await initializeGoogleSignIn()
  })

  return {
    isGoogleLoaded,
    isSigningIn,
    googleError,
    isGoogleEnabled: GOOGLE_OAUTH_ENABLED,
    isGoogleConfigured: isGoogleConfigured(),
    signInWithGoogle,
    signInWithGooglePopup,
    triggerGoogleSignIn,
    signOut,
    initializeGoogleSignIn,
  }
}
