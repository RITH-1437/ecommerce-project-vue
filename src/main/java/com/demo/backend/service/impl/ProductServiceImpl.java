package com.demo.backend.service.impl;

import com.demo.backend.dto.FilterOptionsDTO;
import com.demo.backend.dto.ProductSearchDTO;
import com.demo.backend.exception.BusinessException;
import com.demo.backend.model.*;
import com.demo.backend.repository.CategoryRepository;
import com.demo.backend.repository.ProductColorRepository;
import com.demo.backend.repository.ProductRepository;
import com.demo.backend.repository.ProductSpecificationRepository;
import com.demo.backend.service.ProductService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.cache.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductColorRepository productColorRepository;
    private final ProductSpecificationRepository productSpecificationRepository;

    @PersistenceContext
    private EntityManager em;

    // ----------------------------------------------------
    // CREATE
    // ----------------------------------------------------
    @Override
    @Transactional
    @Caching(put = @CachePut(value = "products", key = "#result.id"), evict = {
            @CacheEvict(value = "products_all", allEntries = true),
            @CacheEvict(value = "products_by_category", allEntries = true)
    })
    public Product create(Product product) {

        if (product.getSlug() != null && productRepository.existsBySlug(product.getSlug())) {
            throw new BusinessException("Slug already exists: " + product.getSlug());
        }

        if (product.getSku() != null && productRepository.existsBySku(product.getSku())) {
            throw new BusinessException("SKU already exists: " + product.getSku());
        }

        if (product.getImages() != null)
            product.getImages().forEach(img -> img.setProduct(product));

        if (product.getColors() != null)
            product.getColors().forEach(c -> c.setProduct(product));

        if (product.getSpecifications() != null)
            product.getSpecifications().forEach(s -> s.setProduct(product));

        return productRepository.save(product);
    }

    // ----------------------------------------------------
    // UPDATE
    // ----------------------------------------------------
    @Override
    @Transactional
    @Caching(put = @CachePut(value = "products", key = "#id"), evict = {
            @CacheEvict(value = "products_all", allEntries = true),
            @CacheEvict(value = "products_by_slug", allEntries = true),
            @CacheEvict(value = "products_by_category", allEntries = true)
    })
    public Product update(Long id, Product incoming) {

        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Product not found"));

        // slug
        if (incoming.getSlug() != null && !incoming.getSlug().equals(existing.getSlug())) {
            if (productRepository.existsBySlug(incoming.getSlug()))
                throw new BusinessException("Slug already exists: " + incoming.getSlug());
            existing.setSlug(incoming.getSlug());
        }

        // sku
        if (incoming.getSku() != null && !incoming.getSku().equals(existing.getSku())) {
            if (productRepository.existsBySku(incoming.getSku()))
                throw new BusinessException("SKU already exists: " + incoming.getSku());
            existing.setSku(incoming.getSku());
        }

        // Scalars
        if (incoming.getName() != null)
            existing.setName(incoming.getName());
        if (incoming.getCategory() != null)
            existing.setCategory(incoming.getCategory());
        if (incoming.getDescription() != null)
            existing.setDescription(incoming.getDescription());
        if (incoming.getShortDescription() != null)
            existing.setShortDescription(incoming.getShortDescription());
        if (incoming.getPrice() != null)
            existing.setPrice(incoming.getPrice());
        if (incoming.getOriginalPrice() != null)
            existing.setOriginalPrice(incoming.getOriginalPrice());
        if (incoming.getStock() != null)
            existing.setStock(incoming.getStock());
        if (incoming.getMinStock() != null)
            existing.setMinStock(incoming.getMinStock());
        if (incoming.getBadge() != null)
            existing.setBadge(incoming.getBadge());
        if (incoming.getImageUrl() != null)
            existing.setImageUrl(incoming.getImageUrl());

        existing.setActive(incoming.isActive());

        // Children
        reconcileImages(existing, incoming.getImages());
        reconcileColors(existing, incoming.getColors());
        reconcileSpecs(existing, incoming.getSpecifications());

        return productRepository.save(existing);
    }

    // ----------------------------------------------------
    // DELETE
    // ----------------------------------------------------
    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "products", key = "#id"),
            @CacheEvict(value = "products_all", allEntries = true),
            @CacheEvict(value = "products_by_slug", allEntries = true),
            @CacheEvict(value = "products_by_category", allEntries = true)
    })
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    // ----------------------------------------------------
    // FIND
    // ----------------------------------------------------
    @Override
    @Cacheable(value = "products", key = "#id")
    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Product not found"));
    }

    @Override
    @Cacheable(value = "products_by_slug", key = "#slug")
    public Product findBySlug(String slug) {
        return productRepository.findBySlug(slug)
                .orElseThrow(() -> new BusinessException("Product slug not found"));
    }

    @Override
    @Cacheable(value = "products_by_category", key = "#categoryId")
    public List<Product> findByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    @Override
    @Cacheable(value = "products_all")
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    // ------------------------------------------------------------
    // DYNAMIC SEARCH (Criteria API)
    // ------------------------------------------------------------
    @Override
    public Page<Product> search(
            String keyword,
            Long categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Integer minStock,
            Pageable pageable) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Product> cq = cb.createQuery(Product.class);
        Root<Product> root = cq.from(Product.class);

        root.fetch("category", JoinType.LEFT);
        cq.select(root).distinct(true);

        List<Predicate> predicates = new ArrayList<>();

        if (keyword != null && !keyword.isBlank()) {
            String like = "%" + keyword.toLowerCase() + "%";
            predicates.add(cb.or(
                    cb.like(cb.lower(root.get("name")), like),
                    cb.like(cb.lower(root.get("description")), like),
                    cb.like(cb.lower(root.get("shortDescription")), like)));
        }

        if (categoryId != null) {
            predicates.add(cb.equal(root.get("category").get("id"), categoryId));
        }

        if (minPrice != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
        }
        if (maxPrice != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));
        }

        if (minStock != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("stock"), minStock));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        // SORTING FIX (FULLY QUALIFIED ORDER)
        if (pageable.getSort() != null) {
            List<jakarta.persistence.criteria.Order> orders = new ArrayList<>();

            for (Sort.Order so : pageable.getSort()) {
                Path<?> path = root.get(so.getProperty());

                jakarta.persistence.criteria.Order critOrder = so.isAscending() ? cb.asc(path) : cb.desc(path);

                orders.add(critOrder);
            }

            cq.orderBy(orders);
        }

        TypedQuery<Product> query = em.createQuery(cq);

        // Pagination
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        query.setFirstResult(pageNumber * pageSize);
        query.setMaxResults(pageSize);

        List<Product> content = query.getResultList();

        // Count query
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Product> countRoot = countQuery.from(Product.class);
        countQuery.select(cb.countDistinct(countRoot));

        List<Predicate> countPredicates = new ArrayList<>();

        if (keyword != null && !keyword.isBlank()) {
            String like = "%" + keyword.toLowerCase() + "%";
            countPredicates.add(cb.or(
                    cb.like(cb.lower(countRoot.get("name")), like),
                    cb.like(cb.lower(countRoot.get("description")), like),
                    cb.like(cb.lower(countRoot.get("shortDescription")), like)));
        }

        if (categoryId != null)
            countPredicates.add(cb.equal(countRoot.get("category").get("id"), categoryId));

        if (minPrice != null)
            countPredicates.add(cb.greaterThanOrEqualTo(countRoot.get("price"), minPrice));

        if (maxPrice != null)
            countPredicates.add(cb.lessThanOrEqualTo(countRoot.get("price"), maxPrice));

        if (minStock != null)
            countPredicates.add(cb.greaterThanOrEqualTo(countRoot.get("stock"), minStock));

        countQuery.where(countPredicates.toArray(new Predicate[0]));
        long total = em.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(content, pageable, total);
    }

    // ------------------------------------------------------------
    // CHILD LIST RECONCILIATION HELPERS
    // ------------------------------------------------------------

    private void reconcileImages(Product existing, List<ProductImage> incoming) {
        if (incoming == null)
            return;

        Map<Long, ProductImage> existingById = existing.getImages().stream()
                .filter(i -> i.getId() != null)
                .collect(Collectors.toMap(ProductImage::getId, i -> i));

        List<ProductImage> result = new ArrayList<>();

        for (ProductImage in : incoming) {
            if (in.getId() != null && existingById.containsKey(in.getId())) {
                ProductImage ex = existingById.get(in.getId());
                ex.setImageUrl(in.getImageUrl());
                ex.setAltText(in.getAltText());
                ex.setPrimary(in.isPrimary());
                ex.setDisplayOrder(in.getDisplayOrder());
                result.add(ex);
            } else {
                ProductImage newImg = ProductImage.builder()
                        .imageUrl(in.getImageUrl())
                        .altText(in.getAltText())
                        .isPrimary(in.isPrimary())
                        .displayOrder(in.getDisplayOrder())
                        .product(existing)
                        .build();
                result.add(newImg);
            }
        }

        existing.getImages().clear();
        existing.getImages().addAll(result);
    }

    private void reconcileColors(Product existing, List<ProductColor> incoming) {
        if (incoming == null)
            return;

        Map<Long, ProductColor> existingById = existing.getColors().stream()
                .filter(c -> c.getId() != null)
                .collect(Collectors.toMap(ProductColor::getId, c -> c));

        List<ProductColor> result = new ArrayList<>();

        for (ProductColor in : incoming) {
            if (in.getId() != null && existingById.containsKey(in.getId())) {
                ProductColor ex = existingById.get(in.getId());
                ex.setColorName(in.getColorName());
                ex.setColorHex(in.getColorHex());
                ex.setStock(in.getStock());
                ex.setDisplayOrder(in.getDisplayOrder());
                ex.setAvailable(in.isAvailable());
                result.add(ex);
            } else {
                ProductColor newColor = ProductColor.builder()
                        .colorName(in.getColorName())
                        .colorHex(in.getColorHex())
                        .stock(in.getStock())
                        .displayOrder(in.getDisplayOrder())
                        .isAvailable(in.isAvailable())
                        .product(existing)
                        .build();
                result.add(newColor);
            }
        }

        existing.getColors().clear();
        existing.getColors().addAll(result);
    }

    private void reconcileSpecs(Product existing, List<ProductSpecification> incoming) {
        if (incoming == null)
            return;

        Map<Long, ProductSpecification> existingById = existing.getSpecifications().stream()
                .filter(s -> s.getId() != null)
                .collect(Collectors.toMap(ProductSpecification::getId, s -> s));

        List<ProductSpecification> result = new ArrayList<>();

        for (ProductSpecification in : incoming) {
            if (in.getId() != null && existingById.containsKey(in.getId())) {
                ProductSpecification ex = existingById.get(in.getId());
                ex.setSpecKey(in.getSpecKey());
                ex.setSpecValue(in.getSpecValue());
                ex.setDisplayOrder(in.getDisplayOrder());
                result.add(ex);
            } else {
                ProductSpecification newSpec = ProductSpecification.builder()
                        .specKey(in.getSpecKey())
                        .specValue(in.getSpecValue())
                        .displayOrder(in.getDisplayOrder())
                        .product(existing)
                        .build();
                result.add(newSpec);
            }
        }

        existing.getSpecifications().clear();
        existing.getSpecifications().addAll(result);
    }

    // ------------------------------------------------------------
    // ADVANCED SEARCH & FILTERS
    // ------------------------------------------------------------

    @Override
    public Page<Product> advancedSearch(ProductSearchDTO searchDTO) {
        // Build sort
        Sort sort = buildSort(searchDTO.getSortBy());
        Pageable pageable = PageRequest.of(
                searchDTO.getPage(),
                searchDTO.getSize(),
                sort);

        // Use the repository method with filters
        return productRepository.searchProducts(
                searchDTO.getKeyword(),
                searchDTO.getCategoryId(),
                searchDTO.getMinPrice(),
                searchDTO.getMaxPrice(),
                searchDTO.getMinRating(),
                searchDTO.getInStock(),
                pageable);
    }

    @Override
    public List<String> getSearchSuggestions(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return Collections.emptyList();
        }

        Pageable limit = PageRequest.of(0, 10);
        return productRepository.findProductNameSuggestions(keyword.trim(), limit);
    }

    @Override
    public FilterOptionsDTO getFilterOptions() {
        FilterOptionsDTO options = new FilterOptionsDTO();

        // Get price range
        BigDecimal minPrice = productRepository.findMinPrice();
        BigDecimal maxPrice = productRepository.findMaxPrice();
        options.setPriceRange(new FilterOptionsDTO.PriceRange(
                minPrice != null ? minPrice : BigDecimal.ZERO,
                maxPrice != null ? maxPrice : BigDecimal.valueOf(10000)));

        // Get available colors
        List<String> colors = productColorRepository.findAll().stream()
                .map(ProductColor::getColorName)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        options.setAvailableColors(colors);

        // Get available storage options from specifications
        List<String> storage = productSpecificationRepository.findAll().stream()
                .filter(spec -> "Storage".equalsIgnoreCase(spec.getSpecKey()) ||
                        "Capacity".equalsIgnoreCase(spec.getSpecKey()))
                .map(ProductSpecification::getSpecValue)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        options.setAvailableStorage(storage);

        // Get categories with product counts
        List<FilterOptionsDTO.CategoryOption> categories = categoryRepository.findAll().stream()
                .map(cat -> {
                    long count = productRepository.findByCategoryId(cat.getId()).size();
                    return new FilterOptionsDTO.CategoryOption(
                            cat.getId(),
                            cat.getName(),
                            count);
                })
                .filter(cat -> cat.getProductCount() > 0)
                .collect(Collectors.toList());
        options.setCategories(categories);

        return options;
    }

    @Override
    public Page<Product> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        return productRepository.findByIsActiveTrueAndPriceBetween(minPrice, maxPrice, pageable);
    }

    @Override
    public Page<Product> findByMinRating(BigDecimal minRating, Pageable pageable) {
        return productRepository.findByIsActiveTrueAndRatingGreaterThanEqual(minRating, pageable);
    }

    @Override
    public Page<Product> findInStockProducts(Pageable pageable) {
        return productRepository.findByIsActiveTrueAndStockGreaterThan(0, pageable);
    }

    // Helper method to build sort
    private Sort buildSort(String sortBy) {
        if (sortBy == null || sortBy.isEmpty()) {
            return Sort.by(Sort.Direction.DESC, "createdAt");
        }

        return switch (sortBy.toLowerCase()) {
            case "price_asc" -> Sort.by(Sort.Direction.ASC, "price");
            case "price_desc" -> Sort.by(Sort.Direction.DESC, "price");
            case "rating" -> Sort.by(Sort.Direction.DESC, "rating");
            case "popular" -> Sort.by(Sort.Direction.DESC, "reviewsCount");
            case "newest" -> Sort.by(Sort.Direction.DESC, "createdAt");
            default -> Sort.by(Sort.Direction.DESC, "createdAt");
        };
    }
}
