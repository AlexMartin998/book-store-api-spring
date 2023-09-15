package com.adrian.bookstoreapi.home.service;

import com.adrian.bookstoreapi.books.dto.PaginatedBooksResponseDto;
import com.adrian.bookstoreapi.books.service.BookService;
import com.adrian.bookstoreapi.common.constants.PaymentStatus;
import com.adrian.bookstoreapi.home.dto.BookHomeResponseDto;
import com.adrian.bookstoreapi.home.dto.PaginatedBooksHomeResponseDto;
import com.adrian.bookstoreapi.home.dto.PaymentOrderRequestDto;
import com.adrian.bookstoreapi.home.dto.PaymentOrderResponseDto;
import com.adrian.bookstoreapi.orders.dto.CreateOrderRequestDto;
import com.adrian.bookstoreapi.orders.dto.OrderResponseDto;
import com.adrian.bookstoreapi.orders.entity.Order;
import com.adrian.bookstoreapi.orders.repository.OrderRepository;
import com.adrian.bookstoreapi.orders.service.OrderService;
import com.adrian.bookstoreapi.payments.dto.PayPalOrderPaymentCaptureResponseDto;
import com.adrian.bookstoreapi.payments.dto.PayPalOrderResponseDto;
import com.adrian.bookstoreapi.payments.service.PaymentGateway;
import com.adrian.bookstoreapi.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {

    private final BookService bookService;
    private final ModelMapper modelMapper;
    private final OrderService orderService;
    private final PaymentGateway paymentGateway;
    private final OrderRepository orderRepository;
    private final UserService userService;


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
    public PaymentOrderResponseDto createPaymentOrder(PaymentOrderRequestDto paymentOrderRequestDto) {
        OrderResponseDto orderResponseDto = orderService.create(
                modelMapper.map(paymentOrderRequestDto, CreateOrderRequestDto.class),
                "alex3@demo.com"); // TODO: req auth to buy a book

        PayPalOrderResponseDto paypalPaymentOrder = (PayPalOrderResponseDto) paymentGateway.createOrder(
                modelMapper.map(orderResponseDto, Order.class),
                paymentOrderRequestDto.getSuccessUrl(),
                paymentOrderRequestDto.getCancelUrl());

        String approveUrl = paypalPaymentOrder.getLinks().stream()
                .filter(link -> link.getRel().equals("approve"))
                .findFirst()
                .orElseThrow()
                .getHref();

        PaymentOrderResponseDto paymentOrderResponseDto = new PaymentOrderResponseDto();
        paymentOrderResponseDto.setApproveUrl(approveUrl);

        return paymentOrderResponseDto;
    }

    @Override
    @Transactional
    public Object capturePaymentOrder(String paymentOrderId) {
        PayPalOrderPaymentCaptureResponseDto orderPaymentCaptureDto =
                (PayPalOrderPaymentCaptureResponseDto) paymentGateway
                        .capturePaymentOrder(paymentOrderId);

        boolean completed = orderPaymentCaptureDto.getStatus().equals("COMPLETED");
        long orderId = 0;

        if (completed) {
            orderId = Long.parseLong(orderPaymentCaptureDto.getPurchaseUnits().get(0).getReferenceId());
            Order order = modelMapper.map(orderService.findOne(orderId), Order.class);

            // update order status
            order.setPaymentStatus(PaymentStatus.PAID);
            orderRepository.save(order);
        }

        return Map.of("completed", completed, "orderId", orderId);
    }

}
