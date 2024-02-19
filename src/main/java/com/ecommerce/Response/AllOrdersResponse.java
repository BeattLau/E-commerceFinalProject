package com.ecommerce.Response;

import com.ecommerce.Entity.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllOrdersResponse {
    private String message;
    private List<Order> orders;
}