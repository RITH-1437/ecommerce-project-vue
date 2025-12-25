package com.demo.backend.controller;

import com.demo.backend.dto.request.ProductRequestDTO;
import com.demo.backend.dto.response.ProductResponseDTO;
import com.demo.backend.mapper.ProductMapper;
import com.demo.backend.model.Category;
import com.demo.backend.model.Product;
import com.demo.backend.service.CategoryService;
import com.demo.backend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final ProductMapper mapper;

    // GET ALL WITH PAGINATION
    @GetMapping
    public Page<com.demo.backend.dto.response.ProductResponseDTO> getAll(Pageable pageable) {
        return productService.findAll(pageable)
                .map(mapper::toResponse);
    }

    // GET BY ID
    @GetMapping("/{id}")
    public com.demo.backend.dto.response.ProductResponseDTO getById(@PathVariable Long id) {
        return mapper.toResponse(productService.findById(id));
    }

    // GET BY SLUG (public)
    @GetMapping("/slug/{slug}")
    public com.demo.backend.dto.response.ProductResponseDTO getBySlug(@PathVariable String slug) {
        return mapper.toResponse(productService.findBySlug(slug));
    }

    // GET BY CATEGORY
    @GetMapping("/category/{categoryId}")
    public List<com.demo.backend.dto.response.ProductResponseDTO> getByCategory(@PathVariable Long categoryId) {
        return productService.findByCategory(categoryId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    // CREATE PRODUCT (ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public com.demo.backend.dto.response.ProductResponseDTO create(@RequestBody ProductRequestDTO dto) {

        // map dto → entity
        Product product = mapper.toEntity(dto);

        // attach category (throws if not found)
        if (dto.getCategoryId() != null) {
            Category category = categoryService.findById(dto.getCategoryId());
            product.setCategory(category);
        }

        // save
        Product saved = productService.create(product);

        return mapper.toResponse(saved);
    }

    // UPDATE PRODUCT (ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public com.demo.backend.dto.response.ProductResponseDTO update(
            @PathVariable Long id,
            @RequestBody ProductRequestDTO dto) {

        Product incoming = mapper.toEntity(dto);

        if (dto.getCategoryId() != null) {
            Category category = categoryService.findById(dto.getCategoryId());
            incoming.setCategory(category);
        }

        Product updated = productService.update(id, incoming);

        return mapper.toResponse(updated);
    }

    // PARTIAL UPDATE (PATCH)
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public com.demo.backend.dto.response.ProductResponseDTO patch(
            @PathVariable Long id,
            @RequestBody ProductRequestDTO dto) {

        Product existing = productService.findById(id);

        if (dto.getName() != null) existing.setName(dto.getName());
        if (dto.getSlug() != null) existing.setSlug(dto.getSlug());
        if (dto.getSku() != null) existing.setSku(dto.getSku());
        if (dto.getDescription() != null) existing.setDescription(dto.getDescription());
        if (dto.getShortDescription() != null) existing.setShortDescription(dto.getShortDescription());
        if (dto.getPrice() != null) existing.setPrice(dto.getPrice());
        if (dto.getOriginalPrice() != null) existing.setOriginalPrice(dto.getOriginalPrice());
        if (dto.getStock() != null) existing.setStock(dto.getStock());
        if (dto.getMinStock() != null) existing.setMinStock(dto.getMinStock());
        if (dto.getBadge() != null) existing.setBadge(dto.getBadge());
        if (dto.getImageUrl() != null) existing.setImageUrl(dto.getImageUrl());

        if (dto.getCategoryId() != null) {
            Category cat = categoryService.findById(dto.getCategoryId());
            existing.setCategory(cat);
        }

        // Only update active if provided in DTO (use Boolean active)
        if (dto.getActive() != null) {
            existing.setActive(dto.getActive());
        }

        // If child lists present in PATCH, we map them and let service reconcile them.
        if (dto.getImages() != null) existing.setImages(mapper.mapImagesFromDto(dto));
        if (dto.getColors() != null) existing.setColors(mapper.mapColorsFromDto(dto));
        if (dto.getSpecifications() != null) existing.setSpecifications(mapper.mapSpecsFromDto(dto));

        Product updated = productService.update(id, existing);
        return mapper.toResponse(updated);
    }

    // DELETE PRODUCT (ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        productService.delete(id);
    }
}
