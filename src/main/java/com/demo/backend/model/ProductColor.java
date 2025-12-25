package com.demo.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_colors", indexes = {
        @Index(name = "idx_product_color_product", columnList = "product_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductColor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private String colorName;
    private String colorHex;

    @Builder.Default
    private Integer stock = 0;

    @Builder.Default
    private Integer displayOrder = 0;

    @Builder.Default
    private boolean isAvailable = true;
}
