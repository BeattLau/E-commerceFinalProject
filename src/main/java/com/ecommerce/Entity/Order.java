package com.ecommerce.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="Orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    private Date date;
    @ManyToOne
    @JoinColumn(name= "user_id")
    private CustomUser user;
    @ElementCollection
    @CollectionTable(name="Order_CartItems", joinColumns=@JoinColumn(name="order_id"))
    private List<OrderedCartItem> purchasedItems;
    private double totalValue;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}
