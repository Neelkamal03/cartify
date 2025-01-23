package com.example.demo.model;

import com.example.demo.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")//ORDER is a reserved keyword in SQL which created
// a conflict, hence we need to specify a different name for table by @Table.
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate orderDate;
    private BigDecimal orderTotalAmount;
    @Enumerated(EnumType.STRING)
    //Enumerated annotation states that how enum type should be stored/persisted in database.
    //Here enum type should be persisted as string in database.
    private OrderStatus orderStatus;
    //No duplicate orderItem will be stored inside Order.
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderItem> orderItems;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
