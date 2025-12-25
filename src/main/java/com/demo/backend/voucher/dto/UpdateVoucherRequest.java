package com.demo.backend.voucher.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateVoucherRequest {

    private String discountType;
    private BigDecimal value;

    private BigDecimal minSpend;
    private BigDecimal maxDiscount;

    private String applicableCategories;

    private Boolean stackable;
    private Integer priority;

    private Integer userLimit;
    private Integer globalLimit;

    private OffsetDateTime expiry;

    private String status;  // ACTIVE, INACTIVE

    private String note;
}
