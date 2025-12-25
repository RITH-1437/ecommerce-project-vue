package com.demo.backend.controller;

import com.demo.backend.model.CartItem;
import com.demo.backend.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart-items")
@RequiredArgsConstructor
public class CartItemController {

    private final CartItemService cartItemService;

    @PostMapping
    public ResponseEntity<CartItem> create(@RequestBody CartItem item) {
        return ResponseEntity.ok(cartItemService.create(item));
    }

    @GetMapping("/cart/{cartId}")
    public ResponseEntity<List<CartItem>> getByCart(@PathVariable Long cartId) {
        return ResponseEntity.ok(cartItemService.findByCartId(cartId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cartItemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
