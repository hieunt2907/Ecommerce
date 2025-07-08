package com.shino.ecommerce.features.product.service;

import com.shino.ecommerce.features.product.dto.request.ProductCreateRequest;
import com.shino.ecommerce.features.product.dto.response.ProductResponse;
import com.shino.ecommerce.features.product.entity.ProductEntity;

import java.util.List;

public interface ProductService {
    ProductResponse createProduct(ProductCreateRequest productCreateRequest);
    List<ProductEntity> getAllProducts();
    ProductEntity getProductById(Long id);
    ProductResponse deleteProduct(Long id);
    List<ProductEntity> getAllActiveProducts();
}
