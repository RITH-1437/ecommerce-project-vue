package com.demo.backend.service;

import com.demo.backend.dto.request.LoginRequestDTO;
import com.demo.backend.dto.request.RegisterRequestDTO;
import com.demo.backend.dto.response.JwtResponseDTO;

public interface AuthService {

    JwtResponseDTO login(LoginRequestDTO req);

    JwtResponseDTO register(RegisterRequestDTO req);

    String generateAccessTokenForEmail(String email);

}
