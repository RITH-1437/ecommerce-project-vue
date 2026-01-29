package com.demo.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductAnalyticsDTO {
    private Long totalProducts;
    private Long activeProducts;
    private Long lowStockProducts;
    private Long outOfStockProducts;
    private BigDecimal totalInventoryValue;
    private List<ProductPerformance> topPerformers;
    private List<ProductPerformance> lowPerformers;
    private List<StockAlert> stockAlerts;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductPerformance {
        private Long productId;
        private String productName;
        private Long views;
        private Long sales;
        private BigDecimal revenue;
        private Double conversionRate;
        private Double averageRating;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StockAlert {
        private Long productId;
        private String productName;
        private Integer currentStock;
        private Integer minimumStock;
        private String alertLevel; // CRITICAL, WARNING, LOW
    }
}
