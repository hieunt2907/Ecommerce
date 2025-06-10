package com.shino.ecommerce.features.sellers.services;

import com.shino.ecommerce.core.GetCurrentUser;
import com.shino.ecommerce.features.sellers.dto.request.SellerCreateRequest;
import com.shino.ecommerce.features.sellers.dto.request.SellerUpdateRequest;
import com.shino.ecommerce.features.sellers.dto.response.SellerResponse;
import com.shino.ecommerce.features.sellers.entity.SellerEntity;
import com.shino.ecommerce.features.sellers.mapper.SellerMapper;
import com.shino.ecommerce.features.sellers.repository.SellerRepository;
import com.shino.ecommerce.features.user.entity.RoleEntity;
import com.shino.ecommerce.features.user.entity.UserEntity;
import com.shino.ecommerce.features.user.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {
    private final SellerRepository sellerRepository;
    private final SellerMapper sellerMapper;
    private final GetCurrentUser getCurrentUser;
    private final RoleRepository roleRepository;

    @Override
    public SellerResponse createSeller(SellerCreateRequest sellerCreateRequest) {
        try {
            UserEntity userEntity = getCurrentUser.getCurrentUser();
            SellerEntity sellerEntity = sellerMapper.toEntity(sellerCreateRequest);
            sellerEntity.setUser(userEntity);
            RoleEntity roleSeller = roleRepository.findByRoleId(4L);
            if (roleSeller == null) {
                throw new RuntimeException("Role Seller not found");
            }
            userEntity.setRoles(List.of(roleSeller));
            sellerRepository.save(sellerEntity);
            return new SellerResponse(sellerEntity, "Seller created successfully");
        } catch (Exception e) {
            throw new RuntimeException("Error creating seller: " + e.getMessage(), e);
        }
    }

    @Override
    public SellerEntity getSellerById(Long sellerId) {
        try {
            return sellerRepository.findBySellerId(sellerId);
        } catch (Exception e) {
            throw new RuntimeException("Error getting seller by id: " + e.getMessage(), e);
        }
    }

    @Override
    public List<SellerEntity> getAllSellers() {
        try {
            return sellerRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error getting all sellers: " + e.getMessage(), e);
        }
    }

    @Override
    public SellerResponse updateSeller(Long sellerId, SellerUpdateRequest sellerUpdateRequest) {
        try {
            SellerEntity sellerEntity = sellerRepository.findBySellerId(sellerId);
            if (sellerEntity == null) {
                throw new RuntimeException("Seller not found");
            }

            sellerEntity = sellerMapper.updateEntity(sellerUpdateRequest, sellerEntity);
            sellerRepository.save(sellerEntity);
            return new SellerResponse(sellerEntity, "Seller updated successfully");
        } catch (Exception e) {
            throw new RuntimeException("Error updating seller: " + e.getMessage(), e);
        }
    }

    @Override
    public SellerResponse deleteSeller(Long sellerId) {
        try {
            SellerEntity sellerEntity = sellerRepository.findBySellerId(sellerId);
            if (sellerEntity == null) {
                throw new RuntimeException("Seller not found");
            }
            sellerRepository.delete(sellerEntity);
            return new SellerResponse(sellerEntity, "Seller deleted successfully");
        } catch (Exception e) {
            throw new RuntimeException("Error deleting seller: " + e.getMessage(), e);
        }
    }

    @Override
    public SellerEntity getCurrentSeller() {
        try {
            Long userId = getCurrentUser.getCurrentUserId();
            SellerEntity sellerEntity = sellerRepository.findByUserId(userId);
            if (sellerEntity == null) {
                throw new RuntimeException("Current seller not found");
            }
            return sellerEntity;
        } catch (Exception e) {
            throw new RuntimeException("Error getting current seller: " + e.getMessage(), e);
        }
    }

    @Override
    public SellerResponse updateCurrentSeller(SellerUpdateRequest sellerUpdateRequest) {
        try {
            Long userId = getCurrentUser.getCurrentUserId();
            SellerEntity sellerEntity = sellerRepository.findByUserId(userId);
            if (sellerEntity == null) {
                throw new RuntimeException("Current seller not found");
            }
            sellerEntity = sellerMapper.updateEntity(sellerUpdateRequest, sellerEntity);

            sellerRepository.save(sellerEntity);
            return new SellerResponse(sellerEntity, "Current seller updated successfully");
        } catch (Exception e) {
            throw new RuntimeException("Error updating current seller: " + e.getMessage(), e);
        }
    }
}
