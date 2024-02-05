package com.ecommerce.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name="shoppingCart")
@RequiredArgsConstructor
@Getter
@Setter
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private CustomUser user;

    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    private Products product;

    private int quantity;
    private double totalValue;
    @OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.ALL)
    private List<CartItems> cartItems;

    public void calculateTotalValue() {
        if (product != null) {
            this.totalValue = product.getPrice() * quantity;
        }
    }
}
