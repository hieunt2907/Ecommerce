package com.shino.ecommerce.features.categories.controller.superadmin.brand;

import com.shino.ecommerce.features.categories.dto.request.BrandCreateRequest;
import com.shino.ecommerce.features.categories.service.brand.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/superadmin/brand")
public class BrandController {
    private final BrandService brandService;

    @PostMapping("/create")
    public ResponseEntity<?> createBrand(@RequestBody BrandCreateRequest brandCreateRequest) {
        try {
            return ResponseEntity.ok(brandService.createBrand(brandCreateRequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
