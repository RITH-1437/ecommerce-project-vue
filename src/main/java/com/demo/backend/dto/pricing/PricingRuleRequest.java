package com.demo.backend.dto.pricing;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PricingRuleRequest {
    @NotBlank(message = "Rule name is required")
    private String name;

    @NotBlank(message = "Condition type is required")
    private String conditionType;

    private String conditionParam;

    @NotBlank(message = "Action type is required")
    private String actionType;

    @NotNull(message = "Action value is required")
    private BigDecimal actionValue;

    @NotBlank(message = "Target is required")
    private String target;

    private Boolean enabled;
    private Integer priority;
}
