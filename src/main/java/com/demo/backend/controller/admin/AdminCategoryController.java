package com.demo.backend.controller.admin;

import com.demo.backend.dto.request.CategoryRequestDTO;
import com.demo.backend.dto.response.CategoryResponseDTO;
import com.demo.backend.model.Category;
import com.demo.backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final CategoryService categoryService;

    // ---------------------------------------------------
    // Helper method to convert Entity -> ResponseDTO
    // ---------------------------------------------------
    private CategoryResponseDTO toDto(Category c) {
        CategoryResponseDTO dto = new CategoryResponseDTO();
        dto.setId(c.getId());
        dto.setName(c.getName());
        dto.setSlug(c.getSlug());
        dto.setDescription(c.getDescription());
        dto.setImageUrl(c.getImageUrl());
        dto.setParentId(c.getParent() != null ? c.getParent().getId() : null);
        dto.setActive(c.isActive());
        dto.setDisplayOrder(c.getDisplayOrder());
        return dto;
    }

    // ---------------------------------------------------
    // Helper method to convert RequestDTO -> Entity
    // ---------------------------------------------------
    private Category toEntity(CategoryRequestDTO dto) {
        Category c = new Category();
        c.setName(dto.getName());
        c.setSlug(dto.getSlug());
        c.setDescription(dto.getDescription());
        c.setImageUrl(dto.getImageUrl());
        c.setActive(dto.isActive());
        c.setDisplayOrder(dto.getDisplayOrder());
        return c;
    }

    // ---------------------------------------------------
    // Create category (ADMIN)
    // ---------------------------------------------------
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CategoryResponseDTO> create(@RequestBody CategoryRequestDTO dto) {

        Category category = toEntity(dto);

        // If parentId exists, set parent
        if (dto.getParentId() != null) {
            Category parent = categoryService.findById(dto.getParentId());
            category.setParent(parent);
        }

        Category saved = categoryService.create(category);
        return ResponseEntity.ok(toDto(saved));
    }

    // ---------------------------------------------------
    // Update category (ADMIN)
    // ---------------------------------------------------
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> update(
            @PathVariable Long id,
            @RequestBody CategoryRequestDTO dto) {

        Category data = toEntity(dto);

        if (dto.getParentId() != null) {
            Category parent = categoryService.findById(dto.getParentId());
            data.setParent(parent);
        }

        Category updated = categoryService.update(id, data);
        return ResponseEntity.ok(toDto(updated));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> patchUpdate(
            @PathVariable Long id,
            @RequestBody CategoryRequestDTO dto) {

        Category existing = categoryService.findById(id);

        // Only update fields that are NOT null
        if (dto.getName() != null) existing.setName(dto.getName());
        if (dto.getSlug() != null) existing.setSlug(dto.getSlug());
        if (dto.getDescription() != null) existing.setDescription(dto.getDescription());
        if (dto.getImageUrl() != null) existing.setImageUrl(dto.getImageUrl());
        if (dto.getDisplayOrder() != null) existing.setDisplayOrder(dto.getDisplayOrder());

        // Boolean must be checked carefully
        // if active is sent → update it
        existing.setActive(dto.isActive());

        // Parent category update
        if (dto.getParentId() != null) {
            Category parent = categoryService.findById(dto.getParentId());
            existing.setParent(parent);
        }

        Category saved = categoryService.create(existing);
        return ResponseEntity.ok(toDto(saved));
    }



    // ---------------------------------------------------
    // Delete category (ADMIN)
    // ---------------------------------------------------
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ---------------------------------------------------
    // Get category by ID (ADMIN)
    // ---------------------------------------------------
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getById(@PathVariable Long id) {
        Category cat = categoryService.findById(id);
        return ResponseEntity.ok(toDto(cat));
    }

    // ---------------------------------------------------
    // Get ALL categories (ADMIN)
    // ---------------------------------------------------
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAll() {
        List<Category> list = categoryService.findAll();

        List<CategoryResponseDTO> result = list.stream()
                .map(this::toDto)
                .toList();

        return ResponseEntity.ok(result);
    }
}
