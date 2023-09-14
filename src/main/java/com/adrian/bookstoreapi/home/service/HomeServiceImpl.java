package com.adrian.bookstoreapi.home.service;

import com.adrian.bookstoreapi.books.dto.PaginatedBooksResponseDto;
import com.adrian.bookstoreapi.books.service.BookService;
import com.adrian.bookstoreapi.home.dto.BookHomeResponseDto;
import com.adrian.bookstoreapi.home.dto.PaginatedBooksHomeResponseDto;
import com.adrian.bookstoreapi.home.dto.PaymentOrderRequestDto;
import com.adrian.bookstoreapi.orders.dto.CreateOrderRequestDto;
import com.adrian.bookstoreapi.orders.dto.OrderResponseDto;
import com.adrian.bookstoreapi.orders.entity.Order;
import com.adrian.bookstoreapi.orders.service.OrderService;
import com.adrian.bookstoreapi.payments.dto.PayPalOrderResponseDto;
import com.adrian.bookstoreapi.payments.service.PaymentGateway;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {

    private final BookService bookService;
    private final ModelMapper modelMapper;
    private final OrderService orderService;
    private final PaymentGateway paymentGateway;


    @Override
    public PaginatedBooksHomeResponseDto findAllBooks(Pageable pageable) {
        PaginatedBooksResponseDto paginatedBooksResponseDto = this.bookService.findAll(pageable);

        return modelMapper.map(paginatedBooksResponseDto, PaginatedBooksHomeResponseDto.class);
    }

    @Override
    public List<BookHomeResponseDto> findLatestBooks() {
        return bookService.findLatestBooks().stream()
                .map(bookResponseDto -> modelMapper.map(bookResponseDto, BookHomeResponseDto.class))
                .toList();
    }


    @Override
    public BookHomeResponseDto findOneBookBySlug(String slug) {
        return modelMapper.map(bookService.findOneBySlug(slug), BookHomeResponseDto.class);
    }

    @Override
    @Transactional
    public PayPalOrderResponseDto createPaymentOrder(PaymentOrderRequestDto paymentOrderRequestDto) {
        OrderResponseDto orderResponseDto = orderService.create(
                modelMapper.map(paymentOrderRequestDto, CreateOrderRequestDto.class),
                "email@email.com");

        PayPalOrderResponseDto paypalPaymentOrder = (PayPalOrderResponseDto) paymentGateway.createOrder(
                modelMapper.map(orderResponseDto, Order.class),
                paymentOrderRequestDto.getSuccessUrl(),
                paymentOrderRequestDto.getCancelUrl());

        return paypalPaymentOrder;
    }

}
