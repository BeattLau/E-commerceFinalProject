package com.ecommerce.Controller;

import com.ecommerce.Entity.Products;
import com.ecommerce.ExceptionHandler.ProductNotFoundException;
import com.ecommerce.Service.CartService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CartController {

    @Autowired
    CartService cartService;

    @PostMapping("/cart")
    public ResponseEntity<List<Products>> addProductToCart(@RequestBody Products products, @RequestParam Long productId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        try {
            List<Products> addedProducts = cartService.addProductToCart(products, Long.valueOf(username), productId);
            return ResponseEntity.ok(addedProducts);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
    }

    @DeleteMapping("/cart/delete")
    public ResponseEntity<String> deleteProductFromCart(@RequestParam Long productId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        try {
            cartService.deleteProductFromCart(Long.valueOf(username), productId);
            return ResponseEntity.ok("Product removed from the cart successfully");
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}