package com.shino.ecommerce.features.sellers.controllers.admin;

import com.shino.ecommerce.features.sellers.dto.request.SellerUpdateRequest;
import com.shino.ecommerce.features.sellers.services.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/seller")
public class AdminSellerController {
    private final SellerService sellerService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllSellers() {
        try {
            return ResponseEntity.ok(sellerService.getAllSellers());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get")
    public ResponseEntity<?> getSellerById(@RequestParam Long sellerId) {
        try {
            return ResponseEntity.ok(sellerService.getSellerById(sellerId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateSeller(@RequestParam Long sellerId, @RequestBody SellerUpdateRequest sellerUpdateRequest) {
        try {
            return ResponseEntity.ok(sellerService.updateSeller(sellerId, sellerUpdateRequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteSeller(@RequestParam Long sellerId) {
        try {
            return ResponseEntity.ok(sellerService.deleteSeller(sellerId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
