package com.demo.backend.controller;

import com.demo.backend.model.Notification;
import com.demo.backend.model.NotificationPreferences;
import com.demo.backend.model.User;
import com.demo.backend.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications", description = "Notification management APIs")
@CrossOrigin(origins = "*", maxAge = 3600)
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    @Operation(summary = "Get all notifications for current user")
    public ResponseEntity<Page<Notification>> getNotifications(
            @AuthenticationPrincipal User userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Notification> notifications = notificationService.getUserNotifications(
                userDetails.getId(), pageable);

        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/unread")
    @Operation(summary = "Get unread notifications")
    public ResponseEntity<List<Notification>> getUnreadNotifications(
            @AuthenticationPrincipal User userDetails) {

        List<Notification> notifications = notificationService.getUnreadNotifications(
                userDetails.getId());

        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/count/unread")
    @Operation(summary = "Get count of unread notifications")
    public ResponseEntity<Map<String, Long>> getUnreadCount(
            @AuthenticationPrincipal User userDetails) {

        long count = notificationService.countUnread(userDetails.getId());
        return ResponseEntity.ok(Map.of("unreadCount", count));
    }

    @PutMapping("/{id}/read")
    @Operation(summary = "Mark notification as read")
    public ResponseEntity<Notification> markAsRead(
            @AuthenticationPrincipal User userDetails,
            @PathVariable Long id) {

        Notification notification = notificationService.markAsRead(id);
        return ResponseEntity.ok(notification);
    }

    @PutMapping("/read-all")
    @Operation(summary = "Mark all notifications as read")
    public ResponseEntity<Map<String, String>> markAllAsRead(
            @AuthenticationPrincipal User userDetails) {

        notificationService.markAllAsRead(userDetails.getId());
        return ResponseEntity.ok(Map.of("message", "All notifications marked as read"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete notification")
    public ResponseEntity<Map<String, String>> deleteNotification(
            @AuthenticationPrincipal User userDetails,
            @PathVariable Long id) {

        notificationService.deleteNotification(id);
        return ResponseEntity.ok(Map.of("message", "Notification deleted successfully"));
    }

    @GetMapping("/preferences")
    @Operation(summary = "Get notification preferences")
    public ResponseEntity<NotificationPreferences> getPreferences(
            @AuthenticationPrincipal User userDetails) {

        NotificationPreferences preferences = notificationService.getPreferences(
                userDetails.getId());

        return ResponseEntity.ok(preferences);
    }

    @PutMapping("/preferences")
    @Operation(summary = "Update notification preferences")
    public ResponseEntity<NotificationPreferences> updatePreferences(
            @AuthenticationPrincipal User userDetails,
            @RequestBody NotificationPreferences preferences) {

        NotificationPreferences updated = notificationService.updatePreferences(
                userDetails.getId(), preferences);

        return ResponseEntity.ok(updated);
    }
}
