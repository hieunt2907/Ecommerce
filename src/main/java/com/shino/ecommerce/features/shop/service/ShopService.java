package com.shino.ecommerce.features.shop.service;

import java.util.List;

import com.shino.ecommerce.features.shop.dto.request.ShopCreateRequest;
import com.shino.ecommerce.features.shop.dto.request.ShopUpdateRequest;
import com.shino.ecommerce.features.shop.entity.ShopEntity;
import com.shino.ecommerce.features.shop.enums.StatusEnum;


public interface ShopService {
    ShopEntity createShop(ShopCreateRequest shopCreateRequest);
    List<ShopEntity> getAllShop();
    ShopEntity getShopById(Long id);
    ShopEntity approveShop(Long id, StatusEnum status);
    ShopEntity updateShop(Long id, ShopUpdateRequest shopUpdateRequest);
    ShopEntity deleteShop(Long id);
    ShopEntity getCurrentUserShopProfile();
    ShopEntity updateCurrentUserShopProfile(ShopUpdateRequest shopUpdateRequest);
    ShopEntity deleteCurrentUserShopProfile();
}
