package com.adrian.bookstoreapi.orders.controller;

import com.adrian.bookstoreapi.orders.dto.OrderResponseDto;
import com.adrian.bookstoreapi.orders.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;


    // send book as binary and handle it as blob in Client
    @GetMapping("/{orderId}/items/{itemId}/books/download")
    public ResponseEntity<?> downloadBookByOrder(@PathVariable Long orderId, @PathVariable Long itemId) {
        return ResponseEntity.ok(orderService.downloadBookByOrder(orderId, itemId));
    }


    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> findOne(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.findOne(id));
    }

}
