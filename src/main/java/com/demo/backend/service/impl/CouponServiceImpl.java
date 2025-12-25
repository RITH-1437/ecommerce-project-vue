package com.demo.backend.service.impl;

import com.demo.backend.exception.BusinessException;
import com.demo.backend.model.Coupon;
import com.demo.backend.repository.CouponRepository;
import com.demo.backend.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;

    @Override
    public Coupon create(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    @Override
    public Coupon update(Long id, Coupon data) {
        Coupon c = findById(id);

        c.setDescription(data.getDescription());
        c.setDiscountValue(data.getDiscountValue());
        c.setDiscountType(data.getDiscountType());
        c.setActive(data.isActive());
        c.setUsageLimit(data.getUsageLimit());
        c.setMinOrderAmount(data.getMinOrderAmount());
        c.setMaxDiscountAmount(data.getMaxDiscountAmount());

        return couponRepository.save(c);
    }

    @Override
    public void delete(Long id) {
        couponRepository.deleteById(id);
    }

    @Override
    public Coupon findById(Long id) {
        return couponRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Coupon not found"));
    }

    @Override
    public Coupon findByCode(String code) {
        return couponRepository.findByCode(code)
                .orElseThrow(() -> new BusinessException("Coupon code not found"));
    }

    @Override
    public List<Coupon> findAll() {
        return couponRepository.findAll();
    }
}
