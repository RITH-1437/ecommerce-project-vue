package com.demo.backend.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductResponseDTO {

    private Long id;

    private String name;
    private String slug;
    private String sku;

    private String description;
    private String shortDescription;

    private BigDecimal price;
    private BigDecimal originalPrice;

    private String imageUrl;

    private Integer stock;
    private Integer minStock;

    private String badge;

    private Double rating;
    private Integer reviewsCount;

    private String categoryName;
}
