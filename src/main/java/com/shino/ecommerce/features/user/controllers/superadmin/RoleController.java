package com.shino.ecommerce.features.user.controllers.superadmin;

import com.shino.ecommerce.features.user.dto.request.RoleCreateRequest;
import com.shino.ecommerce.features.user.services.role.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/superadmin/roles")
public class RoleController {
    private final RoleService roleService;

    @PostMapping("/create")
    public ResponseEntity<?> createRole(@RequestBody RoleCreateRequest roleCreateRequest) {
        try {
            roleService.createRole(roleCreateRequest);
            return ResponseEntity.ok().body("Role created successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
