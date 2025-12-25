package com.demo.backend.dto.request;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CouponUsageRequestDTO {
    private Long couponId;
    private Long userId;
    private Long orderId;
    private BigDecimal discountAmount;
}
