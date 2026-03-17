package com.shino.ecommerce.features.user_address.services;

import com.shino.ecommerce.features.user_address.dto.request.UserAddressCreateRequest;
import com.shino.ecommerce.features.user_address.dto.request.UserAddressUpdateRequest;
import com.shino.ecommerce.features.user_address.dto.response.UserAddressResponse;
import com.shino.ecommerce.features.user_address.entity.UserAddressEntity;

import java.util.List;

public interface UserAddressService {
    UserAddressResponse createUserAddress(UserAddressCreateRequest userAddressCreateRequest);

    UserAddressResponse updateUserAddress(Long addressId, UserAddressUpdateRequest userAddressUpdateRequest);

    List<UserAddressEntity> getAllUserAddress();

    UserAddressEntity getUserAddressById(Long addressId);

    UserAddressResponse deleteUserAddress(Long addressId);

    List<UserAddressEntity> getCurrentUserAddress();

    UserAddressResponse updateCurrentUserAddress(Long userId, UserAddressUpdateRequest userAddressUpdateRequest);
}
