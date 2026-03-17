package com.shino.ecommerce.features.categories.service.brand;

import com.shino.ecommerce.features.categories.dto.request.BrandCreateRequest;
import com.shino.ecommerce.features.categories.entity.BrandEntity;
import com.shino.ecommerce.features.categories.mapper.BrandMapper;
import com.shino.ecommerce.features.categories.repository.BrandRepository;
import com.shino.ecommerce.features.categories.dto.response.BrandResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandMapper brandMapper;
    private final BrandRepository brandRepository;

    @Override
    public BrandResponse createBrand(BrandCreateRequest brandCreateRequest) {
        try {
            if (brandRepository.existsByBrandName(brandCreateRequest.getBrandName())) {
                throw new BadRequestException("Brand with name " + brandCreateRequest.getBrandName() + " already exists");
            }

            BrandEntity brandEntity = brandMapper.toBrandEntity(brandCreateRequest);
            brandRepository.save(brandEntity);
            return new BrandResponse(brandEntity, "Brand created successfully");
        } catch (Exception e) {
            throw new RuntimeException("Error creating brand" + e.getMessage() + e);
        }
    }
}
