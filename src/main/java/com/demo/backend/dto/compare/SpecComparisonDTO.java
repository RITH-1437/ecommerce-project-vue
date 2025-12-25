package com.demo.backend.dto.compare;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SpecComparisonDTO {
    private String key;
    private String valueA;
    private String valueB;
    private String winner;   // A, B, Equal, TBD
    private String note;
}
