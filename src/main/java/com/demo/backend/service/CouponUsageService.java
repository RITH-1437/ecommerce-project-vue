package com.demo.backend.service;

import com.demo.backend.model.CouponUsage;
import java.util.List;

public interface CouponUsageService {

    CouponUsage create(CouponUsage usage);

    List<CouponUsage> findByCouponId(Long couponId);

    List<CouponUsage> findByUserId(Long userId);

    List<CouponUsage> findByOrderId(Long orderId);
}
