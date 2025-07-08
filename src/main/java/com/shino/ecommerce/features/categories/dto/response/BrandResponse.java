package com.shino.ecommerce.features.categories.dto.response;

import com.shino.ecommerce.features.categories.entity.BrandEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrandResponse {
    private BrandEntity brand;
    private String message;
}
