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
    public Order generateOrderFromCart(ShoppingCart shoppingCart) throws AccessDeniedException {
        CustomUser authenticatedUser = userService.getCurrentUser();

        if (!authenticatedUser.equals(shoppingCart.getUser())) {
            throw new AccessDeniedException("You do not have permission to access this resource");
        }

        if (shoppingCart == null || shoppingCart.getCartItems() == null || shoppingCart.getCartItems().isEmpty()) {
            throw new IllegalArgumentException("Shopping cart is empty");
        }
        double totalValue = calculateTotalPrice(shoppingCart.getCartItems());
        shoppingCart.getCartItems().forEach(item -> item.setPurchased(true));
        Order order = new Order();
        order.setUser(shoppingCart.getUser());
        order.setTotalValue(totalValue);
        order.setStatus(OrderStatus.PENDING);
        order.setDate(new Date());

        orderRepository.save(order);

        // Save the shopping cart
        cartService.saveCart(shoppingCart);

        return order;
    }
@Override
public double calculateTotalPrice(List<CartItems> items) {
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
    @Override
    public void placeOrder(Order order) {
        validateOrder(order);
        orderRepository.save(order);

    }
    @Override
    public void validateOrder(Order order) {
        if (order.getUser() == null) {
            throw new IllegalArgumentException("Order must have a user.");
        }
        if (order.getTotalValue() <= 0) {
            throw new IllegalArgumentException("Order total value must be greater than zero.");
        }
    }
    @Override
    public List<Order> getOrderHistoryForUser(CustomUser currentUser) {
        return orderRepository.findOrdersByUserOrderByDateDesc(currentUser);
    }
}