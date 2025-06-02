package com.shino.ecommerce.features.user.repository;

import org.springframework.stereotype.Repository;

import com.shino.ecommerce.features.user.entity.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUserId(Long userId);
    UserEntity findByEmail(String email);
    UserEntity findByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByPhone(String phone);
    UserEntity findByEmailOrUsername(String email, String username);
    boolean existsByEmailAndUsername(String email, String username);
}
