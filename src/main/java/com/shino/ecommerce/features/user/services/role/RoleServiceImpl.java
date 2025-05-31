package com.shino.ecommerce.features.user.services.role;

import org.springframework.stereotype.Service;

import com.shino.ecommerce.features.user.dto.request.RoleCreateRequest;
import com.shino.ecommerce.features.user.entity.RoleEntity;
import com.shino.ecommerce.features.user.mapper.RoleMapper;
import com.shino.ecommerce.features.user.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService{
    private final RoleMapper roleMapper;
    private final RoleRepository roleRepository; // Assuming you have a repository for RoleEntity

    @Override
    public RoleEntity createRole(RoleCreateRequest roleCreateRequest) {
        try {
            if (roleRepository.existsByRoleName(roleCreateRequest.getRoleName())) {
                throw new RuntimeException("Role name already exists");
            }
            RoleEntity roleEntity = roleMapper.toEntity(roleCreateRequest);
            return roleRepository.save(roleEntity);
        } catch (Exception e) {
            throw new RuntimeException("Error creating role: " + e.getMessage(), e);
        }
    }
}
