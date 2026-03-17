package com.shino.ecommerce.features.categories.mapper;

import com.shino.ecommerce.features.categories.dto.request.CategoryCreateRequest;
import com.shino.ecommerce.features.categories.entity.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);
    CategoryEntity toCategoryEntity(CategoryCreateRequest categoryCreateRequest);
}
