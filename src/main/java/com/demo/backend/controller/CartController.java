package com.demo.backend.controller;

import com.demo.backend.model.Cart;
import com.demo.backend.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<Cart> create(@RequestBody Cart cart) {
        return ResponseEntity.ok(cartService.create(cart));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Optional<Cart>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.findByUserId(userId));
    }

    @GetMapping("/session/{sessionId}")
    public ResponseEntity<Optional<Cart>> getBySession(@PathVariable String sessionId) {
        return ResponseEntity.ok(cartService.findBySessionId(sessionId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cartService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
