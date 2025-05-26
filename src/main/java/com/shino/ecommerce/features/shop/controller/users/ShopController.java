package com.shino.ecommerce.features.shop.controller.users;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shino.ecommerce.features.shop.dto.request.ShopCreateRequest;
import com.shino.ecommerce.features.shop.dto.request.ShopUpdateRequest;
import com.shino.ecommerce.features.shop.entity.ShopEntity;
import com.shino.ecommerce.features.shop.service.ShopService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users/shop")
public class ShopController {
    private final ShopService shopService;
    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }
    @PostMapping("/create")
    public ResponseEntity<?> createShop(@Valid @RequestBody ShopCreateRequest shopCreateRequest) {
        try {
            ShopEntity shopEntity = shopService.createShop(shopCreateRequest);
            if (shopEntity != null) {
                return ResponseEntity.ok("Waiting for approval");
            } else {
                return ResponseEntity.status(500).body("Failed to create shop");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to create shop: " + e.getMessage());
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUserShop() {
        try {
            ShopEntity shopEntity = shopService.getCurrentUserShopProfile();
            if (shopEntity != null) {
                return ResponseEntity.ok(shopEntity);
            } else {
                return ResponseEntity.status(404).body("Shop not found for the current user");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to retrieve shop: " + e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateShop(@Valid @RequestBody ShopUpdateRequest shopUpdateRequest) {
        try {
            ShopEntity updatedShop = shopService.updateCurrentUserShopProfile(shopUpdateRequest);
            if (updatedShop != null) {
                return ResponseEntity.ok(updatedShop);
            } else {
                return ResponseEntity.status(404).body("Shop not found for the current user");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to update shop: " + e.getMessage());
        }
    }

}
