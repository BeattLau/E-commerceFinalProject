package com.ecommerce.Response;

import com.ecommerce.Entity.CartItems;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponse {
    private Long productId;
    private String name;
    private int quantity;
    private double price;

    public CartItemResponse(CartItems cartItems) {
        this.productId = cartItems.getProduct().getProductId();
        this.name = cartItems.getProduct().getProductName();
        this.quantity = cartItems.getQuantity();
        this.price = cartItems.getPrice();
    }
}
