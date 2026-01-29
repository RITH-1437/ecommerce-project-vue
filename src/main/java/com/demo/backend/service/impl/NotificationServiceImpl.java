package com.demo.backend.service.impl;

import com.demo.backend.exception.ResourceNotFoundException;
import com.demo.backend.model.*;
import com.demo.backend.model.enums.EmailFrequency;
import com.demo.backend.model.enums.NotificationPriority;
import com.demo.backend.model.enums.NotificationType;
import com.demo.backend.repository.*;
import com.demo.backend.service.NotificationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationTemplateRepository templateRepository;
    private final NotificationPreferencesRepository preferencesRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public Notification createFromTemplate(Long userId, String templateKey, Map<String, Object> parameters) {
        log.info("Creating notification from template: {} for user: {}", templateKey, userId);

        // Verify user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // Get template
        NotificationTemplate template = templateRepository.findByTemplateKeyAndIsActiveTrue(templateKey)
                .orElseThrow(() -> new ResourceNotFoundException("Template not found or inactive: " + templateKey));

        // Check if user wants this type of notification
        NotificationPreferences prefs = getPreferences(userId);
        if (!shouldSendNotification(prefs, template.getType())) {
            log.info("User {} has disabled {} notifications", userId, template.getType());
            return null;
        }

        // Replace placeholders
        String title = replacePlaceholders(template.getTitleTemplate(), parameters);
        String message = replacePlaceholders(template.getMessageTemplate(), parameters);
        String actionUrl = template.getActionUrlTemplate() != null
                ? replacePlaceholders(template.getActionUrlTemplate(), parameters)
                : null;

        // Create notification
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setType(template.getType());
        notification.setPriority(template.getPriority());
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setActionUrl(actionUrl);
        notification.setActionText(template.getActionText());
        notification.setIsRead(false);

        // Store metadata as JSON
        try {
            if (parameters != null && !parameters.isEmpty()) {
                notification.setMetadata(objectMapper.writeValueAsString(parameters));
            }
        } catch (JsonProcessingException e) {
            log.error("Error serializing notification metadata", e);
        }

        // Set expiration if needed (e.g., promotional notifications expire in 30 days)
        if (template.getType() == NotificationType.PROMOTION) {
            notification.setExpiresAt(LocalDateTime.now().plusDays(30));
        }

        notification = notificationRepository.save(notification);
        log.info("Created notification id: {} for user: {}", notification.getId(), userId);

        // TODO: Send email if preferences indicate
        // TODO: Send push notification if enabled

        return notification;
    }

    @Override
    @Transactional
    public Notification createNotification(Long userId, NotificationType type, String title,
            String message, String actionUrl, String actionText) {
        log.info("Creating custom notification for user: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        NotificationPreferences prefs = getPreferences(userId);
        if (!shouldSendNotification(prefs, type)) {
            log.info("User {} has disabled {} notifications", userId, type);
            return null;
        }

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setType(type);
        notification.setPriority(NotificationPriority.NORMAL);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setActionUrl(actionUrl);
        notification.setActionText(actionText);
        notification.setIsRead(false);

        return notificationRepository.save(notification);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Notification> getUserNotifications(Long userId, Pageable pageable) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notification> getUnreadNotifications(Long userId) {
        return notificationRepository.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Notification> getUserNotificationsByType(Long userId, NotificationType type, Pageable pageable) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        // Note: Can enhance with type filtering if needed
    }

    @Override
    @Transactional
    public Notification markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + notificationId));

        if (!notification.getIsRead()) {
            notification.setIsRead(true);
            notification.setReadAt(LocalDateTime.now());
            notification = notificationRepository.save(notification);
            log.info("Marked notification {} as read", notificationId);
        }

        return notification;
    }

    @Override
    @Transactional
    public void markAllAsRead(Long userId) {
        notificationRepository.markAllAsRead(userId, LocalDateTime.now());
        log.info("Marked all notifications as read for user {}", userId);
    }

    @Override
    @Transactional
    public void deleteNotification(Long notificationId) {
        notificationRepository.deleteById(notificationId);
        log.info("Deleted notification {}", notificationId);
    }

    @Override
    @Transactional(readOnly = true)
    public long countUnread(Long userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }

    @Override
    @Transactional
    public NotificationPreferences getPreferences(Long userId) {
        return preferencesRepository.findByUserId(userId)
                .orElseGet(() -> createDefaultPreferences(userId));
    }

    @Override
    @Transactional
    public NotificationPreferences updatePreferences(Long userId, NotificationPreferences preferences) {
        NotificationPreferences existing = getPreferences(userId);

        // Update all fields
        existing.setOrderUpdates(preferences.getOrderUpdates());
        existing.setOrderUpdatesEmail(preferences.getOrderUpdatesEmail());
        existing.setPromotional(preferences.getPromotional());
        existing.setPromotionalEmail(preferences.getPromotionalEmail());
        existing.setProductUpdates(preferences.getProductUpdates());
        existing.setProductUpdatesEmail(preferences.getProductUpdatesEmail());
        existing.setReviewResponses(preferences.getReviewResponses());
        existing.setReviewResponsesEmail(preferences.getReviewResponsesEmail());
        existing.setSystemAnnouncements(preferences.getSystemAnnouncements());
        existing.setSystemAnnouncementsEmail(preferences.getSystemAnnouncementsEmail());
        existing.setEmailFrequency(preferences.getEmailFrequency());
        existing.setPushEnabled(preferences.getPushEnabled());
        existing.setQuietHoursStart(preferences.getQuietHoursStart());
        existing.setQuietHoursEnd(preferences.getQuietHoursEnd());

        return preferencesRepository.save(existing);
    }

    @Override
    @Transactional
    public void notifyOrderStatus(Long userId, Long orderId, String orderNumber,
            String status, String trackingNumber) {
        Map<String, Object> params = new HashMap<>();
        params.put("orderId", orderId);
        params.put("orderNumber", orderNumber);
        params.put("status", status);
        if (trackingNumber != null) {
            params.put("trackingNumber", trackingNumber);
        }

        String templateKey = switch (status.toUpperCase()) {
            case "CONFIRMED", "PROCESSING" -> "ORDER_PLACED";
            case "SHIPPED" -> "ORDER_SHIPPED";
            case "DELIVERED" -> "ORDER_DELIVERED";
            case "CANCELLED" -> "ORDER_CANCELLED";
            default -> "ORDER_STATUS_UPDATE";
        };

        createFromTemplate(userId, templateKey, params);
    }

    @Override
    @Transactional
    public void notifyProductRestock(Long userId, Long productId, String productName, String productType) {
        Map<String, Object> params = new HashMap<>();
        params.put("productId", productId);
        params.put("productName", productName);
        params.put("productType", productType);

        createFromTemplate(userId, "PRODUCT_RESTOCK", params);
    }

    @Override
    @Transactional
    public void notifyPriceDrop(Long userId, Long productId, String productName,
            String productType, Double oldPrice, Double newPrice) {
        Map<String, Object> params = new HashMap<>();
        params.put("productId", productId);
        params.put("productName", productName);
        params.put("productType", productType);
        params.put("oldPrice", String.format("$%.2f", oldPrice));
        params.put("newPrice", String.format("$%.2f", newPrice));

        Double discount = ((oldPrice - newPrice) / oldPrice) * 100;
        params.put("discount", String.format("%.0f%%", discount));

        createFromTemplate(userId, "PRICE_DROP", params);
    }

    @Override
    @Transactional
    public void notifyReviewApproved(Long userId, Long productId, String productName, String productType) {
        Map<String, Object> params = new HashMap<>();
        params.put("productId", productId);
        params.put("productName", productName);
        params.put("productType", productType);

        createFromTemplate(userId, "REVIEW_APPROVED", params);
    }

    @Override
    @Transactional
    public void notifyReviewReply(Long userId, Long productId, String productName, String productType) {
        Map<String, Object> params = new HashMap<>();
        params.put("productId", productId);
        params.put("productName", productName);
        params.put("productType", productType);

        createFromTemplate(userId, "REVIEW_REPLY", params);
    }

    @Override
    @Transactional
    public void sendWelcomeNotification(Long userId) {
        Map<String, Object> params = new HashMap<>();
        createFromTemplate(userId, "WELCOME", params);
    }

    @Override
    @Transactional
    public void sendPromotionalNotification(Long userId, String title, String message,
            String actionUrl, String actionText) {
        createNotification(userId, NotificationType.PROMOTION, title, message, actionUrl, actionText);
    }

    // Helper methods

    private NotificationPreferences createDefaultPreferences(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        NotificationPreferences prefs = new NotificationPreferences();
        prefs.setUser(user);

        // Default: All notifications ON except promotional
        prefs.setOrderUpdates(true);
        prefs.setOrderUpdatesEmail(true);
        prefs.setPromotional(false); // OFF by default
        prefs.setPromotionalEmail(false);
        prefs.setProductUpdates(true);
        prefs.setProductUpdatesEmail(false);
        prefs.setReviewResponses(true);
        prefs.setReviewResponsesEmail(true);
        prefs.setSystemAnnouncements(true);
        prefs.setSystemAnnouncementsEmail(true);
        prefs.setEmailFrequency(EmailFrequency.INSTANT);
        prefs.setPushEnabled(false);

        // Quiet hours disabled by default (null means disabled)

        return preferencesRepository.save(prefs);
    }

    private boolean shouldSendNotification(NotificationPreferences prefs, NotificationType type) {
        return switch (type) {
            case ORDER -> prefs.getOrderUpdates();
            case PRODUCT -> prefs.getProductUpdates();
            case PROMOTION -> prefs.getPromotional();
            case REVIEW -> prefs.getReviewResponses();
            case SYSTEM -> prefs.getSystemAnnouncements();
        };
    }

    private String replacePlaceholders(String template, Map<String, Object> parameters) {
        if (template == null || parameters == null) {
            return template;
        }

        String result = template;
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            String placeholder = "{" + entry.getKey() + "}";
            String value = entry.getValue() != null ? entry.getValue().toString() : "";
            result = result.replace(placeholder, value);
        }

        return result;
    }
}
