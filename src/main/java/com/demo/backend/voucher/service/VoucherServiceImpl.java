package com.demo.backend.voucher.service;

import com.demo.backend.voucher.dto.ApplyVoucherRequest;
import com.demo.backend.voucher.dto.ApplyVoucherResponse;
import com.demo.backend.voucher.dto.CreateVoucherRequest;
import com.demo.backend.voucher.dto.UpdateVoucherRequest;
import com.demo.backend.voucher.exception.VoucherException;
import com.demo.backend.voucher.model.UserVoucher;
import com.demo.backend.voucher.model.Voucher;
import com.demo.backend.voucher.repository.UserVoucherRepository;
import com.demo.backend.voucher.repository.VoucherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VoucherServiceImpl implements VoucherService {

    private final VoucherRepository voucherRepository;
    private final UserVoucherRepository userVoucherRepository;
    private final DiscountCalculator discountCalculator;

    @Override
    public Voucher create(CreateVoucherRequest req) {
        Voucher v = Voucher.from(req);
        return voucherRepository.save(v);
    }

    @Override
    public Voucher update(Long id, UpdateVoucherRequest req) {
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new VoucherException("Voucher not found"));
        voucher.update(req);
        return voucherRepository.save(voucher);
    }

    @Override
    public void delete(Long id) {
        voucherRepository.deleteById(id);
    }

    @Override
    public Voucher getById(Long id) {
        return voucherRepository.findById(id)
                .orElseThrow(() -> new VoucherException("Voucher not found"));
    }

    @Override
    public Voucher getByCode(String code) {
        return voucherRepository.findByCodeIgnoreCase(code)
                .orElseThrow(() -> new VoucherException("Invalid voucher code"));
    }

    @Override
    public List<UserVoucher> getUsageHistory() {
        return userVoucherRepository.findAllWithVoucher();
    }

    @Override
    public List<UserVoucher> getUserHistory(Long userId) {
        return userVoucherRepository.findHistoryByUserId(userId);
    }



    @Override
    public List<Voucher> getAll() {
        return voucherRepository.findAll();
    }

    // ⭐ REQUIRED METHOD — THIS FIXES YOUR ERROR
    @Override
    @Transactional
    public ApplyVoucherResponse apply(String code, Long userId, ApplyVoucherRequest req) {

        Voucher voucher = getByCode(code);

        // Calculate discount
        BigDecimal discount = discountCalculator.calculateDiscount(voucher, req, userId);

        BigDecimal finalTotal = req.getCartTotal().subtract(discount);
        if (finalTotal.compareTo(BigDecimal.ZERO) < 0)
            finalTotal = BigDecimal.ZERO;

        // Save user usage history
        UserVoucher uv = UserVoucher.builder()
                .userId(userId)
                .voucher(voucher)
                .usedAt(OffsetDateTime.now())
                .build();

        userVoucherRepository.save(uv);

        // Increment usage count
        voucher.incrementUsage();
        voucherRepository.save(voucher);

        return ApplyVoucherResponse.builder()
                .success(true)
                .code(code)
                .message("Voucher applied")
                .discountAmount(discount)
                .finalTotal(finalTotal)
                .build();
    }
}
