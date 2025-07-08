package com.shino.ecommerce.features.product.controllers.user;

import com.shino.ecommerce.features.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/product")
public class ProductController {
    private final ProductService productService;

    @GetMapping("/active-all")
    public ResponseEntity<?> getAllActiveProducts() {
        try {
            return ResponseEntity.ok(productService.getAllActiveProducts());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
