package com.adrian.bookstoreapi.orders.controller;

import com.adrian.bookstoreapi.common.constants.PaginationConstants;
import com.adrian.bookstoreapi.orders.dto.OrderResponseDto;
import com.adrian.bookstoreapi.orders.dto.PaginatedOrdersResponseDto;
import com.adrian.bookstoreapi.orders.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;


    @GetMapping
    public ResponseEntity<PaginatedOrdersResponseDto> findAllByCustomer(
            @RequestParam(defaultValue = PaginationConstants.DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = PaginationConstants.DEFAULT_SIZE) int size,
            @RequestParam(defaultValue = PaginationConstants.DEFAULT_SORT_BY) String sortBy,
            @RequestParam(defaultValue = PaginationConstants.DEFAULT_SORT_DIR) String sortDir,
            Authentication authentication
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sortDir);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        String authUserEmail = ((UserDetails) authentication.getPrincipal()).getUsername(); // (email/uuid/username)

        return ResponseEntity.ok(orderService.findAllByCustomer(pageable, authUserEmail));
    }

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
