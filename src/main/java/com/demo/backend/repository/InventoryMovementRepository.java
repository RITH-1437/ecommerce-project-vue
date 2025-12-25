package com.demo.backend.repository;

import com.demo.backend.model.InventoryMovement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryMovementRepository extends JpaRepository<InventoryMovement, Long> {

    List<InventoryMovement> findByProduct_Id(Long productId);

    List<InventoryMovement> findByProduct_IdOrderByCreatedAtDesc(Long productId);
}
