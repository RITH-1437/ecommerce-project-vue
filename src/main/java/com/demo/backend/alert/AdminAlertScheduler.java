package com.demo.backend.alert;

import com.demo.backend.model.Product;
import com.demo.backend.repository.ProductRepository;
import com.demo.backend.util.TrafficMonitor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AdminAlertScheduler {

    private final ProductRepository productRepository;
    private final AlertService alertService;
    private final TrafficMonitor trafficMonitor;

    @Value("${alert.lowstock.threshold:10}")
    private int lowStockThreshold;

    @Value("${alert.traffic.spikeThreshold:200}")
    private int trafficSpikeThreshold;

    // Low stock check (configured by property cron)
    @Scheduled(cron = "${alert.lowstock.cron:0 */5 * * * *}")
    public void checkLowStock() {
        List<Product> low = productRepository.findAll().stream()
                .filter(p -> p.getStock() != null && p.getStock() <= lowStockThreshold)
                .toList();

        for (Product p : low) {
            Map<String, Object> m = new HashMap<>();
            m.put("productId", p.getId());
            m.put("stock", p.getStock());
            m.put("name", p.getName());

            String message = String.format("Low stock for product %s (id=%d): stock=%d", p.getName(), p.getId(), p.getStock());
            alertService.createAlert(AlertType.LOW_STOCK, AlertSeverity.WARNING, message, m);
        }
    }

    // Traffic spike detection runs every minute
    @Scheduled(cron = "${alert.traffic.checkCron:0 */1 * * * *}")
    public void checkTraffic() {
        Map<String, Integer> counts = trafficMonitor.consumeAndReset(); // number of requests in the window
        int total = counts.values().stream().mapToInt(Integer::intValue).sum();
        if (total >= trafficSpikeThreshold) {
            Map<String, Object> m = new HashMap<>();
            m.put("totalRequests", total);
            m.put("perEndpoint", counts);
            String message = "Traffic spike detected: " + total + " requests in the last window";
            alertService.createAlert(AlertType.TRAFFIC_SPIKE, AlertSeverity.CRITICAL, message, m);
        }
    }
}
