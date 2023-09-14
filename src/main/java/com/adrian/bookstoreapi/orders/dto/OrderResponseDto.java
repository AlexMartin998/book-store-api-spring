package com.adrian.bookstoreapi.orders.dto;

import com.adrian.bookstoreapi.common.constants.PaymentStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
public class OrderResponseDto {

    private Long id;
    private BigDecimal totalAmount;
    private PaymentStatus paymentStatus;
//    private List<OrderItemDto> orderItems;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
