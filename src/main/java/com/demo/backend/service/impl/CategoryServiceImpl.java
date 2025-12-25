package com.demo.backend.service.impl;

import com.demo.backend.exception.BusinessException;
import com.demo.backend.model.Category;
import com.demo.backend.repository.CategoryRepository;
import com.demo.backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Category create(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category update(Long id, Category data) {
        Category cat = categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Category not found"));

        cat.setName(data.getName());
        cat.setSlug(data.getSlug());
        cat.setDescription(data.getDescription());
        cat.setImageUrl(data.getImageUrl());
        cat.setParent(data.getParent());
        cat.setActive(data.isActive());
        cat.setDisplayOrder(data.getDisplayOrder());

        return categoryRepository.save(cat);
    }

    @Override
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Category not found"));
    }

    @Override
    public Category findBySlug(String slug) {
        return categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new BusinessException("Category slug not found"));
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }
}
