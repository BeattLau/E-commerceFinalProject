package eCommerce.Product.Service;

import eCommerce.Product.Model.Products;
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
