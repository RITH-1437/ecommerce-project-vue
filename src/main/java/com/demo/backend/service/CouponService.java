package com.demo.backend.service;

import com.demo.backend.model.Coupon;
import java.util.List;

public interface CouponService {

    Coupon create(Coupon coupon);

    Coupon update(Long id, Coupon coupon);

    void delete(Long id);

    Coupon findById(Long id);

    Coupon findByCode(String code);

    List<Coupon> findAll();
}
