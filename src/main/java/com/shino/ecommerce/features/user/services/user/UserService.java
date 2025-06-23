package com.shino.ecommerce.features.user.services.user;

import com.shino.ecommerce.features.user.dto.request.ChangePasswordRequest;
import com.shino.ecommerce.features.user.dto.request.OtpVerificationRequest;
import com.shino.ecommerce.features.user.dto.request.UserCreateRequest;
import com.shino.ecommerce.features.user.dto.request.UserUpdateRequest;
import com.shino.ecommerce.features.user.dto.response.AuthenticationResponse;
import com.shino.ecommerce.features.user.dto.response.UserResponse;
import com.shino.ecommerce.features.user.entity.UserEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {
    UserResponse createUser(UserCreateRequest userCreateRequest);
    UserResponse updateUser(Long userId, UserUpdateRequest updateRequest);
    String requestChangePassword();
    String verifyChangePassword(String otp);
    UserResponse changePassword(ChangePasswordRequest changePassword);
    String requestChangeEmail();
    String verifyChangeEmail(String otp);
    AuthenticationResponse changeEmail(String email);
    UserResponse verifyEmail(OtpVerificationRequest otpVerificationRequest);
    List<UserEntity> getAllUsers();
    UserEntity getUserById(Long userId);
    UserResponse deleteUser(Long userId);
    UserResponse getCurrentUserProfile();
    UserResponse updateUserProfile(UserUpdateRequest userUpdateRequest);
    UserResponse updateAvatar(MultipartFile file) throws IOException;
}
