package com.demo.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for advanced product search and filtering
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSearchDTO {

    // Search keyword
    private String keyword;

    // Category filter
    private Long categoryId;

    // Price range filter
    private BigDecimal minPrice;
    private BigDecimal maxPrice;

    // Rating filter
    private BigDecimal minRating;

    // Stock filter
    private Boolean inStock;

    // Color filter (comma-separated)
    private String colors;

    // Storage filter (comma-separated, e.g., "64GB,128GB,256GB")
    private String storage;

    // Sort options: price_asc, price_desc, rating, newest, popular
    private String sortBy = "newest";

    // Pagination
    private Integer page = 0;
    private Integer size = 20;
}
