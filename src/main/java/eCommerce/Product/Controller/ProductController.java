package eCommerce.Product.Controller;

import eCommerce.Product.Model.Products;
import eCommerce.Product.Service.ProductsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ProductController {
    @Autowired
    private ProductsServiceImpl productsService;

    @GetMapping("/products")
    public ResponseEntity<List<Products>> getAllProducts() {
        return ResponseEntity.ok().body(productsService.getAllProducts());
    }

    @GetMapping("/{productName}")
    public ResponseEntity<Products> getProductByProductName(@PathVariable Long productName) {
        Products product = productsService.getProductByProductName(String.valueOf(productName));
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Products> getProductById(@PathVariable Long productId) {
        Products product = productsService.getProductByProductId(String.valueOf(productId));
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
