package com.example.demo.controller;

import com.example.demo.dto.OrderDto;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Order;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.order.IOrderService;
import com.example.demo.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {
    private final IOrderService orderService;

    @PostMapping("/order")
    public ResponseEntity<ApiResponse> createOrder(@RequestParam Long userId) {
        try {
            Order order = orderService.placeOrder(userId);
            OrderDto orderDto=orderService.convertToDto(order);
            return ResponseEntity.ok(new ApiResponse("Order successful", orderDto));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error Occurred", e.getMessage()));
        }
    }

    @GetMapping("/{orderId}/order")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long orderId){
        try {
            OrderDto order=orderService.getOrder(orderId);
            return ResponseEntity.ok(new ApiResponse("Order", order));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
    @GetMapping("/{userId}/orders")
    public ResponseEntity<ApiResponse> getUserOrders(@PathVariable Long userId){
        try {
            List<OrderDto> order=orderService.getUserOrders(userId);
            return ResponseEntity.ok(new ApiResponse("Orders of this user" +userId, order));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error Occurred", e.getMessage()));
        }
    }
}
