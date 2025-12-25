package com.demo.backend.dto.request;

import com.demo.backend.model.ProductSpecification;
import lombok.Data;

@Data
public class ProductSpecRequestDTO {
    private Long id;
    private String specKey;
    private String specValue;
    private Integer displayOrder;

    public ProductSpecification toEntity() {
        ProductSpecification s = ProductSpecification.builder()
                .specKey(this.specKey)
                .specValue(this.specValue)
                .displayOrder(this.displayOrder)
                .build();
        if (this.id != null) s.setId(this.id);
        return s;
    }
}
