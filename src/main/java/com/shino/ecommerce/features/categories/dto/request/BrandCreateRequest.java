package com.shino.ecommerce.features.categories.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrandCreateRequest {
    private String brandName;
    private String description;
}
