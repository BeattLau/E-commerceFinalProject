package com.ecommerce.Service;

import com.ecommerce.Entity.CustomUser;
import com.ecommerce.Entity.Products;
import com.ecommerce.Entity.ShoppingCart;
import com.ecommerce.ExceptionHandler.ProductNotFoundException;
import com.ecommerce.ExceptionHandler.ShoppingCartNotFoundException;
import com.ecommerce.ExceptionHandler.UserNotFoundException;
import com.ecommerce.Request.CartRequest;
import com.ecommerce.Response.CartResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CartService {
    public CartResponse getCartContents(CustomUser user);

    public CartResponse addProductToCart(CartRequest cartRequest, CustomUser user) throws ProductNotFoundException;

    void deleteProductFromCart(String username, Long productId) throws ProductNotFoundException, UserNotFoundException, ShoppingCartNotFoundException;

    void saveCart(ShoppingCart shoppingCart);

    public ShoppingCart findCartByCartId(Long cartId);
    public ShoppingCart findCartByUserId(Long userId);

    ShoppingCart getCartForUser(CustomUser currentUser);

    void clearCart(CustomUser currentUser);
}



