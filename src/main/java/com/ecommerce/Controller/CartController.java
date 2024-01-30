package com.ecommerce.Controller;

import com.ecommerce.Entity.Products;
import com.ecommerce.ExceptionHandler.ProductNotFoundException;
import com.ecommerce.ExceptionHandler.UserNotFoundException;
import com.ecommerce.Service.CartService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CartController {

    @Autowired
    CartService cartService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Products>> getCartContents(@PathVariable Long userId) {
        try {
            List<Products> cartContents = cartService.getCartContents(userId);
            return ResponseEntity.ok(cartContents);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
    }
    @PostMapping("/add")
    public ResponseEntity<List<Products>> addProductToCart(
            @RequestBody Products products,
            @RequestParam Long userId,
            @RequestParam Long productId) {
        try {
            List<Products> addedProducts = cartService.addProductToCart(products, userId, productId);
            return ResponseEntity.ok(addedProducts);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
    }
    @DeleteMapping("/cart/delete")
    public ResponseEntity<String> deleteProductFromCart(
            @RequestParam Long userId,
            @RequestParam Long productId) {
        try {
            cartService.deleteProductFromCart(userId, productId);
            return ResponseEntity.ok("Product removed from the cart successfully");
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}