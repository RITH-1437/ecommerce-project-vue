package com.demo.backend.dto.request;

import com.demo.backend.model.ProductImage;
import lombok.Data;

@Data
public class ProductImageRequestDTO {
    private Long id; // optional: present for updates
    private String imageUrl;
    private String altText;
    // wrapper so null = not provided
    private Boolean primary;
    private Integer displayOrder;

    public ProductImage toEntity() {
        ProductImage pi = ProductImage.builder()
                .imageUrl(this.imageUrl)
                .altText(this.altText)
                .displayOrder(this.displayOrder)
                .build();

        if (this.id != null) pi.setId(this.id);
        if (this.primary != null) pi.setPrimary(this.primary); // setter on entity
        return pi;
    }
}
