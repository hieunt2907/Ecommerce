package com.shino.ecommerce.features.shop.service;

import org.springframework.stereotype.Component;

import com.shino.ecommerce.core.GetCurrentUser;
import com.shino.ecommerce.features.auth.entity.AuthEntity;

import com.shino.ecommerce.features.shop.dto.request.ShopCreateRequest;
import com.shino.ecommerce.features.shop.dto.request.ShopUpdateRequest;
import com.shino.ecommerce.features.shop.entity.ShopEntity;
import com.shino.ecommerce.features.shop.enums.StatusEnum;
import com.shino.ecommerce.features.shop.mapper.ShopMapper;
import com.shino.ecommerce.features.shop.repository.ShopRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService{
    private final GetCurrentUser getCurrentUser;
    private final ShopRepository shopRepository;
    private final ShopMapper shopMapper;
    // private final GetCurrentUser getCurrentUser;

    @Override
    public ShopEntity createShop(ShopCreateRequest shopCreateRequest) {
        try {
            ShopEntity shopEntity = shopMapper.toEntity(shopCreateRequest);
            return shopRepository.save(shopEntity);
        } catch (Exception e) {
            // Log the exception (not shown here for brevity)
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ShopEntity getShopById(Long id) {
        try {
            return shopRepository.findById(id).orElse(null);
        } catch (Exception e) {
            // Log the exception (not shown here for brevity)
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ShopEntity updateShop(Long id, ShopUpdateRequest shopUpdateRequest) {
        try {
            ShopEntity existingShop = shopRepository.findById(id).orElse(null);
            if (existingShop != null) {
                ShopEntity updatedShop = shopMapper.toEntity(shopUpdateRequest);
                updatedShop.setId(existingShop.getId()); // Preserve the ID
                return shopRepository.save(updatedShop);
            }
            } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }  

    @Override
    public ShopEntity deleteShop(Long id) {
        try {
            ShopEntity shopEntity = shopRepository.findById(id).orElse(null);
            if (shopEntity != null) {
                shopRepository.delete(shopEntity);
                return shopEntity; // Return the deleted entity
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ShopEntity approveShop(Long id, StatusEnum status) {
        try {
            ShopEntity shopEntity = shopRepository.findById(id).orElse(null);
            if (shopEntity != null) {
                shopEntity.setStatus(status);
                return shopRepository.save(shopEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("Failed to approve shop", e);
        }
        return null;
    }

    @Override
    public ShopEntity getCurrentUserShopProfile() {
        try {
            AuthEntity currentUser = getCurrentUser.getCurrentUser();
            if (currentUser == null) {
                throw new IllegalStateException("User not authenticated");
            }
            return shopRepository.findByAuthId(currentUser.getId());
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("Failed to get current user shop profile", e);
        }
    }

    @Override
    public ShopEntity updateCurrentUserShopProfile(ShopUpdateRequest shopUpdateRequest) {
        try {
            AuthEntity currentUser = getCurrentUser.getCurrentUser();
            if (currentUser == null) {
                throw new IllegalStateException("User not authenticated");
            }
            ShopEntity existingShop = shopRepository.findByAuthId(currentUser.getId());
            if (existingShop == null) {
                throw new IllegalStateException("Shop profile not found for current user");
            }
            
            ShopEntity updatedShop = shopMapper.toEntity(shopUpdateRequest);
            updatedShop.setId(existingShop.getId()); // Preserve the ID
            
            return shopRepository.save(updatedShop);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("Failed to update current user shop profile", e);
        }
    }

    

    // private boolean isOwnerShop(Long id) {
    //     AuthEntity currentUser = getCurrentUser.getCurrentUser();
    //     ShopEntity shopEntity = shopRepository.findById(id).orElse(null);
    //     return shopEntity != null && 
    //            shopEntity.getAuth() != null && 
    //            shopEntity.getAuth().getId().equals(currentUser.getId());
    // }
}
