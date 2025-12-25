package com.demo.backend.service;

import com.demo.backend.dto.payment.PaymentRequestDTO;
import com.demo.backend.dto.payment.PaymentResponseDTO;

public interface PaymentService {

    PaymentResponseDTO createPayment(PaymentRequestDTO req);

    void handleCallback(String tranId, String status);

    String verifyPayment(String transactionId);
}
