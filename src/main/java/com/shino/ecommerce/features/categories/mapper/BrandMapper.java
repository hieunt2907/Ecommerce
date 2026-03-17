package com.shino.ecommerce.features.categories.mapper;

import com.shino.ecommerce.features.categories.dto.request.BrandCreateRequest;
import com.shino.ecommerce.features.categories.entity.BrandEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BrandMapper {
    BrandMapper INSTANCE = Mappers.getMapper(BrandMapper.class);
    BrandEntity toBrandEntity(BrandCreateRequest brandCreateRequest);
}
