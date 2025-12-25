package com.demo.backend.service;

import com.demo.backend.dto.request.UserRequestDTO;
import com.demo.backend.dto.response.UserResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    UserResponseDTO createFromDto(UserRequestDTO dto);
    UserResponseDTO updateFromDto(Long id, UserRequestDTO dto);
    void delete(Long id);
    UserResponseDTO findById(Long id);
    UserResponseDTO findByEmail(String email);
    List<UserResponseDTO> findAll();
    Page<UserResponseDTO> searchUsers(Pageable pageable, String search);
    void changePassword(Long userId, String rawPassword);
}
