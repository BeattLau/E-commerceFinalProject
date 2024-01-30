package com.ecommerce.Service;

import com.ecommerce.Entity.Products;
import com.ecommerce.ExceptionHandler.ProductNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CartService {
    List<Products> addProductsToCart(Products products, Long userId);
    void deleteProductFromCart(Long productId, Long userId) throws ProductNotFoundException;
    }



