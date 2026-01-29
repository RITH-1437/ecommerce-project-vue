package com.demo.backend.service.impl;

import com.demo.backend.dto.WishlistItemDTO;
import com.demo.backend.exception.BusinessException;
import com.demo.backend.exception.ResourceNotFoundException;
import com.demo.backend.model.*;
import com.demo.backend.repository.*;
import com.demo.backend.service.WishlistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final WishlistItemRepository wishlistItemRepository;
    private final WishlistShareRepository wishlistShareRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    public Wishlist add(Wishlist wishlist) {
        return wishlistRepository.save(wishlist);
    }

    @Override
    public void remove(Long id) {
        wishlistRepository.deleteById(id);
    }

    @Override
    public List<Wishlist> findByUserId(Long userId) {
        return wishlistRepository.findByUserId(userId);
    }

    @Override
    public boolean exists(Long userId, Long productId) {
        return wishlistRepository.existsByUserIdAndProductId(userId, productId);
    }

    // ========== NEW ENHANCED METHODS ==========

    @Override
    @Transactional
    public WishlistItem addToWishlist(Long userId, Long productId, String notes,
            Boolean notifyOnPriceDrop, Boolean notifyOnRestock) {
        log.info("Adding product {} to wishlist for user {}", productId, userId);

        // Check if already in wishlist
        if (wishlistItemRepository.existsByUserIdAndProductId(userId, productId)) {
            throw new BusinessException("Product is already in your wishlist");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        WishlistItem wishlistItem = WishlistItem.builder()
                .user(user)
                .product(product)
                .priceAtAddition(product.getPrice())
                .notes(notes)
                .notifyOnPriceDrop(notifyOnPriceDrop != null ? notifyOnPriceDrop : false)
                .notifyOnRestock(notifyOnRestock != null ? notifyOnRestock : false)
                .build();

        return wishlistItemRepository.save(wishlistItem);
    }

    @Override
    @Transactional
    public void removeFromWishlist(Long userId, Long productId) {
        log.info("Removing product {} from wishlist for user {}", productId, userId);
        wishlistItemRepository.deleteByUserIdAndProductId(userId, productId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WishlistItemDTO> getUserWishlist(Long userId) {
        List<WishlistItem> items = wishlistItemRepository.findByUserIdWithProductDetails(userId);
        return items.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isInWishlist(Long userId, Long productId) {
        return wishlistItemRepository.existsByUserIdAndProductId(userId, productId);
    }

    @Override
    @Transactional(readOnly = true)
    public long getWishlistCount(Long userId) {
        return wishlistItemRepository.countByUserId(userId);
    }

    @Override
    @Transactional
    public WishlistItem updateWishlistItem(Long userId, Long productId, String notes,
            Boolean notifyOnPriceDrop, Boolean notifyOnRestock) {
        WishlistItem item = wishlistItemRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist item not found"));

        if (notes != null) {
            item.setNotes(notes);
        }
        if (notifyOnPriceDrop != null) {
            item.setNotifyOnPriceDrop(notifyOnPriceDrop);
        }
        if (notifyOnRestock != null) {
            item.setNotifyOnRestock(notifyOnRestock);
        }

        return wishlistItemRepository.save(item);
    }

    @Override
    @Transactional
    public void clearWishlist(Long userId) {
        log.info("Clearing wishlist for user {}", userId);
        List<WishlistItem> items = wishlistItemRepository.findByUserIdOrderByAddedAtDesc(userId);
        wishlistItemRepository.deleteAll(items);
    }

    @Override
    @Transactional
    public WishlistShare createShareLink(Long userId, Integer expirationDays) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String token = UUID.randomUUID().toString();

        LocalDateTime expiresAt = expirationDays != null && expirationDays > 0
                ? LocalDateTime.now().plusDays(expirationDays)
                : null;

        WishlistShare share = WishlistShare.builder()
                .user(user)
                .shareToken(token)
                .expiresAt(expiresAt)
                .isActive(true)
                .build();

        return wishlistShareRepository.save(share);
    }

    @Override
    @Transactional
    public List<WishlistItemDTO> getSharedWishlist(String shareToken) {
        WishlistShare share = wishlistShareRepository.findByShareTokenAndIsActiveTrue(shareToken)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid or expired share link"));

        // Check if expired
        if (share.getExpiresAt() != null && share.getExpiresAt().isBefore(LocalDateTime.now())) {
            share.setIsActive(false);
            wishlistShareRepository.save(share);
            throw new BusinessException("Share link has expired");
        }

        // Increment view count
        share.incrementViewCount();
        wishlistShareRepository.save(share);

        // Get wishlist
        return getUserWishlist(share.getUser().getId());
    }

    @Override
    @Transactional
    public void deactivateShareLink(Long userId, String shareToken) {
        WishlistShare share = wishlistShareRepository.findByShareTokenAndIsActiveTrue(shareToken)
                .orElseThrow(() -> new ResourceNotFoundException("Share link not found"));

        if (!share.getUser().getId().equals(userId)) {
            throw new BusinessException("Unauthorized");
        }

        share.setIsActive(false);
        wishlistShareRepository.save(share);
    }

    // Helper method to convert WishlistItem to DTO
    private WishlistItemDTO convertToDTO(WishlistItem item) {
        Product product = item.getProduct();
        BigDecimal currentPrice = product.getPrice();
        BigDecimal priceAtAddition = item.getPriceAtAddition();

        boolean priceDrop = priceAtAddition != null &&
                currentPrice.compareTo(priceAtAddition) < 0;

        BigDecimal savings = priceDrop
                ? priceAtAddition.subtract(currentPrice)
                : BigDecimal.ZERO;

        return WishlistItemDTO.builder()
                .id(item.getId())
                .productId(product.getId())
                .productName(product.getName())
                .productSlug(product.getSlug())
                .productImage(product.getImageUrl())
                .shortDescription(product.getShortDescription())
                .currentPrice(currentPrice)
                .originalPrice(product.getOriginalPrice())
                .priceAtAddition(priceAtAddition)
                .priceDrop(priceDrop)
                .savings(savings)
                .stock(product.getStock())
                .inStock(product.getStock() > 0)
                .rating(product.getRating())
                .reviewsCount(product.getReviewsCount())
                .badge(product.getBadge())
                .addedAt(item.getAddedAt())
                .notes(item.getNotes())
                .notifyOnPriceDrop(item.getNotifyOnPriceDrop())
                .notifyOnRestock(item.getNotifyOnRestock())
                .build();
    }
}
