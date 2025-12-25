package com.demo.backend.service.impl;

import com.demo.backend.dto.payment.PaymentRequestDTO;
import com.demo.backend.dto.payment.PaymentResponseDTO;
import com.demo.backend.model.Order;
import com.demo.backend.model.enums.OrderStatus;
import com.demo.backend.model.enums.PaymentStatus;
import com.demo.backend.repository.OrderRepository;
import com.demo.backend.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final OkHttpClient client = new OkHttpClient();
    private final OrderRepository orderRepository;

    // 🔐 ABA PayWay credentials from application.properties
    @Value("${payway.merchantId}")
    private String merchantId;

    @Value("${payway.apiKey}")
    private String apiKey;

    @Value("${payway.secretKey}")
    private String secretKey;

    @Value("${payway.baseUrl}")
    private String baseUrl;

    @Value("${payway.callbackUrl}")
    private String callbackUrl;

    @Value("${payway.returnUrl}")
    private String returnUrl;

    @Value("${payway.defaultCurrency:USD}")
    private String defaultCurrency;

    @Override
    public PaymentResponseDTO createPayment(PaymentRequestDTO req) {

        try {
            // Transaction ID (your system reference)
            String tranId = "TRX_" + System.currentTimeMillis();

            // Request body for ABA PayWay API
            JSONObject payload = new JSONObject()
                    .put("merchant_id", merchantId)
                    .put("order_id", tranId)
                    .put("amount", req.getAmount())
                    .put("currency", req.getCurrency() == null ? defaultCurrency : req.getCurrency())
                    .put("return_url", returnUrl)
                    .put("continue_success_url", returnUrl)
                    .put("callback_url", callbackUrl);

            // Generate signature (security requirement)
            String hash = generateHash(tranId, req.getAmount().doubleValue());

            payload.put("hash", hash);

            RequestBody body = RequestBody.create(
                    payload.toString(),
                    MediaType.parse("application/json"));

            Request request = new Request.Builder()
                    .url(baseUrl + "/checkout/create")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Api-Key", apiKey)
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();
            String raw = response.body().string();

            log.info("ABA Pay Response: {}", raw);

            JSONObject res = new JSONObject(raw);

            if (!res.has("data")) {
                return PaymentResponseDTO.builder()
                        .success(false)
                        .message("ABA PayWay error: " + raw)
                        .build();
            }

            String paymentUrl = res.getJSONObject("data").getString("payment_url");

            return PaymentResponseDTO.builder()
                    .success(true)
                    .paymentUrl(paymentUrl)
                    .transactionId(tranId)
                    .message("Payment URL generated successfully")
                    .build();

        } catch (Exception e) {
            log.error("Error creating ABA payment", e);
            return PaymentResponseDTO.builder()
                    .success(false)
                    .message("Error: " + e.getMessage())
                    .build();
        }
    }

    @Override
    @Transactional
    public void handleCallback(String tranId, String status) {
        log.info("ABA Callback <<< Transaction={} Status={}", tranId, status);

        try {
            Order order = orderRepository.findByTransactionId(tranId).orElse(null);
            if (order == null) {
                log.error("Order not found for transactionId: {}", tranId);
                return;
            }
            if ("success".equalsIgnoreCase(status)) {
                order.setPaymentStatus(PaymentStatus.COMPLETED);
                order.setStatus(OrderStatus.PROCESSING);
            } else {
                order.setPaymentStatus(PaymentStatus.FAILED);
                order.setStatus(OrderStatus.CANCELLED);
            }
            orderRepository.save(order);
            log.info("Updated order {} with payment status {}", order.getId(), status);
        } catch (Exception e) {
            log.error("Error updating order for transaction {}: {}", tranId, e.getMessage());
        }
    }

    @Override
    public String verifyPayment(String transactionId) {
        try {
            String url = baseUrl + "/transaction/detail?tran_id=" + transactionId;

            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Api-Key", apiKey)
                    .get()
                    .build();

            Response response = client.newCall(request).execute();
            return response.body().string();

        } catch (Exception e) {
            return "Error verifying payment: " + e.getMessage();
        }
    }

    // 🔐 SIGNATURE GENERATION (required by ABA)
    private String generateHash(String tranId, Double amount) {
        String data = merchantId + tranId + amount;
        return hmacSha512(secretKey, data);
    }

    private String hmacSha512(String key, String data) {
        try {
            Mac sha512 = Mac.getInstance("HmacSHA512");
            SecretKeySpec sk = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            sha512.init(sk);
            byte[] hashBytes = sha512.doFinal(data.getBytes(StandardCharsets.UTF_8));

            // Convert to hex
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes)
                sb.append(String.format("%02x", b));
            return sb.toString();

        } catch (Exception e) {
            throw new RuntimeException("Error generating HMAC: " + e.getMessage());
        }
    }
}
