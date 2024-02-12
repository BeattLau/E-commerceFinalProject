package com.ecommerce.Service;

import com.ecommerce.Entity.Products;
import com.ecommerce.ExceptionHandler.ProductNotFoundException;
import com.ecommerce.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductsServiceImpl implements ProductsService {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public Products addProducts(Products products) {
        try {
            if (products.getProductName() == null || products.getProductName().isEmpty()) {
                throw new IllegalArgumentException("Product name cannot be empty");
            }
            if (products.getPrice() <= 0) {
                throw new IllegalArgumentException("Price must be greater than zero");
            }
            return productRepository.save(products);
        } catch (IllegalArgumentException ex) {
        } catch (Exception ex) {
            throw new RuntimeException("An error occurred while adding the product", ex);
        }
        return products;
    }
    public Products updateProducts(Products updatedProducts) throws Exception {
        Products existingProducts = productRepository.findById(updatedProducts.getProductId())
                .orElseThrow(() -> new Exception("Products not found with id: " + updatedProducts.getProductId()));
        existingProducts.setProductName(updatedProducts.getProductName());
        existingProducts.setDescription(updatedProducts.getDescription());
        existingProducts.setPrice(updatedProducts.getPrice());
        existingProducts.setQuantity(updatedProducts.getQuantity());
        return productRepository.save(existingProducts);
    }

    public void deleteProducts(Long productId) throws ProductNotFoundException {
        if (productRepository.existsById(productId)) {
            productRepository.deleteById(productId);
        } else {
            throw new ProductNotFoundException("Product with ID " + productId + " not found");
        }
    }
    @Override
    public Products getProductByProductName(String productName) {
            return productRepository.findProductsByProductName(productName);
    }
    @Override
    public Products getProductByProductId(String productId) {
        return productRepository.findProductsByProductId(Long.valueOf(productId));
    }
    public Page<Products> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }
    public boolean productExists(Long productId) {
        return productRepository.existsById(productId);
    }

}