package com.demo.backend.repository;

import com.demo.backend.model.PriceAdjustmentLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PriceAdjustmentLogRepository extends JpaRepository<PriceAdjustmentLog, Long> {
    List<PriceAdjustmentLog> findByProductIdOrderByCreatedAtDesc(Long productId);
}
