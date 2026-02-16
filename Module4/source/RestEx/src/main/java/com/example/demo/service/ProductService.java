package com.example.demo.service;

import com.example.demo.entity.Product;
import com.example.demo.model.ProductSearch;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ServiceLayerException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Page<Product> getProductsSorted(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("DESC") 
            ? Sort.by(sortBy).descending() 
            : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        return productRepository.findAll(pageable);
    }
    

    public Product findById(long productId) throws ServiceLayerException {
        Optional<Product> dbProduct = productRepository.findById(productId);
        if (dbProduct.isPresent()) {
            return dbProduct.get();
        } else {
            throw new ServiceLayerException("Product not found");
        }
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public List<Product> findProducts(ProductSearch productSearch) {
        String productNamePattern = productSearch.getPattern();
        if (productNamePattern == null) {
            return productRepository.findAll();
        } else {
            return productRepository.findByNamePattern(productNamePattern);
        }
    }

    public Product update(Product product) throws ServiceLayerException {
        Long id = product.getId();
        if (id == null) throw new ServiceLayerException("Invalid request");
        Optional<Product> dbProduct = productRepository.findById(id);
        if (dbProduct.isEmpty()) throw new ServiceLayerException("Invalid product id");
        Product targetProduct = dbProduct.get();
        BeanUtils.copyProperties(product, targetProduct);
        return productRepository.save(targetProduct);
    }

    public void delete(long productId) throws ServiceLayerException {
        Optional<Product> dbProduct = productRepository.findById(productId);
        if (dbProduct.isEmpty()) throw new ServiceLayerException("Invalid product id");
        productRepository.delete(dbProduct.get());
    }
}