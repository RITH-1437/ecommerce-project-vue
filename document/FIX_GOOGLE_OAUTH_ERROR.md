# 🔧 Fix Google OAuth "Error 401: invalid_client"

## The Problem

You're seeing "**Access blocked: Authorization Error**" with "**Error 401: invalid_client**" because the Google Client ID in your `.env` file is not a real, valid OAuth client.

## ✅ Complete Solution (Follow These Exact Steps)

### Step 1: Create Google Cloud Project (2 minutes)

1. **Go to Google Cloud Console**: https://console.cloud.google.com/
2. **Click "Select a project"** at the top
3. **Click "NEW PROJECT"**
4. **Enter project name**: `Apple Store Auth` (or any name you prefer)
5. **Click "CREATE"**
6. **Wait for creation** and make sure your new project is selected

### Step 2: Enable Google Sign-In API (1 minute)

1. **In your Google Cloud Console**, go to **"APIs & Services" > "Library"**
2. **Search for**: `Google Sign-In API` or `Identity and Access Management (IAM) API`
3. **Click on it** and **click "ENABLE"**
4. **Wait for it to enable** (green checkmark will appear)

### Step 3: Configure OAuth Consent Screen (3 minutes)

1. **Go to**: **"APIs & Services" > "OAuth consent screen"**
2. **Choose "External"** (unless you have a Google Workspace account)
3. **Click "CREATE"**
4. **Fill in required fields**:
   - **App name**: `Apple Store`
   - **User support email**: Your email
   - **Developer contact information**: Your email
5. **Click "SAVE AND CONTINUE"**
6. **Skip "Scopes"** - Click "SAVE AND CONTINUE"
7. **Add test users** - Add your Gmail address that you want to test with
8. **Click "SAVE AND CONTINUE"**

### Step 4: Create OAuth Client ID (2 minutes)

1. **Go to**: **"APIs & Services" > "Credentials"**
2. **Click "+ CREATE CREDENTIALS"**
3. **Select "OAuth client ID"**
4. **Choose "Web application"**
5. **Name**: `Apple Store Web Client`
6. **Add Authorized JavaScript origins**:
   ```
   http://localhost:5173
   ```
7. **Add Authorized redirect URIs**:
   ```
   http://localhost:5173
   http://localhost:5173/auth
   ```
8. **Click "CREATE"**
9. **COPY THE CLIENT ID** (it looks like: `123456789-abcdefghijklmnop.apps.googleusercontent.com`)

### Step 5: Update Your .env File (30 seconds)

1. **Open your `.env` file**
2. **Replace this line**:
   ```env
   VITE_GOOGLE_CLIENT_ID=YOUR_REAL_CLIENT_ID_HERE.apps.googleusercontent.com
   ```
   **With your actual Client ID**:
   ```env
   VITE_GOOGLE_CLIENT_ID=123456789-abcdefghijklmnop.apps.googleusercontent.com
   ```

### Step 6: Restart and Test (1 minute)

1. **Stop your development server** (Ctrl+C)
2. **Restart it**:
   ```bash
   npm run dev
   ```
3. **Go to**: http://localhost:5173/auth
4. **Click "Continue with Google"**
5. **It should now work!** ✅

## 🚨 Common Issues and Solutions

### Issue: Still getting "invalid_client"

**Solution**:

- Double-check your Client ID is copied correctly (no extra spaces)
- Make sure you restarted the development server
- Verify the Client ID ends with `.apps.googleusercontent.com`

### Issue: "Redirect URI mismatch"

**Solution**:

- In Google Console, add both `http://localhost:5173` and `http://localhost:5173/auth` to authorized redirect URIs
- Make sure there are NO trailing slashes

### Issue: "This app isn't verified"

**Solution**:

- This is normal for development
- Click "Advanced" then "Go to Apple Store (unsafe)"
- Or add your email as a test user in OAuth consent screen

### Issue: Pop-up blocked

**Solution**:

- Allow pop-ups for localhost in your browser
- Check browser settings for pop-up blocker

## 🎯 What Should Happen After Fixing

1. **Click "Continue with Google"** → Google sign-in popup opens (no error)
2. **Select your Google account** → Popup closes automatically
3. **Redirected to home page** → Your profile picture appears in the header
4. **Click profile dropdown** → Shows "Google Account" badge with your info

## 📋 Quick Checklist

- [ ] Google Cloud project created
- [ ] Google Sign-In API enabled
- [ ] OAuth consent screen configured with your email as test user
- [ ] OAuth client ID created with correct origins/redirects
- [ ] Client ID copied to .env file (no typos)
- [ ] Development server restarted
- [ ] Tested at http://localhost:5173/auth

## ⚡ Still Need Help?

If you're still having issues:

1. **Check the browser console** (F12) for error messages
2. **Verify your .env file** has the correct Client ID
3. **Make sure you're using the email** you added as a test user
4. **Try in incognito/private browser window**

---

**Expected time to complete**: About 8-10 minutes total

The key is that you MUST use your own real Google OAuth client - demo/placeholder IDs will always fail with "Error 401: invalid_client".
