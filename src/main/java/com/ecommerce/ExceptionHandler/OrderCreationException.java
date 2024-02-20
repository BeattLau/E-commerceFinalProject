package com.ecommerce.ExceptionHandler;

public class OrderCreationException extends RuntimeException{
    public OrderCreationException(String message) {
        super(message);
    }
}
