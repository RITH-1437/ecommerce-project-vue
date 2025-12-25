package com.demo.backend.controller;

import com.demo.backend.model.Coupon;
import com.demo.backend.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PostMapping
    public ResponseEntity<Coupon> create(@RequestBody Coupon coupon) {
        return ResponseEntity.ok(couponService.create(coupon));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Coupon> update(@PathVariable Long id, @RequestBody Coupon data) {
        return ResponseEntity.ok(couponService.update(id, data));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Coupon> findById(@PathVariable Long id) {
        return ResponseEntity.ok(couponService.findById(id));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<Coupon> findByCode(@PathVariable String code) {
        return ResponseEntity.ok(couponService.findByCode(code));
    }

    @GetMapping
    public ResponseEntity<List<Coupon>> all() {
        return ResponseEntity.ok(couponService.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        couponService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
