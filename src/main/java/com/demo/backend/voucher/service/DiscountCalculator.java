package com.demo.backend.voucher.service;

import com.demo.backend.voucher.dto.ApplyVoucherRequest;
import com.demo.backend.voucher.exception.VoucherException;
import com.demo.backend.voucher.model.Voucher;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DiscountCalculator {

    public BigDecimal calculateDiscount(Voucher voucher, ApplyVoucherRequest req, Long userId) {

        BigDecimal total = req.getCartTotal();

        if (voucher.getDiscountType().equalsIgnoreCase("PERCENT")) {
            BigDecimal discount = total.multiply(voucher.getValue())
                    .divide(BigDecimal.valueOf(100));

            if (voucher.getMaxDiscount() != null &&
                    discount.compareTo(voucher.getMaxDiscount()) > 0) {
                discount = voucher.getMaxDiscount();
            }

            return discount;
        }

        if (voucher.getDiscountType().equalsIgnoreCase("FIXED")) {
            return voucher.getValue();
        }

        throw new VoucherException("Unsupported discount type");
    }
}
