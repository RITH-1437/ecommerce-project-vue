package com.demo.backend.service.impl;

import com.demo.backend.model.Order;
import com.demo.backend.model.OrderStatusHistory;
import com.demo.backend.model.User;
import com.demo.backend.model.enums.OrderStatus;
import com.demo.backend.repository.OrderRepository;
import com.demo.backend.repository.OrderStatusHistoryRepository;
import com.demo.backend.repository.UserRepository;
import com.demo.backend.service.OrderStatusHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service implementation for managing order status history.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class OrderStatusHistoryServiceImpl implements OrderStatusHistoryService {

    private final OrderStatusHistoryRepository orderStatusHistoryRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Override
    public OrderStatusHistory createStatusHistory(Long orderId, OrderStatus previousStatus,
            OrderStatus newStatus, String notes,
            String location, Long changedByUserId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        User changedBy = null;
        if (changedByUserId != null) {
            changedBy = userRepository.findById(changedByUserId).orElse(null);
        }

        OrderStatusHistory history = OrderStatusHistory.builder()
                .order(order)
                .previousStatus(previousStatus)
                .newStatus(newStatus)
                .notes(notes)
                .location(location)
                .changedBy(changedBy)
                .changedAt(LocalDateTime.now())
                .build();

        return orderStatusHistoryRepository.save(history);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderStatusHistory> getOrderHistory(Long orderId) {
        return orderStatusHistoryRepository.findByOrderIdOrderByChangedAtDesc(orderId);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderStatusHistory getLatestStatusChange(Long orderId) {
        List<OrderStatusHistory> history = orderStatusHistoryRepository
                .findByOrderIdOrderByChangedAtDesc(orderId);
        return history.isEmpty() ? null : history.get(0);
    }
}
