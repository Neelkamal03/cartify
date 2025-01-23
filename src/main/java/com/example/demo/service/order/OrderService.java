package com.example.demo.service.order;

import com.example.demo.dto.OrderDto;
import com.example.demo.enums.OrderStatus;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.*;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepo;
import com.example.demo.service.cart.CartService;
import com.example.demo.service.cart.ICartService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final ProductRepo productRepo;
    private final ICartService cartService;
    private final ModelMapper modelMapper;

    @Override
    public Order placeOrder(Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        Order order = createOrder(cart);
        List<OrderItem> orderItems = createOrderItems(order, cart);
        order.setOrderItems(new HashSet<>(orderItems));
        order.setOrderTotalAmount(calculateTotalAmount(orderItems));
        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(cart.getId());
        return savedOrder;
    }

    private Order createOrder(Cart cart) {
        //Whatever the items in the cart we gonna create Order from it.
        //And we can also create mulitple orders.
        //Create order.
        //set user for that order.
        //Set orderItems in order.
        // OrderItems comes from cartItems present in cart.-->call createOrderItem.
        //call totalPrice on orderItem and set TotalPrice for order.
        Order order = new Order();
        //set the user...
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
       // order.setOrderDate(order.getOrderDate());
        order.setOrderDate(LocalDate.now());
        return order;
    }

    private List<OrderItem> createOrderItems(Order order, Cart cart) {
//        List<OrderItem> orderItems = new ArrayList<>();
        //When using a list if you leave list without initializing it will throw NullPointerException.
        //So always initialize the list.
        //List<OrderItem> orderItems;   orderItems.add(new OrderItem()); // Throws NullPointerException
//        for (CartItem item : cart.getCartItems()) {
//            Product product = item.getProduct();
//            product.setInventory(product.getInventory() - item.getQuantity());
//            productRepo.save(product);
//            OrderItem orderItem = new OrderItem(product, order, item.getQuantity(), item.getUnitPrice());
//            orderItems.add(orderItem);
//        }
//        return orderItems;
        return cart.getCartItems().stream().map(cartItem -> {
            Product product=cartItem.getProduct();
            product.setInventory(product.getInventory()-cartItem.getQuantity());
            productRepo.save(product);
            return new OrderItem(
                    product,
                    order,
                    cartItem.getQuantity(),
                    cartItem.getUnitPrice()
            );
        }).toList();
    }

    private BigDecimal calculateTotalAmount(List<OrderItem> orderItems) {
        //BigDecimal is like an Object/class you cannot add an instance of class.
        //We need to call its method.
        //Here BigDecimal is immutable you cannot use += with BigDecimal
        // because it does not modify the value in place.
        //+= is used for primitive types like int, double, etc.,
        // but it cannot be directly used with objects like BigDecimal.
//        BigDecimal totalPrice = BigDecimal.ZERO;
//        for (OrderItem item : orderItems) {
//            BigDecimal itemTotal = item.getPrice().multiply(new BigDecimal(item.getQuantity()));
//            totalPrice=totalPrice.add(itemTotal);
//        }
//        return totalPrice;
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (OrderItem item : orderItems) {
            BigDecimal itemTotal = item.getPrice().multiply(new BigDecimal(item.getQuantity()));
            totalPrice = totalPrice.add(itemTotal); // Correctly update totalPrice
        }
        return totalPrice;
    }


    @Override
    public OrderDto getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .map(this::convertToDto)
                .orElseThrow(()->new ResourceNotFoundException("Order Not found!"));
    }

    @Override
    public List<OrderDto> getUserOrders(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
//        List<OrderDto> orderDtos=new ArrayList<>();
//        for(Order order:orders){
//            orderDtos.add(convertOrderToDto(order));
//        }
//        return orderDtos;
        return orders.stream()
                .map(this::convertToDto)
                .toList();
        //When we use map and curly braces inside lambda function we need to return
        // block of statements: if we have single statements it will return syntax error.
        //For a single statement, you don't need braces ({}), and return is not allowed.
        //If there are multiple statements, you must use braces ({}),
        // and you must explicitly use return
//        .map(order -> {
//            System.out.println("Converting order: " + order.getId());
//            return convertToDto(order); // Explicit return is required in a block
//        })

    }

    @Override
    public OrderDto convertToDto(Order order) {
        return modelMapper.map(order, OrderDto.class);
    }
}
