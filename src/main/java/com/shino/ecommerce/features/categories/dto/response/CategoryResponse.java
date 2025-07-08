package com.shino.ecommerce.features.categories.dto.response;

import com.shino.ecommerce.features.categories.entity.CategoryEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {
    private CategoryEntity category;
    private String message;
}
