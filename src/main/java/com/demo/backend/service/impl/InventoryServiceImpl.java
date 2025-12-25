package com.demo.backend.service.impl;

import com.demo.backend.model.InventoryMovement;
import com.demo.backend.model.Product;
import com.demo.backend.repository.InventoryMovementRepository;
import com.demo.backend.repository.ProductRepository;
import com.demo.backend.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final ProductRepository productRepository;
    private final InventoryMovementRepository movementRepository;

    @Override
    @Transactional
    public void reserveStock(Long productId, int qty, String orderNumber) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getStock() < qty)
            throw new RuntimeException("Insufficient stock");

        product.setStock(product.getStock() - qty);
        productRepository.save(product);

        log(product, InventoryMovement.MovementType.RESERVE, qty, orderNumber, "Stock reserved");
    }

    @Override
    @Transactional
    public void releaseStock(Long productId, int qty, String orderNumber) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setStock(product.getStock() + qty);
        productRepository.save(product);

        log(product, InventoryMovement.MovementType.RELEASE, qty, orderNumber, "Stock released");
    }

    @Override
    @Transactional
    public void deductStock(Long productId, int qty, String orderNumber) {

        log(productRepository.findById(productId).orElseThrow(),
                InventoryMovement.MovementType.STOCK_OUT, qty,
                orderNumber, "Stock deducted after completed payment");
    }

    @Override
    @Transactional
    public void addStock(Long productId, int qty, String note) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setStock(product.getStock() + qty);
        productRepository.save(product);

        log(product, InventoryMovement.MovementType.STOCK_IN, qty, null, note);
    }

    @Override
    @Transactional
    public void adjustStock(Long productId, int qty, String note) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setStock(qty);
        productRepository.save(product);

        log(product, InventoryMovement.MovementType.ADJUSTMENT, qty, null, note);
    }

    private void log(Product product, InventoryMovement.MovementType type, int qty,
                     String reference, String note) {

        InventoryMovement m = InventoryMovement.builder()
                .product(product)
                .movementType(type)
                .quantity(qty)
                .reference(reference)
                .note(note)
                .build();

        movementRepository.save(m);
    }
}
