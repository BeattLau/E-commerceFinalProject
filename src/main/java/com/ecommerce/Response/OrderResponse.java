package com.ecommerce.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

    private String message;
    private List<CartItemResponse> cartProductResponseList;
    private double totalPrice;
}