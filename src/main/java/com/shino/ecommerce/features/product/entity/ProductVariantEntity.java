package com.shino.ecommerce.features.product.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;


import com.shino.ecommerce.features.cart.entity.CartItemEntity;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "product_variants")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "variant_id")
    private Long variantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @Column(name = "variant_name", length = 100)
    private String variantName;

    @Column(unique = true, length = 100)
    private String sku;

    @Column(nullable = false)
    private Double price;

    @Column(name = "compare_price")
    private Double comparePrice;

    @Column(name = "cost_price")
    private Double costPrice;

    @Column(name = "stock_quantity")
    private Integer stockQuantity = 0;

    @Column(precision = 8)
    private Double weight;

    @Column(length = 100)
    private String barcode;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Relationships
    @OneToMany(mappedBy = "variant", cascade = CascadeType.ALL)
    private List<ProductImageEntity> images;

    @OneToMany(mappedBy = "variant", cascade = CascadeType.ALL)
    private List<CartItemEntity> cartItems;
}