package com.demo.backend.dto.response;

import lombok.Data;

@Data
public class ProductSpecResponseDTO {
    private Long id;
    private String specKey;
    private String specValue;
    private Integer displayOrder;
}
