package com.shino.ecommerce.features.user.controllers.user;

import com.shino.ecommerce.features.user.dto.request.OtpVerificationRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.shino.ecommerce.features.user.dto.request.ChangePasswordRequest;
import com.shino.ecommerce.features.user.dto.request.UserUpdateRequest;
import com.shino.ecommerce.features.user.services.user.Userservice;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user/users")
public class UserController {
    private final Userservice userservice;

    @PostMapping("/password/request")
    public ResponseEntity<?> requesetChangePassword() {
        return ResponseEntity.ok(userservice.requestChangePassword());
    }

    @PostMapping("/password/verify")
    public ResponseEntity<?> verifyChangePassword(@RequestParam String otp) {
        return ResponseEntity.ok(userservice.verifyChangePassword(otp));
    }

    @PatchMapping("/password/confirm")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        return ResponseEntity.ok(userservice.changePassword(changePasswordRequest));
    }

    @PostMapping("/email/request")
    public ResponseEntity<?> requestChangeEmail() {
        try {
            return ResponseEntity.ok(userservice.requestChangeEmail());
        } catch (Exception e) {
            throw new RuntimeException("Error requesting change email: " + e.getMessage(), e);
        }
    }

    @PostMapping("/email/verify")
    public ResponseEntity<?> verifyChangeEmail(@RequestParam String otp) {
        try {
            return ResponseEntity.ok(userservice.verifyChangeEmail(otp));
        } catch (Exception e) {
            throw new RuntimeException("Error verify change email: " + e.getMessage(), e);
        }
    }

    @PostMapping("/email/change")
    public ResponseEntity<?> changeEmail(@RequestParam String email) {
        try {
            return ResponseEntity.ok(userservice.changeEmail(email));
        } catch (Exception e) {
            throw new RuntimeException("Error change email: " + e.getMessage(), e);
        }
    }

    @PatchMapping("/email/confirm")
    public ResponseEntity<?> confirmEmail(@RequestBody OtpVerificationRequest otpVerificationRequest) {
        try {
            return ResponseEntity.ok(userservice.verifyEmail(otpVerificationRequest));
        } catch (Exception e) {
            throw new RuntimeException("Error confirm email: " + e.getMessage(), e);
        }
    }

    @GetMapping("/get")
    public ResponseEntity<?> getCurrentUserProfile() {
        try {
            return ResponseEntity.ok(userservice.getCurrentUserProfile());
        } catch (Exception e) {
            throw new RuntimeException("Error getting current user profile: " + e.getMessage(), e);
        }
    }


    @PutMapping("/update")
    public ResponseEntity<?> updateUserProfile(@Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        try {
            return ResponseEntity.ok(userservice.updateUserProfile(userUpdateRequest));
        } catch (Exception e) {
            throw new RuntimeException("Error update user: " + e.getMessage(), e);
        }
    }
}
