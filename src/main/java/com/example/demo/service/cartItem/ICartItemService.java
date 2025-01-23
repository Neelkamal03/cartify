package com.example.demo.service.cartItem;

import com.example.demo.model.CartItem;

public interface ICartItemService {
    void addItemToCart(Long cartId,Long productId, int quantity);
    void removeItemFromCart(Long cartId,Long productId);
    void updateItemQuantity(Long cartId, Long productId, int quantity);

    CartItem getCartItemFromCart(Long cartId, Long productId);
}
