package com.shino.ecommerce.features.shop.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.shino.ecommerce.core.GetCurrentUser;
import com.shino.ecommerce.features.auth.entity.AuthEntity;
import com.shino.ecommerce.features.shop.dto.request.ShopCreateRequest;
import com.shino.ecommerce.features.shop.dto.request.ShopUpdateRequest;
import com.shino.ecommerce.features.shop.entity.ShopEntity;
import com.shino.ecommerce.features.shop.enums.StatusEnum;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ShopMapper {
    private final GetCurrentUser getCurrentUser;
    public ShopEntity toEntity(ShopCreateRequest shopCreateRequest) {
        AuthEntity currentUser = getCurrentUser.getCurrentUser();
        ShopEntity shopEntity = new ShopEntity();
        if (currentUser != null) {
            AuthEntity authEntity = new AuthEntity();
            authEntity.setId(currentUser.getId());
            shopEntity.setAuth(authEntity);
        }
        shopEntity.setShopName(shopCreateRequest.getShopName());
        shopEntity.setShopDescription(shopCreateRequest.getShopDescription());
        shopEntity.setLogo_url(shopCreateRequest.getLogo_url());
        shopEntity.setBanner_url(shopCreateRequest.getBanner_url());
        shopEntity.setEmail(shopCreateRequest.getEmail());
        shopEntity.setPhoneNumber(shopCreateRequest.getPhoneNumber());
        shopEntity.setAddress(shopCreateRequest.getAddress());
        shopEntity.setStatus(StatusEnum.PENDING);
        shopEntity.setCreatedAt(LocalDateTime.now());
        return shopEntity;
    }

    public ShopEntity toEntity(ShopUpdateRequest shopUpdateRequest) {
        ShopEntity shopEntity = new ShopEntity();
        shopEntity.setShopName(shopUpdateRequest.getShopName());
        shopEntity.setShopDescription(shopUpdateRequest.getShopDescription());
        shopEntity.setLogo_url(shopUpdateRequest.getLogo_url());
        shopEntity.setBanner_url(shopUpdateRequest.getBanner_url());
        shopEntity.setEmail(shopUpdateRequest.getEmail());
        shopEntity.setPhoneNumber(shopUpdateRequest.getPhoneNumber());
        shopEntity.setAddress(shopUpdateRequest.getAddress());
        shopEntity.setStatus(StatusEnum.PENDING);
        shopEntity.setUpdatedAt(LocalDateTime.now());
        return shopEntity;
    }
}
