package com.shino.ecommerce.features.user_address.dto.response;

import com.shino.ecommerce.features.user_address.entity.UserAddressEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAddressResponse {
    private UserAddressEntity userAddressEntity;
    private String message;
}
