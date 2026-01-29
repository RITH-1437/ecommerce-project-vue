package com.demo.backend.service;

import com.demo.backend.dto.WishlistItemDTO;
import com.demo.backend.model.Wishlist;
import com.demo.backend.model.WishlistItem;
import com.demo.backend.model.WishlistShare;

import java.util.List;

public interface WishlistService {

    Wishlist add(Wishlist wishlist);

    void remove(Long id);

    List<Wishlist> findByUserId(Long userId);

    boolean exists(Long userId, Long productId);

    /**
     * Add product to wishlist with notifications
     */
    WishlistItem addToWishlist(Long userId, Long productId, String notes,
            Boolean notifyOnPriceDrop, Boolean notifyOnRestock);

    /**
     * Remove product from wishlist by IDs
     */
    void removeFromWishlist(Long userId, Long productId);

    /**
     * Get user's complete wishlist with product details
     */
    List<WishlistItemDTO> getUserWishlist(Long userId);

    /**
     * Check if product is in user's wishlist
     */
    boolean isInWishlist(Long userId, Long productId);

    /**
     * Get wishlist count for user
     */
    long getWishlistCount(Long userId);

    /**
     * Update wishlist item notification settings
     */
    WishlistItem updateWishlistItem(Long userId, Long productId, String notes,
            Boolean notifyOnPriceDrop, Boolean notifyOnRestock);

    /**
     * Clear entire wishlist for user
     */
    void clearWishlist(Long userId);

    /**
     * Create shareable wishlist link
     */
    WishlistShare createShareLink(Long userId, Integer expirationDays);

    /**
     * Get shared wishlist by token
     */
    List<WishlistItemDTO> getSharedWishlist(String shareToken);

    /**
     * Deactivate share link
     */
    void deactivateShareLink(Long userId, String shareToken);
}
