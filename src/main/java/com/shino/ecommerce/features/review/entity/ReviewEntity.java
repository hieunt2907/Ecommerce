package com.shino.ecommerce.features.review.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.shino.ecommerce.features.product.entity.ProductEntity;
import com.shino.ecommerce.features.user.entity.UserEntity;
import com.shino.ecommerce.features.order.entity.OrderEntity;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

    @Column(nullable = false)
    private Integer rating; // 1-5

    @Column(name = "review_title")
    private String reviewTitle;

    @Column(name = "review_content", columnDefinition = "TEXT")
    private String reviewContent;

    @Column(name = "is_verified_purchase")
    private Boolean isVerifiedPurchase = true;

    @Column(name = "is_anonymous")
    private Boolean isAnonymous = false;

    @Column(name = "likes_count")
    private Integer likesCount = 0;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relationships
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private List<ReviewImageEntity> images;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private Set<ReviewLikeEntity> likes;
}
