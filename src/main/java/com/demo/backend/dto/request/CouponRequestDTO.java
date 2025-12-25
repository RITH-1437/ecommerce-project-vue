package com.demo.backend.dto.request;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CouponRequestDTO {
    private String code;
    private String description;
    private String discountType; // FIXED, PERCENTAGE
    private BigDecimal discountValue;

    private BigDecimal minOrderAmount;
    private BigDecimal maxDiscountAmount;

    private Integer usageLimit;

    private boolean active;

    private LocalDateTime validFrom;
    private LocalDateTime validUntil;
}
