package com.shino.ecommerce.features.user.services.user;

import org.springframework.stereotype.Service;

import com.shino.ecommerce.features.user.dto.request.UserCreateRequest;
import com.shino.ecommerce.features.user.dto.request.UserUpdateRequest;
import com.shino.ecommerce.features.user.dto.response.UserResponse;
import com.shino.ecommerce.features.user.entity.UserEntity;
import com.shino.ecommerce.features.user.mapper.UserMapper;
import com.shino.ecommerce.features.user.repository.UserRepository;
import com.shino.ecommerce.features.user.utils.HashPassword;
import com.shino.ecommerce.core.*;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements Userservice {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final HashPassword hashPassword;
    private GetCurrentUser getCurrentUser;

    @Override
    public UserResponse createUser(UserCreateRequest userCreateRequest) {
        try {
            if (userRepository.existsByEmail(userCreateRequest.getEmail())) {
                throw new RuntimeException("Email already exists");
            }
            if (userRepository.existsByUsername(userCreateRequest.getUsername())) {
                throw new RuntimeException("Username already exists");
            }
            if (userRepository.existsByPhone(userCreateRequest.getPhone())) {
                throw new RuntimeException("Phone number already exists");
            }
            UserEntity userEntity = userMapper.toEntity(userCreateRequest);
            userEntity.setPasswordHash(hashPassword.hashPassword(userCreateRequest.getPasswordHash()));
            userRepository.save(userEntity);
            return new UserResponse(userEntity, "User create successful");
        } catch (Exception e) {
            throw new RuntimeException("Error creating user: " + e.getMessage(), e);
        }
    }

    @Override
    public UserResponse updateUser(Long userId, UserUpdateRequest userUpdateRequest) {
        try {
            UserEntity userEntity = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            Long currenUserId = getCurrentUser.getCurrentUserId();
            if (isSuperAdmin(userEntity) && !userId.equals(currenUserId)) {
                throw new RuntimeException("Can not update SUPERADMIN");
            }
            userEntity = userMapper.toEntity(userUpdateRequest);
            userRepository.save(userEntity);
            return new UserResponse(userEntity, "User update successful");
        } catch (Exception e) {
            throw new RuntimeException("Error update user: " + e.getMessage(), e);
        }
    }

    private boolean isSuperAdmin(UserEntity userEntity) {
        return userEntity.getRoles().stream()
                .anyMatch(role -> "SUPERADMIN".equals(role.getRoleName()));
    }
}
