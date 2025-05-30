package com.shino.ecommerce.features.order.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;


import java.time.LocalDateTime;
import java.time.LocalDate;



@Entity
@Table(name = "order_shipping")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderShippingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipping_id")
    private Long shippingId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

    @Column(name = "shipping_method", nullable = false, length = 100)
    private String shippingMethod;

    @Column(name = "shipping_provider", length = 100)
    private String shippingProvider;

    @Column(name = "tracking_number", length = 100)
    private String trackingNumber;

    @Column(name = "recipient_name", nullable = false, length = 100)
    private String recipientName;

    @Column(name = "recipient_phone", nullable = false, length = 20)
    private String recipientPhone;

    @Column(name = "shipping_address", nullable = false, columnDefinition = "TEXT")
    private String shippingAddress;

    @Column(name = "estimated_delivery_date")
    private LocalDate estimatedDeliveryDate;

    @Column(name = "actual_delivery_date")
    private LocalDateTime actualDeliveryDate;

    @Column(name = "shipping_notes", columnDefinition = "TEXT")
    private String shippingNotes;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
