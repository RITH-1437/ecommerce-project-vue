package com.demo.backend.service.impl;

import com.demo.backend.dto.request.UserRequestDTO;
import com.demo.backend.dto.response.UserResponseDTO;
import com.demo.backend.exception.BusinessException;
import com.demo.backend.mapper.UserMapper;
import com.demo.backend.model.User;
import com.demo.backend.model.enums.UserRole;
import com.demo.backend.repository.UserRepository;
import com.demo.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponseDTO createFromDto(UserRequestDTO dto) {
        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            throw new BusinessException("Email is required");
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessException("Email is already in use");
        }

        User user = userMapper.toEntity(dto);

        if (dto.getRole() != null) {
            user.setRole(parseRole(dto.getRole()));
        }

        if (dto.getActive() != null) user.setActive(dto.getActive());
        if (dto.getEmailVerified() != null) user.setEmailVerified(dto.getEmailVerified());

        user = userRepository.save(user);
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional
    public UserResponseDTO updateFromDto(Long id, UserRequestDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("User not found with id: " + id));

        userMapper.updateFromDto(dto, user);

        if (dto.getRole() != null) {
            user.setRole(parseRole(dto.getRole()));
        }

        user = userRepository.save(user);
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new BusinessException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("User not found with id: " + id));
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("User not found with email: " + email));
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> findAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDTO> searchUsers(Pageable pageable, String search) {
        Page<User> page;
        if (search == null || search.isBlank()) {
            page = userRepository.findAll(pageable);
        } else {
            // our query already uses LIKE %:kw% so pass without extra %
            page = userRepository.search(search.toLowerCase(), pageable);
        }
        return page.map(userMapper::toResponse);
    }

    private UserRole parseRole(String roleStr) {
        try {
            return UserRole.valueOf(roleStr.trim().toUpperCase());
        } catch (Exception ex) {
            throw new BusinessException("Invalid role: " + roleStr);
        }
    }

    @Override
    @Transactional
    public void changePassword(Long userId, String rawPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("User not found with id: " + userId));
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        userRepository.save(user);
    }
}
