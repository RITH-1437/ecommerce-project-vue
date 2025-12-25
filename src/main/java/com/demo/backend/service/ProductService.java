package com.demo.backend.service;

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
}
