package com.shino.ecommerce.features.categories.controller.superadmin.category;

import com.shino.ecommerce.features.categories.dto.request.CategoryCreateRequest;
import com.shino.ecommerce.features.categories.service.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/superadmin/category")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/create")
    public ResponseEntity<?> createBrand(@RequestBody CategoryCreateRequest categoryCreateRequest) {
        try {
            return ResponseEntity.ok().body(categoryService.createCategory(categoryCreateRequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
