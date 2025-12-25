package com.demo.backend.dto.ai;

import com.demo.backend.dto.response.ProductResponseDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AIChatResponse {

    private String reply;

    private List<ProductResponseDTO> recommendations;
}
