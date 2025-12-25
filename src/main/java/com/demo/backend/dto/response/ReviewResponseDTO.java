package com.demo.backend.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReviewResponseDTO {
    private Long id;

    private Long productId;
    private Long userId;
    private Long orderId;

    private Integer rating;
    private String title;
    private String comment;

    private boolean verifiedPurchase;
    private boolean approved;

    private Integer helpfulCount;

    private String status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
