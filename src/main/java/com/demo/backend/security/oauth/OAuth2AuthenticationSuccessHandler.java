package com.demo.backend.security.oauth;

import com.demo.backend.model.User;
import com.demo.backend.model.enums.UserRole;
import com.demo.backend.model.RefreshToken;
import com.demo.backend.repository.UserRepository;
import com.demo.backend.service.RefreshTokenService;
import com.demo.backend.security.jwt.JwtTokenProvider;
import com.demo.backend.security.oauth.user.GoogleOAuth2UserInfo;
import com.demo.backend.security.oauth.user.OAuth2UserInfo;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // we assume registrationId = google here; handle others similarly
        OAuth2UserInfo userInfo = new GoogleOAuth2UserInfo(attributes);

        String email = userInfo.getEmail();
        String googleId = userInfo.getId();
        String name = userInfo.getName();
        String picture = userInfo.getImageUrl();

        // find or create user
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            user = User.builder()
                    .name(name)
                    .email(email)
                    .googleId(googleId)
                    .avatarUrl(picture)
                    .emailVerified(true)
                    .role(UserRole.CUSTOMER)
                    .isActive(true)
                    .build();
            user = userRepository.save(user);
        } else {
            // update google id and avatar if desired
            boolean changed = false;
            if (user.getGoogleId() == null || !user.getGoogleId().equals(googleId)) {
                user.setGoogleId(googleId);
                changed = true;
            }
            if (picture != null && !picture.equals(user.getAvatarUrl())) {
                user.setAvatarUrl(picture);
                changed = true;
            }
            if (!user.isEmailVerified()) {
                user.setEmailVerified(true);
                changed = true;
            }
            if (changed)
                user = userRepository.save(user);
        }

        // create access token
        String accessToken = jwtTokenProvider.generateToken(user.getEmail());

        // create refresh token and set HttpOnly cookie
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
        Cookie cookie = new Cookie("refreshToken", refreshToken.getToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // set true in production (HTTPS)
        cookie.setPath("/");
        cookie.setMaxAge(
                (int) (refreshToken.getExpiresAt().atZone(java.time.ZoneId.systemDefault()).toInstant().getEpochSecond()
                        - java.time.Instant.now().getEpochSecond()));
        response.addCookie(cookie);

        // Option A: Redirect to frontend with access token in query (short-lived) —
        // careful with exposing token
        // String redirectUrl = FRONTEND_URL + "/oauth-success?accessToken=" +
        // accessToken;
        // response.sendRedirect(redirectUrl);

        // Option B (safer): Return JSON body (for SPA using popup flow) — we write JSON
        // directly
        response.setContentType("application/json");
        String json = String.format("{\"accessToken\":\"%s\",\"userId\":%d,\"email\":\"%s\",\"role\":\"%s\"}",
                accessToken, user.getId(), user.getEmail(), user.getRole().name());
        response.getWriter().write(json);
    }
}
