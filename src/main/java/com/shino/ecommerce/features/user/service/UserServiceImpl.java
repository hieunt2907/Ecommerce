package com.shino.ecommerce.features.user.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.shino.ecommerce.core.GetCurrentUser;
import com.shino.ecommerce.features.auth.entity.AuthEntity;
import com.shino.ecommerce.features.auth.enums.RoleEnum;
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
    private final GetCurrentUser getCurrentUser;

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
        try {
            if (isCurrentUserSuperAdmin()) {
                // Nếu người dùng hiện tại là super admin, trả về tất cả users
                return userRepository.findAll();
            }
            // Lấy tất cả users và lọc bỏ super admin
            return userRepository.findAll().stream()
                    .filter(user -> user.getAuth() != null &&
                            user.getAuth().getRole() != RoleEnum.ROLE_SUPER_ADMIN)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public UserEntity getUserById(Long id) {
        try {
            UserEntity user = userRepository.findById(id).orElse(null);

            if (isCurrentUserSuperAdmin()) {
                // Nếu người dùng hiện tại là super admin, trả về user bình thường
                return user;
            }
            // Kiểm tra xem user có phải là super admin không
            if (user != null &&
                    user.getAuth() != null &&
                    user.getAuth().getRole() == RoleEnum.ROLE_SUPER_ADMIN) {
                return null; // Trả về null nếu là super admin
            }
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public UserEntity updateUser(Long id, UpdateUserRequest updateUserRequest) {
        UserEntity existingUser = userRepository.findById(id).orElse(null);
        if (existingUser != null) {
            if (existingUser.getAuth().getRole() == RoleEnum.ROLE_SUPER_ADMIN) {
                if (!isCurrentUserSuperAdmin()) {
                    throw new IllegalArgumentException("Cannot update super admin user");
                }
            }
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
                if (existingUser.getAuth().getRole() == RoleEnum.ROLE_SUPER_ADMIN) {
                    throw new IllegalArgumentException("Cannot delete super admin");
                }
                userRepository.delete(existingUser);
                return existingUser;
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to delete user: " + e.getMessage());
        }
        return null;
    }

    @Override
    public UserEntity getCurrentUserProfile() {
        try {
            AuthEntity authEntity = getCurrentUser.getCurrentUser();
            if (authEntity == null) {
                throw new IllegalStateException("User not authenticated");
            }
            return userRepository.findByAuthId(authEntity.getId());
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("Failed to get current user profile", e);
        }
    }

    @Override
    public UserEntity updateCurrentUserProfile(UpdateUserRequest updateUserRequest) {
        try {
            AuthEntity authEntity = getCurrentUser.getCurrentUser();
            if (authEntity == null) {
                throw new IllegalStateException("User not authenticated");
            }
            UserEntity existingUser = userRepository.findByAuthId(authEntity.getId());
            if (existingUser == null) {
                throw new IllegalStateException("User profile not found");
            }
            
            UserEntity updatedUser = userMapper.toEntity(updateUserRequest);
            updatedUser.setId(existingUser.getId());
            
            return userRepository.save(updatedUser);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("Failed to update current user profile", e);
        }
    }

    private boolean isCurrentUserSuperAdmin() {
        try {
            AuthEntity currentUser = getCurrentUser.getCurrentUser();
            return currentUser != null &&
                    currentUser != null &&
                    currentUser.getRole() == RoleEnum.ROLE_SUPER_ADMIN;
        } catch (Exception e) {
            return false;
        }
    }
}
