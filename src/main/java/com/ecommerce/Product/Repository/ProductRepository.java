package com.ecommerce.Product.Repository;

import com.ecommerce.Product.Model.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Products, Long> {
    Products findProductsByProductId(Long productId);
    Products findProductsByProductName(String productName);


}
