package com.demo.backend.service;

import com.demo.backend.dto.compare.CompareRequestDTO;
import com.demo.backend.dto.compare.ProductComparisonDTO;

public interface ProductComparisonService {

    /**
     * Compare two products using:
     * - Specs
     * - Basic fields (price, rating, stock)
     * - AI summary analysis
     *
     * @param req user request containing IDs or names of products to compare
     * @return DTO containing full comparison details
     */
    ProductComparisonDTO compare(CompareRequestDTO req);
}
