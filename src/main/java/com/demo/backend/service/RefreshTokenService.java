package com.demo.backend.service;

import com.demo.backend.model.RefreshToken;
import com.demo.backend.model.User;

import java.util.Optional;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(User user);
    Optional<RefreshToken> findByToken(String token);
    RefreshToken verifyAndGet(String token);         // throws if invalid/expired/revoked
    void revoke(String token);
    void revokeAllForUser(User user);
}
