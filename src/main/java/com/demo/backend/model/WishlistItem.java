package com.demo.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "wishlist_items", uniqueConstraints = @UniqueConstraint(columnNames = { "user_id",
        "product_id" }), indexes = {
                @Index(name = "idx_wishlist_user", columnList = "user_id"),
                @Index(name = "idx_wishlist_product", columnList = "product_id"),
                @Index(name = "idx_wishlist_added_at", columnList = "added_at")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishlistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "added_at")
    private LocalDateTime addedAt;

    @Column(length = 500)
    private String notes;

    @Column(name = "price_at_addition", precision = 10, scale = 2)
    private BigDecimal priceAtAddition;

    @Column(name = "notify_on_price_drop")
    @Builder.Default
    private Boolean notifyOnPriceDrop = false;

    @Column(name = "notify_on_restock")
    @Builder.Default
    private Boolean notifyOnRestock = false;

    @PrePersist
    public void onCreate() {
        this.addedAt = LocalDateTime.now();
    }
}
