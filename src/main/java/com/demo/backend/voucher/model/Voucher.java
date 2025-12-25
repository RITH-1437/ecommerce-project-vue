package com.demo.backend.voucher.model;

import com.demo.backend.voucher.dto.CreateVoucherRequest;
import com.demo.backend.voucher.dto.UpdateVoucherRequest;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Locale;

@Entity
@Table(name = "voucher")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Voucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private String discountType; // PERCENT or FIXED
    private BigDecimal value;

    private BigDecimal minSpend;
    private BigDecimal maxDiscount;

    private String applicableCategories;

    private Boolean stackable;
    private Integer priority;

    private Integer userLimit;
    private Integer globalLimit;
    private Integer usedCount;

    private OffsetDateTime expiry;

    private String status;

    private String note;

    // ---------------- FACTORY ----------------

    public static Voucher from(CreateVoucherRequest req) {
        return Voucher.builder()
                .code(req.getCode().trim().toUpperCase(Locale.ROOT))
                .discountType(req.getDiscountType().toUpperCase(Locale.ROOT))
                .value(req.getValue())
                .minSpend(req.getMinSpend())
                .maxDiscount(req.getMaxDiscount())
                .applicableCategories(req.getApplicableCategories())
                .stackable(req.getStackable() != null ? req.getStackable() : false)
                .priority(req.getPriority() != null ? req.getPriority() : 0)
                .userLimit(req.getUserLimit())
                .globalLimit(req.getGlobalLimit())
                .usedCount(0)
                .expiry(req.getExpiry())
                .status("ACTIVE")
                .note(req.getNote())
                .build();
    }

    // ---------------- UPDATE ----------------

    public void update(UpdateVoucherRequest req) {

        if (req.getDiscountType() != null)
            this.discountType = req.getDiscountType().toUpperCase(Locale.ROOT);

        if (req.getValue() != null)
            this.value = req.getValue();

        if (req.getMinSpend() != null)
            this.minSpend = req.getMinSpend();

        if (req.getMaxDiscount() != null)
            this.maxDiscount = req.getMaxDiscount();

        if (req.getApplicableCategories() != null)
            this.applicableCategories = req.getApplicableCategories();

        if (req.getStackable() != null)
            this.stackable = req.getStackable();

        if (req.getPriority() != null)
            this.priority = req.getPriority();

        if (req.getUserLimit() != null)
            this.userLimit = req.getUserLimit();

        if (req.getGlobalLimit() != null)
            this.globalLimit = req.getGlobalLimit();

        if (req.getExpiry() != null)
            this.expiry = req.getExpiry();

        if (req.getStatus() != null)
            this.status = req.getStatus();

        if (req.getNote() != null)
            this.note = req.getNote();
    }

    public void incrementUsage() {
        if (usedCount == null) usedCount = 0;
        usedCount++;
    }
}
