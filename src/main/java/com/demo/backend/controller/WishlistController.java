package com.demo.backend.controller;

import com.demo.backend.dto.WishlistItemDTO;
import com.demo.backend.model.Wishlist;
import com.demo.backend.model.WishlistItem;
import com.demo.backend.model.WishlistShare;
import com.demo.backend.model.User;
import com.demo.backend.service.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
@Tag(name = "Wishlist", description = "Wishlist management APIs")
@CrossOrigin(origins = "*", maxAge = 3600)
public class WishlistController {

    private final WishlistService wishlistService;

    // ========== NEW ENHANCED ENDPOINTS ==========

    @GetMapping
    @Operation(summary = "Get user's wishlist")
    public ResponseEntity<List<WishlistItemDTO>> getWishlist(
            @AuthenticationPrincipal User userDetails) {

        List<WishlistItemDTO> wishlist = wishlistService.getUserWishlist(userDetails.getId());
        return ResponseEntity.ok(wishlist);
    }

    @PostMapping("/add")
    @Operation(summary = "Add product to wishlist")
    public ResponseEntity<WishlistItem> addToWishlist(
            @AuthenticationPrincipal User userDetails,
            @RequestBody AddToWishlistRequest request) {

        WishlistItem item = wishlistService.addToWishlist(
                userDetails.getId(),
                request.getProductId(),
                request.getNotes(),
                request.getNotifyOnPriceDrop(),
                request.getNotifyOnRestock());

        return ResponseEntity.ok(item);
    }

    @DeleteMapping("/product/{productId}")
    @Operation(summary = "Remove product from wishlist")
    public ResponseEntity<Map<String, String>> removeFromWishlist(
            @AuthenticationPrincipal User userDetails,
            @PathVariable Long productId) {

        wishlistService.removeFromWishlist(userDetails.getId(), productId);
        return ResponseEntity.ok(Map.of("message", "Product removed from wishlist"));
    }

    @GetMapping("/check/{productId}")
    @Operation(summary = "Check if product is in wishlist")
    public ResponseEntity<Map<String, Boolean>> checkInWishlist(
            @AuthenticationPrincipal User userDetails,
            @PathVariable Long productId) {

        boolean inWishlist = wishlistService.isInWishlist(userDetails.getId(), productId);
        return ResponseEntity.ok(Map.of("inWishlist", inWishlist));
    }

    @GetMapping("/count")
    @Operation(summary = "Get wishlist items count")
    public ResponseEntity<Map<String, Long>> getWishlistCount(
            @AuthenticationPrincipal User userDetails) {

        long count = wishlistService.getWishlistCount(userDetails.getId());
        return ResponseEntity.ok(Map.of("count", count));
    }

    @PutMapping("/product/{productId}")
    @Operation(summary = "Update wishlist item settings")
    public ResponseEntity<WishlistItem> updateWishlistItem(
            @AuthenticationPrincipal User userDetails,
            @PathVariable Long productId,
            @RequestBody UpdateWishlistItemRequest request) {

        WishlistItem item = wishlistService.updateWishlistItem(
                userDetails.getId(),
                productId,
                request.getNotes(),
                request.getNotifyOnPriceDrop(),
                request.getNotifyOnRestock());

        return ResponseEntity.ok(item);
    }

    @DeleteMapping("/clear")
    @Operation(summary = "Clear entire wishlist")
    public ResponseEntity<Map<String, String>> clearWishlist(
            @AuthenticationPrincipal User userDetails) {

        wishlistService.clearWishlist(userDetails.getId());
        return ResponseEntity.ok(Map.of("message", "Wishlist cleared successfully"));
    }

    @PostMapping("/share")
    @Operation(summary = "Create shareable wishlist link")
    public ResponseEntity<WishlistShare> createShareLink(
            @AuthenticationPrincipal User userDetails,
            @RequestBody CreateShareLinkRequest request) {

        WishlistShare share = wishlistService.createShareLink(
                userDetails.getId(),
                request.getExpirationDays());

        return ResponseEntity.ok(share);
    }

    @GetMapping("/shared/{shareToken}")
    @Operation(summary = "View shared wishlist (public)")
    public ResponseEntity<List<WishlistItemDTO>> getSharedWishlist(
            @PathVariable String shareToken) {

        List<WishlistItemDTO> wishlist = wishlistService.getSharedWishlist(shareToken);
        return ResponseEntity.ok(wishlist);
    }

    @DeleteMapping("/share/{shareToken}")
    @Operation(summary = "Deactivate share link")
    public ResponseEntity<Map<String, String>> deactivateShareLink(
            @AuthenticationPrincipal User userDetails,
            @PathVariable String shareToken) {

        wishlistService.deactivateShareLink(userDetails.getId(), shareToken);
        return ResponseEntity.ok(Map.of("message", "Share link deactivated"));
    }

    // ========== LEGACY ENDPOINTS (BACKWARD COMPATIBILITY) ==========

    @PostMapping
    public ResponseEntity<Wishlist> add(@RequestBody Wishlist wishlist) {
        return ResponseEntity.ok(wishlistService.add(wishlist));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Wishlist>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(wishlistService.findByUserId(userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        wishlistService.remove(id);
        return ResponseEntity.noContent().build();
    }

    // ========== REQUEST DTOs ==========

    @Data
    static class AddToWishlistRequest {
        private Long productId;
        private String notes;
        private Boolean notifyOnPriceDrop;
        private Boolean notifyOnRestock;
    }

    @Data
    static class UpdateWishlistItemRequest {
        private String notes;
        private Boolean notifyOnPriceDrop;
        private Boolean notifyOnRestock;
    }

    @Data
    static class CreateShareLinkRequest {
        private Integer expirationDays;
    }
}
