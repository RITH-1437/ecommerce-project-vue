package com.demo.backend.service;

import com.demo.backend.model.Wishlist;
import java.util.List;

public interface WishlistService {

    Wishlist add(Wishlist wishlist);

    void remove(Long id);

    List<Wishlist> findByUserId(Long userId);

    boolean exists(Long userId, Long productId);
}
