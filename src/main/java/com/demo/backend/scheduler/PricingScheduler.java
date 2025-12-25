package com.demo.backend.scheduler;

import com.demo.backend.service.impl.DynamicPricingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PricingScheduler {

    private final DynamicPricingService pricingService;

    // Run every hour (adjust as needed)
    @Scheduled(cron = "0 0 * * * *")
    public void hourlyAdjust() {
        log.info("Running hourly dynamic pricing job");
        try {
            var adjustments = pricingService.applyRulesNow();
            log.info("Dynamic pricing applied, adjustments: {}", adjustments.size());
        } catch (Exception ex) {
            log.error("Dynamic pricing job failed", ex);
        }
    }
}
