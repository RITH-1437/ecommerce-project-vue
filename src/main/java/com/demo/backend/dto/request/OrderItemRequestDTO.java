package com.demo.backend.dto.request;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderItemRequestDTO {
    private Long productId;
    private String productName;
    private String productSku;
    private String productImage;
    private String selectedColor;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal subtotal;
}
