package com.ecommerce.Service;

import com.ecommerce.Entity.Products;
import com.ecommerce.ExceptionHandler.ProductNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductsService {
    Products addProducts(Products products);
    Products updateProducts(Products updateProducts) throws Exception;

    void deleteProducts(Long productId) throws ProductNotFoundException;

    Products getProductByProductName(String productName);
    Products getProductByProductId(String productId);

    List<Products> getAllProducts();

    boolean productExists(Long productId);

//    Products addProductsToCart(Products products);

}
