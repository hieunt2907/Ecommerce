package com.shino.ecommerce.features.user.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.shino.ecommerce.core.GetCurrentUser;
import com.shino.ecommerce.features.auth.entity.AuthEntity;
import com.shino.ecommerce.features.user.dto.request.CreateUserRequest;
import com.shino.ecommerce.features.user.entity.UserEntity;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final GetCurrentUser getCurrentUser;

    public UserEntity toEntity(CreateUserRequest createUserRequest) {
        UserEntity userEntity = new UserEntity();
        AuthEntity currentUser = getCurrentUser.getCurrentUser();
        if (currentUser != null) {
            AuthEntity authEntity = new AuthEntity();
            authEntity.setId(currentUser.getId());
            userEntity.setAuth(authEntity);
        }
        userEntity.setName(createUserRequest.getName());
        userEntity.setAddress(createUserRequest.getAddress());
        userEntity.setCity(createUserRequest.getCity());
        userEntity.setState(createUserRequest.getState());
        userEntity.setCountry(createUserRequest.getCountry());
        userEntity.setZipCode(createUserRequest.getZipCode());
        userEntity.setCitizenCard(createUserRequest.getCitizenCard());
        userEntity.setPhoneNumber(createUserRequest.getPhoneNumber());
        userEntity.setDateOfBirth(createUserRequest.getDateOfBirth());
        userEntity.setGender(createUserRequest.getGender());
        userEntity.setCreatedAt(LocalDateTime.now());
        return userEntity;
    }
}
