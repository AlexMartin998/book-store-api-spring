package com.adrian.bookstoreapi.orders.entity;

import com.adrian.bookstoreapi.common.constants.PaymentStatus;
import com.adrian.bookstoreapi.users.entity.Usuario;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Data
@Entity
@Table(name = "_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference("customer_order_ref")
    private Usuario customer;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY) // mappedBy = prop name in @ManyToOne | Eager to PayPal itemAmount
    @JsonManagedReference("order_orderitem_ref")
    private List<OrderItem> orderItems;


    @PrePersist
    private void prePersist() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    private void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
