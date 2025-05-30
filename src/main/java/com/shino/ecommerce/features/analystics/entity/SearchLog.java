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
@Table(name = "search_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long logId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "search_query", nullable = false)
    private String searchQuery;

    @Column(name = "results_count")
    private Integer resultsCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clicked_product_id")
    private ProductEntity clickedProduct;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @CreationTimestamp
    @Column(name = "searched_at")
    private LocalDateTime searchedAt;
}
