package com.demo.backend.voucher.controller;

import com.demo.backend.security.SecurityUtil;
import com.demo.backend.voucher.dto.ApplyVoucherRequest;
import com.demo.backend.voucher.dto.ApplyVoucherResponse;
import com.demo.backend.voucher.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vouchers")
@RequiredArgsConstructor
public class VoucherController {

    private final VoucherService voucherService;

    @PostMapping("/apply/{code}")
    public ApplyVoucherResponse apply(
            @PathVariable String code,
            @RequestBody ApplyVoucherRequest req
    ) {
        Long userId = SecurityUtil.getUserId();
        return voucherService.apply(code, userId, req);
    }
}
