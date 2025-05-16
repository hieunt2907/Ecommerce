package com.shino.ecommerce.features.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shino.ecommerce.features.auth.entity.AuthEntity;

@Repository
public interface AuthRepository extends JpaRepository<AuthEntity, Long> {
    AuthEntity findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    
}
