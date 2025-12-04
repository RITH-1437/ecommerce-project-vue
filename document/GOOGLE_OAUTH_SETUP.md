# Google OAuth Integration Setup Guide

This guide will help you set up Google OAuth authentication for the Apple Store Vue.js application.

## Prerequisites

1. A Google Cloud Platform (GCP) account
2. A project in Google Cloud Console

## Step 1: Create Google OAuth Credentials

1. Go to the [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project or select an existing one
3. Navigate to **APIs & Services** > **Credentials**
4. Click **+ CREATE CREDENTIALS** > **OAuth client ID**
5. If prompted, configure the OAuth consent screen first:
   - Choose **External** user type
   - Fill in the required application information
   - Add your email to test users during development

## Step 2: Configure OAuth Client ID

1. Choose **Web application** as the application type
2. Give it a name (e.g., "Apple Store Web App")
3. Add authorized origins:
   - `http://localhost:5173` (for development)
   - `https://yourdomain.com` (for production)
4. Add authorized redirect URIs:
   - `http://localhost:5173` (for development)
   - `https://yourdomain.com` (for production)
5. Click **Create**
6. Copy the **Client ID** (it will look like: `xxxxx.apps.googleusercontent.com`)

## Step 3: Configure Environment Variables

1. Copy `.env.example` to `.env`:

   ```bash
   cp .env.example .env
   ```

2. Update the `.env` file with your Google Client ID:
   ```env
   VITE_GOOGLE_CLIENT_ID=your-actual-client-id.apps.googleusercontent.com
   VITE_APP_NAME=Apple Store
   VITE_APP_URL=http://localhost:5173
   ```

## Step 4: Test the Integration

1. Start the development server:

   ```bash
   npm run dev
   ```

2. Navigate to the authentication page (`/auth`)
3. Click "Continue with Google"
4. You should see the Google Sign-In popup
5. After successful authentication, you'll be redirected to the home page

## Features

### Authentication Store (`src/stores/auth.js`)

- Centralized user state management using Pinia
- Persistent authentication state (localStorage)
- Support for both email and Google authentication
- Role-based access control (customer/admin)

### Google Auth Composable (`src/composables/useGoogleAuth.js`)

- Google Sign-In SDK integration
- JWT token handling
- Error handling and loading states
- Automatic profile data extraction

### Enhanced UI Features

- Google profile picture display in header
- Provider indication (Google Account badge)
- Loading states during authentication
- Error handling with user-friendly messages
- Responsive design for all screen sizes

## Security Notes

1. **Client-Side Only**: This implementation is client-side only. For production apps, consider server-side verification of Google tokens.

2. **Environment Variables**: Never commit your actual Google Client ID to version control. Use environment variables.

3. **HTTPS Required**: Google OAuth requires HTTPS in production. The localhost exception is only for development.

4. **Scope Limitations**: This implementation only requests basic profile information. Adjust scopes as needed for your application.

## Troubleshooting

### Common Issues

1. **"Invalid Client ID"**:
   - Verify your Client ID is correct in `.env`
   - Ensure the domain is added to authorized origins

2. **"Redirect URI Mismatch"**:
   - Check that your redirect URIs match exactly in Google Console
   - Ensure you're using the correct port number

3. **Pop-up Blocked**:
   - Some browsers block popups by default
   - Users may need to allow popups for your site

4. **CORS Issues**:
   - Ensure your domain is added to authorized origins
   - Check that you're not mixing HTTP and HTTPS

### Development Tips

- Use Chrome DevTools to inspect network requests and console errors
- The Google Sign-In library provides detailed error messages in the console
- Test with different Google accounts to ensure broad compatibility

## Production Deployment

1. Update authorized origins and redirect URIs in Google Console
2. Update the `VITE_APP_URL` in your production environment variables
3. Ensure your production domain uses HTTPS
4. Test the authentication flow thoroughly before going live

## Additional Resources

- [Google Identity Documentation](https://developers.google.com/identity)
- [Google Sign-In for Websites](https://developers.google.com/identity/sign-in/web)
- [Vue 3 Composition API Guide](https://vuejs.org/guide/extras/composition-api-faq.html)
- [Pinia Store Documentation](https://pinia.vuejs.org/)
