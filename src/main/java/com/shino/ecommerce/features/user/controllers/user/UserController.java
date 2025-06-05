package com.shino.ecommerce.features.user.controllers.user;

import com.shino.ecommerce.features.user.dto.request.ChangePasswordRequest;
import com.shino.ecommerce.features.user.dto.request.OtpVerificationRequest;
import com.shino.ecommerce.features.user.dto.request.UserUpdateRequest;
import com.shino.ecommerce.features.user.services.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user/users")
public class UserController {
    private final UserService userservice;

    @PostMapping("/password/request")
    public ResponseEntity<?> requestChangePassword() {
        try {
            return ResponseEntity.ok(userservice.requestChangePassword());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/password/verify")
    public ResponseEntity<?> verifyChangePassword(@RequestParam String otp) {
        try {
            return ResponseEntity.ok(userservice.verifyChangePassword(otp));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/password/confirm")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        try {
            return ResponseEntity.ok(userservice.changePassword(changePasswordRequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/email/request")
    public ResponseEntity<?> requestChangeEmail() {
        try {
            return ResponseEntity.ok(userservice.requestChangeEmail());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/email/verify")
    public ResponseEntity<?> verifyChangeEmail(@RequestParam String otp) {
        try {
            return ResponseEntity.ok(userservice.verifyChangeEmail(otp));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/email/change")
    public ResponseEntity<?> changeEmail(@RequestParam String email) {
        try {
            return ResponseEntity.ok(userservice.changeEmail(email));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/email/confirm")
    public ResponseEntity<?> confirmEmail(@RequestBody OtpVerificationRequest otpVerificationRequest) {
        try {
            return ResponseEntity.ok(userservice.verifyEmail(otpVerificationRequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get")
    public ResponseEntity<?> getCurrentUserProfile() {
        try {
            return ResponseEntity.ok(userservice.getCurrentUserProfile());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PutMapping("/update")
    public ResponseEntity<?> updateUserProfile(@Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        try {
            return ResponseEntity.ok(userservice.updateUserProfile(userUpdateRequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
