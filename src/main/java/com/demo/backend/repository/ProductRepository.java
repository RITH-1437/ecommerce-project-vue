package com.demo.backend.repository;

import com.demo.backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("""
        SELECT p FROM Product p
        WHERE LOWER(p.name) LIKE %:kw%
        OR LOWER(p.description) LIKE %:kw%
        OR LOWER(p.shortDescription) LIKE %:kw%
    """)
    List<Product> searchByNameOrDesc(@Param("kw") String keyword);

    @Query("""
        SELECT p FROM Product p
        WHERE LOWER(p.name) LIKE %:a%
           OR LOWER(p.name) LIKE %:b%
    """)
    List<Product> findByNames(@Param("a") String a, @Param("b") String b);

    Optional<Product> findBySlug(String slug);

    List<Product> findByCategoryId(Long categoryId);

    boolean existsBySlug(String slug);

    boolean existsBySku(String sku);
}
