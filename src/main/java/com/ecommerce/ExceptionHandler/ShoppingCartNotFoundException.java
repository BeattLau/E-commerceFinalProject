package com.ecommerce.ExceptionHandler;

public class ShoppingCartNotFoundException extends Exception {
    public ShoppingCartNotFoundException(String message) {
        super(message);
    }
}
