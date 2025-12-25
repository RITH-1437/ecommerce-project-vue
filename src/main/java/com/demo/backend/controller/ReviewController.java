package com.demo.backend.controller;

import com.demo.backend.model.Review;
import com.demo.backend.model.enums.ReviewStatus;
import com.demo.backend.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<List<Review>> getAll() {
        return ResponseEntity.ok(reviewService.findAll());
    }

    @PostMapping
    public ResponseEntity<Review> create(@RequestBody Review review) {
        return ResponseEntity.ok(reviewService.create(review));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Review> update(@PathVariable Long id, @RequestBody Review data) {
        return ResponseEntity.ok(reviewService.update(id, data));
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<List<Review>> getByProduct(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.findByProductId(id));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<Review>> getByUser(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.findByUserId(id));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Review>> getByStatus(@PathVariable ReviewStatus status) {
        return ResponseEntity.ok(reviewService.findByStatus(status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reviewService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
