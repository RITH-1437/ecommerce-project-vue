package com.demo.backend.service.impl;

import com.demo.backend.dto.request.LoginRequestDTO;
import com.demo.backend.dto.request.RegisterRequestDTO;
import com.demo.backend.dto.response.JwtResponseDTO;
import com.demo.backend.model.User;
import com.demo.backend.model.enums.UserRole;
import com.demo.backend.repository.UserRepository;
import com.demo.backend.security.jwt.JwtUtils;
import com.demo.backend.service.AuthService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    // ===========================
    // LOGIN
    // ===========================
    @Override
    public JwtResponseDTO login(LoginRequestDTO req) {

        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        req.getEmail(),
                        req.getPassword()
                )
        );

        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow();

        String token = jwtUtils.generateToken(
                user.getId(),
                user.getEmail(),
                List.of(user.getRole().name())
        );

        return new JwtResponseDTO(token);
    }

    // ===========================
    // REGISTER
    // ===========================
    @Override
    public JwtResponseDTO register(RegisterRequestDTO req) {

        User user = new User();
        user.setEmail(req.getEmail());
        user.setPasswordHash(passwordEncoder.encode(req.getPassword()));

        // Handle role: default to CUSTOMER if not provided or invalid
        UserRole assignedRole = UserRole.CUSTOMER; // Default role

        if (req.getRole() != null && !req.getRole().isBlank()) {
            try {
                UserRole requestedRole = UserRole.valueOf(req.getRole().toUpperCase());
                // Security: Prevent self-registration as ADMIN
                if (requestedRole != UserRole.ADMIN) {
                    assignedRole = requestedRole;
                }
                // If ADMIN was requested, silently default to CUSTOMER for security
            } catch (IllegalArgumentException e) {
                // Invalid role provided, use default CUSTOMER
            }
        }

        user.setRole(assignedRole);
        user.setActive(true);

        userRepository.save(user);

        String token = jwtUtils.generateToken(
                user.getId(),
                user.getEmail(),
                List.of(user.getRole().name())
        );

        return new JwtResponseDTO(token);
    }

    // ===========================
    // GENERATE ACCESS TOKEN (REFRESH)
    // ===========================
    @Override
    public String generateAccessTokenForEmail(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow();

        return jwtUtils.generateToken(
                user.getId(),
                user.getEmail(),
                List.of(user.getRole().name())
        );
    }
}
