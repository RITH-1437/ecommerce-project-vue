package com.demo.backend.controller;

import com.demo.backend.model.OrderStatusHistory;
import com.demo.backend.model.enums.OrderStatus;
import com.demo.backend.service.OrderService;
import com.demo.backend.service.OrderStatusHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller for managing order status and history tracking.
 */
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderTrackingController {

    private final OrderService orderService;
    private final OrderStatusHistoryService orderStatusHistoryService;

    /**
     * Get order status history.
     * GET /api/orders/{orderId}/history
     */
    @GetMapping("/{orderId}/history")
    public ResponseEntity<List<OrderStatusHistory>> getOrderHistory(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderStatusHistory(orderId));
    }

    /**
     * Update order status.
     * PUT /api/orders/{orderId}/status
     */
    @PutMapping("/{orderId}/status")
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody Map<String, Object> payload) {

        OrderStatus newStatus = OrderStatus.valueOf((String) payload.get("status"));
        String notes = (String) payload.get("notes");
        Long userId = payload.get("userId") != null ? Long.valueOf(payload.get("userId").toString()) : null;

        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, newStatus, notes, userId));
    }

    /**
     * Get latest status change for an order.
     * GET /api/orders/{orderId}/latest-status
     */
    @GetMapping("/{orderId}/latest-status")
    public ResponseEntity<OrderStatusHistory> getLatestStatusChange(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderStatusHistoryService.getLatestStatusChange(orderId));
    }
}
