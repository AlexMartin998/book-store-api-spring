package com.adrian.bookstoreapi.orders.dto;

import com.adrian.bookstoreapi.common.constants.PaymentStatus;
import com.adrian.bookstoreapi.orders.entity.OrderItem;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Data
public class OrderResponseDto {

    private Long id;
    private BigDecimal totalAmount;
    private PaymentStatus paymentStatus;
    private List<OrderItem> orderItems;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
