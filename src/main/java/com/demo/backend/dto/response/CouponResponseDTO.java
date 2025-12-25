package com.demo.backend.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CouponResponseDTO {

    private Long id;
    private String code;
    private String description;

    private String discountType;
    private BigDecimal discountValue;

    private BigDecimal minOrderAmount;
    private BigDecimal maxDiscountAmount;

    private Integer usageLimit;
    private Integer usedCount;

    private boolean active;

    private LocalDateTime validFrom;
    private LocalDateTime validUntil;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
