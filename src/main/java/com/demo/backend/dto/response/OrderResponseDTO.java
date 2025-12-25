package com.demo.backend.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponseDTO {

    private Long id;
    private String orderNumber;

    private Long userId;

    private String status;
    private String priority;

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
    private String trackingNumber;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime shippedAt;
    private LocalDateTime deliveredAt;

    private List<OrderItemResponseDTO> items;
}
