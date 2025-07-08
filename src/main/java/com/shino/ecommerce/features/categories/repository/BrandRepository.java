package com.shino.ecommerce.features.categories.repository;

import com.shino.ecommerce.features.categories.entity.BrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<BrandEntity, Long> {
    boolean existsByBrandName(String brandName);
}
