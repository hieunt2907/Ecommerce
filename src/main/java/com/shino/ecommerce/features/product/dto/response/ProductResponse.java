package com.shino.ecommerce.features.product.dto.response;

import com.shino.ecommerce.features.product.entity.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private ProductEntity productEntity;
    private String message;
}
