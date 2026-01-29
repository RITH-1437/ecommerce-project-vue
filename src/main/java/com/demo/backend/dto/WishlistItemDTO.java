package com.demo.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishlistItemDTO {

    private Long id;
    private Long productId;
    private String productName;
    private String productSlug;
    private String productImage;
    private String shortDescription;
    private BigDecimal currentPrice;
    private BigDecimal originalPrice;
    private BigDecimal priceAtAddition;
    private Boolean priceDrop;
    private BigDecimal savings;
    private Integer stock;
    private Boolean inStock;
    private BigDecimal rating;
    private Integer reviewsCount;
    private String badge;
    private LocalDateTime addedAt;
    private String notes;
    private Boolean notifyOnPriceDrop;
    private Boolean notifyOnRestock;
}
