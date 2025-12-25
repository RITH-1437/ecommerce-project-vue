package com.demo.backend.service;

public interface InventoryService {
    void reserveStock(Long productId, int qty, String orderNumber);
    void releaseStock(Long productId, int qty, String orderNumber);
    void deductStock(Long productId, int qty, String orderNumber);
    void addStock(Long productId, int qty, String note);
    void adjustStock(Long productId, int qty, String note);
}
