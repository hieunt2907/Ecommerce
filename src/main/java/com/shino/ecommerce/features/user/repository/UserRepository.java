package com.shino.ecommerce.features.user.repository;

import com.shino.ecommerce.features.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUserId(Long userId);
    UserEntity findByEmail(String email);
    UserEntity findByUsername(String username);
    boolean existsByEmail(String email);

    @Query(value = "SELECT EXISTS(SELECT 1 FROM users WHERE LOWER(username) = LOWER(:username))", nativeQuery = true)
    boolean existsByUsername(String username);
    boolean existsByPhone(String phone);
    boolean existsByEmailAndUsername(String email, String username);
}
