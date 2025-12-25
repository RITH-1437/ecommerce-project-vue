package com.demo.backend.service.impl;

import com.demo.backend.dto.compare.*;
import com.demo.backend.dto.response.ProductResponseDTO;
import com.demo.backend.mapper.ProductMapper;
import com.demo.backend.model.Product;
import com.demo.backend.model.ProductSpecification;
import com.demo.backend.repository.ProductRepository;
import com.demo.backend.repository.ProductSpecificationRepository;
import com.demo.backend.service.OpenAIService;
import com.demo.backend.service.ProductComparisonService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductComparisonServiceImpl implements ProductComparisonService {

    private static final Logger logger = LoggerFactory.getLogger(ProductComparisonServiceImpl.class);

    private final ProductRepository productRepository;
    private final ProductSpecificationRepository specRepository;
    private final ProductMapper productMapper;
    private final OpenAIService openAIService;

    @Override
    public ProductComparisonDTO compare(CompareRequestDTO req) {

        Product productA = resolveProduct(req.getProductIdA(), req.getNameA());
        Product productB = resolveProduct(req.getProductIdB(), req.getNameB());

        if (productA == null || productB == null) {
            throw new IllegalArgumentException("Both products must be resolvable by id or name.");
        }

        // Convert to DTOs
        ProductResponseDTO respA = productMapper.toResponse(productA);
        ProductResponseDTO respB = productMapper.toResponse(productB);

        // Build spec maps
        Map<String, String> mapA = specsToMap(specRepository.findByProductIdOrderByDisplayOrderAsc(productA.getId()));
        Map<String, String> mapB = specsToMap(specRepository.findByProductIdOrderByDisplayOrderAsc(productB.getId()));

        // Union of all keys
        Set<String> keys = new LinkedHashSet<>();
        keys.addAll(mapA.keySet());
        keys.addAll(mapB.keySet());

        List<SpecComparisonDTO> comparisons = new ArrayList<>();

        for (String key : keys) {
            String vA = StringUtils.defaultIfBlank(mapA.get(key), "—");
            String vB = StringUtils.defaultIfBlank(mapB.get(key), "—");

            String winner = determineWinner(key, vA, vB, respA, respB);

            comparisons.add(SpecComparisonDTO.builder()
                    .key(key)
                    .valueA(vA)
                    .valueB(vB)
                    .winner(winner)
                    .note(null)
                    .build());
        }

        addBasicFieldComparison(comparisons, respA, respB);

        // Build AI summary
        String prompt = buildPrompt(productA, productB, comparisons);
        String aiSummary = openAIService.ask(prompt);

        String recommendation = decideRecommendation(comparisons, respA, respB);

        return ProductComparisonDTO.builder()
                .productA(respA)
                .productB(respB)
                .specs(comparisons)
                .summary(aiSummary)
                .recommendation(recommendation)
                .build();
    }

    private Product resolveProduct(Long id, String name) {
        if (id != null) {
            return productRepository.findById(id).orElse(null);
        }
        if (StringUtils.isNotBlank(name)) {
            List<Product> found = productRepository.searchByNameOrDesc(name.toLowerCase());
            return found.isEmpty() ? null : found.get(0);
        }
        return null;
    }

    private Map<String, String> specsToMap(List<ProductSpecification> specs) {
        return specs.stream()
                .collect(Collectors.toMap(
                        s -> normalizeKey(s.getSpecKey()),
                        ProductSpecification::getSpecValue,
                        (first, second) -> first,
                        LinkedHashMap::new
                ));
    }

    private String normalizeKey(String k) {
        if (k == null) return "";
        return k.trim().replaceAll("\\s+", " ");
    }

    private String determineWinner(String key, String vA, String vB,
                                   ProductResponseDTO a, ProductResponseDTO b) {

        String lk = key.toLowerCase();

        // Compare Price
        if (lk.contains("price")) {
            BigDecimal pa = a.getPrice();
            BigDecimal pb = b.getPrice();
            if (pa == null || pb == null) return "TBD";
            int cmp = pa.compareTo(pb);
            return cmp == 0 ? "Equal" : (cmp < 0 ? "A" : "B");
        }

        // Numeric specs
        OptionalDouble na = parseNumber(vA);
        OptionalDouble nb = parseNumber(vB);

        if (na.isPresent() && nb.isPresent()) {
            double da = na.getAsDouble();
            double db = nb.getAsDouble();

            if (da == db) return "Equal";

            // Bigger is better for RAM, mAh, capacity
            if (lk.contains("mah") || lk.contains("battery") ||
                    lk.contains("capacity") || lk.contains("ram") || lk.contains("gb"))
                return da > db ? "A" : "B";

            return da > db ? "A" : "B";
        }

        if (vA.equalsIgnoreCase(vB)) return "Equal";
        return "TBD";
    }

    private void addBasicFieldComparison(List<SpecComparisonDTO> comps,
                                         ProductResponseDTO a, ProductResponseDTO b) {

        // PRICE
        BigDecimal pa = a.getPrice();
        BigDecimal pb = b.getPrice();

        comps.add(SpecComparisonDTO.builder()
                .key("Price")
                .valueA(pa == null ? "—" : pa.toString())
                .valueB(pb == null ? "—" : pb.toString())
                .winner(pa == null || pb == null ? "TBD"
                        : pa.compareTo(pb) < 0 ? "A"
                        : pa.compareTo(pb) == 0 ? "Equal" : "B")
                .note("Lower price is preferred")
                .build());

        // STOCK
        Integer sa = a.getStock();
        Integer sb = b.getStock();

        comps.add(SpecComparisonDTO.builder()
                .key("Stock")
                .valueA(sa == null ? "—" : sa.toString())
                .valueB(sb == null ? "—" : sb.toString())
                .winner(sa == null || sb == null ? "TBD"
                        : sa > sb ? "A"
                        : Objects.equals(sa, sb) ? "Equal" : "B")
                .note("Higher stock is better")
                .build());
    }

    private OptionalDouble parseNumber(String s) {
        if (s == null) return OptionalDouble.empty();
        var matcher = java.util.regex.Pattern.compile("([0-9]+(\\.[0-9]+)?)")
                .matcher(s.replaceAll(",", ""));
        if (matcher.find()) {
            try {
                return OptionalDouble.of(Double.parseDouble(matcher.group(1)));
            } catch (Exception e) {
                logger.warn("Number parse failed: {}", s);
            }
        }
        return OptionalDouble.empty();
    }

    private String buildPrompt(Product a, Product b, List<SpecComparisonDTO> comps) {
        StringBuilder sb = new StringBuilder();
        sb.append("Compare the following two products:\n\n");
        sb.append("Product A: ").append(a.getName()).append("\n");
        sb.append("Product B: ").append(b.getName()).append("\n\n");
        sb.append("Specs:\n");
        for (SpecComparisonDTO c : comps) {
            sb.append(String.format("- %s: A=%s | B=%s | winner=%s\n",
                    c.getKey(), c.getValueA(), c.getValueB(), c.getWinner()));
        }
        sb.append("\nProvide a 4–6 sentence summary and a recommendation.");
        return sb.toString();
    }

    private String decideRecommendation(List<SpecComparisonDTO> comps,
                                        ProductResponseDTO a, ProductResponseDTO b) {

        int scoreA = 0, scoreB = 0;

        for (SpecComparisonDTO c : comps) {
            if ("A".equals(c.getWinner())) scoreA++;
            else if ("B".equals(c.getWinner())) scoreB++;
        }

        if (scoreA == scoreB) return "Either";
        return scoreA > scoreB ? "A" : "B";
    }
}
