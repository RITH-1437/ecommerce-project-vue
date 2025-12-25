package com.demo.backend.dto.compare;

import com.demo.backend.dto.response.ProductResponseDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductComparisonDTO {
    private ProductResponseDTO productA;
    private ProductResponseDTO productB;
    private List<SpecComparisonDTO> specs;

    private String summary;
    private String recommendation;
}
