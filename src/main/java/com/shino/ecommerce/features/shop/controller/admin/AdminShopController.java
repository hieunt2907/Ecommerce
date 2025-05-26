package com.shino.ecommerce.features.shop.controller.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shino.ecommerce.features.shop.entity.ShopEntity;
import com.shino.ecommerce.features.shop.enums.StatusEnum;
import com.shino.ecommerce.features.shop.service.ShopService;

@RestController
@RequestMapping("/api/admin/shop")
public class AdminShopController {
    private final ShopService shopService;

    public AdminShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    @PatchMapping("/approve")
    public ResponseEntity<?> approveShop(Long shopId, StatusEnum status) {
        try {
            ShopEntity approved = shopService.approveShop(shopId, status);
            if (approved.getStatus() == StatusEnum.APPROVED) {
                return ResponseEntity.ok("Shop approved successfully");
            } else if (approved.getStatus() == StatusEnum.REJECTED) {
                return ResponseEntity.status(400).body("Shop rejected successfully");
            } else {
                return ResponseEntity.status(404).body("Shop not found or already approved");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to approve shop: " + e.getMessage());
        }
    }
    
}
