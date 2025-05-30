package com.shino.ecommerce.features.product.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductAttributeValueIdEntity implements java.io.Serializable {
    private Long product;
    private Long attribute;
}