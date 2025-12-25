package com.demo.backend.dto.pricing;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class PriceAdjustmentDTO {
    private Long productId;
    private Long ruleId;
    private BigDecimal oldPrice;
    private BigDecimal newPrice;
    private String reason;
    private LocalDateTime createdAt;
}
