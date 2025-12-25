package com.demo.backend.service.impl;

import com.demo.backend.model.CouponUsage;
import com.demo.backend.repository.CouponUsageRepository;
import com.demo.backend.service.CouponUsageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponUsageServiceImpl implements CouponUsageService {

    private final CouponUsageRepository couponUsageRepository;

    @Override
    public CouponUsage create(CouponUsage usage) {
        return couponUsageRepository.save(usage);
    }

    @Override
    public List<CouponUsage> findByCouponId(Long couponId) {
        return couponUsageRepository.findByCouponId(couponId);
    }

    @Override
    public List<CouponUsage> findByUserId(Long userId) {
        return couponUsageRepository.findByUserId(userId);
    }

    @Override
    public List<CouponUsage> findByOrderId(Long orderId) {
        return couponUsageRepository.findByOrderId(orderId);
    }
}
