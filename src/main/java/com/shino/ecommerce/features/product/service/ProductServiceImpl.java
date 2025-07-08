package com.shino.ecommerce.features.product.service;

import com.shino.ecommerce.features.product.dto.request.ProductCreateRequest;
import com.shino.ecommerce.features.product.dto.response.ProductResponse;
import com.shino.ecommerce.features.product.entity.ProductEntity;
import com.shino.ecommerce.features.product.mapper.ProductMapper;
import com.shino.ecommerce.features.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductResponse createProduct(ProductCreateRequest productCreateRequest) {
        try {
            if (productRepository.existsByProductName(productCreateRequest.getProductName())) {
                throw new RuntimeException("Product name already exists" + productCreateRequest.getProductName());
            }

            if (productRepository.existsByProductSlug(productCreateRequest.getProductSlug())) {
                throw new RuntimeException("Product slug already exists" + productCreateRequest.getProductSlug());
            }

            ProductEntity productEntity = productMapper.toProductEntity(productCreateRequest);
            productRepository.save(productEntity);
            return new ProductResponse(productEntity, "Create product successfully");
        } catch (Exception e) {
            throw new RuntimeException("Error creating product" + e.getMessage(), e);
        }
    }

    @Override
    public List<ProductEntity> getAllProducts() {
        try {
            return productRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error getting all products" + e.getMessage(), e);
        }
    }

    @Override
    public List<ProductEntity> getAllActiveProducts() {
        try {
            return productRepository.findAllActiveProducts();
        } catch (Exception e) {
            throw new RuntimeException("Error getting all active products" + e.getMessage(), e);
        }
    }

    @Override
    public ProductEntity getProductById(Long id) {
        try {
            return productRepository.findByProductId(id);
        } catch (Exception e) {
            throw new RuntimeException("Error getting product by id" + e.getMessage(), e);
        }
    }

    @Override
    public ProductResponse deleteProduct(Long id) {
        try {
            ProductEntity productEntity = productRepository.findByProductId(id);
            if (productEntity == null) {
                throw new RuntimeException("Product not found");
            }
            productRepository.delete(productEntity);
            return new ProductResponse(productEntity, "Delete product successfully");
        } catch (Exception e) {
            throw new RuntimeException("Error deleting product by id" + e.getMessage(), e);
        }
    }
}
