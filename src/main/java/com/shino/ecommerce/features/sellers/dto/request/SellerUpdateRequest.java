package com.shino.ecommerce.features.sellers.dto.request;

import com.shino.ecommerce.features.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SellerUpdateRequest {
    private UserEntity user;
    private String shopName;
    private String shopDescription;
    private String shopLogo;
    private String businessLicense;
    private String taxCode;
    private Boolean isVerified;
    private Double sellerRating;
    private Integer totalProducts;
    private Integer totalOrders;
    private LocalDateTime lastActive;
}
