package com.ecommerce.Controller;

import com.ecommerce.Entity.CustomUser;
import com.ecommerce.Entity.Order;
import com.ecommerce.Entity.OrderStatus;
import com.ecommerce.Entity.ShoppingCart;
import com.ecommerce.ExceptionHandler.OrderPlacementException;
import com.ecommerce.Repository.CartItemsRepository;
import com.ecommerce.Repository.CartRepository;
import com.ecommerce.Repository.OrderRepository;
import com.ecommerce.Repository.UserRepository;
import com.ecommerce.Response.OrderResponse;
import com.ecommerce.Service.CartService;
import com.ecommerce.Service.OrderService;
import com.ecommerce.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private CartService cartService;

    @GetMapping("/orders/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<Order>> getOrders() {
        return ResponseEntity.ok().body(orderService.getAllOrders());
    }

    @PostMapping("/orders/place-order-from-cart")
    public ResponseEntity<?> placeOrderFromCart() {
        try {
            // Step 1: Retrieve the current authenticated user
            CustomUser currentUser = userService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            // Step 2: Retrieve the shopping cart for the current user
            ShoppingCart shoppingCart = cartService.getCartForUser(currentUser);

            // Step 3: Check if the shopping cart is empty
            if (shoppingCart == null || shoppingCart.getCartItems() == null || shoppingCart.getCartItems().isEmpty()) {
                return ResponseEntity.badRequest().body("Shopping cart is empty");
            }

            // Step 4: Place the order directly using the service method
            Order order = orderService.placeOrderFromCart(shoppingCart);

            // Step 5: Update purchased items in the shopping cart
            cartService.updateCartItemsWithOrder(shoppingCart, order);

            // Step 6: Clear the shopping cart for the current user
            cartService.clearCart(currentUser);

            // Step 7: Return success response
            return ResponseEntity.ok("Order placed successfully.");
        } catch (OrderPlacementException e) {
            // Step 8: Handle OrderPlacementException and return error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to place order: " + e.getMessage());
        } catch (Exception e) {
            // Step 9: Handle other exceptions and return error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to place order");
        }
    }

    @PutMapping("/orders/{orderId}/status/{newStatus}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long orderId, @PathVariable OrderStatus newStatus) {
        Order updatedOrder = orderService.updateOrderStatus(orderId, newStatus);
        if (updatedOrder != null) {
            return ResponseEntity.ok(updatedOrder);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long orderId) {
        Order order = orderService.getOrderById(orderId);
        if (order != null) {
            return ResponseEntity.ok(order);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/orders/{date}")
    public ResponseEntity<Order> getOrderByDate(@PathVariable Date date) {
        Order order = orderService.getOrderByDate(date);
        if (order != null) {
            return ResponseEntity.ok(order);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/orders/order-history")
    public ResponseEntity<List<Order>> getOrderHistoryForCurrentUser() {
        CustomUser currentUser = userService.getCurrentUser();
        List<Order> orderHistory = orderService.getOrderHistoryForUser(currentUser);
        return ResponseEntity.ok(orderHistory);
    }
    @DeleteMapping("/orders/{orderId}")
    public ResponseEntity<Void> deleteOrderById(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }
}