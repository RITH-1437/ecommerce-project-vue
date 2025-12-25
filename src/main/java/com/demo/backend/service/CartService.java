package com.demo.backend.service;

import com.demo.backend.model.Cart;
import java.util.Optional;

public interface CartService {

    Cart create(Cart cart);

    Optional<Cart> findByUserId(Long userId);

    Optional<Cart> findBySessionId(String sessionId);

    void delete(Long id);
}
