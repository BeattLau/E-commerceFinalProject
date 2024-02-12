package com.ecommerce.Service;

import com.ecommerce.Entity.CartItems;
import com.ecommerce.Entity.CustomUser;
import com.ecommerce.Entity.Products;
import com.ecommerce.Entity.ShoppingCart;
import com.ecommerce.ExceptionHandler.ProductNotFoundException;
import com.ecommerce.ExceptionHandler.UserNotFoundException;
import com.ecommerce.Repository.CartRepository;
import com.ecommerce.Repository.ProductRepository;
import com.ecommerce.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;

    private CustomUser getCurrentUser() throws UserNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof CustomUser) {
            return (CustomUser) authentication.getPrincipal();
        } else {
            throw new UserNotFoundException("User not found");
        }
    }

    @Override @Transactional
    public List<Products> getCartContents() throws UserNotFoundException {
        CustomUser user = getCurrentUser();
        Hibernate.initialize(user.getShoppingCart());
        return user.getShoppingCart().stream()
                .map(ShoppingCart::getProduct)
                .collect(Collectors.toList());
    }


    @Override
    public List<Products> addProductToCart(Long productId, String username) {
        CustomUser user = userRepository.findByUsername(username);
        Products productToAdd = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));

        ShoppingCart shoppingCart = user.getShoppingCart()
                .stream()
                .findFirst()
                .orElse(new ShoppingCart());

        CartItems cartItem = new CartItems();
        cartItem.setShoppingCart(shoppingCart);
        cartItem.setProducts(productToAdd);
        cartItem.setQuantity(1);
        cartItem.setPrice(productToAdd.getPrice());
        cartItem.setPurchased(false);
        cartItem.setOrder(null);
        shoppingCart.getCartItems().add(cartItem);

        user.getShoppingCart().add(shoppingCart);
        userRepository.save(user);
        return Collections.singletonList(productToAdd);
    }


    @Override
    public void deleteProductFromCart(Long productId, Long userId) throws ProductNotFoundException {
        CustomUser user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        Products productToRemove = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with ID " + productId + " not found"));

        if (user.getShoppingCart().remove(productToRemove)) {
            userRepository.save(user);
        } else {
            throw new ProductNotFoundException("Product with ID " + productId + " not found in the user's cart");
        }
    }

    @Override
    public void saveCart(ShoppingCart shoppingCart) {

    }
}