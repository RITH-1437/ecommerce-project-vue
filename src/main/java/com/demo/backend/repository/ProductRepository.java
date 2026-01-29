package com.demo.backend.repository;

import com.demo.backend.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

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

    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);

    boolean existsBySlug(String slug);

    boolean existsBySku(String sku);

    // Advanced search methods
    @Query("""
                SELECT DISTINCT p FROM Product p
                LEFT JOIN p.category c
                WHERE p.isActive = true
                AND (:keyword IS NULL OR
                     LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                     LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                     LOWER(p.shortDescription) LIKE LOWER(CONCAT('%', :keyword, '%')))
                AND (:categoryId IS NULL OR c.id = :categoryId)
                AND (:minPrice IS NULL OR p.price >= :minPrice)
                AND (:maxPrice IS NULL OR p.price <= :maxPrice)
                AND (:minRating IS NULL OR p.rating >= :minRating)
                AND (:inStock IS NULL OR :inStock = false OR p.stock > 0)
            """)
    Page<Product> searchProducts(
            @Param("keyword") String keyword,
            @Param("categoryId") Long categoryId,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("minRating") BigDecimal minRating,
            @Param("inStock") Boolean inStock,
            Pageable pageable);

    // Get autocomplete suggestions
    @Query("""
                SELECT DISTINCT p.name FROM Product p
                WHERE p.isActive = true
                AND LOWER(p.name) LIKE LOWER(CONCAT(:keyword, '%'))
                ORDER BY p.name
            """)
    List<String> findProductNameSuggestions(@Param("keyword") String keyword, Pageable pageable);

    // Get price range for filtering
    @Query("SELECT MIN(p.price) FROM Product p WHERE p.isActive = true")
    BigDecimal findMinPrice();

    @Query("SELECT MAX(p.price) FROM Product p WHERE p.isActive = true")
    BigDecimal findMaxPrice();

    // Get products by price range
    Page<Product> findByIsActiveTrueAndPriceBetween(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    // Get products by minimum rating
    Page<Product> findByIsActiveTrueAndRatingGreaterThanEqual(BigDecimal minRating, Pageable pageable);

    // Get in-stock products
    Page<Product> findByIsActiveTrueAndStockGreaterThan(Integer stock, Pageable pageable);
}
