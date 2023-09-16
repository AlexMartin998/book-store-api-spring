package com.adrian.bookstoreapi.orders.service;

import com.adrian.bookstoreapi.orders.dto.CreateOrderRequestDto;
import com.adrian.bookstoreapi.orders.dto.OrderResponseDto;
import com.adrian.bookstoreapi.orders.dto.PaginatedOrdersResponseDto;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    OrderResponseDto create(CreateOrderRequestDto orderRequestDto, String authUserEmail);

    OrderResponseDto findOne(Long id);

    Object downloadBookByOrder(Long orderId, Long itemId);

    PaginatedOrdersResponseDto findAllByCustomer(Pageable pageable, String authUserEmail);

    OrderResponseDto findOneByCustomer(Long id, Long customerId);

    PaginatedOrdersResponseDto findAll(Pageable pageable);
}
