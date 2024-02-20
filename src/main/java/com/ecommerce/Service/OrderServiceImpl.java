package com.ecommerce.Service;

import com.ecommerce.Entity.*;
import com.ecommerce.ExceptionHandler.OrderPlacementException;
import com.ecommerce.Repository.CartItemsRepository;
import com.ecommerce.Repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service @Transactional @AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    private CartItemsRepository cartItemsRepository;
    private CartService cartService;
    private UserService userService;
    @Override
    public Order placeOrderFromCart(ShoppingCart shoppingCart) throws OrderPlacementException {
        try {
            CustomUser authenticatedUser = userService.getCurrentUser();

            if (!authenticatedUser.equals(shoppingCart.getUser())) {
                throw new AccessDeniedException("You do not have permission to access this resource");
            }

            if (shoppingCart == null || shoppingCart.getCartItems() == null || shoppingCart.getCartItems().isEmpty()) {
                throw new IllegalArgumentException("Shopping cart is empty");
            }

            double totalValue = calculateTotalPrice(shoppingCart.getCartItems());
            List<CartItems> purchasedItems = shoppingCart.getCartItems().stream()
                    .filter(CartItems::isPurchased)
                    .collect(Collectors.toList());

            Order order = new Order();
            order.setUser(shoppingCart.getUser());
            order.setPurchasedItems(createOrderedCartItems(purchasedItems));
            order.setTotalValue(totalValue);
            order.setStatus(OrderStatus.PENDING);
            order.setDate(new Date());

            orderRepository.save(order); // Save the order to get the order_id

// Set the order_id for each CartItems entity
            for (CartItems cartItems : purchasedItems) {
                cartItems.setOrderOrderId(order); // Assuming there's a setOrder method in CartItems class
            }

            cartItemsRepository.saveAll(purchasedItems); // Save all CartItems entities with updated order_id

            cartService.clearCart(authenticatedUser);

            return order;
        } catch (Exception e) {
            throw new OrderPlacementException("Failed to place order", e);
        }
    }


    private List<OrderedCartItem> createOrderedCartItems(List<CartItems> cartItems) {
        return cartItems.stream()
                .map(cartItem -> {
                    OrderedCartItem orderedCartItem = new OrderedCartItem();
                    orderedCartItem.setCartItemId(cartItem.getCartItemsId());
                    orderedCartItem.setProductName(cartItem.getProduct().getProductName());
                    return orderedCartItem;
                })
                .collect(Collectors.toList());
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
    public Order placeOrder(Order order) {
        validateOrder(order);
        orderRepository.save(order);

        return order;
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