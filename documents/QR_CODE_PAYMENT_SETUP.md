# ABA PayWay QR Code Payment - Configuration Guide

## ✅ Status: READY FOR QR CODE INTEGRATION

---

## 🔧 Configuration Issues Fixed

### **Problem Identified:**

Your `payway.apiKey` contained an **RSA Private Key** (multiline text starting with `MIICXQIBAAKBgQC...`), not an API key.

### **What Was Fixed:**

1. ✅ Corrected API endpoint to v1 purchase API
2. ✅ Fixed hash generation algorithm for ABA PayWay v1
3. ✅ Added QR code payment support with `payment_option`
4. ✅ Updated payload format to match ABA requirements
5. ✅ Added proper items and shipping fields
6. ✅ Changed hash from Base64 to lowercase hex format

---

## 📝 Configuration Checklist

### **1. Get Your Real Credentials from ABA Dashboard**

Login to: https://merchant.payway.com.kh

You need these 3 values:

| Field           | Where to Find            | Current Value                       |
| --------------- | ------------------------ | ----------------------------------- |
| **Merchant ID** | Dashboard → Settings     | `1548101` ✅                        |
| **API Key**     | Dashboard → API Settings | ⚠️ **REPLACE WITH REAL VALUE**      |
| **Secret Key**  | Dashboard → API Settings | `Iloveyouforever@096` (verify this) |

### **2. Update application.properties**

```properties
# Merchant ID - Your unique merchant identifier
payway.merchantId=1548101

# API Key - Get from ABA Dashboard (NOT the RSA private key)
payway.apiKey=YOUR_ACTUAL_API_KEY_HERE

# Secret Key - For HMAC hash generation
payway.secretKey=YOUR_SECRET_KEY_HERE

# Base URL - Use sandbox for testing
payway.baseUrl=https://checkout-sandbox.payway.com.kh/api/payment-gateway/v1

# Callback URL - Where ABA sends payment results
payway.callbackUrl=http://localhost:1437/api/payments/aba/callback

# Return URL - Where user is redirected after payment
payway.returnUrl=http://localhost:3000/payment-success

# Payment Options - For QR code only
payway.paymentOption=abapay
```

### **Payment Option Values:**

- `abapay` - ABA Mobile App QR Code **← Use this for QR**
- `cards` - Credit/Debit card payment
- `abapay_deeplink` - ABA Mobile deeplink
- `abapay,cards` - Both QR and cards

---

## 🎯 How QR Code Payment Works

### **Flow Diagram:**

```
Customer Checkout
      ↓
Your Backend: POST /api/payments/create
      ↓
ABA PayWay API: Returns QR Code URL
      ↓
Show QR Code to Customer
      ↓
Customer Scans with ABA Mobile App
      ↓
Customer Confirms Payment in App
      ↓
ABA Sends Callback to Your Server
      ↓
Update Order Status
      ↓
Show Success Message
```

---

## 🧪 Testing QR Code Payment

### **Step 1: Create Payment Request**

```bash
curl -X POST http://localhost:1437/api/payments/create \
  -H "Content-Type: application/json" \
  -d '{
    "orderId": "ORD-TEST-001",
    "amount": 5.00,
    "currency": "USD",
    "firstName": "Sok",
    "lastName": "Dara",
    "phone": "012345678",
    "email": "test@example.com"
  }'
```

### **Expected Response:**

```json
{
  "transactionId": "ORD-TEST-001_1733270400000",
  "paymentUrl": "https://checkout-sandbox.payway.com.kh/qr/xxxxx",
  "status": "PENDING"
}
```

### **Step 2: Display QR Code**

**Option A: Redirect user to paymentUrl** (shows QR code page)

```javascript
window.location.href = response.paymentUrl;
```

**Option B: Generate QR code image in your frontend**

```javascript
// Use a QR code library like qrcode.js
import QRCode from "qrcode";

QRCode.toCanvas(document.getElementById("qr-canvas"), response.paymentUrl);
```

### **Step 3: Test with ABA Mobile App**

1. Download **ABA Mobile** app (sandbox version for testing)
2. Login with test account from ABA
3. Scan the QR code
4. Confirm payment
5. Check your backend callback logs

---

## 🔐 Hash Signature Format

**ABA PayWay v1 Hash String:**

```
req_time + merchant_id + tran_id + amount + items + shipping + firstname + lastname + email + phone + type
```

**Example:**

```
1733270400 + 1548101 + ORD-001_1733270400000 + 5.00 + [{"name":"ORD-001","quantity":"1","price":"5.00"}] + 0.00 + Sok + Dara + test@example.com + 012345678 + purchase
```

**Then:** HMAC-SHA512 with your secret key → Convert to lowercase hex

---

## 📱 Frontend Integration Example

### **React/Next.js:**

```javascript
async function handlePayment() {
  const response = await fetch("http://localhost:1437/api/payments/create", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      orderId: order.orderNumber,
      amount: order.totalAmount,
      currency: "USD",
      firstName: user.firstName,
      lastName: user.lastName,
      phone: user.phone,
      email: user.email,
    }),
  });

  const data = await response.json();

  // Option 1: Redirect to ABA's QR page
  window.location.href = data.paymentUrl;

  // Option 2: Show QR code in modal
  showQRModal(data.paymentUrl);
}

function showQRModal(url) {
  const qr = new QRCode(document.getElementById("qr-container"), {
    text: url,
    width: 256,
    height: 256,
  });
}
```

---

## 🔄 Handling Payment Callback

The callback endpoint is already configured: `POST /api/payments/aba/callback`

**What ABA Sends:**

```
tran_id: "ORD-TEST-001_1733270400000"
status: "success" | "failed" | "cancelled"
hash: "verification_signature"
amount: "5.00"
```

**Update Your Service:**

```java
@Override
public void handleCallback(String transactionId, String status) {
    log.info("Payment callback: {} - {}", transactionId, status);

    // Extract order ID
    String orderId = transactionId.split("_")[0];

    // Update order status
    if ("success".equalsIgnoreCase(status)) {
        orderRepository.updatePaymentStatus(orderId, "PAID");
        // Send email notification
        emailService.sendPaymentConfirmation(orderId);
    } else if ("failed".equalsIgnoreCase(status)) {
        orderRepository.updatePaymentStatus(orderId, "PAYMENT_FAILED");
    }
}
```

---

## 🚨 Common Issues & Solutions

### **Issue 1: "Invalid Hash"**

**Solution:**

- Verify your `secretKey` is correct
- Check hash generation order matches ABA docs
- Ensure amount format is `%.2f` (e.g., `5.00` not `5`)

### **Issue 2: "Invalid Merchant ID"**

**Solution:**

- Double-check merchant ID from ABA dashboard
- Ensure no extra spaces in configuration

### **Issue 3: QR Code Not Working**

**Solution:**

- Use sandbox ABA Mobile app for testing
- Check if `payment_option=abapay` is set
- Verify baseUrl is correct (sandbox vs production)

### **Issue 4: Callback Not Received**

**Solution:**

- Use **ngrok** to expose localhost: `ngrok http 1437`
- Update `callbackUrl` to ngrok URL
- Check firewall/antivirus blocking incoming requests

### **Issue 5: API Key Error**

**Solution:**

- The RSA private key you had is NOT the API key
- Contact ABA support to get your actual API key
- API key format: Usually alphanumeric string, not multiline

---

## 📞 Getting Your Real API Key

### **Steps:**

1. Login to https://merchant.payway.com.kh
2. Navigate to **Settings** → **API Configuration**
3. Look for **API Key** (NOT Private Key)
4. Copy and paste into `application.properties`

### **If You Can't Find It:**

- Contact ABA Support: **support@payway.com.kh**
- Phone: **+855 23 225 333**
- Provide your Merchant ID: `1548101`

---

## 🎯 Production Deployment Checklist

Before going live:

- [ ] Replace sandbox URL with production URL

  ```properties
  payway.baseUrl=https://checkout.payway.com.kh/api/payment-gateway/v1
  ```

- [ ] Update callback URL to your production domain

  ```properties
  payway.callbackUrl=https://yourdomain.com/api/payments/aba/callback
  ```

- [ ] Update return URL to production frontend

  ```properties
  payway.returnUrl=https://yourdomain.com/payment-success
  ```

- [ ] Use environment variables for secrets (not hardcoded)

  ```bash
  export PAYWAY_API_KEY=your_production_key
  export PAYWAY_SECRET_KEY=your_production_secret
  ```

- [ ] Enable HTTPS on your server

- [ ] Test with small amount first (e.g., $1)

- [ ] Monitor logs for first few transactions

- [ ] Set up error alerts

---

## ✅ Quick Test Command

```bash
# Start your backend
mvnw spring-boot:run

# In another terminal, test payment creation
curl -X POST http://localhost:1437/api/payments/create \
  -H "Content-Type: application/json" \
  -d '{
    "orderId": "TEST-QR-001",
    "amount": 1.00,
    "currency": "USD",
    "firstName": "Test",
    "lastName": "User",
    "phone": "012345678",
    "email": "test@example.com"
  }'
```

**Expected:** You should get a `paymentUrl` in the response. Open it in a browser to see the QR code.

---

## 📊 What's Different for QR vs Card Payment?

| Feature             | QR Code (`abapay`)        | Card Payment (`cards`)  |
| ------------------- | ------------------------- | ----------------------- |
| **Payment Method**  | Scan with ABA Mobile      | Enter card details      |
| **User Experience** | Fast, no typing           | Slower, manual entry    |
| **Security**        | ABA Mobile authentication | Card CVV required       |
| **Setup**           | No extra config           | May need PCI compliance |
| **Best For**        | Local customers with ABA  | International customers |

---

**Last Updated:** December 4, 2025  
**Status:** ✅ Ready for QR Code Testing

**Next Step:** Get your real API key from ABA Dashboard and replace `YOUR_ACTUAL_API_KEY_HERE` in `application.properties`
