package com.demo.backend.service.impl;

import com.demo.backend.model.Wishlist;
import com.demo.backend.repository.WishlistRepository;
import com.demo.backend.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;

    @Override
    public Wishlist add(Wishlist wishlist) {
        return wishlistRepository.save(wishlist);
    }

    @Override
    public void remove(Long id) {
        wishlistRepository.deleteById(id);
    }

    @Override
    public List<Wishlist> findByUserId(Long userId) {
        return wishlistRepository.findByUserId(userId);
    }

    @Override
    public boolean exists(Long userId, Long productId) {
        return wishlistRepository.existsByUserIdAndProductId(userId, productId);
    }
}
