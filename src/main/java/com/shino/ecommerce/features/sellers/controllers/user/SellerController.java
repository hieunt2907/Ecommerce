package com.shino.ecommerce.features.sellers.controllers.user;

import com.shino.ecommerce.features.sellers.dto.request.SellerCreateRequest;
import com.shino.ecommerce.features.sellers.services.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class SellerController {
    private final SellerService sellerService;

    @PostMapping("/create")
    public ResponseEntity<?> createSeller(SellerCreateRequest sellerCreateRequest) {
        try {
            return ResponseEntity.ok(sellerService.createSeller(sellerCreateRequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
