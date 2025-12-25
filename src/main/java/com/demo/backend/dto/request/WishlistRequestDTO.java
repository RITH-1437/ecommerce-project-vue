package com.demo.backend.dto.request;

import lombok.Data;

@Data
public class WishlistRequestDTO {
    private Long userId;
    private Long productId;
}
