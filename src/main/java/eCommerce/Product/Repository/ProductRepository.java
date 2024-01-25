package eCommerce.Product.Repository;

import eCommerce.Product.Model.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Products, Long> {
    Products findProductsByProductId(Long productId);
    Products findProductsByProductName(String productName);


}
