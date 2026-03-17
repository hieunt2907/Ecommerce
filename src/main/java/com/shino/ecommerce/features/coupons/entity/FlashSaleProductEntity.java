package com.shino.ecommerce.features.coupons.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.shino.ecommerce.features.product.entity.ProductEntity;


@Entity
@Table(name = "flash_sale_products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlashSaleProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id", nullable = false)
    private FlashSaleEntity sale;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;


    @Column(name = "original_price", nullable = false)
    private Double originalPrice;

    @Column(name = "sale_price", nullable = false)
    private Double salePrice;

    @Column(name = "quantity_limit")
    private Integer quantityLimit;

    @Column(name = "sold_quantity")
    private Integer soldQuantity = 0;

}
