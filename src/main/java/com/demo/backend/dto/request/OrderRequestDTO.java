package com.demo.backend.dto.request;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderRequestDTO {

    private Long userId;

    private String status;    // PENDING, PROCESSING, ...
    private String priority;  // LOW, MEDIUM, HIGH

    private BigDecimal subtotal;
    private BigDecimal discountAmount;
    private BigDecimal taxAmount;
    private BigDecimal shippingAmount;
    private BigDecimal total;

    private Long billingAddressId;
    private Long shippingAddressId;

    private String paymentMethod;
    private String paymentStatus;
    private String transactionId;

    private String couponCode;

    private String notes;

    private List<OrderItemRequestDTO> items;
}
