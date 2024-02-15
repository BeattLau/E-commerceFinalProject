package com.ecommerce.Controller;

import com.ecommerce.Entity.CustomUser;
import com.ecommerce.Entity.Products;
import com.ecommerce.Entity.ShoppingCart;
import com.ecommerce.ExceptionHandler.ProductNotFoundException;
import com.ecommerce.ExceptionHandler.ShoppingCartNotFoundException;
import com.ecommerce.ExceptionHandler.UserNotFoundException;
import com.ecommerce.Service.CartService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CartController {

    @Autowired
    CartService cartService;
    ShoppingCart shoppingCart;
    @GetMapping("/cart/contents")
    public ResponseEntity<List<Products>> getCartContents() {
        try {
            List<Products> cartContents = cartService.getCartContents();
            if (cartContents.isEmpty()) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.ok(cartContents);
            }
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    @PostMapping("/cart/add/{productId}")
    public ResponseEntity<Map<String, Object>> addProductToCart(@PathVariable Long productId) {
        try {
            CustomUser currentUser = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            List<Products> addedProducts = cartService.addProductToCart(productId, currentUser.getUsername());
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Item successfully added to cart");
            response.put("addedProducts", addedProducts);

            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyMap());
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        } catch (ProductNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    @DeleteMapping("/cart/delete/{productId}")
    public ResponseEntity<String> deleteProductFromCart(@PathVariable Long productId) {
        try {
            CustomUser currentUser = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            cartService.deleteProductFromCart(currentUser.getUsername(), productId);
            return ResponseEntity.ok("Product removed from the cart successfully");
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        } catch (ShoppingCartNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/cart/{cartId}")
    public ResponseEntity<?> getCartByCartId(@PathVariable Long cartId) {
        ShoppingCart cart = cartService.findCartByCartId(cartId);
        if (cart != null) {
            return ResponseEntity.ok(cart);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/cart/{userId}")
    public ResponseEntity<?> getCartByUserId(@PathVariable Long userId) {
        ShoppingCart cart = cartService.findCartByUserId(userId);
        if (cart != null) {
            return ResponseEntity.ok(cart);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}