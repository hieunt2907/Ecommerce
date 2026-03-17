package com.shino.ecommerce.features.sellers.repository;

import com.shino.ecommerce.features.sellers.entity.SellerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRepository extends JpaRepository<SellerEntity, Long> {
    SellerEntity findBySellerId(Long sellerId);

    boolean existsBySellerId(Long sellerId);

    @Query("SELECT s FROM SellerEntity s WHERE s.user.userId = :userId")
    SellerEntity findByUserId(Long userId);
}
