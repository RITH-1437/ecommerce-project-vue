package com.demo.backend.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CartItemResponseDTO {
    private Long id;

    private Long cartId;
    private Long productId;

    private String selectedColor;

    private Integer quantity;

    private LocalDateTime addedAt;
    private LocalDateTime updatedAt;
}
