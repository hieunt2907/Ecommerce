package com.shino.ecommerce.features.user_address.mapper;

import com.shino.ecommerce.features.user_address.dto.request.UserAddressCreateRequest;
import com.shino.ecommerce.features.user_address.dto.request.UserAddressUpdateRequest;
import com.shino.ecommerce.features.user_address.entity.UserAddressEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserAddressMapper {
    UserAddressMapper INSTANCE = Mappers.getMapper(UserAddressMapper.class);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "addressId", ignore = true)
    @Mapping(target = "isDefault", ignore = true)
    UserAddressEntity toEntity(UserAddressCreateRequest userAddressCreateRequest);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "addressId", ignore = true)
    UserAddressEntity updateEntity(UserAddressUpdateRequest userAddressCreateRequest, @MappingTarget UserAddressEntity userAddressEntity);
}
