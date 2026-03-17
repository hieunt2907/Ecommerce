package com.shino.ecommerce.features.product.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shino.ecommerce.features.categories.entity.BrandEntity;
import com.shino.ecommerce.features.categories.entity.CategoryEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private BrandEntity brand;

    // Basic Product Information
    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "product_slug", unique = true, nullable = false)
    private String productSlug;

    @Column(name = "short_description", columnDefinition = "TEXT")
    private String shortDescription;

    @Column(name = "full_description", columnDefinition = "LONGTEXT")
    private String fullDescription;

    @Column(unique = true, length = 100)
    private String sku;

    // Pricing (simplified - single price)
    @Column(nullable = false)
    private Double price;

    @Column(name = "compare_price")
    private Double comparePrice;

    @Column(name = "cost_price")
    private Double costPrice;

    // Stock Management
    @Column(name = "stock_quantity")
    private Integer stockQuantity = 0;

    // Product Specifications
    @Column(precision = 8)
    private Double weight;

    @Column(length = 50)
    private String dimensions;

    @Column(length = 100)
    private String barcode;

    // Images (stored as JSON or comma-separated URLs)
    @Column(name = "image_urls", columnDefinition = "TEXT")
    private String imageUrls; // JSON array of image URLs

    @Column(name = "primary_image_url")
    private String primaryImageUrl;


    // Status
    @Enumerated(EnumType.STRING)
    private ProductStatus status = ProductStatus.ACTIVE;

    @Column(name = "is_active")
    private Boolean isActive = true;

//    // SEO and Marketing
//    @Column(name = "meta_title")
//    private String metaTitle;
//
//    @Column(name = "meta_description")
//    private String metaDescription;
//
//    @Column(name = "tags")
//    private String tags; // Comma-separated tags

    // Analytics
    @Column(name = "view_count")
    private Integer viewCount = 0;

    @Column(name = "purchase_count")
    private Integer purchaseCount = 0;

    @Column(name = "rating_average")
    private Double ratingAverage = 0.0;

    @Column(name = "rating_count")
    private Integer ratingCount = 0;

    // Timestamps
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Enums
    public enum ProductStatus {
        ACTIVE, INACTIVE, OUT_OF_STOCK, DISCONTINUED
    }
}