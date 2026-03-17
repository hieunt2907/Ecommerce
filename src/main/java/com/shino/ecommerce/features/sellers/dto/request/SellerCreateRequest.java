package com.shino.ecommerce.features.sellers.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SellerCreateRequest {
    private String shopName;
    private String shopDescription;
    private String shopLogo;
    private String businessLicense;
    private String taxCode;
    private Integer totalProducts;
}
