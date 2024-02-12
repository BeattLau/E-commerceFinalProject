package com.ecommerce.Repository;

import com.ecommerce.Entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface ProductRepository extends JpaRepository<Products, Long> {
    Products findProductsByProductId(Long productId);
    Products findProductsByProductName(String productName);
    Page<Products> findAll(Pageable pageable);
}
