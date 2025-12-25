package com.demo.backend.dto.request;

import lombok.Data;

@Data
public class CategoryRequestDTO {
    private String name;
    private String slug;
    private String description;
    private String imageUrl;
    private Long parentId;
    private boolean active;
    private Integer displayOrder;
}
