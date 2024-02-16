package com.ecommerce.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "shopping_cart")
@Getter
@Setter
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private CustomUser user;

    private int quantity;
    private double totalPrice;

    @OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.ALL)
    private List<CartItems> cartItems;

    public void calculateTotalPrice() {
        if (cartItems != null) {
            double totalPrice = 0.0;
            for (CartItems cartItem : cartItems) {
                totalPrice += cartItem.getPrice();
            }
            this.totalPrice = totalPrice;
        }
    }
}
