package com.ecommerce.Repository;

import com.ecommerce.Entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;

@Repository
public interface ProductRepository extends JpaRepository<Products, Long> {
    Products findProductsByProductId(Long productId);
    Products findProductsByProductName(String productName);


}
