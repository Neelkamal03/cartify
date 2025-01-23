package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //Initializing mean default value of totalAmount will be zero, if nothing is put in it.
    private BigDecimal totalAmount=BigDecimal.ZERO;
    //Its a onetomany relation {mappedBy=cart} mean a
    //Foriegn will be created cart.id in other other table of reference purpose
    //Set is collection of no duplicate elements
    //cascade.all means if cart is deleted all set of cartItems related to it will be deleted.
    //Orphan removal basically means if any cartItem doesn't have a foreign key(cart.id), remove that cartItem from table.
    //No cart item can exist without belonging to a cart.
    @OneToMany(mappedBy="cart", cascade=CascadeType.ALL, orphanRemoval = true)
    private Set<CartItem> cartItems=new HashSet<>();

    @OneToOne
    @JoinColumn(name="user_id")
    private User user;

    public void addItem(CartItem cartItem){
        System.out.println("Adding item to cart: " + cartItem);
        this.cartItems.add(cartItem);
        cartItem.setCart(this);
        updateTotalAmount();
    }
    public void removeItem(CartItem cartItem){
        System.out.println("Removing item from cart: " + cartItem);
        this.cartItems.remove(cartItem);
        cartItem.setCart(null);
        updateTotalAmount();
    }
    public void updateTotalAmount(){
        this.totalAmount=cartItems.stream()
                .map(item->{
                    BigDecimal unitPrice=item.getUnitPrice();
                    if(unitPrice==null){
                        return BigDecimal.ZERO;
                    }
                    return unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
                }).reduce(BigDecimal.ZERO,BigDecimal::add);
    }
}
