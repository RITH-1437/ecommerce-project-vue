package com.demo.backend.service;

import com.demo.backend.model.CartItem;
import java.util.List;

public interface CartItemService {

    CartItem create(CartItem item);

    void delete(Long id);

    List<CartItem> findByCartId(Long cartId);
}
