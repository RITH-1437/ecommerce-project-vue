package com.demo.backend.dto.request;

import lombok.Data;

@Data
public class CartItemRequestDTO {
    private Long cartId;
    private Long productId;
    private String selectedColor;
    private Integer quantity;
}
