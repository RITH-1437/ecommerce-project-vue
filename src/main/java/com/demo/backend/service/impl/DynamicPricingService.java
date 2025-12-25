package com.demo.backend.service.impl;

import com.demo.backend.dto.pricing.PriceAdjustmentDTO;
import com.demo.backend.model.PricingRule;
import com.demo.backend.model.PriceAdjustmentLog;
import com.demo.backend.model.Product;
import com.demo.backend.repository.OrderItemRepository;
import com.demo.backend.repository.PricingRuleRepository;
import com.demo.backend.repository.PriceAdjustmentLogRepository;
import com.demo.backend.repository.ProductRepository;
import com.demo.backend.service.OpenAIService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DynamicPricingService {

    private final PricingRuleRepository ruleRepository;
    private final ProductRepository productRepository;
    private final PriceAdjustmentLogRepository logRepository;
    private final OrderItemRepository orderItemRepository;
    private final OpenAIService openAIService; // optional for suggestions

    /**
     * Apply rules once now (synchronously) — safe for admin trigger.
     */
    @Transactional
    public List<PriceAdjustmentDTO> applyRulesNow() {
        try {
            List<PricingRule> rules = ruleRepository.findAllByEnabledTrueOrderByPriorityDesc();
            if (rules.isEmpty()) {
                log.info("No enabled pricing rules found");
                return Collections.emptyList();
            }

            List<Product> products = productRepository.findAll(); // consider filtering scope per rule to avoid loading
                                                                  // all in prod
            if (products.isEmpty()) {
                log.info("No products found for pricing rules");
                return Collections.emptyList();
            }

            Map<Long, Product> productMap = products.stream().collect(Collectors.toMap(Product::getId, p -> p));

            List<PriceAdjustmentDTO> adjustments = new ArrayList<>();

            for (PricingRule rule : rules) {
                try {
                    List<Product> targets = resolveTargets(rule, productMap.values());
                    if (targets.isEmpty()) {
                        log.debug("No targets found for rule: {}", rule.getName());
                        continue;
                    }

                    for (Product p : targets) {
                        try {
                            BigDecimal oldPrice = p.getPrice();
                            if (oldPrice == null || oldPrice.compareTo(BigDecimal.ZERO) <= 0) {
                                log.warn("Skipping product {} with invalid price: {}", p.getId(), oldPrice);
                                continue;
                            }

                            BigDecimal newPrice = evaluateAction(rule, p);
                            if (newPrice != null && newPrice.compareTo(oldPrice) != 0
                                    && newPrice.compareTo(BigDecimal.ZERO) > 0) {
                                // persist update
                                p.setPrice(newPrice);
                                productRepository.save(p);

                                // log
                                PriceAdjustmentLog log = PriceAdjustmentLog.builder()
                                        .productId(p.getId())
                                        .ruleId(rule.getId())
                                        .oldPrice(oldPrice)
                                        .newPrice(newPrice)
                                        .reason("Applied rule: " + rule.getName())
                                        .build();
                                logRepository.save(log);

                                adjustments.add(PriceAdjustmentDTO.builder()
                                        .productId(p.getId())
                                        .ruleId(rule.getId())
                                        .oldPrice(oldPrice)
                                        .newPrice(newPrice)
                                        .reason("Applied: " + rule.getName())
                                        .createdAt(log.getCreatedAt())
                                        .build());
                            }
                        } catch (Exception ex) {
                            log.error("Error applying rule {} to product {}: {}", rule.getName(), p.getId(),
                                    ex.getMessage());
                        }
                    }
                } catch (Exception ex) {
                    log.error("Error processing rule {}: {}", rule.getName(), ex.getMessage());
                }
            }

            log.info("Applied pricing rules, total adjustments: {}", adjustments.size());
            return adjustments;
        } catch (Exception ex) {
            log.error("Failed to apply pricing rules: {}", ex.getMessage(), ex);
            throw ex;
        }
    }

    /**
     * Resolve product targets from a rule.
     * Very basic: target formats supported:
     * - "all"
     * - "category:{id}"
     * - "product:{id}"
     */
    private List<Product> resolveTargets(PricingRule rule, Collection<Product> allProducts) {
        String target = rule.getTarget();
        if (target == null || "all".equalsIgnoreCase(target)) {
            return new ArrayList<>(allProducts);
        }
        if (target.startsWith("product:")) {
            try {
                Long id = Long.parseLong(target.substring("product:".length()));
                Product p = productRepository.findById(id).orElse(null);
                return p == null ? Collections.emptyList() : Collections.singletonList(p);
            } catch (NumberFormatException ex) {
                return Collections.emptyList();
            }
        }
        if (target.startsWith("category:")) {
            try {
                Long catId = Long.parseLong(target.substring("category:".length()));
                return allProducts.stream()
                        .filter(p -> p.getCategory() != null && Objects.equals(p.getCategory().getId(), catId))
                        .collect(Collectors.toList());
            } catch (NumberFormatException ex) {
                return Collections.emptyList();
            }
        }
        return Collections.emptyList();
    }

    /**
     * Evaluate rule action and return new price (or null if not applicable).
     */
    private BigDecimal evaluateAction(PricingRule rule, Product p) {
        // first check condition
        if (!evaluateCondition(rule, p))
            return null;

        BigDecimal old = p.getPrice();
        if (old == null || old.compareTo(BigDecimal.ZERO) <= 0) {
            log.warn("Cannot apply pricing rule to product {} with invalid price: {}", p.getId(), old);
            return null;
        }

        BigDecimal v = rule.getActionValue();
        if (v == null) {
            log.warn("Action value is null for rule: {}", rule.getName());
            return null;
        }

        try {
            switch (rule.getActionType()) {
                case "percentage_increase":
                    if (v.compareTo(BigDecimal.ZERO) < 0) {
                        log.warn("Negative percentage increase not allowed: {}", v);
                        return null;
                    }
                    return old.multiply(BigDecimal.ONE.add(v.divide(BigDecimal.valueOf(100))));
                case "percentage_decrease":
                    if (v.compareTo(BigDecimal.ZERO) < 0) {
                        log.warn("Negative percentage decrease not allowed: {}", v);
                        return null;
                    }
                    BigDecimal newPrice = old.multiply(BigDecimal.ONE.subtract(v.divide(BigDecimal.valueOf(100))));
                    if (newPrice.compareTo(BigDecimal.ZERO) <= 0) {
                        log.warn("Price decrease would result in zero or negative price for product {}", p.getId());
                        return null;
                    }
                    return newPrice;
                case "multiplier":
                    if (v.compareTo(BigDecimal.ZERO) <= 0) {
                        log.warn("Multiplier must be positive: {}", v);
                        return null;
                    }
                    return old.multiply(v);
                case "fixed_price":
                    if (v.compareTo(BigDecimal.ZERO) <= 0) {
                        log.warn("Fixed price must be positive: {}", v);
                        return null;
                    }
                    return v;
                default:
                    log.warn("Unknown action type: {}", rule.getActionType());
                    return null;
            }
        } catch (Exception ex) {
            log.error("Error evaluating action for rule {}: {}", rule.getName(), ex.getMessage());
            return null;
        }
    }

    /**
     * Evaluate rule condition. Basic supported conditions:
     * - stock_below:{n}
     * - sales_velocity_above:{n} (requires sales data; here we stub as false)
     * - always
     * - time_window:2025-12-05T00:00,2025-12-06T00:00 (iso start,end)
     */
    private boolean evaluateCondition(PricingRule rule, Product p) {
        String type = rule.getConditionType();
        if (type == null || type.trim().isEmpty()) {
            log.warn("Condition type is null or empty for rule: {}", rule.getName());
            return false;
        }

        String param = rule.getConditionParam();

        try {
            if ("always".equalsIgnoreCase(type))
                return true;

            if ("stock_below".equalsIgnoreCase(type)) {
                if (param == null || param.trim().isEmpty()) {
                    log.warn("Stock threshold parameter missing for rule: {}", rule.getName());
                    return false;
                }
                int threshold = Integer.parseInt(param.trim());
                if (threshold < 0) {
                    log.warn("Invalid stock threshold {} for rule: {}", threshold, rule.getName());
                    return false;
                }
                Integer stock = p.getStock() == null ? 0 : p.getStock();
                return stock < threshold;
            }

            if ("time_window".equalsIgnoreCase(type)) {
                if (param == null || param.trim().isEmpty()) {
                    log.warn("Time window parameter missing for rule: {}", rule.getName());
                    return false;
                }
                String[] parts = param.split(",");
                if (parts.length != 2) {
                    log.warn("Invalid time window format for rule: {}", rule.getName());
                    return false;
                }
                java.time.LocalDateTime start = java.time.LocalDateTime.parse(parts[0].trim());
                java.time.LocalDateTime end = java.time.LocalDateTime.parse(parts[1].trim());
                if (start.isAfter(end)) {
                    log.warn("Start time is after end time for rule: {}", rule.getName());
                    return false;
                }
                java.time.LocalDateTime now = java.time.LocalDateTime.now();
                return (now.isEqual(start) || now.isAfter(start)) && now.isBefore(end);
            }

            if ("category_match".equalsIgnoreCase(type)) {
                if (param == null || param.trim().isEmpty()) {
                    log.warn("Category ID parameter missing for rule: {}", rule.getName());
                    return false;
                }
                Long catId = Long.parseLong(param.trim());
                return p.getCategory() != null && Objects.equals(p.getCategory().getId(), catId);
            }

            if ("sales_velocity_above".equalsIgnoreCase(type)) {
                if (param == null || param.trim().isEmpty()) {
                    log.warn("Sales velocity threshold parameter missing for rule: {}", rule.getName());
                    return false;
                }
                try {
                    int threshold = Integer.parseInt(param.trim());
                    // Calculate sales velocity: units sold per day over last 30 days
                    LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
                    Integer totalSold = orderItemRepository.getTotalQuantitySold(p.getId(), thirtyDaysAgo);
                    if (totalSold == null)
                        totalSold = 0;
                    double velocity = totalSold / 30.0;
                    return velocity > threshold;
                } catch (NumberFormatException e) {
                    log.warn("Invalid sales velocity threshold for rule {}: {}", rule.getName(), param);
                    return false;
                }
            }

            log.warn("Unknown condition type: {} for rule: {}", type, rule.getName());
            return false;
        } catch (NumberFormatException ex) {
            log.warn("Invalid number format in condition parameter for rule {}: {}", rule.getName(), param);
            return false;
        } catch (Exception ex) {
            log.error("Error evaluating condition for rule {}: {}", rule.getName(), ex.getMessage());
            return false;
        }
    }

    /**
     * Ask AI for a suggested percentage change for a product (optional).
     * This method is advisory only and does not change DB.
     */
    public String aiSuggestChange(Product product, String context) {
        // safe short prompt
        String prompt = "You are pricing assistant. Suggest a percentage price increase or decrease for product: "
                + product.getName() + ".\nContext: " + context + "\nAnswer as single number: +5 or -10 for percent.";
        try {
            String raw = openAIService.ask(prompt);
            return raw;
        } catch (Exception ex) {
            log.warn("AI suggestion failed: {}", ex.getMessage());
            return null;
        }
    }
}
