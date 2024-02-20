package com.ecommerce.ExceptionHandler;

public class OrderPlacementException extends RuntimeException {
    public OrderPlacementException(String message) {
        super(message);
    }
}
