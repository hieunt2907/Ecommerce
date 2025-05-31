package com.shino.ecommerce.features.user.services.user;

import com.shino.ecommerce.features.user.dto.request.UserCreateRequest;
import com.shino.ecommerce.features.user.entity.UserEntity;

public interface Userservice {
    UserEntity createUser(UserCreateRequest userCreateRequest);
}
