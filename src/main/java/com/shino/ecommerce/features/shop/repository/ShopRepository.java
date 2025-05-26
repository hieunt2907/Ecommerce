package com.shino.ecommerce.features.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shino.ecommerce.features.shop.entity.ShopEntity;

@Repository
public interface ShopRepository extends JpaRepository<ShopEntity, Long> {
    // Method to find a shop by its name
    ShopEntity findByShopName(String shopName);

    // Method to check if a shop exists by its name
    boolean existsByShopName(String shopName);
    
    // Changed method name to match entity field
    ShopEntity findByAuthId(Long authId);
    
    // Changed method name to match entity field
    boolean existsByAuth_Id(Long authId);
}
