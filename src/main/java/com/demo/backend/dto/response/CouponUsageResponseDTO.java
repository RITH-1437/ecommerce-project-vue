package com.demo.backend.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CouponUsageResponseDTO {
    private Long id;

    private Long couponId;
    private Long userId;
    private Long orderId;

    private BigDecimal discountAmount;

    private LocalDateTime usedAt;
}
