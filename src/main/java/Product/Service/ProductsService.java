package Product.Service;

import Product.Model.Products;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductsService {
    Products createProducts(Products products);
    Products updateProducts(Products updateProducts) throws Exception;

    Products getProduct(String productName);

    List<Products> getProducts();

//    Products addProductsToCart(Products products);

}
