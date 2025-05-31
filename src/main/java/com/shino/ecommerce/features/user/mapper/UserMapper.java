package com.shino.ecommerce.features.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.shino.ecommerce.features.user.dto.request.UserCreateRequest;
import com.shino.ecommerce.features.user.entity.UserEntity;

import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "addresses", ignore = true)
    @Mapping(target = "avatarUrl", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "isVerified", ignore = true)
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "seller", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "wishlists", ignore = true)
    UserEntity toEntity(UserCreateRequest userCreateRequest);

}
