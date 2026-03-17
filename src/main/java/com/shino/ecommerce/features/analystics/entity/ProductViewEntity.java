package com.shino.ecommerce.features.analystics.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import com.shino.ecommerce.features.product.entity.ProductEntity;

import com.shino.ecommerce.features.user.entity.UserEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_views")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductViewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "view_id")
    private Long viewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;

    @CreationTimestamp
    @Column(name = "viewed_at")
    private LocalDateTime viewedAt;
}
