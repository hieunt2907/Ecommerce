package com.shino.ecommerce.features.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.shino.ecommerce.features.user.dto.request.RoleCreateRequest;
import com.shino.ecommerce.features.user.entity.RoleEntity;


@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    @Mapping(target = "roleId", ignore = true)
    RoleEntity toEntity(RoleCreateRequest roleCreate);
    
    RoleCreateRequest toDto(RoleEntity roleEntity);
}
