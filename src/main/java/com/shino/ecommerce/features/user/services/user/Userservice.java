package com.shino.ecommerce.features.user.services.user;

import com.shino.ecommerce.features.user.dto.request.UserCreateRequest;
import com.shino.ecommerce.features.user.dto.request.UserUpdateRequest;
import com.shino.ecommerce.features.user.dto.response.UserResponse;

public interface Userservice {
    UserResponse createUser(UserCreateRequest userCreateRequest);
    UserResponse updateUser(Long userId, UserUpdateRequest updateRequest);
}
