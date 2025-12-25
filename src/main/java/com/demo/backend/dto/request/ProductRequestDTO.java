package com.demo.backend.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductRequestDTO {

    private String name;
    private String slug;
    private String sku;

    private Long categoryId;

    private String description;
    private String shortDescription;

    private BigDecimal price;
    private BigDecimal originalPrice;

    private Integer stock;
    private Integer minStock;

    private String badge;
    // Use wrapper so PATCH can omit it (null)
    private Boolean active;

    private String imageUrl;

    private List<ProductImageRequestDTO> images;
    private List<ProductColorRequestDTO> colors;
    private List<ProductSpecRequestDTO> specifications;
}
