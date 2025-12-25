package com.demo.backend.dto.response;

import lombok.Data;

@Data
public class ProductColorResponseDTO {
    private Long id;
    private String colorName;
    private String colorHex;
    private Integer stock;
    private Integer displayOrder;
    private boolean available;
}
