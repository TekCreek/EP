package com.example.demo.util;

import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.model.ProductVO;
import com.example.demo.model.UserVO;

/**
 * Utility class to transform objects between different layers (e.g., Entity to DTO, DTO to Entity).
 */
public class ObjectTransformer {
    
    public static UserVO modelFromEntity(User user) {
        if (user == null) {
            return null;
        }
        UserVO modelUserVO = new UserVO();
        modelUserVO.setId(user.getId());
        modelUserVO.setUsername(user.getUsername());
        modelUserVO.setEmail(user.getEmail());
        modelUserVO.setPassword(user.getPassword());
        return modelUserVO;
    }

    public static User entityFromModel(UserVO userVO) {
        if (userVO == null) {
            return null;
        }
        User entityUser = new User();
        entityUser.setId(userVO.getId());
        entityUser.setUsername(userVO.getUsername());
        entityUser.setEmail(userVO.getEmail());
        entityUser.setPassword(userVO.getPassword());
        return entityUser;
    }

    public static ProductVO modelFromEntity(Product product) {
        if (product == null) {
            return null;
        }
        ProductVO modelProductVO = new ProductVO();
        modelProductVO.setId(product.getId());
        modelProductVO.setName(product.getName());
        modelProductVO.setPrice(product.getPrice());
        return modelProductVO;
    }

    public static Product entityFromModel(ProductVO productVO) {
        if (productVO == null) {
            return null;
        }
        Product entityProduct = new Product();
        entityProduct.setId(productVO.getId());
        entityProduct.setName(productVO.getName());
        entityProduct.setPrice(productVO.getPrice());
        return entityProduct;
    }
}
