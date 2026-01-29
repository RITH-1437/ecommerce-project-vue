package com.demo.backend.service;

import com.demo.backend.model.OrderStatusHistory;
import com.demo.backend.model.enums.OrderStatus;

import java.util.List;

/**
 * Service interface for managing order status history.
 */
public interface OrderStatusHistoryService {

    /**
     * Create a new status history entry when order status changes.
     */
    OrderStatusHistory createStatusHistory(Long orderId, OrderStatus previousStatus,
            OrderStatus newStatus, String notes,
            String location, Long changedByUserId);

    /**
     * Get all status history for an order.
     */
    List<OrderStatusHistory> getOrderHistory(Long orderId);

    /**
     * Get the latest status change for an order.
     */
    OrderStatusHistory getLatestStatusChange(Long orderId);
}
