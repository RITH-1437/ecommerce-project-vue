package com.demo.backend.model;

import com.demo.backend.model.enums.ShipmentStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Entity to track shipment details for orders.
 * Manages carrier information, tracking numbers, and delivery estimates.
 */
@Entity
@Table(name = "shipments", indexes = {
        @Index(name = "idx_tracking_number", columnList = "trackingNumber"),
        @Index(name = "idx_order_shipment", columnList = "order_id"),
        @Index(name = "idx_shipment_status", columnList = "status")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    @Column(unique = true)
    private String trackingNumber;

    @Column(nullable = false)
    private String carrier; // e.g., "DHL", "FedEx", "UPS", "Local Courier"

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable = false)
    private ShipmentStatus status = ShipmentStatus.PENDING;

    private String trackingUrl;

    @Column(columnDefinition = "TEXT")
    private String currentLocation;

    private LocalDateTime estimatedDeliveryDate;

    private LocalDateTime shippedAt;

    private LocalDateTime deliveredAt;

    @Column(columnDefinition = "TEXT")
    private String deliveryNotes;

    private String recipientName;

    private String recipientPhone;

    @Column(columnDefinition = "TEXT")
    private String deliverySignature;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
