package com.example.demo.service;

import com.example.demo.entity.Product;
import com.example.demo.model.ProductSearch;
import com.example.demo.model.ProductVO;
import com.example.demo.repository.ProductRepository;
import com.example.demo.util.ObjectTransformer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<ProductVO> findAll() {
        return productRepository.findAll()
                .stream()
                .map(ObjectTransformer::modelFromEntity)
                .toList();
    }

    public ProductVO save(ProductVO productVO) throws ServiceLayerException {
        Long id = productVO.getId();
        if (id != null) throw new ServiceLayerException("Product id is not expected in the request, remove id field.");
        Product product = ObjectTransformer.entityFromModel(productVO);
        return ObjectTransformer.modelFromEntity(productRepository.save(product));
    }

    public List<ProductVO> findProducts(ProductSearch productSearch) {
        String productNamePattern = productSearch.getPattern();
        if (productNamePattern == null) {
            return productRepository.findAll().stream().map(ObjectTransformer::modelFromEntity).toList();
        } else {
            return productRepository.findByNamePattern(productNamePattern).stream().map(ObjectTransformer::modelFromEntity).toList();
        }
    }

    public ProductVO update(Product product) throws ServiceLayerException {
        Long id = product.getId();
        if (id == null) throw new ServiceLayerException("Invalid request");
        Optional<Product> dbProduct = productRepository.findById(id);
        if (dbProduct.isEmpty()) throw new ServiceLayerException("Invalid product id");
        Product targetProduct = dbProduct.get();
        BeanUtils.copyProperties(product, targetProduct);
        return ObjectTransformer.modelFromEntity(productRepository.save(targetProduct));
    }

    public void delete(long productId) throws ServiceLayerException {
        Optional<Product> dbProduct = productRepository.findById(productId);
        if (dbProduct.isEmpty()) throw new ServiceLayerException("Invalid product id");
        productRepository.delete(dbProduct.get());
    }

    public ProductVO find(long id) throws ServiceLayerException {
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) {
            throw new ServiceLayerException("Invalid product id");
        }
        return ObjectTransformer.modelFromEntity(product.get());
    }
}