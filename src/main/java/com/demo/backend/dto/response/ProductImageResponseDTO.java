package com.demo.backend.dto.response;

import lombok.Data;

@Data
public class ProductImageResponseDTO {
    private Long id;
    private String imageUrl;
    private String altText;
    private boolean primary;
    private Integer displayOrder;
}
