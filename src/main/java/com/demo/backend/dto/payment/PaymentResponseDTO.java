package com.demo.backend.dto.payment;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentResponseDTO {

    private boolean success;
    private String paymentUrl;        // ABA checkout URL (deeplink)
    private String transactionId;     // Transaction reference
    private String message;           // Status message
}
