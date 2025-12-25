package com.demo.backend.service;

import com.demo.backend.model.Product;

import java.util.List;

public interface ProductSearchService {
    List<Product> searchRelevantProducts(String query);
    List<Product> compareProducts(String a, String b);
}
