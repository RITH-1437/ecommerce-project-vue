package com.demo.backend.dto.request;

import com.demo.backend.model.ProductColor;
import lombok.Data;

@Data
public class ProductColorRequestDTO {
    private Long id;
    private String colorName;
    private String colorHex;
    private Integer stock;
    private Integer displayOrder;
    private Boolean available;

    public ProductColor toEntity() {
        ProductColor c = ProductColor.builder()
                .colorName(this.colorName)
                .colorHex(this.colorHex)
                .stock(this.stock)
                .displayOrder(this.displayOrder)
                .build();

        if (this.id != null) c.setId(this.id);
        if (this.available != null) c.setAvailable(this.available);
        return c;
    }
}
