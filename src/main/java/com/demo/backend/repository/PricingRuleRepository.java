package com.demo.backend.repository;

import com.demo.backend.model.PricingRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PricingRuleRepository extends JpaRepository<PricingRule, Long> {
    List<PricingRule> findAllByEnabledTrueOrderByPriorityDesc();
}
