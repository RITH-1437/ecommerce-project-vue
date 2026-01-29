package com.demo.backend.repository;

import com.demo.backend.model.OrderStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for OrderStatusHistory entity.
 * Provides methods to retrieve order status change history.
 */
@Repository
public interface OrderStatusHistoryRepository extends JpaRepository<OrderStatusHistory, Long> {

    /**
     * Find all status history entries for a specific order, ordered by timestamp
     * descending.
     */
    List<OrderStatusHistory> findByOrderIdOrderByChangedAtDesc(Long orderId);

    /**
     * Find all status history entries for a specific order.
     */
    List<OrderStatusHistory> findByOrderId(Long orderId);
}
