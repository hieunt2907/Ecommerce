package com.shino.ecommerce.features.user.dto.request;

import java.time.LocalDate;
import java.util.List;

import com.shino.ecommerce.features.user.entity.RoleEntity;
import com.shino.ecommerce.features.user.entity.UserEntity.Gender;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {
    @Email(message = "Email should be valid")
    private String email;
    private String phone;
    private String avartarUrl;
    private LocalDate dateOfBirth;
    private Gender gender;
    private List<RoleEntity> roles;
}
