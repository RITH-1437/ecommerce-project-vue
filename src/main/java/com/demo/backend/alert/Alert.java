package com.demo.backend.alert;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "alerts", indexes = {
        @Index(name = "idx_alert_type", columnList = "type"),
        @Index(name = "idx_alert_created", columnList = "createdAt")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AlertType type;

    @Enumerated(EnumType.STRING)
    private AlertSeverity severity;

    @Column(columnDefinition = "TEXT")
    private String message;

    /**
     * Optional JSON string containing extra metadata (product id, endpoint, ip, txnId, etc).
     */
    @Column(columnDefinition = "TEXT")
    private String metadata;

    @Builder.Default
    private boolean acknowledged = false;
    private String acknowledgedBy;
    private LocalDateTime acknowledgedAt;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}
