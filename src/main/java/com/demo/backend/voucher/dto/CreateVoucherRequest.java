package com.demo.backend.voucher.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateVoucherRequest {

    private String code;
    private String discountType;   // PERCENT or FIXED
    private BigDecimal value;

    private BigDecimal minSpend;
    private BigDecimal maxDiscount;

    private String applicableCategories; // "IPHONE,MACBOOK"

    private Boolean stackable;
    private Integer priority;

    private Integer userLimit;
    private Integer globalLimit;

    private OffsetDateTime expiry;

    private String note;
}
