package com.shino.ecommerce.features.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shino.ecommerce.features.user.entity.UserEntity;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    
    // Method to find a user by their auth ID
    UserEntity findByAuthId(Long authId);

    // Method to check if a user exists by their auth ID
    boolean existsByAuthId(Long authId);
    
    // Method to check if a user exists by their email
    boolean existsByAuthEmail(String email);


}
