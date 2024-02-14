package com.ecommerce.Controller;

import com.ecommerce.Entity.CustomUser;
import com.ecommerce.Entity.Order;
import com.ecommerce.Entity.OrderStatus;
import com.ecommerce.Entity.ShoppingCart;
import com.ecommerce.Service.OrderService;
import com.ecommerce.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;

    @PostMapping("/generate-from-cart")
    public ResponseEntity<?> generateOrderFromCart(@RequestBody ShoppingCart shoppingCart) {
        try {
            Order order = orderService.generateOrderFromCart(shoppingCart);
            return ResponseEntity.ok(order);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to generate order");
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
        @PutMapping("/orders/{orderId}/status/{newStatus}")
        public ResponseEntity<Order> updateOrderStatus(@PathVariable Long orderId, @PathVariable OrderStatus newStatus) {
            Order updatedOrder = orderService.updateOrderStatus(orderId, newStatus);
            if (updatedOrder != null) {
                return ResponseEntity.ok(updatedOrder);
            } else {
                return ResponseEntity.notFound().build();
            }
        }
    @GetMapping("/order-history")
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