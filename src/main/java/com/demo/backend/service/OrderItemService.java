package com.demo.backend.service;

import com.demo.backend.model.OrderItem;
import java.util.List;

public interface OrderItemService {

    OrderItem create(OrderItem item);

    void delete(Long id);

    List<OrderItem> findByOrderId(Long orderId);

    List<OrderItem> findByProductId(Long productId);
}
