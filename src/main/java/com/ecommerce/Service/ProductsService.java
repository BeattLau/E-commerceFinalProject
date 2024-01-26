package com.ecommerce.Service;

import com.ecommerce.Entity.Products;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductsService {
    Products createProducts(Products products);
    Products updateProducts(Products updateProducts) throws Exception;

    Products getProductByProductName(String productName);
    Products getProductByProductId(String productId);

    List<Products> getAllProducts();

//    Products addProductsToCart(Products products);

}