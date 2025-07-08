package com.shino.ecommerce.features.user.dto.request;

import java.time.LocalDate;
import java.util.Set;

import com.shino.ecommerce.features.user.entity.RoleEntity;
import com.shino.ecommerce.features.user.entity.UserEntity.Gender;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequest {
    @Pattern(regexp = "^[a-zA-Z0-9_]{3,20}$", message = "Username must be 3-20 characters long and can only contain letters, numbers, and underscores.")
    private String username;
    @Email(message = "Email should be valid")
    private String email;
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "Password must be at least 8 characters long and contain at least one letter, one number, and one special character.")
    private String passwordHash;
    private String phone;
    private String avatarUrl;
    private LocalDate dateOfBirth;
    private Gender gender;
    private Set<RoleEntity> roles;
}
