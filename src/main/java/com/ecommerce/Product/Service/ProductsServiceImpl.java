package com.ecommerce.Product.Service;

import com.ecommerce.Product.Model.Products;
import com.ecommerce.Product.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Products createProducts(Products products) {
        log.info("Creating new product {} and saving to the database", products.getProductName());
        return productRepository.save(products);
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

    @Override
    public Products getProductByProductName(String productName) {
            log.info("Fetching product {}", productName);
            return productRepository.findProductsByProductName(productName);
    }
    @Override
    public Products getProductByProductId(String productId) {
        log.info("Fetching product {}", productId);
        return productRepository.findProductsByProductId(Long.valueOf(productId));
    }
    @Override
    public List<Products> getAllProducts() {
        log.info("Fetching all products");
        return productRepository.findAll();
    }

 /*   public Products addProductsToCart(Long productId, Long userId) {
        Products product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        user.getShoppingCart().add(product);
        return product;
    }*/
}


