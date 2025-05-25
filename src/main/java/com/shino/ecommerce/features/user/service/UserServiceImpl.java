package com.shino.ecommerce.features.user.service;

import org.springframework.stereotype.Service;

import com.shino.ecommerce.features.user.dto.request.CreateUserRequest;
import com.shino.ecommerce.features.user.dto.response.CreateUserResponse;
import com.shino.ecommerce.features.user.entity.UserEntity;
import com.shino.ecommerce.features.user.mapper.UserMapper;
import com.shino.ecommerce.features.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    public CreateUserResponse createUser(CreateUserRequest createUserRequest) {
        // Convert the request DTO to an entity using the mapper
        try {
            
            UserEntity userEntity = userMapper.toEntity(createUserRequest);
            // Save the entity to the repository
            userRepository.save(userEntity);
            // Convert the saved entity back to a response DTO
            return new CreateUserResponse("User created successfully", userEntity.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return new CreateUserResponse("Failed to create user: " + e.getMessage(), null);
        }
    }
}
