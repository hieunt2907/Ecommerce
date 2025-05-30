package com.shino.ecommerce.features.categories.entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;


import com.shino.ecommerce.features.product.entity.ProductEntity;

import java.time.LocalDateTime;
import java.util.List;
@Entity
@Table(name = "brands")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrandEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brand_id")
    private Long brandId;

    @Column(name = "brand_name", unique = true, nullable = false, length = 100)
    private String brandName;

    @Column(name = "brand_logo")
    private String brandLogo;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Relationships
    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL)
    private List<ProductEntity> products;
}