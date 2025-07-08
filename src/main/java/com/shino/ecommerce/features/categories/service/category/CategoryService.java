package com.shino.ecommerce.features.categories.service.category;

import com.shino.ecommerce.features.categories.dto.request.CategoryCreateRequest;
import com.shino.ecommerce.features.categories.dto.response.CategoryResponse;

public interface CategoryService {
    CategoryResponse createCategory(CategoryCreateRequest categoryCreateRequest);
}
