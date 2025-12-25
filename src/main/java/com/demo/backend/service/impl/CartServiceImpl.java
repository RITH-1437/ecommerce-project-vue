package com.demo.backend.service.impl;

import com.demo.backend.model.Cart;
import com.demo.backend.repository.CartRepository;
import com.demo.backend.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    @Override
    public Cart create(Cart cart) {
        return cartRepository.save(cart);
    }

    @Override
    public Optional<Cart> findByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }

    @Override
    public Optional<Cart> findBySessionId(String sessionId) {
        return cartRepository.findBySessionId(sessionId);
    }

    @Override
    public void delete(Long id) {
        cartRepository.deleteById(id);
    }
}
