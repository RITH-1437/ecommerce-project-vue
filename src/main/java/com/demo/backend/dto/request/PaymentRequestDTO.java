package com.demo.backend.dto.request;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PaymentRequestDTO {
    private String orderId;
    private BigDecimal amount;
    private String currency = "USD"; // or "KHR" for Riel
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
}
