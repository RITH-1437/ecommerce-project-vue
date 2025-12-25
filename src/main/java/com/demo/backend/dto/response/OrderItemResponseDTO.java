package com.demo.backend.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderItemResponseDTO {
    private Long id;
    private Long orderId;

    private Long productId;
    private String productName;
    private String productSku;
    private String productImage;

    private String selectedColor;

    private Integer quantity;

    private BigDecimal price;
    private BigDecimal subtotal;
}
