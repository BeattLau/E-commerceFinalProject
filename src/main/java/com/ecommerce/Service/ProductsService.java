package com.ecommerce.Service;

import com.ecommerce.Entity.Products;
import com.ecommerce.ExceptionHandler.ProductNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface ProductsService {
    Products addProducts(Products products);
    Products updateProducts(Products updateProducts) throws Exception;

    void deleteProducts(Long productId) throws ProductNotFoundException;

    Products getProductByProductName(String productName);
    Products getProductByProductId(Long productId);

    Page<Products> getAllProducts(Pageable pageable);

    boolean productExists(Long productId);

}