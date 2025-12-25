package com.demo.backend.controller;

import com.demo.backend.model.Wishlist;
import com.demo.backend.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @PostMapping
    public ResponseEntity<Wishlist> add(@RequestBody Wishlist wishlist) {
        return ResponseEntity.ok(wishlistService.add(wishlist));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Wishlist>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(wishlistService.findByUserId(userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        wishlistService.remove(id);
        return ResponseEntity.noContent().build();
    }
}
