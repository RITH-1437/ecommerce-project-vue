package com.demo.backend.voucher.controller;

import com.demo.backend.voucher.dto.CreateVoucherRequest;
import com.demo.backend.voucher.dto.UpdateVoucherRequest;
import com.demo.backend.voucher.model.Voucher;
import com.demo.backend.voucher.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/vouchers")
@RequiredArgsConstructor
public class VoucherAdminController {

    private final VoucherService voucherService;

    // CREATE
    @PostMapping
    public ResponseEntity<Voucher> createVoucher(@RequestBody CreateVoucherRequest req) {
        Voucher created = voucherService.create(req);
        return ResponseEntity.ok(created);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Voucher> updateVoucher(
            @PathVariable Long id,
            @RequestBody UpdateVoucherRequest req
    ) {
        Voucher updated = voucherService.update(id, req);
        return ResponseEntity.ok(updated);
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<Voucher>> getAllVouchers() {
        return ResponseEntity.ok(voucherService.getAll());
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Voucher> getVoucher(@PathVariable Long id) {
        return ResponseEntity.ok(voucherService.getById(id));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVoucher(@PathVariable Long id) {
        voucherService.delete(id);
        return ResponseEntity.ok("Voucher deleted successfully.");
    }
}
