package com.demo.backend.repository;

import com.demo.backend.model.WishlistShare;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface WishlistShareRepository extends JpaRepository<WishlistShare, Long> {

    // Find active share by token
    Optional<WishlistShare> findByShareTokenAndIsActiveTrue(String shareToken);

    // Find all shares for a user
    List<WishlistShare> findByUserIdOrderByCreatedAtDesc(Long userId);

    // Find active shares for a user
    List<WishlistShare> findByUserIdAndIsActiveTrueOrderByCreatedAtDesc(Long userId);

    // Delete expired shares
    void deleteByExpiresAtBeforeAndIsActiveTrue(LocalDateTime now);
}
