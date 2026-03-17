package com.shino.ecommerce.features.sellers.dto.response;

import com.shino.ecommerce.features.sellers.entity.SellerEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SellerResponse {
    private SellerEntity sellerEntity;
    private String message;
}
