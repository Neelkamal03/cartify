package com.example.demo.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
public class CartDto {
    private Long id;
    private Set<CartItemDto> cartItems;
    private BigDecimal totalAmount;
}
