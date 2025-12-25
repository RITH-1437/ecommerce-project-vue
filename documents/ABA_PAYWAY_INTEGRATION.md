# ABA PayWay Integration Guide

## ✅ Integration Status: **COMPLETE**

Your Spring Boot application is now fully configured to integrate with ABA PayWay payment gateway.

---

## 📋 What Has Been Configured

### 1. **Configuration Files**

- ✅ `application.properties` - Contains all ABA PayWay credentials and URLs
- ✅ `PaywayConfig.java` - Spring configuration class to load properties

### 2. **Core Components**

- ✅ `PaymentService.java` - Service interface
- ✅ `PaymentServiceImpl.java` - Implementation with ABA PayWay API integration
- ✅ `PaymentController.java` - REST endpoints for payment operations
- ✅ `PaymentRequestDTO.java` - Request data transfer object
- ✅ `PaymentResponseDTO.java` - Response data transfer object
- ✅ `RestTemplate` bean - HTTP client for API calls

### 3. **Security Features**

- ✅ HMAC-SHA512 hash signature generation
- ✅ Request signature verification
- ✅ Secure credential management

---

## 🚀 How to Use ABA PayWay Integration

### **Step 1: Update Configuration**

Edit `src/main/resources/application.properties`:

```properties
# ABA PayWay Configuration
payway.merchantId=YOUR_MERCHANT_ID          # Replace with your merchant ID
payway.apiKey=YOUR_API_KEY                  # Replace with your API key
payway.secretKey=YOUR_SECRET_KEY            # Replace with your secret key
payway.baseUrl=https://sandbox.payway.com.kh/api/payment-gateway  # Sandbox URL
payway.callbackUrl=https://your-backend.com/api/payments/aba/callback  # Your callback URL
payway.returnUrl=https://your-frontend.com/payment-success  # Your return URL
```

**Important Notes:**

- Use **sandbox URL** for testing: `https://sandbox.payway.com.kh/api/payment-gateway`
- Use **production URL** for live: `https://checkout.payway.com.kh/api/payment-gateway`
- Update `callbackUrl` to your actual backend domain
- Update `returnUrl` to your actual frontend payment success page

---

### **Step 2: Create Payment Request**

**Endpoint:** `POST /api/payments/create`

**Request Body:**

```json
{
  "orderId": "ORD-12345",
  "amount": 99.99,
  "currency": "USD",
  "firstName": "Sok",
  "lastName": "Dara",
  "phone": "012345678",
  "email": "sokdara@example.com"
}
```

**Response:**

```json
{
  "transactionId": "ORD-12345_1733270400000",
  "paymentUrl": "https://checkout.payway.com.kh/payments/...",
  "status": "PENDING"
}
```

**Java Example:**

```java
@Autowired
private PaymentService paymentService;

public void processCheckout(Order order, User user) {
    PaymentRequestDTO request = new PaymentRequestDTO();
    request.setOrderId(order.getOrderNumber());
    request.setAmount(order.getTotalAmount());
    request.setCurrency("USD");  // or "KHR" for Riel
    request.setFirstName(user.getName().split(" ")[0]);
    request.setLastName(user.getName().split(" ")[1]);
    request.setPhone(user.getPhone());
    request.setEmail(user.getEmail());

    PaymentResponseDTO response = paymentService.createPayment(request);

    // Redirect user to response.getPaymentUrl()
    return "redirect:" + response.getPaymentUrl();
}
```

---

### **Step 3: Handle Payment Callback**

ABA PayWay will send payment results to your callback URL.

**Endpoint:** `POST /api/payments/aba/callback`

**Parameters:**

- `tran_id` - Transaction ID
- `status` - Payment status (SUCCESS, FAILED, CANCELLED)
- `hash` - Signature for verification

**Update Your Service:**

```java
@Override
public void handleCallback(String transactionId, String status) {
    log.info("Payment callback: {} - {}", transactionId, status);

    // Extract order ID from transaction ID
    String orderId = transactionId.split("_")[0];

    // Update order status based on payment result
    if ("SUCCESS".equalsIgnoreCase(status)) {
        orderService.updateOrderStatus(orderId, OrderStatus.PAID);
        // Send confirmation email
        emailService.sendOrderConfirmation(orderId);
    } else if ("FAILED".equalsIgnoreCase(status)) {
        orderService.updateOrderStatus(orderId, OrderStatus.PAYMENT_FAILED);
    } else if ("CANCELLED".equalsIgnoreCase(status)) {
        orderService.updateOrderStatus(orderId, OrderStatus.CANCELLED);
    }
}
```

---

### **Step 4: Check Payment Status**

**Endpoint:** `GET /api/payments/status/{transactionId}`

**Response:** Returns payment status as string (SUCCESS, PENDING, FAILED, etc.)

**Usage:**

```bash
curl http://localhost:1437/api/payments/status/ORD-12345_1733270400000
```

---

## 🔄 Payment Flow Diagram

```
1. Customer → Checkout → Your Backend
                ↓
2. Your Backend → Create Payment → ABA PayWay API
                ↓
3. ABA PayWay API → Returns Payment URL
                ↓
4. Your Backend → Redirect Customer → ABA Payment Page
                ↓
5. Customer → Enters Payment Details → ABA Payment Page
                ↓
6. ABA → Processes Payment → Sends Callback → Your Backend
                ↓
7. Your Backend → Updates Order Status → Database
                ↓
8. ABA → Redirects Customer → Your Return URL
                ↓
9. Your Frontend → Shows Success/Failure Message
```

---

## 🛠️ Testing Guide

### **Test with Postman/cURL**

```bash
# Create Payment
curl -X POST http://localhost:1437/api/payments/create \
  -H "Content-Type: application/json" \
  -d '{
    "orderId": "TEST-001",
    "amount": 10.00,
    "currency": "USD",
    "firstName": "Test",
    "lastName": "User",
    "phone": "012345678",
    "email": "test@example.com"
  }'

# Check Status
curl http://localhost:1437/api/payments/status/TEST-001_1733270400000
```

### **Test Payment Flow**

1. Start your backend: `mvnw spring-boot:run`
2. Send POST request to `/api/payments/create`
3. Copy the `paymentUrl` from response
4. Open URL in browser to see ABA payment page
5. Use ABA test cards for sandbox testing
6. Check callback logs in console

---

## 🔐 Security Best Practices

### **1. Environment Variables**

Never commit sensitive keys to Git. Use environment variables:

```bash
# Windows (CMD)
set PAYWAY_MERCHANT_ID=your_merchant_id
set PAYWAY_API_KEY=your_api_key
set PAYWAY_SECRET_KEY=your_secret_key

# Linux/Mac
export PAYWAY_MERCHANT_ID=your_merchant_id
export PAYWAY_API_KEY=your_api_key
export PAYWAY_SECRET_KEY=your_secret_key
```

Update `application.properties`:

```properties
payway.merchantId=${PAYWAY_MERCHANT_ID}
payway.apiKey=${PAYWAY_API_KEY}
payway.secretKey=${PAYWAY_SECRET_KEY}
```

### **2. Hash Verification**

Always verify the hash signature in callbacks:

```java
private boolean verifyHash(String receivedHash, Map<String, Object> data) {
    String calculatedHash = generateHash(data);
    return calculatedHash.equals(receivedHash);
}
```

### **3. HTTPS Only**

Use HTTPS in production for all callback and return URLs.

---

## 💡 Currency Support

- **USD** - US Dollar (recommended for international)
- **KHR** - Cambodian Riel (local currency)

Set in request:

```java
request.setCurrency("USD");  // or "KHR"
```

---

## 📝 Transaction ID Format

Format: `{orderId}_{timestamp}`

Example: `ORD-12345_1733270400000`

This ensures:

- ✅ Unique transaction IDs
- ✅ Easy order lookup
- ✅ Traceability

---

## 🚨 Common Issues & Solutions

### **Issue 1: Payment URL Not Generated**

- Check if `baseUrl` is correct (sandbox vs production)
- Verify `apiKey` and `merchantId` are valid
- Check logs for API errors

### **Issue 2: Callback Not Received**

- Ensure `callbackUrl` is publicly accessible (use ngrok for local testing)
- Check firewall/security group settings
- Verify endpoint `/api/payments/aba/callback` is not protected by authentication

### **Issue 3: Hash Mismatch**

- Verify `secretKey` matches ABA dashboard
- Check hash generation algorithm (HMAC-SHA512)
- Ensure data order matches ABA specification

### **Issue 4: Payment Stuck in PENDING**

- Use `/api/payments/status/{transactionId}` to check
- Contact ABA support if issue persists
- Check ABA dashboard for transaction details

---

## 📞 ABA PayWay Support

- **Website:** https://www.payway.com.kh
- **Documentation:** https://docs.payway.com.kh
- **Email:** support@payway.com.kh
- **Phone:** +855 23 225 333

---

## 🎯 Next Steps

1. **Test Integration:**

   - Use sandbox credentials
   - Test with different amounts and currencies
   - Verify callback handling

2. **Implement Order Updates:**

   - Connect `handleCallback()` to your Order service
   - Update order status in database
   - Send customer notifications

3. **Add Error Handling:**

   - Handle network failures
   - Implement retry logic
   - Log all transactions

4. **Go Live:**

   - Switch to production URL
   - Update credentials in environment variables
   - Test with real transactions
   - Monitor logs closely

5. **Enhance Security:**
   - Implement IP whitelisting for callbacks
   - Add request rate limiting
   - Enable transaction logging
   - Set up monitoring alerts

---

## ✅ Integration Checklist

- [x] PaywayConfig.java created
- [x] PaymentService interface created
- [x] PaymentServiceImpl implemented
- [x] PaymentController endpoints created
- [x] DTOs (PaymentRequestDTO, PaymentResponseDTO) created
- [x] RestTemplate bean configured
- [x] Hash signature generation implemented
- [x] Callback endpoint configured
- [x] Application properties configured
- [ ] Update credentials with real values
- [ ] Test payment flow end-to-end
- [ ] Connect to Order service
- [ ] Implement email notifications
- [ ] Deploy to production
- [ ] Monitor transactions

---

## 📌 Quick Reference

**Create Payment:** `POST /api/payments/create`  
**Check Status:** `GET /api/payments/status/{transactionId}`  
**Callback:** `POST /api/payments/aba/callback`  
**Return URL:** `GET /api/payments/return`

**Server Port:** `1437`  
**Base URL:** `http://localhost:1437`

---

**Last Updated:** December 4, 2025  
**Status:** ✅ Ready for Testing
