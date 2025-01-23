package com.example.demo.service.cartItem;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.Product;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.service.cart.ICartService;
import com.example.demo.service.product.IProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService {
    private final CartItemRepository cartItemRepository;
    private final IProductService productService;
    private final ICartService cartService;
    private final CartRepository cartRepository;

    @Override
    public void addItemToCart(Long cartId, Long productId, int quantity) {
        //1.Get the cart
        //2.Get the product
        //3.Check if the product already in the cart
        //4.If yes, then increase the quantity with the requested quantity.
        //5.If No,then initiate a new cartItem entry.
        Cart cart = cartService.getCartById(cartId);
        Product product = productService.getProductById(productId);
        CartItem cartItem = cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst().orElse(new CartItem());

        if (cartItem.getId() == null) {
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUnitPrice(product.getPrice());
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }
        System.out.println("Total Amount before save: " + cart.getTotalAmount());
        cartItem.setTotalPrice();
        cart.addItem(cartItem);
        System.out.println("Total Amount after save: " + cart.getTotalAmount());
        System.out.println("Cart before save: " + cart);
        System.out.println("CartItems before save: " + cart.getCartItems());
        cartItemRepository.save(cartItem);
        cartRepository.save(cart);
    }

    @Override
    public void removeItemFromCart(Long cartId, Long productId) {
        Cart cart = cartService.getCartById(cartId);
        //We need to remove that cart which have this product.
        //Product product=productService.getProductById(productId);
        CartItem itemToRemove = getCartItemFromCart(cartId, productId);
        cart.removeItem(itemToRemove);
        cartRepository.save(cart);
        //cartItemRepository.deleteById(cartId);
        //we don't have to explicitly remove or call delete method to remove cartItem from its table
        //orphanRemoval = true: Ensures that any CartItem removed
        //from the cartItems collection is also deleted from the database.
    }

    @Override
    public void updateItemQuantity(Long cartId, Long productId, int quantity) {
        Cart cart = cartService.getCartById(cartId);
        Product product=productService.getProductById(productId);
        cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresent(item -> {
                    item.setQuantity(quantity);
                    item.setUnitPrice(item.getProduct().getPrice());
                    item.setTotalPrice();
                });
        BigDecimal totalAmount = cart.getCartItems()
                .stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalAmount(totalAmount);
        cartRepository.save(cart);
    }

    @Override
    public CartItem getCartItemFromCart(Long cartId, Long productId) {
        Cart cart = cartService.getCartById(cartId);
        return cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst().orElseThrow(() -> new ResourceNotFoundException("Item not found"));
    }
}
