package com.shino.ecommerce.features.user.service;


import java.util.List;

import com.shino.ecommerce.features.user.dto.request.CreateUserRequest;
import com.shino.ecommerce.features.user.dto.request.UpdateUserRequest;
import com.shino.ecommerce.features.user.dto.response.CreateUserResponse;
import com.shino.ecommerce.features.user.entity.UserEntity;

public interface UserService {
    CreateUserResponse createUser(CreateUserRequest createUserRequest);
    List<UserEntity> getAllUsers();
    UserEntity getUserById(Long id);
    UserEntity updateUser(Long id, UpdateUserRequest updateUserRequest);
    UserEntity deleteUser(Long id);
}
