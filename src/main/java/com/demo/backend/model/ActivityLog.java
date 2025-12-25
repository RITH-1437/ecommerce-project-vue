package com.demo.backend.model;

import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "activity_logs", indexes = {
        @Index(name = "idx_user", columnList = "user_id"),
        @Index(name = "idx_entity", columnList = "entityType, entityId"),
        @Index(name = "idx_created", columnList = "createdAt"),
        @Index(name = "idx_action", columnList = "action")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String action;

    private String entityType;

    private Long entityId;

    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    private Map<String, Object> oldValues;

    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    private Map<String, Object> newValues;

    private String ipAddress;

    @Column(columnDefinition = "TEXT")
    private String userAgent;

    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
