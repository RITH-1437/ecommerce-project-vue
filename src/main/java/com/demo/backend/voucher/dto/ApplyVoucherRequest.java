package com.demo.backend.voucher.dto;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplyVoucherRequest {
    private BigDecimal cartTotal;
    private List<String> cartCategories;
}
