package com.shino.ecommerce.features.user.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shino.ecommerce.features.user.dto.request.CreateUserRequest;
import com.shino.ecommerce.features.user.dto.request.UpdateUserRequest;
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

    @Override
    public List<UserEntity> getAllUsers() {
        // Retrieve all users from the repository
        try {
            return userRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // Return an empty list in case of an error
        }
    }

    @Override
    public UserEntity getUserById(Long id) {
        // Retrieve a user by ID from the repository
        try {
            return userRepository.findById(id).orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Return null in case of an error
        }
    }

    @Override
    public UserEntity updateUser(Long id, UpdateUserRequest updateUserRequest) {
        UserEntity existingUser = userRepository.findById(id).orElse(null);
        if (existingUser != null) {
            // Update the existing user entity with the new data
            UserEntity updatedUser = userMapper.toEntity(updateUserRequest);
            updatedUser.setId(existingUser.getId()); // Ensure the ID remains the same
            return userRepository.save(updatedUser);
        }
        return null;
    }

    @Override
    public UserEntity deleteUser(Long id) {
        try {
            UserEntity existingUser = userRepository.findById(id).orElse(null);
            if (existingUser != null) {
                userRepository.delete(existingUser);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
