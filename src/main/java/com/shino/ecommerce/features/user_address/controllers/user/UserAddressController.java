package com.shino.ecommerce.features.user_address.controllers.user;

import com.shino.ecommerce.features.user_address.dto.request.UserAddressCreateRequest;
import com.shino.ecommerce.features.user_address.dto.request.UserAddressUpdateRequest;
import com.shino.ecommerce.features.user_address.services.UserAddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/addresses")
public class UserAddressController {
    private final UserAddressService userAddressService;

    @PostMapping("/create")
    public ResponseEntity<?> createUserAddress(@Valid @RequestBody UserAddressCreateRequest userAddressCreateRequest) {
        try {
            return ResponseEntity.ok(userAddressService.createUserAddress(userAddressCreateRequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get")
    public ResponseEntity<?> getCurrentUserAddress() {
        try {
            return ResponseEntity.ok(userAddressService.getCurrentUserAddress());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateCurrentUserAddress(@Valid @RequestParam Long addressId, @RequestBody UserAddressUpdateRequest userAddressUpdateRequest) {
        try {
            return ResponseEntity.ok(userAddressService.updateCurrentUserAddress(addressId, userAddressUpdateRequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
