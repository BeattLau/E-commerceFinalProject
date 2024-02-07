package com.ecommerce.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

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

    private double totalValue;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}
