package com.ecommerce.Service;

import com.ecommerce.Entity.CustomUser;
import com.ecommerce.Entity.Products;
import com.ecommerce.Entity.ShoppingCart;
import com.ecommerce.ExceptionHandler.ProductNotFoundException;
import com.ecommerce.ExceptionHandler.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CartService {
    List<Products> getCartContents() throws UserNotFoundException;

    List<Products> addProductToCart(Long productId, String username);

    void deleteProductFromCart(String username, Long productId) throws ProductNotFoundException;

    void saveCart(ShoppingCart shoppingCart);

    public ShoppingCart findCartByCartId(Long cartId);
    public ShoppingCart findCartByUserId(Long userId);
}



