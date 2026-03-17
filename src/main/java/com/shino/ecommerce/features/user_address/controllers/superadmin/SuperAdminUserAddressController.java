package com.shino.ecommerce.features.user_address.controllers.superadmin;

import com.shino.ecommerce.features.user_address.dto.request.UserAddressUpdateRequest;
import com.shino.ecommerce.features.user_address.services.UserAddressService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/superadmin/addresses")
public class SuperAdminUserAddressController {
    private final UserAddressService userAddressService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllUserAddress() {
        try {
            return ResponseEntity.ok(userAddressService.getAllUserAddress());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get")
    public ResponseEntity<?> getUserAddressById(@RequestParam Long addressId) {
        try {
            return ResponseEntity.ok(userAddressService.getUserAddressById(addressId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUserAddress(@RequestParam Long addressId, @RequestBody UserAddressUpdateRequest userAddressUpdateRequest) {
        try {
            return ResponseEntity.ok(userAddressService.updateUserAddress(addressId, userAddressUpdateRequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUserAddress(@RequestParam Long addressId) {
        try {
            return ResponseEntity.ok(userAddressService.deleteUserAddress(addressId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
