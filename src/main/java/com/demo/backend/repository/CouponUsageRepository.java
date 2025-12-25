package com.demo.backend.repository;

import com.demo.backend.model.CouponUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CouponUsageRepository extends JpaRepository<CouponUsage, Long> {
    List<CouponUsage> findByCouponId(Long couponId);
    List<CouponUsage> findByUserId(Long userId);
    List<CouponUsage> findByOrderId(Long orderId);
}
