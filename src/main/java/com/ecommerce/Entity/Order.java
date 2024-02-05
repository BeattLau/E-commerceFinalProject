package com.ecommerce.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity @Getter @Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long orderId;
    private Date date;
    @ManyToOne
    @JoinColumn(name= "userId")
    private CustomUser user;
    private double totalValue;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}
