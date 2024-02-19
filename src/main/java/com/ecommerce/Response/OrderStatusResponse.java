package com.ecommerce.Response;

import com.ecommerce.Entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusResponse {
    private String message;
    private List<CartItemResponse> cartProductResponseList;
    private double totalPrice;
    private OrderStatus orderStatus;
}
