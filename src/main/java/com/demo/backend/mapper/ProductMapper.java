package com.demo.backend.mapper;

import com.demo.backend.dto.request.*;
import com.demo.backend.dto.response.ProductResponseDTO;
import com.demo.backend.model.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    // DTO -> ENTITY
    public Product toEntity(ProductRequestDTO dto) {
        if (dto == null) return null;
        Product p = new Product();

        p.setName(dto.getName());
        p.setSlug(dto.getSlug());
        p.setSku(dto.getSku());
        p.setDescription(dto.getDescription());
        p.setShortDescription(dto.getShortDescription());
        p.setPrice(dto.getPrice());
        p.setOriginalPrice(dto.getOriginalPrice());
        p.setStock(dto.getStock());
        p.setMinStock(dto.getMinStock());
        p.setBadge(dto.getBadge());
        p.setActive(dto.getActive() != null ? dto.getActive() : p.isActive());
        p.setImageUrl(dto.getImageUrl());

        // map children (ids preserved)
        if (dto.getImages() != null) {
            p.setImages(mapImagesFromDto(dto));
        }
        if (dto.getColors() != null) {
            p.setColors(mapColorsFromDto(dto));
        }
        if (dto.getSpecifications() != null) {
            p.setSpecifications(mapSpecsFromDto(dto));
        }

        return p;
    }

    // small helpers to allow patch to pass lists
    public List<ProductImage> mapImagesFromDto(ProductRequestDTO dto) {
        return dto.getImages().stream()
                .map(ProductImageRequestDTO::toEntity)
                .collect(Collectors.toList());
    }

    public List<ProductColor> mapColorsFromDto(ProductRequestDTO dto) {
        return dto.getColors().stream()
                .map(ProductColorRequestDTO::toEntity)
                .collect(Collectors.toList());
    }

    public List<ProductSpecification> mapSpecsFromDto(ProductRequestDTO dto) {
        return dto.getSpecifications().stream()
                .map(ProductSpecRequestDTO::toEntity)
                .collect(Collectors.toList());
    }

    // ENTITY -> DTO
    public ProductResponseDTO toResponse(Product p) {
        if (p == null) return null;

        ProductResponseDTO dto = new ProductResponseDTO();

        dto.setId(p.getId());
        dto.setName(p.getName());
        dto.setSlug(p.getSlug());
        dto.setSku(p.getSku());
        dto.setDescription(p.getDescription());
        dto.setShortDescription(p.getShortDescription());
        dto.setPrice(p.getPrice());
        dto.setOriginalPrice(p.getOriginalPrice());
        dto.setImageUrl(p.getImageUrl());
        dto.setStock(p.getStock());
        dto.setMinStock(p.getMinStock());
        dto.setBadge(p.getBadge());
        dto.setRating(p.getRating() != null ? p.getRating().doubleValue() : 0.0);
        dto.setReviewsCount(p.getReviewsCount());

        if (p.getCategory() != null) dto.setCategoryName(p.getCategory().getName());

        // you can add mapping of images/colors/specs to response DTOs if needed
        return dto;
    }
}
