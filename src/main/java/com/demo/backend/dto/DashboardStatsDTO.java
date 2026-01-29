package com.demo.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDTO {
    private Long totalUsers;
    private Long activeUsers;
    private Long totalOrders;
    private BigDecimal totalRevenue;
    private Long totalProducts;
    private Long lowStockProducts;
    private Long pendingOrders;
    private BigDecimal averageOrderValue;

    // Growth metrics (compared to previous period)
    private Double userGrowthRate;
    private Double revenueGrowthRate;
    private Double orderGrowthRate;
    private Double conversionRate;
}
