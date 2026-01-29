package com.demo.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO for filter options and ranges
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterOptionsDTO {

    private PriceRange priceRange;
    private List<String> availableColors;
    private List<String> availableStorage;
    private List<CategoryOption> categories;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PriceRange {
        private BigDecimal min;
        private BigDecimal max;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryOption {
        private Long id;
        private String name;
        private Long productCount;
    }
}
