package com.ecommerce.Service;

import com.ecommerce.Entity.CartItems;
import com.ecommerce.Entity.Order;
import com.ecommerce.Entity.OrderStatus;
import com.ecommerce.Entity.ShoppingCart;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface OrderService {
    Order convertCartToOrder(ShoppingCart shoppingCart);
    double calculateTotalValue(List<CartItems> items);
    List<Order> getAllOrders();
    Order getOrderById(Long orderId);
    Order getOrderByDate(Date date);
    Order saveOrder(Order order);
    void deleteOrder(Long orderId);
    Order updateOrderStatus(Long orderId, OrderStatus newStatus);
}