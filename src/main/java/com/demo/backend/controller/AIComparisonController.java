package com.demo.backend.controller;

import com.demo.backend.dto.compare.CompareRequestDTO;
import com.demo.backend.dto.compare.ProductComparisonDTO;
import com.demo.backend.service.ProductComparisonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AIComparisonController {

    private final ProductComparisonService comparisonService;

    @PostMapping("/compare")
    public ResponseEntity<ProductComparisonDTO> compare(@RequestBody CompareRequestDTO req) {
        ProductComparisonDTO result = comparisonService.compare(req);
        return ResponseEntity.ok(result);
    }
}
