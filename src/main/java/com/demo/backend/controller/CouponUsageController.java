package com.demo.backend.controller;

import com.demo.backend.model.CouponUsage;
import com.demo.backend.service.CouponUsageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coupon-usage")
@RequiredArgsConstructor
public class CouponUsageController {

    private final CouponUsageService usageService;

    @PostMapping
    public ResponseEntity<CouponUsage> create(@RequestBody CouponUsage usage) {
        return ResponseEntity.ok(usageService.create(usage));
    }

    @GetMapping("/coupon/{id}")
    public ResponseEntity<List<CouponUsage>> findByCoupon(@PathVariable Long id) {
        return ResponseEntity.ok(usageService.findByCouponId(id));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<CouponUsage>> findByUser(@PathVariable Long id) {
        return ResponseEntity.ok(usageService.findByUserId(id));
    }

    @GetMapping("/order/{id}")
    public ResponseEntity<List<CouponUsage>> findByOrder(@PathVariable Long id) {
        return ResponseEntity.ok(usageService.findByOrderId(id));
    }
}
