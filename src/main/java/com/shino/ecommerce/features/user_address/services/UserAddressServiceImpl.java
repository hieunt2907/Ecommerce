package com.shino.ecommerce.features.user_address.services;

import com.shino.ecommerce.core.GetCurrentUser;
import com.shino.ecommerce.features.user.entity.UserEntity;
import com.shino.ecommerce.features.user.repository.UserRepository;
import com.shino.ecommerce.features.user_address.dto.request.UserAddressCreateRequest;
import com.shino.ecommerce.features.user_address.dto.request.UserAddressUpdateRequest;
import com.shino.ecommerce.features.user_address.dto.response.UserAddressResponse;
import com.shino.ecommerce.features.user_address.entity.UserAddressEntity;
import com.shino.ecommerce.features.user_address.mapper.UserAddressMapper;
import com.shino.ecommerce.features.user_address.repository.UserAddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAddressServiceImpl implements UserAddressService {
    private final UserAddressRepository userAddressRepository;
    private final UserAddressMapper userAddressMapper;
    private final GetCurrentUser getCurrentUser;

    @Override
    public UserAddressResponse createUserAddress(UserAddressCreateRequest userAddressCreateRequest) {
        try {
            UserEntity userEntity = getCurrentUser.getCurrentUser();
            UserAddressEntity userAddressEntity = userAddressMapper.toEntity(userAddressCreateRequest);
            userAddressEntity.setUser(userEntity);
            userAddressRepository.save(userAddressEntity);
            return new UserAddressResponse(userAddressEntity, "User address created successfully");
        } catch (Exception e) {
            throw new RuntimeException("Error creating user address: " + e.getMessage(), e);
        }
    }

    @Override
    public UserAddressResponse updateUserAddress(Long addressId, UserAddressUpdateRequest userAddressUpdateRequest) {
        try {
            UserAddressEntity userAddressEntity = userAddressRepository.findByAddressId(addressId);
            if (userAddressEntity == null) {
                throw new RuntimeException("User address not found");
            }
            userAddressEntity = userAddressMapper.updateEntity(userAddressUpdateRequest, userAddressEntity);
            userAddressRepository.save(userAddressEntity);
            return new UserAddressResponse(userAddressEntity, "User address updated successfully");
        } catch (Exception e) {
            throw new RuntimeException("Error updating user address: " + e.getMessage(), e);
        }
    }

    @Override
    public List<UserAddressEntity> getAllUserAddress() {
        try {
            return userAddressRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error getting all user address: " + e.getMessage(), e);
        }
    }

    @Override
    public UserAddressEntity getUserAddressById(Long addressId) {
        try {
            return userAddressRepository.findByAddressId(addressId);
        } catch (Exception e) {
            throw new RuntimeException("Error getting user address by id: " + e.getMessage(), e);
        }
    }

    @Override
    public UserAddressResponse deleteUserAddress(Long addressId) {
        try {
            UserAddressEntity userAddressEntity = userAddressRepository.findByAddressId(addressId);
            if (userAddressEntity == null) {
                throw new RuntimeException("User address not found");
            }
            userAddressRepository.delete(userAddressEntity);
            return new UserAddressResponse(userAddressEntity, "User address deleted successfully");
        } catch (Exception e) {
            throw new RuntimeException("Error deleting user address: " + e.getMessage(), e);
        }
    }

    @Override
    public List<UserAddressEntity> getCurrentUserAddress() {
        try {
            Long userId = getCurrentUser.getCurrentUserId();
            List<UserAddressEntity> userAddresses = userAddressRepository.findAllByUserId(userId);
            if (userAddresses.isEmpty()) {
                throw new RuntimeException("No user addresses found");
            }
            return userAddresses;
        } catch (Exception e) {
            throw new RuntimeException("Error getting current user addresses: " + e.getMessage(), e);
        }
    }

    @Override
    public UserAddressResponse updateCurrentUserAddress(Long addressId, UserAddressUpdateRequest userAddressUpdateRequest) {
        try {
            Long userId = getCurrentUser.getCurrentUserId();

            if (addressId == null) {
                throw new RuntimeException("Address ID is required for updating an address");
            }

            UserAddressEntity userAddressEntity = userAddressRepository.findByAddressId(addressId);
            if (userAddressEntity == null) {
                throw new RuntimeException("User address not found");
            }

            // Verify that the address belongs to the current user
            if (!userAddressEntity.getUser().getUserId().equals(userId)) {
                throw new RuntimeException("You don't have permission to update this address");
            }

            userAddressEntity = userAddressMapper.updateEntity(userAddressUpdateRequest, userAddressEntity);
            userAddressRepository.save(userAddressEntity);
            return new UserAddressResponse(userAddressEntity, "User address updated successfully");
        } catch (Exception e) {
            throw new RuntimeException("Error updating current user address: " + e.getMessage(), e);
        }
    }
}