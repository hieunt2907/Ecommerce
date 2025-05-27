package com.shino.ecommerce.features.user.controller.users;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shino.ecommerce.features.user.dto.request.CreateUserRequest;
import com.shino.ecommerce.features.user.dto.request.UpdateUserRequest;
import com.shino.ecommerce.features.user.dto.response.CreateUserResponse;
import com.shino.ecommerce.features.user.service.UserService;

import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<CreateUserResponse> createUser(@Valid @RequestBody CreateUserRequest createUserRequest) {
        try {
            CreateUserResponse response = userService.createUser(createUserRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new CreateUserResponse("Failed to create user: " + e.getMessage(), null));
        }
    }
    
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        try {
            return ResponseEntity.ok(userService.getCurrentUserProfile());
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Failed to get user profile: " + e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UpdateUserRequest updateUserRequest) {
        try {
            return ResponseEntity.ok(userService.updateCurrentUserProfile(updateUserRequest));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to update user: " + e.getMessage());
        }
    }
}
