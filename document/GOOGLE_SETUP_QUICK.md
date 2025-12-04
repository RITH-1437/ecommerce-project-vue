# Quick Google OAuth Setup Guide

## 🚀 Quick Start (5 minutes)

### Step 1: Get Your Google OAuth Client ID

1. **Go to Google Cloud Console**: https://console.cloud.google.com/
2. **Create a new project** or select existing one
3. **Enable Google Sign-In API**:
   - Go to "APIs & Services" > "Library"
   - Search for "Google Sign-In API" and enable it
4. **Create OAuth Credentials**:
   - Go to "APIs & Services" > "Credentials"
   - Click "+ CREATE CREDENTIALS" > "OAuth client ID"
   - Choose "Web application"
   - Add authorized origins: `http://localhost:5173`
   - Add authorized redirect URIs: `http://localhost:5173`
   - Copy the Client ID (looks like: `123456789-abcdef.apps.googleusercontent.com`)

### Step 2: Update Your .env File

Replace the placeholder in your `.env` file:

```env
# Replace this line:
VITE_GOOGLE_CLIENT_ID=1087013467001-9dq8p7vqkd0l3j5v0s2l7p6r7h5k8m2n.apps.googleusercontent.com

# With your actual Client ID:
VITE_GOOGLE_CLIENT_ID=YOUR_ACTUAL_CLIENT_ID.apps.googleusercontent.com
```

### Step 3: Test the Integration

1. **Restart your development server**:

   ```bash
   npm run dev
   ```

2. **Open your browser**: http://localhost:5173/auth

3. **Try Google Sign-In**: Click "Continue with Google" button

## 🔧 Troubleshooting

### Common Issues and Solutions

#### 1. "Google OAuth (Not Configured)" Button

**Issue**: Button shows as not configured
**Solution**: Make sure your .env file has the correct VITE_GOOGLE_CLIENT_ID

#### 2. "Invalid Client ID" Error

**Issue**: Google shows invalid client ID
**Solution**:

- Verify your Client ID is correct in .env
- Make sure it ends with .googleusercontent.com
- Restart your development server after changing .env

#### 3. "Redirect URI Mismatch" Error

**Issue**: Google shows redirect URI mismatch
**Solution**:

- In Google Console, add `http://localhost:5173` to authorized origins
- Add `http://localhost:5173` to authorized redirect URIs
- Make sure there are no trailing slashes

#### 4. Pop-up Blocked

**Issue**: Browser blocks Google sign-in popup
**Solution**:

- Allow popups for localhost in browser settings
- Try the "Try Google Sign-In Again" button if it appears

#### 5. "Failed to load Google Sign-In SDK"

**Issue**: Network error loading Google SDK
**Solution**:

- Check internet connection
- Try disabling browser extensions temporarily
- Clear browser cache and cookies

### Testing Without Real Google Account

The current setup includes a demo Client ID that provides limited functionality for testing the UI. For full functionality, you need your own Google OAuth credentials.

## 📋 Verification Checklist

- [ ] Google Cloud project created
- [ ] OAuth consent screen configured
- [ ] OAuth client ID created
- [ ] Authorized origins added (`http://localhost:5173`)
- [ ] Client ID copied to .env file
- [ ] Development server restarted
- [ ] No console errors when clicking Google button
- [ ] Google sign-in popup appears
- [ ] Successfully redirected after authentication

## 🎯 Expected Behavior

**With Proper Setup**:

1. Click "Continue with Google" → Google popup opens
2. Select Google account → Popup closes
3. Redirected to home page with profile picture in header
4. User dropdown shows Google account information

**Without Setup**:

1. "Google OAuth (Not Configured)" button appears
2. Warning message shows configuration needed
3. Email/password authentication still works normally

## 🔗 Helpful Links

- [Google Cloud Console](https://console.cloud.google.com/)
- [Google Sign-In Documentation](https://developers.google.com/identity/sign-in/web)
- [OAuth 2.0 Setup Guide](https://developers.google.com/identity/protocols/oauth2)

---

**Need Help?** Check the browser console (F12) for error messages that can help diagnose the issue.
