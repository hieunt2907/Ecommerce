package com.shino.ecommerce.features.user.controllers.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shino.ecommerce.features.user.dto.request.ChangePasswordRequest;
import com.shino.ecommerce.features.user.dto.request.UserUpdateRequest;
import com.shino.ecommerce.features.user.services.user.Userservice;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user/")
public class UserController {
    private Userservice userservice;

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestParam Long userId, @RequestBody UserUpdateRequest userUpdateRequest) {
        return ResponseEntity.ok(userservice.updateUser(userId, userUpdateRequest));
    }

    @PostMapping("/changepassword/request")
    public ResponseEntity<?> requesetChangePassword() {
        return ResponseEntity.ok(userservice.requestChangePassword());
    }

    @PostMapping("/changepassword/verify")
    public ResponseEntity<?> verifyChangePassword(@RequestParam String otp) {
        return ResponseEntity.ok(userservice.verifyChangePassword(otp));
    }

    @PatchMapping("/changepassword/confirm")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        return ResponseEntity.ok(userservice.changePassword(changePasswordRequest));
    }
}
