package com.example.demo.service.cart;

import com.example.demo.dto.CartDto;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Cart;
import com.example.demo.model.User;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ModelMapper modelMapper;
    private final AtomicLong cartIdGenerator = new AtomicLong(0);

    @Override
    public Cart getCartById(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        return cartRepository.save(cart);
    }

    //A transaction is a sequence of read-write operations that are executed as a single unit of work.
    //The key properties of transactions are known as ACID properties,
    //Which stand for:Atomicity,Consistency,Isolation,Durability.
    //ex:-Let’s say you have an e-commerce system,
    // and you want to update a shopping cart
    //1.Read operation:Fetch the cart from the database.
    //2.Write operation: Update the total price of the cart.
    //3.Write operation: Save the updated cart back to the database.
    //Why Use Transactions for These Operations?
    //Without transactions:If the product is added to the cart, but there’s a failure when updating the total amount, the cart could end up in an inconsistent state
    // (e.g., the product is in the cart, but the total price is not updated).
    //@Transactions ensure that all operations complete successfully or not at all, maintaining consistency.
    //What Happens If Something Goes Wrong?
    //If any of the operations in the transaction fail, the whole transaction is rolled back. For instance:
    //If the product doesn’t exist in the database or there’s an issue calculating the total amount, an exception would trigger the rollback, and the cart wouldn’t be updated.
    //Lazy Loading:-
    @Transactional
    @Override
    public void clearCart(Long id) {
        Cart cart = getCartById(id);
        //cartItemRepository.deleteAllByCartId(id);
        cart.getCartItems().clear();
        cart.setTotalAmount(BigDecimal.ZERO);
        cartRepository.save(cart);

        //cartRepository.deleteById(id);
    }

    @Override
    public BigDecimal getTotalPrice(Long id) {
        Cart cart = getCartById(id);
        return cart.getTotalAmount();
    }

    //Cart is created when a user is created.
    @Override
    public Cart initializeNewCart(User user) {
        return Optional.ofNullable(getCartByUserId(user.getId()))
                .orElseGet(()->{
                    Cart cart=new Cart();
                    cart.setUser(user);
                    return cartRepository.save(cart);
                });
    }

    @Override
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }
    @Override
    public CartDto convertToDto(Cart cart){
        return modelMapper.map(cart, CartDto.class);
    }
}
