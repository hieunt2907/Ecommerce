package com.shino.ecommerce.features.product.mapper;

import com.shino.ecommerce.features.product.dto.request.ProductCreateRequest;
import com.shino.ecommerce.features.product.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    ProductEntity toProductEntity(ProductCreateRequest productCreateRequest);
}
