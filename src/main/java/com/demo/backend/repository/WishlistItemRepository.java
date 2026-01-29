package com.demo.backend.repository;

import com.demo.backend.model.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WishlistItemRepository extends JpaRepository<WishlistItem, Long> {

    // Find all wishlist items for a user
    List<WishlistItem> findByUserIdOrderByAddedAtDesc(Long userId);

    // Check if product is in user's wishlist
    boolean existsByUserIdAndProductId(Long userId, Long productId);

    // Find specific wishlist item
    Optional<WishlistItem> findByUserIdAndProductId(Long userId, Long productId);

    // Delete by user and product
    void deleteByUserIdAndProductId(Long userId, Long productId);

    // Count wishlist items for user
    long countByUserId(Long userId);

    // Find all wishlist items with price drop notification enabled
    @Query("SELECT w FROM WishlistItem w WHERE w.product.id = :productId AND w.notifyOnPriceDrop = true")
    List<WishlistItem> findByProductIdWithPriceDropNotification(@Param("productId") Long productId);

    // Find all wishlist items with restock notification enabled
    @Query("SELECT w FROM WishlistItem w WHERE w.product.id = :productId AND w.notifyOnRestock = true")
    List<WishlistItem> findByProductIdWithRestockNotification(@Param("productId") Long productId);

    // Get wishlist with product details (eager fetch)
    @Query("SELECT w FROM WishlistItem w " +
            "JOIN FETCH w.product p " +
            "LEFT JOIN FETCH p.category " +
            "WHERE w.user.id = :userId " +
            "ORDER BY w.addedAt DESC")
    List<WishlistItem> findByUserIdWithProductDetails(@Param("userId") Long userId);
}
