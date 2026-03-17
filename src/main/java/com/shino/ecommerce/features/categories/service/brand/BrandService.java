package com.shino.ecommerce.features.categories.service.brand;

import com.shino.ecommerce.features.categories.dto.request.BrandCreateRequest;
import com.shino.ecommerce.features.categories.dto.response.BrandResponse;

public interface BrandService {
    BrandResponse createBrand(BrandCreateRequest brandCreateRequest);
}
