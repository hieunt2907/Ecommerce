package com.shino.ecommerce.core;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.shino.ecommerce.features.auth.entity.AuthEntity;
import com.shino.ecommerce.features.auth.enums.RoleEnum;

@Component
public class GetCurrentUser {
    
    public AuthEntity getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof AuthEntity) {
            return (AuthEntity) authentication.getPrincipal();
        }
        return null;
    }
    
    public Long getCurrentUserId() {
        AuthEntity auth = getCurrentUser();
        return auth != null ? auth.getId() : null;
    }
    
    public String getCurrentUsername() {
        AuthEntity auth = getCurrentUser();
        return auth != null ? auth.getUsername() : null;
    }
    
    public  RoleEnum getCurrentUserRole() {
        AuthEntity auth = getCurrentUser();
        return auth != null ? auth.getRole() : null;
    }
    
    public  boolean hasRole(RoleEnum role) {
        RoleEnum currentRole = getCurrentUserRole();
        return currentRole != null && currentRole.equals(role);
    }
    
    public  boolean isAuthenticated() {
        return getCurrentUser() != null;
    }
}
