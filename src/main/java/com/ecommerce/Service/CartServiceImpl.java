package com.ecommerce.Service;

import com.ecommerce.Entity.CustomUser;
import com.ecommerce.Entity.Products;
import com.ecommerce.ExceptionHandler.ProductNotFoundException;
import com.ecommerce.Repository.ProductRepository;
import com.ecommerce.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Products> addProductsToCart(Products products, Long userId) {
        CustomUser user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        Products productToAdd = productRepository.findById(products.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + products.getProductId()));
        user.getShoppingCart().add(productToAdd);
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
}