package com.demo.backend.service.impl;

import com.demo.backend.exception.BusinessException;
import com.demo.backend.model.Review;
import com.demo.backend.model.enums.ReviewStatus;
import com.demo.backend.repository.ReviewRepository;
import com.demo.backend.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    @Override
    public Review create(Review review) {
        return reviewRepository.save(review);
    }

    @Override
    public Review update(Long id, Review data) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Review not found"));

        review.setRating(data.getRating());
        review.setTitle(data.getTitle());
        review.setStatus(data.getStatus());

        return reviewRepository.save(review);
    }

    @Override
    public void delete(Long id) {
        reviewRepository.deleteById(id);
    }

    @Override
    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    @Override
    public List<Review> findByProductId(Long productId) {
        return reviewRepository.findByProductId(productId);
    }

    @Override
    public List<Review> findByUserId(Long userId) {
        return reviewRepository.findByUserId(userId);
    }

    @Override
    public List<Review> findByStatus(ReviewStatus status) {
        return reviewRepository.findByStatus(status);
    }

    @Override
    public Review incrementHelpfulCount(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Review not found"));
        review.setHelpfulCount(review.getHelpfulCount() + 1);
        return reviewRepository.save(review);
    }

    @Override
    public Review approve(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Review not found"));
        review.setStatus(ReviewStatus.APPROVED);
        review.setApproved(true);
        return reviewRepository.save(review);
    }

    @Override
    public Review reject(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Review not found"));
        review.setStatus(ReviewStatus.REJECTED);
        review.setApproved(false);
        return reviewRepository.save(review);
    }
}
