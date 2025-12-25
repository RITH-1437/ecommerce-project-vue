package com.demo.backend.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class TrafficMonitor {

    private final ConcurrentHashMap<String, AtomicInteger> counter = new ConcurrentHashMap<>();

    @Value("${alert.traffic.windowSeconds:60}")
    private int windowSeconds;

    /**
     * Called by a filter on every request to record the endpoint hit.
     */
    public void recordRequest(String endpoint) {
        counter.computeIfAbsent(endpoint, k -> new AtomicInteger(0)).incrementAndGet();
    }

    /**
     * Returns counts snapshot and resets counters.
     */
    public Map<String, Integer> consumeAndReset() {
        Map<String, Integer> snapshot = new ConcurrentHashMap<>();
        for (Map.Entry<String, AtomicInteger> e : counter.entrySet()) {
            snapshot.put(e.getKey(), e.getValue().get());
            e.getValue().set(0);
        }
        return snapshot;
    }
}
