package com.demo.backend.model;

import com.demo.backend.model.enums.TrackingEventType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Entity to track individual tracking events for shipments.
 * Provides detailed timeline of package movement.
 */
@Entity
@Table(name = "tracking_events", indexes = {
        @Index(name = "idx_shipment_events", columnList = "shipment_id"),
        @Index(name = "idx_event_timestamp", columnList = "eventTimestamp")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrackingEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipment_id", nullable = false)
    private Shipment shipment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TrackingEventType eventType;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String location;

    @Column(nullable = false)
    private LocalDateTime eventTimestamp;

    @Column(columnDefinition = "TEXT")
    private String notes;

    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
