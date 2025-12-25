package com.demo.backend.voucher.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplyVoucherResponse {
    private boolean success;
    private String code;
    private String message;
    private BigDecimal discountAmount;
    private BigDecimal finalTotal;
}
