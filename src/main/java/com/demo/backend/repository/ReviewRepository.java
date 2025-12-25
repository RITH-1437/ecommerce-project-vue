package com.demo.backend.repository;

import com.demo.backend.model.Review;
import com.demo.backend.model.enums.ReviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProductId(Long productId);
    List<Review> findByUserId(Long userId);
    List<Review> findByStatus(ReviewStatus status);
}
