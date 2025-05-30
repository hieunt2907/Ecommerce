package com.shino.ecommerce.features.review.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;



import com.shino.ecommerce.features.user.entity.UserEntity;


import java.time.LocalDateTime;


@Entity
@Table(name = "review_likes",
       uniqueConstraints = @UniqueConstraint(columnNames = {"review_id", "user_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewLikeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private ReviewEntity review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
