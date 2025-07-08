package com.shino.ecommerce.features.product.dto.request;

import com.shino.ecommerce.features.categories.entity.BrandEntity;
import com.shino.ecommerce.features.categories.entity.CategoryEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreateRequest {
    private CategoryEntity category;
    private BrandEntity brand;
    private String productName;
    private String productSlug;
    private String shortDescription;
    private String fullDescription;
    private String sku;
    private Double price;
    private Double comparePrice;
    private Double costPrice;
    private Integer stockQuantity;
    private Double weight;
    private String dimensions;
    private String barcode;
}
