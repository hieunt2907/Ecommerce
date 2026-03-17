package com.shino.ecommerce.features.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shino.ecommerce.features.user.entity.RoleEntity;

@Repository
public interface RoleRepository  extends JpaRepository<RoleEntity, Long>{
    boolean existsByRoleName(String roleName);
    RoleEntity findByRoleId(Long roleId);
}
