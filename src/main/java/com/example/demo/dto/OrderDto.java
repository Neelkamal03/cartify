package com.example.demo.dto;

import com.example.demo.enums.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDto {
    private Long id;
    private LocalDate orderDate;
    private BigDecimal orderTotalAmount;
    private String orderStatus;
    private List<OrderItemDto> orderItems;
    private Long userId;
}
