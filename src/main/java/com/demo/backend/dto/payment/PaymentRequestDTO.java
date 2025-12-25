package com.demo.backend.dto.payment;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequestDTO {

    private Long orderId; // order to pay
    private BigDecimal amount; // total price
    private String currency; // USD or KHR
}
