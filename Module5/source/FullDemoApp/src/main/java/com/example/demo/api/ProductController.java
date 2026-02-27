package com.example.demo.api;

import com.example.demo.entity.Product;
import com.example.demo.model.ProductSearch;
import com.example.demo.model.ProductVO;
import com.example.demo.service.ProductService;
import com.example.demo.service.ServiceLayerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public List<ProductVO> getAllProducts() {
        return productService.findAll();
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable long id) {
        try {
            return ResponseEntity.ok(productService.find(id));
        } catch (ServiceLayerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody ProductVO productVO) {
        try {
            return ResponseEntity.ok(productService.save(productVO));
        } catch (ServiceLayerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/find")
    public List<ProductVO> findProducts(@RequestBody ProductSearch productSearch) {
        return productService.findProducts(productSearch);

    }

    @PutMapping
    public ResponseEntity<?> updateProduct(@RequestBody Product product) {
        try {
            return ResponseEntity.ok(productService.update(product));
        } catch (ServiceLayerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") long productId) {
        try {
            productService.delete(productId);
            return ResponseEntity.ok("Product deleted");
        } catch (ServiceLayerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
