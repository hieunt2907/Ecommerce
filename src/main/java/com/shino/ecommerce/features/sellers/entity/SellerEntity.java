package com.shino.ecommerce.features.sellers.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shino.ecommerce.features.product.entity.ProductEntity;
import com.shino.ecommerce.features.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "sellers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seller_id")
    private Long sellerId;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "shop_name", nullable = false, length = 100)
    private String shopName;

    @Column(name = "shop_description", columnDefinition = "TEXT")
    private String shopDescription;

    @Column(name = "shop_logo")
    private String shopLogo;

    @Column(name = "business_license", length = 100)
    private String businessLicense;

    @Column(name = "tax_code", length = 50)
    private String taxCode;

    @Column(name = "is_verified")
    private Boolean isVerified = false;

    @Column(name = "seller_rating")
    private Double sellerRating = 0.0;

    @Column(name = "total_products")
    private Integer totalProducts = 0;

    @Column(name = "total_orders")
    private Integer totalOrders = 0;

    @CreationTimestamp
    @Column(name = "join_date")
    private LocalDateTime joinDate;

    @Column(name = "last_active")
    private LocalDateTime lastActive;

    // Relationships
    @JsonIgnore
    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    private List<SellerBankAccountEntity> bankAccounts;

    @JsonIgnore
    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    private List<ProductEntity> products;
}
