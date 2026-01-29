package com.demo.backend.service;

import com.demo.backend.model.Review;
import com.demo.backend.model.enums.ReviewStatus;
import java.util.List;

public interface ReviewService {

    Review create(Review review);

    Review update(Long id, Review review);

    void delete(Long id);

    List<Review> findAll();

    List<Review> findByProductId(Long productId);

    List<Review> findByUserId(Long userId);

    List<Review> findByStatus(ReviewStatus status);

    Review incrementHelpfulCount(Long id);

    Review approve(Long id);

    Review reject(Long id);
}
