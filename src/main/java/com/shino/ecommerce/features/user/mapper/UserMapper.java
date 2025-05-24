package com.shino.ecommerce.features.user.mapper;

import org.springframework.stereotype.Component;

import com.shino.ecommerce.features.user.dto.request.CreateUserRequest;
import com.shino.ecommerce.features.user.entity.UserEntity;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserMapper {
    public UserEntity toEntity(CreateUserRequest createUserRequest) {
        UserEntity userEntity = new UserEntity();
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
        return userEntity;
    }
}
