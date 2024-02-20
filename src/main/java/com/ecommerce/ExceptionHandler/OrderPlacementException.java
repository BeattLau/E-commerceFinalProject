package com.ecommerce.ExceptionHandler;

public class OrderPlacementException extends RuntimeException {
    public OrderPlacementException(String message, Exception e) {
        super(message);
    }
}
