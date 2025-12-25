package com.demo.backend.util;

import com.demo.backend.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class RefreshTokenCleanupScheduler {

    private final RefreshTokenRepository repo;

    // runs every day
    @Scheduled(cron = "0 0 3 * * *")
    public void cleanupExpiredTokens() {
        repo.findAll().stream()
                .filter(rt -> rt.getExpiresAt().isBefore(LocalDateTime.now()))
                .forEach(rt -> repo.delete(rt));
    }
}
