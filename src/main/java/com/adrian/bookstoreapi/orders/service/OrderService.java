package com.adrian.bookstoreapi.orders.service;

import com.adrian.bookstoreapi.orders.dto.CreateOrderRequestDto;
import com.adrian.bookstoreapi.orders.dto.OrderResponseDto;

public interface OrderService {

    OrderResponseDto create(CreateOrderRequestDto orderRequestDto, String authUserEmail);

}
