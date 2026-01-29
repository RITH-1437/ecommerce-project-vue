package com.demo.backend.service;

import com.demo.backend.model.Order;
import com.demo.backend.model.OrderStatusHistory;
import com.demo.backend.model.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface OrderService {

    Order create(Order order);

    Order update(Long id, Order order);

    void delete(Long id);

    Order findById(Long id);

    Order findByOrderNumber(String orderNumber);

    List<Order> findByUserId(Long userId);

    List<Order> findByStatus(OrderStatus status);

    List<Order> findAll();

    Page<Order> findAll(Pageable pageable);

    /**
     * Update order status and create status history entry
     */
    Order updateOrderStatus(Long orderId, OrderStatus newStatus, String notes, Long userId);

    /**
     * Get order status history
     */
    List<OrderStatusHistory> getOrderStatusHistory(Long orderId);

    /**
     * Get orders by user with pagination and filters
     */
    Page<Order> findByUserIdWithFilters(Long userId, OrderStatus status, Pageable pageable);
}
