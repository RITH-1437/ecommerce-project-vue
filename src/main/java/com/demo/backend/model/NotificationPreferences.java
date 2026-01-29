package com.demo.backend.model;

import com.demo.backend.model.enums.EmailFrequency;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Entity representing user notification preferences.
 */
@Entity
@Table(name = "notification_preferences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationPreferences {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    // Order Notifications
    @Builder.Default
    @Column(nullable = false)
    private Boolean orderUpdates = true;

    @Builder.Default
    @Column(nullable = false)
    private Boolean orderUpdatesEmail = true;

    // Promotional Notifications
    @Builder.Default
    @Column(nullable = false)
    private Boolean promotional = true;

    @Builder.Default
    @Column(nullable = false)
    private Boolean promotionalEmail = false;

    // Product Notifications
    @Builder.Default
    @Column(nullable = false)
    private Boolean productUpdates = true;

    @Builder.Default
    @Column(nullable = false)
    private Boolean productUpdatesEmail = false;

    // Review Notifications
    @Builder.Default
    @Column(nullable = false)
    private Boolean reviewResponses = true;

    @Builder.Default
    @Column(nullable = false)
    private Boolean reviewResponsesEmail = true;

    // System Notifications
    @Builder.Default
    @Column(nullable = false)
    private Boolean systemAnnouncements = true;

    @Builder.Default
    @Column(nullable = false)
    private Boolean systemAnnouncementsEmail = true;

    // Push Notifications
    @Builder.Default
    @Column(nullable = false)
    private Boolean pushEnabled = false;

    // Email Settings
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private EmailFrequency emailFrequency = EmailFrequency.INSTANT;

    // Quiet Hours
    private LocalTime quietHoursStart;
    private LocalTime quietHoursEnd;

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
