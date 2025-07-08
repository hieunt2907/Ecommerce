package com.shino.ecommerce.features.categories.service.category;

import com.shino.ecommerce.features.categories.dto.request.CategoryCreateRequest;
import com.shino.ecommerce.features.categories.entity.CategoryEntity;
import com.shino.ecommerce.features.categories.mapper.CategoryMapper;
import com.shino.ecommerce.features.categories.repository.CategoryRepository;
import com.shino.ecommerce.features.categories.dto.response.CategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryMapper  categoryMapper;
    private final CategoryRepository categoryRepository;

    @Override
    public CategoryResponse createCategory(CategoryCreateRequest categoryCreateRequest) {
        try {
            if (categoryRepository.existsByCategoryName(categoryCreateRequest.getCategoryName())) {
                throw new RuntimeException("Category with name " + categoryCreateRequest.getCategoryName() + " already exists");
            }

            CategoryEntity categoryEntity = categoryMapper.toCategoryEntity(categoryCreateRequest);
            categoryRepository.save(categoryEntity);
            return new CategoryResponse(categoryEntity, "Create category successfully");
        } catch (Exception e) {
            throw new  RuntimeException("Error creating category" + e.getMessage() + e);
        }
    }
}
