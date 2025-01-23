package com.example.demo.service.cart;

import com.example.demo.dto.CartDto;
import com.example.demo.model.Cart;
import com.example.demo.model.User;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCartById(Long id);
    void clearCart(Long id);
    BigDecimal getTotalPrice(Long id);
    //Cart is created when a user is created.
    Cart initializeNewCart(User user);

    Cart getCartByUserId(Long userId);

    CartDto convertToDto(Cart cart);
}
