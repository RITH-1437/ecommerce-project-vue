package com.demo.backend.voucher.controller;

import com.demo.backend.security.SecurityUtil;
import com.demo.backend.voucher.dto.ApplyVoucherRequest;
import com.demo.backend.voucher.dto.ApplyVoucherResponse;
import com.demo.backend.voucher.model.Voucher;
import com.demo.backend.voucher.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/vouchers")
@RequiredArgsConstructor
public class VoucherController {

    private final VoucherService voucherService;

    @PostMapping("/apply/{code}")
    public ApplyVoucherResponse apply(
            @PathVariable String code,
            @RequestBody ApplyVoucherRequest req) {
        Long userId = SecurityUtil.getUserId();
        return voucherService.apply(code, userId, req);
    }

    /**
     * Get all active vouchers that are currently available for use
     * Only returns vouchers that are:
     * - Active (status = "ACTIVE")
     * - Not expired (expiry is null or in the future)
     * - Still have available uses (globalLimit is null or usedCount < globalLimit)
     */
    @GetMapping("/active")
    public List<Voucher> getActiveVouchers() {
        OffsetDateTime now = OffsetDateTime.now();
        return voucherService.getAll().stream()
                .filter(v -> "ACTIVE".equalsIgnoreCase(v.getStatus()))
                .filter(v -> v.getExpiry() == null || v.getExpiry().isAfter(now))
                .filter(v -> v.getGlobalLimit() == null || v.getUsedCount() < v.getGlobalLimit())
                .collect(Collectors.toList());
    }
}
