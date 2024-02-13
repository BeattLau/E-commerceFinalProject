package com.ecommerce.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Products")
@RequiredArgsConstructor
@Getter
@Setter
public class Products {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    private String productName;
    private double price;
    private String description;
    private int quantity;

    @OneToMany(mappedBy = "products", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<CartItems> shoppingCarts = new ArrayList<>();
}
