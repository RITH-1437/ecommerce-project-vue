package com.demo.backend.controller.auth;

import com.demo.backend.dto.request.LoginRequestDTO;
import com.demo.backend.dto.request.RegisterRequestDTO;
import com.demo.backend.dto.response.JwtResponseDTO;
import com.demo.backend.dto.response.TokenRefreshResponseDTO;
import com.demo.backend.exception.RefreshTokenException;
import com.demo.backend.model.RefreshToken;
import com.demo.backend.model.User;
import com.demo.backend.service.AuthService;
import com.demo.backend.service.RefreshTokenService;
import com.demo.backend.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<TokenRefreshResponseDTO> login(@Valid @RequestBody LoginRequestDTO req,
            HttpServletResponse response) {

        JwtResponseDTO jwt = authService.login(req);
        User user = userRepository.findByEmail(req.getEmail()).orElseThrow();

        RefreshToken rt = refreshTokenService.createRefreshToken(user);

        // Set cookie
        Cookie cookie = new Cookie("refreshToken", rt.getToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(2592000); // 30 days
        response.addCookie(cookie);

        // Return access token only
        return ResponseEntity.ok(
                new TokenRefreshResponseDTO(
                        jwt.getAccessToken(),
                        null,
                        user.getId(),
                        user.getEmail(),
                        user.getRole().name()));
    }

    @PostMapping("/register")
    public ResponseEntity<JwtResponseDTO> register(@Valid @RequestBody RegisterRequestDTO req) {
        JwtResponseDTO jwtResp = authService.register(req);
        return ResponseEntity.ok(jwtResp);
    }

    // Exchange refresh token for new access token
    @PostMapping("/refresh")
    public ResponseEntity<TokenRefreshResponseDTO> refreshToken(HttpServletRequest request) {
        // Get refresh token from cookie
        String refreshTokenStr = Arrays.stream(request.getCookies() != null ? request.getCookies() : new Cookie[0])
                .filter(c -> c.getName().equals("refreshToken"))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);

        if (refreshTokenStr == null || refreshTokenStr.trim().isEmpty()) {
            throw new RefreshTokenException("Refresh token is required");
        }

        // This will throw RefreshTokenException if token is invalid, expired, or
        // revoked
        RefreshToken stored = refreshTokenService.verifyAndGet(refreshTokenStr);

        User user = stored.getUser();

        // generate new access token
        String newAccessToken = authService.generateAccessTokenForEmail(user.getEmail());

        // rotate refresh token: revoke old and create new (recommended)
        refreshTokenService.revoke(stored.getToken());
        refreshTokenService.createRefreshToken(user);

        TokenRefreshResponseDTO resp = new TokenRefreshResponseDTO(
                newAccessToken,
                null,
                user.getId(),
                user.getEmail(),
                user.getRole().name());

        return ResponseEntity.ok(resp);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {

        // Get refresh token from cookie
        String refreshToken = Arrays.stream(request.getCookies() != null ? request.getCookies() : new Cookie[0])
                .filter(c -> c.getName().equals("refreshToken"))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);

        if (refreshToken != null) {
            refreshTokenService.revoke(refreshToken);
        }

        // Delete cookie
        Cookie deleteCookie = new Cookie("refreshToken", null);
        deleteCookie.setPath("/");
        deleteCookie.setHttpOnly(true);
        deleteCookie.setMaxAge(0);
        response.addCookie(deleteCookie);

        return ResponseEntity.noContent().build();
    }

}
