package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int quantity;//if a user add 3 unit of 'Product A' you need to track the quantity
    // to calculate total price.
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    //Many cartItem can have one product.
    //Cart 1: A user adds Product A to their cart. One CartItem is created for this product.
    //Cart 2: Another user adds the same Product A to their cart. A new CartItem is created for this second cart.
    //That means multiple cartItem across multiple user can have similar products.
    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="cart_id")
    private Cart cart;
    //to set the instance variable for a class we always use this.
    //other the default value will setIn for any variable.
    public void setTotalPrice(){
        this.totalPrice=this.unitPrice.multiply(new BigDecimal(quantity));
    }
}
