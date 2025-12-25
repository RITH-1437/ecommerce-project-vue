package com.demo.backend.service;

import com.demo.backend.model.Category;
import java.util.List;

public interface CategoryService {

    Category create(Category category);

    Category update(Long id, Category category);

    void delete(Long id);

    Category findById(Long id);

    Category findBySlug(String slug);

    List<Category> findAll();
}
