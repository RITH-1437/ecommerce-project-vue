package com.demo.backend.dto.request;

import lombok.Data;

@Data
public class ReviewRequestDTO {
    private Long productId;
    private Long userId;
    private Long orderId;

    private Integer rating;
    private String title;
    private String comment;

    private String status; // PENDING, APPROVED, REJECTED
}
