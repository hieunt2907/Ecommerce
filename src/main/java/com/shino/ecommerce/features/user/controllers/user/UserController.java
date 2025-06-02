package com.shino.ecommerce.features.user.controllers.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shino.ecommerce.features.user.dto.request.UserUpdateRequest;
import com.shino.ecommerce.features.user.services.user.Userservice;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user/")
public class UserController {
    private Userservice userservice;

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(Long userId, UserUpdateRequest userUpdateRequest) {
        return ResponseEntity.ok(userservice.updateUser(userId, userUpdateRequest));
    }
}
