package com.shino.ecommerce.features.product.repository;

import com.shino.ecommerce.features.product.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    boolean existsByProductName(String productName);
    boolean existsByProductSlug(String productSlug);
    ProductEntity findByProductId(Long id);
    @Query(value = "SELECT * FROM products WHERE is_active = true", nativeQuery = true)
    List<ProductEntity> findAllActiveProducts();

}
