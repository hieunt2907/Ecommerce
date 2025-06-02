package com.shino.ecommerce.core;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.shino.ecommerce.features.user.entity.UserEntity;

@Component
public class GetCurrentUser {
    public UserEntity getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserEntity) {
                return (UserEntity) authentication.getPrincipal();
            }
        return null;
    }

    public Long getCurrentUserId() {
        UserEntity userEntity = getCurrentUser();
        return userEntity.getUserId();
    }

    public String getCurrentUserEmail() {
        UserEntity userEntity = getCurrentUser();
        return userEntity.getEmail();
    }
}
