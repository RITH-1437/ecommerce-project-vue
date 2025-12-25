package com.demo.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_movements")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class InventoryMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Enumerated(EnumType.STRING)
    private MovementType movementType;

    private Integer quantity;

    private String reference;

    private String note;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum MovementType {
        STOCK_IN,
        STOCK_OUT,
        RESERVE,
        RELEASE,
        ADJUSTMENT
    }
}
