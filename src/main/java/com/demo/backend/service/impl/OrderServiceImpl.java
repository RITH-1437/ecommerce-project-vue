package com.demo.backend.service.impl;

import com.demo.backend.exception.BusinessException;
import com.demo.backend.model.Order;
import com.demo.backend.model.OrderStatusHistory;
import com.demo.backend.model.enums.OrderStatus;
import com.demo.backend.repository.OrderRepository;
import com.demo.backend.service.OrderService;
import com.demo.backend.service.OrderStatusHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderStatusHistoryService orderStatusHistoryService;

    @Override
    public Order create(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Order update(Long id, Order data) {
        Order order = findById(id);

        order.setStatus(data.getStatus());
        order.setPriority(data.getPriority());
        order.setTrackingNumber(data.getTrackingNumber());
        order.setPaymentStatus(data.getPaymentStatus());
        order.setBillingAddress(data.getBillingAddress());
        order.setShippingAddress(data.getShippingAddress());
        order.setNotes(data.getNotes());

        return orderRepository.save(order);
    }

    @Override
    public void delete(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Order not found"));
    }

    @Override
    public Order findByOrderNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new BusinessException("Order number not found"));
    }

    @Override
    public List<Order> findByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public List<Order> findByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public Page<Order> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    @Override
    public Order updateOrderStatus(Long orderId, OrderStatus newStatus, String notes, Long userId) {
        Order order = findById(orderId);
        OrderStatus previousStatus = order.getStatus();

        // Update order status
        order.setStatus(newStatus);
        Order updatedOrder = orderRepository.save(order);

        // Create status history entry
        orderStatusHistoryService.createStatusHistory(
                orderId, previousStatus, newStatus, notes, null, userId);

        return updatedOrder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderStatusHistory> getOrderStatusHistory(Long orderId) {
        return orderStatusHistoryService.getOrderHistory(orderId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Order> findByUserIdWithFilters(Long userId, OrderStatus status, Pageable pageable) {
        if (status != null) {
            return orderRepository.findByUserIdAndStatus(userId, status, pageable);
        }
        return orderRepository.findByUserId(userId, pageable);
    }
}
