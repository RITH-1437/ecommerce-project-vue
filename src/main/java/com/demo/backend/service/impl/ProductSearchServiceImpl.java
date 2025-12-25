package com.demo.backend.service.impl;

import com.demo.backend.model.Product;
import com.demo.backend.repository.ProductRepository;
import com.demo.backend.service.ProductSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductSearchServiceImpl implements ProductSearchService {

    private final ProductRepository productRepository;

    @Override
    public List<Product> searchRelevantProducts(String query) {
        return productRepository.searchByNameOrDesc(query.toLowerCase());
    }

    @Override
    public List<Product> compareProducts(String a, String b) {
        return productRepository.findByNames(a.toLowerCase(), b.toLowerCase());
    }
}
