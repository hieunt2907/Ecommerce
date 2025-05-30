package com.shino.ecommerce.features.user.repository;

import org.springframework.stereotype.Repository;

import com.shino.ecommerce.features.user.entity.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    // Custom query methods can be added here if needed
    // For example, to find a user by email:
    // Optional<UserEntity> findByEmail(String email);

    // Additional methods for user management can be implemented as needed

    
}
