package com.shino.ecommerce.features.categories.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryCreateRequest {
    private String categoryName;
    private String categorySlug;
    private String description;
}
