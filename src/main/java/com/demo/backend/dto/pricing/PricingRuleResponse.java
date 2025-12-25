package com.demo.backend.dto.pricing;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class PricingRuleResponse {
    private Long id;
    private String name;
    private String conditionType;
    private String conditionParam;
    private String actionType;
    private BigDecimal actionValue;
    private String target;
    private Boolean enabled;
    private Integer priority;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
