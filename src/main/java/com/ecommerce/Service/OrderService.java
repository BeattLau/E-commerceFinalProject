package com.ecommerce.Service;

import com.ecommerce.Entity.*;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.Date;
import java.util.List;

@Service
public interface OrderService {
    List<Order> getUserOrders(Long userId) throws AccessDeniedException;
    Order convertCartToOrder(ShoppingCart shoppingCart);
    double calculateTotalValue(List<CartItems> items);
    List<Order> getAllOrders();
    Order getOrderById(Long orderId);
    Order getOrderByDate(Date date);
    Order saveOrder(Order order);
    void deleteOrder(Long orderId);
    Order updateOrderStatus(Long orderId, OrderStatus newStatus);
    void placeOrder(Order order);
    void validateOrder(Order order);

    List<Order> getOrderHistoryForUser(CustomUser currentUser);
}