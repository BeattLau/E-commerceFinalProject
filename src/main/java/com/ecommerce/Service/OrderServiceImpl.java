package com.ecommerce.Service;

import com.ecommerce.Entity.*;
import com.ecommerce.Repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.Date;
import java.util.List;
@Service
public class OrderServiceImpl implements OrderService{
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartService cartService;
    @Autowired
    private UserService userService;

    @Override
    public List<Order> getUserOrders(Long userId) throws AccessDeniedException {
        CustomUser authenticatedUser = userService.getCurrentUser();

        if (authenticatedUser.getUserId().equals(userId)) {
            return (List<Order>) orderRepository.findOrderByUserId(userId);
        } else {
            throw new AccessDeniedException("You do not have permission to access this resource");
        }
    }
    @Override
    public Order convertCartToOrder(ShoppingCart shoppingCart) {
        if (shoppingCart == null || shoppingCart.getUser() == null || shoppingCart.getCartItems() == null || shoppingCart.getCartItems().isEmpty()) {
            return null;
        }
        double totalValue = shoppingCart.getCartItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        shoppingCart.getCartItems().forEach(item -> item.setPurchased(true));

        Order order = new Order();
        order.setUser(shoppingCart.getUser());
        order.setTotalValue(totalValue);
        order.setStatus(OrderStatus.PENDING);

        orderRepository.save(order);

        cartService.saveCart(shoppingCart);

        return order;
    }
    @Override
    public double calculateTotalValue(List<CartItems> items) {
        return items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }
    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.findOrderByOrderId(orderId);
    }

    @Override
    public Order getOrderByDate(Date date) {
        return orderRepository.findOrderByDate(date);
    }

    @Override
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }
    @Override
    public Order updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order= orderRepository.findOrderByOrderId(orderId);
        order.setStatus(newStatus);
        return orderRepository.save(order);
    }
}