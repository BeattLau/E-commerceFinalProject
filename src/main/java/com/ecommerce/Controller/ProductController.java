package com.ecommerce.Controller;

import com.ecommerce.Request.ProductRequest;
import com.ecommerce.Service.ProductsService;
import com.ecommerce.Service.ProductsServiceImpl;
import com.ecommerce.Entity.Products;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ProductController {
    @Autowired
    private ProductsService productsService;

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
    @PostMapping("/products/add")
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    public ResponseEntity<Products>addProduct(@RequestBody ProductRequest productRequest){
        try {
            Products newProduct = new Products();
            newProduct.setProductName(productRequest.getProductName());
            newProduct.setDescription(productRequest.getDescription());
            newProduct.setPrice(productRequest.getPrice());
            newProduct.setQuantity(productRequest.getQuantity());

            Products savedProduct = productsService.addProducts(newProduct);
            return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/update/{productId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    public ResponseEntity<Products> updateProduct(
            @PathVariable Long productId,
            @RequestBody ProductRequest productRequest) {
        try {
            Products existingProduct = productsService.getProductByProductId(String.valueOf(productId));
            if (existingProduct == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            existingProduct.setProductName(productRequest.getProductName());
            existingProduct.setDescription(productRequest.getDescription());
            existingProduct.setPrice(productRequest.getPrice());
            existingProduct.setQuantity(productRequest.getQuantity());

            Products updatedProduct = productsService.updateProducts(existingProduct);
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/delete/{productId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        try {
            if (!productsService.productExists(productId)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            productsService.deleteProducts(productId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
