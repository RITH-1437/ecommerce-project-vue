package com.demo.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_specifications", indexes = {
        @Index(name = "idx_product_spec_product", columnList = "product_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSpecification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many specs -> one product
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "spec_key", length = 100)
    private String specKey;

    @Column(name = "spec_value", columnDefinition = "TEXT")
    private String specValue;

    private Integer displayOrder;
}
