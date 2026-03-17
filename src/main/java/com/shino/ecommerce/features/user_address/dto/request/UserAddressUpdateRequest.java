package com.shino.ecommerce.features.user_address.dto.request;

import com.shino.ecommerce.features.user_address.entity.UserAddressEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAddressUpdateRequest {
    private UserAddressEntity.AddressType addressType;
    private String recipientName;
    private String phone;
    private String addressLine;
    private String ward;
    private String district;
    private String city;
    private String province;
    private String postalCode;
    private Boolean isDefault;
}
