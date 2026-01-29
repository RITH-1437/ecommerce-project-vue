package com.demo.backend.controller;

import com.demo.backend.dto.FilterOptionsDTO;
import com.demo.backend.dto.ProductSearchDTO;
import com.demo.backend.model.Product;
import com.demo.backend.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
@Tag(name = "Search & Filters", description = "Advanced product search and filtering APIs")
@CrossOrigin(origins = "*", maxAge = 3600)
public class SearchController {

    private final ProductService productService;

    @PostMapping("/products")
    @Operation(summary = "Advanced product search with filters")
    public ResponseEntity<Page<Product>> searchProducts(@RequestBody ProductSearchDTO searchDTO) {
        Page<Product> results = productService.advancedSearch(searchDTO);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/suggestions")
    @Operation(summary = "Get search autocomplete suggestions")
    public ResponseEntity<List<String>> getSearchSuggestions(
            @RequestParam String keyword) {

        List<String> suggestions = productService.getSearchSuggestions(keyword);
        return ResponseEntity.ok(suggestions);
    }

    @GetMapping("/filter-options")
    @Operation(summary = "Get available filter options (price range, colors, storage, categories)")
    public ResponseEntity<FilterOptionsDTO> getFilterOptions() {
        FilterOptionsDTO options = productService.getFilterOptions();
        return ResponseEntity.ok(options);
    }
}
