package com.shino.ecommerce.features.sellers.services;

import com.shino.ecommerce.features.sellers.dto.request.SellerCreateRequest;
import com.shino.ecommerce.features.sellers.dto.request.SellerUpdateRequest;
import com.shino.ecommerce.features.sellers.dto.response.SellerResponse;
import com.shino.ecommerce.features.sellers.entity.SellerEntity;

import java.util.List;

public interface SellerService {
    SellerResponse createSeller(SellerCreateRequest sellerCreateRequest);

    SellerEntity getSellerById(Long sellerId);

    List<SellerEntity> getAllSellers();

    SellerResponse updateSeller(Long sellerId, SellerUpdateRequest sellerUpdateRequest);

    SellerResponse deleteSeller(Long sellerId);

    SellerEntity getCurrentSeller();

    SellerResponse updateCurrentSeller(SellerUpdateRequest sellerUpdateRequest);
}
