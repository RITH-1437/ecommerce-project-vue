package com.demo.backend.service;

import com.demo.backend.dto.FilterOptionsDTO;
import com.demo.backend.dto.ProductSearchDTO;
import com.demo.backend.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    Product create(Product product);

    Product update(Long id, Product product);

    void delete(Long id);

    Product findById(Long id);

    Product findBySlug(String slug);

    List<Product> findByCategory(Long categoryId);

    List<Product> findAll();

    Page<Product> findAll(org.springframework.data.domain.Pageable pageable);

    // dynamic search with filters
    Page<Product> search(String keyword,
            Long categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Integer minStock,
            Pageable pageable);

    // Advanced search with comprehensive filters
    Page<Product> advancedSearch(ProductSearchDTO searchDTO);

    // Get autocomplete suggestions
    List<String> getSearchSuggestions(String keyword);

    // Get available filter options
    FilterOptionsDTO getFilterOptions();

    // Get products by specific filters
    Page<Product> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    Page<Product> findByMinRating(BigDecimal minRating, Pageable pageable);

    Page<Product> findInStockProducts(Pageable pageable);
}
