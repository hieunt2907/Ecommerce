package com.shino.ecommerce.features.user.services.role;


import com.shino.ecommerce.features.user.dto.request.RoleCreateRequest;
import com.shino.ecommerce.features.user.entity.RoleEntity;

public interface RoleService {
    RoleEntity createRole(RoleCreateRequest roleCreateRequest);
    
}
