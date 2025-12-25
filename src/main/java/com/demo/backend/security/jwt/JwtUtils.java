package com.demo.backend.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtils {

    // Must be at least 32 chars → use env in real app
    private final SecretKey key = Keys.hmacShaKeyFor(
            "THIS_IS_A_VERY_LONG_SECRET_KEY_32_CHARS_MIN".getBytes()
    );

    private final long EXP_MS = 1000L * 60 * 60 * 24; // 24h

    // ---------------------------------------------------
    // GENERATE TOKEN
    // ---------------------------------------------------
    public String generateToken(Long userId, String email, List<String> roles) {

        Date now = new Date();
        Date exp = new Date(now.getTime() + EXP_MS);

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("email", email)
                .claim("roles", roles)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key) // HS256 by default
                .compact();
    }

    // ---------------------------------------------------
    // VALIDATE TOKEN
    // ---------------------------------------------------
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // ---------------------------------------------------
    // EXTRACT CLAIMS
    // ---------------------------------------------------
    public Claims getClaims(String token) {
        Jws<Claims> jws = Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
        return jws.getBody();
    }

    // ---------------------------------------------------
    // GET ROLES
    // ---------------------------------------------------
    @SuppressWarnings("unchecked")
    public List<String> getRoles(String token) {
        Object roles = getClaims(token).get("roles");
        return roles instanceof List ? (List<String>) roles : List.of();
    }

    // ---------------------------------------------------
    // GET USER ID
    // ---------------------------------------------------
    public String getUserId(String token) {
        return getClaims(token).getSubject();
    }
}
