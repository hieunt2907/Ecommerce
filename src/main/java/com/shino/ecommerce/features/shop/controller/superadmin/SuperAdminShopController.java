package com.shino.ecommerce.features.shop.controller.superadmin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shino.ecommerce.features.shop.entity.ShopEntity;
import com.shino.ecommerce.features.shop.enums.StatusEnum;
import com.shino.ecommerce.features.shop.service.ShopService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/superadmin/shop")
public class SuperAdminShopController {
    private final ShopService shopService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllShops() {
        try {
            return ResponseEntity.ok(shopService.getAllShop());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to retrieve shops: " + e.getMessage());
        }
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

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteShop(Long shopId) {
        try {
            ShopEntity deleted = shopService.deleteShop(shopId);
            if (deleted == null) {
                return ResponseEntity.ok("Shop deleted successfully");
            } else {
                return ResponseEntity.status(404).body("Shop not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to delete shop: " + e.getMessage());
        }
    }
}
