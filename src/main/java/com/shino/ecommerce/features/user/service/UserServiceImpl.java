package com.shino.ecommerce.features.user.service;

import org.springframework.stereotype.Service;

import com.shino.ecommerce.features.user.dto.request.CreateUserRequest;
import com.shino.ecommerce.features.user.dto.response.CreateUserResponse;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public CreateUserResponse createUser(CreateUserRequest createUserRequest) {

        return new CreateUserResponse("User created successfully", "12345");
    }


}
