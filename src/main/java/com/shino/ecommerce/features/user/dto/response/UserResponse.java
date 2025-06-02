package com.shino.ecommerce.features.user.dto.response;

import com.shino.ecommerce.features.user.entity.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private UserEntity userEntity;
    private String message;
}
