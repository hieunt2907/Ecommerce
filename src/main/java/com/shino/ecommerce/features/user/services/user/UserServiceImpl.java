package com.shino.ecommerce.features.user.services.user;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.shino.ecommerce.common.messaging.dto.EmailDTO;
import com.shino.ecommerce.common.messaging.producer.EmailProducer;
import com.shino.ecommerce.core.GetCurrentUser;
import com.shino.ecommerce.features.user.dto.request.ChangePasswordRequest;
import com.shino.ecommerce.features.user.dto.request.OtpVerificationRequest;
import com.shino.ecommerce.features.user.dto.request.UserCreateRequest;
import com.shino.ecommerce.features.user.dto.request.UserUpdateRequest;
import com.shino.ecommerce.features.user.dto.response.AuthenticationResponse;
import com.shino.ecommerce.features.user.dto.response.UserResponse;
import com.shino.ecommerce.features.user.entity.UserEntity;
import com.shino.ecommerce.features.user.mapper.UserMapper;
import com.shino.ecommerce.features.user.repository.UserRepository;
import com.shino.ecommerce.features.user.utils.EmailSessionUtil;
import com.shino.ecommerce.features.user.utils.HashPassword;
import com.shino.ecommerce.features.user.utils.OtpSend;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final HashPassword hashPassword;
    private final GetCurrentUser getCurrentUser;
    private final OtpSend otpSend;
    private final EmailProducer emailProducer;
    private final RedisTemplate<String, Boolean> redisBooleanTemplate;
    private final EmailSessionUtil emailSessionUtil;
    private final Cloudinary cloudinary;

    @Override
    public UserResponse createUser(UserCreateRequest userCreateRequest) {
        try {
            if (userCreateRequest.getPasswordHash() == null || userCreateRequest.getPasswordHash().trim().isEmpty()) {
                throw new RuntimeException("Password cannot be empty");
            }
            
            UserEntity userEntity = userMapper.toEntity(userCreateRequest);
            userEntity.setPasswordHash(hashPassword.hashPassword(userCreateRequest.getPasswordHash()));
            userRepository.save(userEntity);
            return new UserResponse(userEntity, "User create successful");
        } catch (Exception e) {
            throw new RuntimeException("Error creating user: " + e.getMessage(), e);
        }
    }

    @Override
    public UserResponse updateUser(Long userId, UserUpdateRequest userUpdateRequest) {
        try {
            UserEntity userEntity = userRepository.findByUserId(userId);
            if (userEntity == null) {
                throw new RuntimeException("User not found");
            }

            Long currentUserId = getCurrentUser.getCurrentUserId();

            if (isSuperAdmin(userEntity)) {
                if (!userId.equals(currentUserId) && !isCurrentUserSuperAdmin()) {
                    throw new RuntimeException("Only superadmin can update another superadmin");
                }
            }

            userEntity = userMapper.updateEntity(userUpdateRequest, userEntity);
            userRepository.save(userEntity);
            return new UserResponse(userEntity, "User update successful");
        } catch (Exception e) {
            throw new RuntimeException("Error update user: " + e.getMessage(), e);
        }
    }

    @Override
    public String requestChangePassword() {
        try {
            String email = getCurrentUser.getCurrentUserEmail();
            redisBooleanTemplate.opsForValue().set(email, false);

            String otp = otpSend.generateOTP();
            otpSend.saveOTP(email, otp);
            EmailDTO emailDTO = new EmailDTO(email, "OTP sent successfully", otp);
            emailProducer.sendEmailAsync(emailDTO);
            return "OTP sent successfully";
        } catch (Exception e) {
            throw new RuntimeException("Error requesting password change: " + e.getMessage(), e);
        }
    }

    @Override
    public String verifyChangePassword(String otp) {
        try {
            String email = getCurrentUser.getCurrentUserEmail();
            if (!otpSend.validateOTP(email, otp)) {
                redisBooleanTemplate.opsForValue().set(email, false);
                throw new RuntimeException("Invalid OTP");
            }
            redisBooleanTemplate.opsForValue().set(email, true, Duration.ofMinutes(5));
            return "Verify change password successfully";
        } catch (Exception e) {
            throw new RuntimeException("Error verify change password" + e.getMessage() + e);
        }
    }

    @Override
    public UserResponse changePassword(ChangePasswordRequest changePassword) {
        try {
            String email = getCurrentUser.getCurrentUserEmail();
            UserEntity userEntity = userRepository.findByEmail(email);
            if (!hashPassword.verifyPassword(changePassword.getOldPassword(), userEntity.getPasswordHash())) {
                throw new RuntimeException("Old password need correct");
            }
            if (!Boolean.TRUE.equals(redisBooleanTemplate.opsForValue().get(email))) {
                throw new RuntimeException("Verify request change password failed");
            }
            userEntity.setPasswordHash(hashPassword.hashPassword(changePassword.getNewPassword()));
            userRepository.save(userEntity);
            redisBooleanTemplate.delete(email);
            return new UserResponse(userEntity, "Change password successfully");
        } catch (Exception e) {
            throw new RuntimeException("Error change password" + e.getMessage() + e);
        }
    }

    @Override
    public String requestChangeEmail() {
        try {
            String email = getCurrentUser.getCurrentUserEmail();
            redisBooleanTemplate.opsForValue().set(email, false);
            String otp = otpSend.generateOTP();
            otpSend.saveOTP(email, otp);
            EmailDTO emailDTO = new EmailDTO(email, "Request Change Email", otp);
            emailProducer.sendEmailAsync(emailDTO);
            return "OTP sent successfully";

        } catch (Exception e) {
            throw new RuntimeException("Error requesting email change: " + e.getMessage(), e);
        }
    }

    @Override
    public String verifyChangeEmail(String otp) {
        try {
            String email = getCurrentUser.getCurrentUserEmail();
            if (!otpSend.validateOTP(email, otp)) {
                redisBooleanTemplate.opsForValue().set(email, false);
                throw new RuntimeException("Invalid OTP");
            }
            redisBooleanTemplate.opsForValue().set(email, true, Duration.ofMinutes(5));
            return "Verify change email successfully";
        } catch (Exception e) {
            throw new RuntimeException("Error verify change email" + e.getMessage() + e);
        }
    }

    @Override
    public AuthenticationResponse changeEmail(String email) {
        try {
            if (userRepository.existsByEmail(email)) {
                throw new RuntimeException("Email already exists");
            }

            String currentEmail = getCurrentUser.getCurrentUserEmail();
            if (!Boolean.TRUE.equals(redisBooleanTemplate.opsForValue().get(currentEmail))) {
                throw new RuntimeException("Verify request change email failed");
            }

            String otp = otpSend.generateOTP();
            otpSend.saveOTP(email, otp);
            EmailDTO emailDTO = new EmailDTO(email, "Verify New Email", otp);
            String sessionId = emailSessionUtil.createEmailSession(email);
            emailProducer.sendEmailAsync(emailDTO);
            redisBooleanTemplate.delete(currentEmail);
            return new AuthenticationResponse(sessionId, "OTP sent successfully");
        } catch (Exception e) {
            throw new RuntimeException("Error change email" + e.getMessage() + e);
        }
    }

    @Override
    public UserResponse verifyEmail(OtpVerificationRequest otpVerificationRequest) {
        try {
            String email = emailSessionUtil.getEmailFromSession(otpVerificationRequest.getSessionId());
            UserEntity userEntity = getCurrentUser.getCurrentUser();
            if (!otpSend.validateOTP(email, otpVerificationRequest.getOtp())) {
                throw new RuntimeException("Invalid OTP");
            }

            userEntity.setEmail(email);
            userRepository.save(userEntity);
            emailSessionUtil.invalidateSession(otpVerificationRequest.getSessionId());
            return new UserResponse(userEntity, "Verify email successfully");
        } catch (Exception e) {
            throw new RuntimeException("Error verify email" + e.getMessage() + e);
        }
    }

    @Override
    public List<UserEntity> getAllUsers() {
        try {
            List<UserEntity> users = userRepository.findAll();
            // If current user is not a superadmin, filter out superadmin users
            if (!isCurrentUserSuperAdmin()) {
                users = users.stream()
                    .filter(user -> !isSuperAdmin(user))
                    .toList();
            }
            return users;
        } catch (Exception e) {
            throw new RuntimeException("Error getting all user: " + e.getMessage(), e);
        }
    }

    @Override
    public UserEntity getUserById(Long userId) {
        try {
            UserEntity userEntity = userRepository.findByUserId(userId);
            if (userEntity == null) {
                throw new RuntimeException("User not found");
            }
            // Non-superadmin users cannot view superadmin users
            if (isSuperAdmin(userEntity) && !isCurrentUserSuperAdmin()) {
                throw new RuntimeException("You don't have permission to view this user");
            }
            return userEntity;
        } catch (Exception e) {
            throw new RuntimeException("Error getting user by id: " + e.getMessage(), e);
        }
    }

    @Override
    public UserResponse deleteUser(Long userId) {
        try {
            UserEntity userEntity = userRepository.findByUserId(userId);
            if (userEntity == null) {
                throw new RuntimeException("User not found");
            }

            Long currentUserId = getCurrentUser.getCurrentUserId();

            // Không cho phép user tự xóa chính mình
            if (userId.equals(currentUserId)) {
                throw new RuntimeException("Cannot delete yourself");
            }

            /* Chỉ SUPERADMIN mới có thể xóa user khác
            if (!isCurrentUserSuperAdmin()) {
                throw new RuntimeException("Only SUPERADMIN can delete users");
            }*/

            // SUPERADMIN không thể bị xóa
            if (isSuperAdmin(userEntity)) {
                throw new RuntimeException("Cannot delete SUPERADMIN");
            }

            userRepository.delete(userEntity);
            return new UserResponse(userEntity, "User delete successful");
        } catch (Exception e) {
            throw new RuntimeException("Error deleting user: " + e.getMessage(), e);
        }
    }

    @Override
    public UserResponse getCurrentUserProfile() {
        try {
            UserEntity userEntity = userRepository.findByUserId(getCurrentUser.getCurrentUserId());
            return new UserResponse(userEntity, "User profile get successful");
        } catch (Exception e) {
            throw new RuntimeException("Error getting current user profile: " + e.getMessage(), e);
        }
    }

    @Override
    public UserResponse updateUserProfile(UserUpdateRequest userUpdateRequest) {
        try {
            UserEntity userEntity = userRepository.findByUserId(getCurrentUser.getCurrentUserId());
            userEntity = userMapper.updateEntity(userUpdateRequest, userEntity);
            userEntity.setIsVerified(userEntity.getIsVerified());
            userEntity.setIsActive(userEntity.getIsActive());
            userEntity.setRoles(userEntity.getRoles());
            userRepository.save(userEntity);
            return new UserResponse(userEntity, "User profile update successful");
        } catch (Exception e) {
            throw new RuntimeException("Error update user profile: " + e.getMessage(), e);
        }
    }

    @Override
    public UserResponse updateAvatar(MultipartFile file) throws IOException {
        try {
            UserEntity userEntity = userRepository.findByUserId(getCurrentUser.getCurrentUserId());
            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap("resource_type", "auto"));
            String avatarUrl = uploadResult.get("secure_url").toString();
            userEntity.setAvatarUrl(avatarUrl);
            userRepository.save(userEntity);
            uploadResult.remove("secure_url");
            return new UserResponse(userEntity, "Avatar update successful");
        } catch (Exception e) {
            throw new IOException("Error updating avatar: " + e.getMessage(), e);
        }
    }

    private boolean isSuperAdmin(UserEntity userEntity) {
        return userEntity.getRoles().stream()
                .anyMatch(role -> "ROLE_SUPERADMIN".equals(role.getRoleName()));
    }

    private boolean isCurrentUserSuperAdmin() {
        UserEntity currentUser = getCurrentUser.getCurrentUser();
        if (currentUser == null) {
            return false;
        }
        return isSuperAdmin(currentUser);
    }
}