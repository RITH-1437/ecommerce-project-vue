package com.demo.backend.dto.compare;

import lombok.Data;

@Data
public class CompareRequestDTO {
    private Long productIdA;
    private Long productIdB;

    private String nameA;
    private String nameB;
}
