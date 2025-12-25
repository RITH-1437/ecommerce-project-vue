package com.demo.backend.voucher.service;

import com.demo.backend.voucher.dto.ApplyVoucherRequest;
import com.demo.backend.voucher.dto.ApplyVoucherResponse;
import com.demo.backend.voucher.dto.CreateVoucherRequest;
import com.demo.backend.voucher.dto.UpdateVoucherRequest;
import com.demo.backend.voucher.model.UserVoucher;
import com.demo.backend.voucher.model.Voucher;

import java.util.List;

public interface VoucherService {

    Voucher create(CreateVoucherRequest req);

    Voucher update(Long id, UpdateVoucherRequest req);

    void delete(Long id);

    Voucher getById(Long id);

    Voucher getByCode(String code);

    List<Voucher> getAll();

    ApplyVoucherResponse apply(String code, Long userId, ApplyVoucherRequest req);

    // ✅ Only these TWO methods (NOT THREE)
    List<UserVoucher> getUsageHistory();

    List<UserVoucher> getUserHistory(Long userId);

}
