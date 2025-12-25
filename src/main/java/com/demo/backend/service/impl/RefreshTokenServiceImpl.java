package com.demo.backend.service.impl;

import com.demo.backend.exception.RefreshTokenException;
import com.demo.backend.model.RefreshToken;
import com.demo.backend.model.User;
import com.demo.backend.repository.RefreshTokenRepository;
import com.demo.backend.service.RefreshTokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository repo;

    @Value("${app.jwt.refresh-expiration-ms}")
    private long refreshExpirationMs;

    @Override
    public RefreshToken createRefreshToken(User user) {
        // Optionally delete old tokens or keep them (we'll keep many but revokeable)
        RefreshToken rt = RefreshToken.builder()
                .token(generateTokenString())
                .user(user)
                .expiresAt(LocalDateTime.now().plusSeconds(refreshExpirationMs / 1000))
                .revoked(false)
                .build();
        return repo.save(rt);
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return repo.findByToken(token);
    }

    @Override
    @Transactional
    public RefreshToken verifyAndGet(String token) {
        RefreshToken refreshToken = repo.findByToken(token)
                .orElseThrow(() -> new RefreshTokenException("Refresh token not found"));

        if (refreshToken.isRevoked()) {
            throw new RefreshTokenException("Refresh token has been revoked");
        }
        if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RefreshTokenException("Refresh token has expired");
        }
        return refreshToken;
    }

    @Override
    @Transactional
    public void revoke(String token) {
        repo.findByToken(token).ifPresent(rt -> {
            rt.setRevoked(true);
            repo.save(rt);
        });
    }

    @Override
    @Transactional
    public void revokeAllForUser(User user) {
        repo.findByUserAndRevokedFalse(user)
                .forEach(rt -> {
                    rt.setRevoked(true);
                    repo.save(rt);
                });
    }

    private String generateTokenString() {
        // strong random token - UUID + random
        return UUID.randomUUID().toString() + "." + UUID.randomUUID().toString();
    }
}
