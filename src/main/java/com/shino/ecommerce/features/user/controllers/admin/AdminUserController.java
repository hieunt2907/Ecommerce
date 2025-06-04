package com.shino.ecommerce.features.user.controllers.admin;

import com.shino.ecommerce.features.user.dto.request.UserUpdateRequest;
import com.shino.ecommerce.features.user.services.user.Userservice;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {
    private final Userservice userservice;

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestParam Long userId, @RequestBody UserUpdateRequest userUpdateRequest) {
        try {
            return ResponseEntity.ok(userservice.updateUser(userId, userUpdateRequest));
        } catch (Exception e) {
            throw new RuntimeException("Error update user: " + e.getMessage(), e);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        try {
            return ResponseEntity.ok(userservice.getAllUsers());
        } catch (Exception e) {
            throw new RuntimeException("Error getting all user: " + e.getMessage(), e);
        }
    }

    @GetMapping("/get")
    public ResponseEntity<?> getUserById(@RequestParam Long id) {
        try {
            return ResponseEntity.ok(userservice.getUserById(id));
        } catch (Exception e) {
            throw new RuntimeException("Error getting user by id: " + e.getMessage(), e);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestParam Long id) {
        try {
            return ResponseEntity.ok(userservice.deleteUser(id));
        } catch (Exception e) {
            throw new RuntimeException("Error deleting user: " + e.getMessage(), e);
        }
    }
}
