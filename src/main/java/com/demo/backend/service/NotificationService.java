package com.demo.backend.service;

import com.demo.backend.model.Notification;
import com.demo.backend.model.NotificationPreferences;
import com.demo.backend.model.enums.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * Service interface for managing notifications.
 */
public interface NotificationService {

    /**
     * Create a notification from template.
     * 
     * @param userId      User to notify
     * @param templateKey Template identifier (e.g., "ORDER_SHIPPED")
     * @param parameters  Parameters to replace in template
     * @return Created notification
     */
    Notification createFromTemplate(Long userId, String templateKey, Map<String, Object> parameters);

    /**
     * Create a custom notification.
     */
    Notification createNotification(Long userId, NotificationType type, String title,
            String message, String actionUrl, String actionText);

    /**
     * Get all notifications for a user (paginated).
     */
    Page<Notification> getUserNotifications(Long userId, Pageable pageable);

    /**
     * Get unread notifications for a user.
     */
    List<Notification> getUnreadNotifications(Long userId);

    /**
     * Get notifications by type.
     */
    Page<Notification> getUserNotificationsByType(Long userId, NotificationType type, Pageable pageable);

    /**
     * Mark notification as read.
     */
    Notification markAsRead(Long notificationId);

    /**
     * Mark all notifications as read for a user.
     */
    void markAllAsRead(Long userId);

    /**
     * Delete notification.
     */
    void deleteNotification(Long notificationId);

    /**
     * Count unread notifications for a user.
     */
    long countUnread(Long userId);

    /**
     * Get or create notification preferences for a user.
     */
    NotificationPreferences getPreferences(Long userId);

    /**
     * Update notification preferences.
     */
    NotificationPreferences updatePreferences(Long userId, NotificationPreferences preferences);

    /**
     * Send order status notification.
     */
    void notifyOrderStatus(Long userId, Long orderId, String orderNumber,
            String status, String trackingNumber);

    /**
     * Send product restock notification.
     */
    void notifyProductRestock(Long userId, Long productId, String productName, String productType);

    /**
     * Send price drop notification.
     */
    void notifyPriceDrop(Long userId, Long productId, String productName,
            String productType, Double oldPrice, Double newPrice);

    /**
     * Send review approved notification.
     */
    void notifyReviewApproved(Long userId, Long productId, String productName, String productType);

    /**
     * Send review reply notification.
     */
    void notifyReviewReply(Long userId, Long productId, String productName, String productType);

    /**
     * Send welcome notification to new user.
     */
    void sendWelcomeNotification(Long userId);

    /**
     * Send promotional notification.
     */
    void sendPromotionalNotification(Long userId, String title, String message,
            String actionUrl, String actionText);
}
