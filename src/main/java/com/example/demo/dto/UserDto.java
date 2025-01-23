package com.example.demo.dto;

import com.example.demo.model.Cart;
import com.example.demo.model.Order;
import lombok.Data;
import org.hibernate.annotations.NaturalId;
import java.util.List;

@Data
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private List<OrderDto> orders;
    private CartDto cart;
}
