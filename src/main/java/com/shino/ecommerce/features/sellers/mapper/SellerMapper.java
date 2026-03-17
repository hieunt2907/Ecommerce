package com.shino.ecommerce.features.sellers.mapper;

import com.shino.ecommerce.features.sellers.dto.request.SellerCreateRequest;
import com.shino.ecommerce.features.sellers.dto.request.SellerUpdateRequest;
import com.shino.ecommerce.features.sellers.entity.SellerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SellerMapper {
    SellerMapper INSTANCE = Mappers.getMapper(SellerMapper.class);

    @Mapping(target = "sellerId", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "isVerified", ignore = true)
    @Mapping(target = "sellerRating", ignore = true)
    @Mapping(target = "totalOrders", ignore = true)
    @Mapping(target = "joinDate", ignore = true)
    @Mapping(target = "lastActive", ignore = true)
    @Mapping(target = "bankAccounts", ignore = true)
    @Mapping(target = "products", ignore = true)
    SellerEntity toEntity(SellerCreateRequest sellerCreateRequest);

    SellerEntity updateEntity(SellerUpdateRequest sellerUpdateRequest, @MappingTarget SellerEntity sellerEntity);
}
