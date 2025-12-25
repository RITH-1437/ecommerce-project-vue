package com.demo.backend.controller;

import com.demo.backend.dto.payment.PaymentRequestDTO;
import com.demo.backend.dto.payment.PaymentResponseDTO;
import com.demo.backend.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * Create payment request
     */
    @PostMapping("/create")
    public ResponseEntity<PaymentResponseDTO> createPayment(@RequestBody PaymentRequestDTO request) {
        PaymentResponseDTO response = paymentService.createPayment(request);
        return ResponseEntity.ok(response);
    }

    /**
     * ABA PayWay callback endpoint
     */
    @PostMapping("/aba/callback")
    public ResponseEntity<String> handleCallback(
            @RequestParam String tran_id,
            @RequestParam String status,
            @RequestParam(required = false) String hash) {

        paymentService.handleCallback(tran_id, status);
        return ResponseEntity.ok("OK");
    }

    /**
     * Check payment status
     */
    @GetMapping("/status/{transactionId}")
    public ResponseEntity<String> checkStatus(@PathVariable String transactionId) {
        String status = paymentService.verifyPayment(transactionId);
        return ResponseEntity.ok(status);
    }

    /**
     * Return URL from ABA (user redirected here after payment)
     */
    @GetMapping("/return")
    public ResponseEntity<String> paymentReturn(
            @RequestParam String tran_id,
            @RequestParam String status) {

        return ResponseEntity.ok("Payment " + status + " for transaction " + tran_id);
    }
}
