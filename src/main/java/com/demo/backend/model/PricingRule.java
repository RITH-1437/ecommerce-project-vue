package com.demo.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pricing_rules")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PricingRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Human name
    @Column(nullable = false)
    private String name;

    // Example: "stock_below", "sales_velocity_above", "time_window", "category_match", "always"
    @Column(nullable = false)
    private String conditionType;

    // Condition parameter encoded as JSON or simple text, example: "10" for stock_below, or "category:phones"
    @Column(columnDefinition = "TEXT")
    private String conditionParam;

    // Action type: "percentage_increase", "percentage_decrease", "fixed_price", "multiplier"
    @Column(nullable = false)
    private String actionType;

    // Action value: e.g. 10 for percentage 10%, or 0.9 for multiplier
    @Column(precision = 10, scale = 4)
    private BigDecimal actionValue;

    // Target scope: "product:123", "category:5", "all"
    @Column(nullable = false)
    private String target;

    @Builder.Default
    private boolean enabled = true;

    // Priority order (higher executed first)
    @Builder.Default
    private Integer priority = 0;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
