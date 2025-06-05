package com.shino.ecommerce.features.user_address.repository;

import com.shino.ecommerce.features.user.entity.UserEntity;
import com.shino.ecommerce.features.user_address.entity.UserAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddressEntity, Long> {
    UserAddressEntity findByUser(UserEntity user);

    UserAddressEntity findByAddressId(Long addressId);

    // Get all addresses for a user
    @Query("SELECT ua FROM UserAddressEntity ua WHERE ua.user.userId = :userId")
    List<UserAddressEntity> findAllByUserId(@Param("userId") Long userId);

    // Get default address for a user
    @Query("SELECT ua FROM UserAddressEntity ua WHERE ua.user.userId = :userId AND ua.isDefault = true")
    Optional<UserAddressEntity> findDefaultByUserId(@Param("userId") Long userId);

    // Get the first address if no default is set
    @Query("SELECT ua FROM UserAddressEntity ua WHERE ua.user.userId = :userId ORDER BY ua.createdAt ASC")
    List<UserAddressEntity> findByUserIdOrderByCreatedAt(@Param("userId") Long userId);

}
