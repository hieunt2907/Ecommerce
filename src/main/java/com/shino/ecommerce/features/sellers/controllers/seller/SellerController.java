package com.shino.ecommerce.features.sellers.controllers.seller;

import com.shino.ecommerce.features.sellers.services.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/seller/seller")
public class SellerController {
    private final SellerService sellerService;

}
