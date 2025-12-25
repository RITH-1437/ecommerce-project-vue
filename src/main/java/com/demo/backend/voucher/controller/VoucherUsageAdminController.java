package com.demo.backend.voucher.controller;

import com.demo.backend.voucher.model.UserVoucher;
import com.demo.backend.voucher.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/vouchers/usage")
@RequiredArgsConstructor
public class VoucherUsageAdminController {

    private final VoucherService voucherService;

    // All usage
    @GetMapping
    public List<UserVoucher> getAllUsage() {
        return voucherService.getUsageHistory();
    }

    // Usage for a specific user
    @GetMapping("/user/{userId}")
    public List<UserVoucher> getUserUsage(@PathVariable Long userId) {
        return voucherService.getUserHistory(userId);
    }
}
