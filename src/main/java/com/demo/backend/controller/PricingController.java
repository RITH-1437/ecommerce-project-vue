package com.demo.backend.controller;

import com.demo.backend.dto.pricing.PricingRuleRequest;
import com.demo.backend.dto.pricing.PricingRuleResponse;
import com.demo.backend.dto.pricing.PriceAdjustmentDTO;
import com.demo.backend.exception.BusinessException;
import com.demo.backend.model.PricingRule;
import com.demo.backend.repository.PricingRuleRepository;
import com.demo.backend.repository.PriceAdjustmentLogRepository;
import com.demo.backend.service.impl.DynamicPricingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/pricing")
@RequiredArgsConstructor
public class PricingController {

    private final PricingRuleRepository ruleRepository;
    private final DynamicPricingService pricingService;
    private final PriceAdjustmentLogRepository logRepository;

    @GetMapping("/rules")
    public List<PricingRuleResponse> listRules() {
        return ruleRepository.findAll().stream().map(this::toResp).collect(Collectors.toList());
    }

    @PostMapping("/rules")
    public PricingRuleResponse createRule(@Valid @RequestBody PricingRuleRequest req) {
        PricingRule r = PricingRule.builder()
                .name(req.getName())
                .conditionType(req.getConditionType())
                .conditionParam(req.getConditionParam())
                .actionType(req.getActionType())
                .actionValue(req.getActionValue())
                .target(req.getTarget())
                .enabled(req.getEnabled() == null ? true : req.getEnabled())
                .priority(req.getPriority() == null ? 0 : req.getPriority())
                .build();
        return toResp(ruleRepository.save(r));
    }

    @PutMapping("/rules/{id}")
    public PricingRuleResponse updateRule(@PathVariable Long id, @Valid @RequestBody PricingRuleRequest req) {
        PricingRule r = ruleRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Pricing rule not found with id: " + id));
        r.setName(req.getName());
        r.setConditionType(req.getConditionType());
        r.setConditionParam(req.getConditionParam());
        r.setActionType(req.getActionType());
        r.setActionValue(req.getActionValue());
        r.setTarget(req.getTarget());
        r.setEnabled(req.getEnabled() == null ? true : req.getEnabled());
        r.setPriority(req.getPriority() == null ? 0 : req.getPriority());
        return toResp(ruleRepository.save(r));
    }

    @DeleteMapping("/rules/{id}")
    public ResponseEntity<Void> deleteRule(@PathVariable Long id) {
        if (!ruleRepository.existsById(id)) {
            throw new BusinessException("Pricing rule not found with id: " + id);
        }
        ruleRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/run")
    public List<PriceAdjustmentDTO> runNow() {
        try {
            return pricingService.applyRulesNow();
        } catch (Exception ex) {
            throw new BusinessException("Failed to apply pricing rules: " + ex.getMessage());
        }
    }

    private PricingRuleResponse toResp(PricingRule r) {
        return PricingRuleResponse.builder()
                .id(r.getId())
                .name(r.getName())
                .conditionType(r.getConditionType())
                .conditionParam(r.getConditionParam())
                .actionType(r.getActionType())
                .actionValue(r.getActionValue())
                .target(r.getTarget())
                .enabled(r.isEnabled())
                .priority(r.getPriority())
                .createdAt(r.getCreatedAt())
                .updatedAt(r.getUpdatedAt())
                .build();
    }
}
