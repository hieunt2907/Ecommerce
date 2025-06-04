package com.shino.ecommerce.core;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.shino.ecommerce.features.user.entity.UserEntity;
import com.shino.ecommerce.features.user.repository.UserRepository;

@Component
public class GetCurrentUser {
    @Autowired
    private UserRepository userRepository;

    public UserEntity getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            return userRepository.findByUsername(username);
        }
        return null;
    }

    public Long getCurrentUserId() {
        UserEntity userEntity = getCurrentUser();
        return userEntity != null ? userEntity.getUserId() : null;
    }

    public String getCurrentUserEmail() {
        UserEntity userEntity = getCurrentUser();
        return userEntity != null ? userEntity.getEmail() : null;
    }
}
