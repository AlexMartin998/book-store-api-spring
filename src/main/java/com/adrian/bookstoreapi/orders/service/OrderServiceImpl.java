package com.adrian.bookstoreapi.orders.service;

import com.adrian.bookstoreapi.books.entity.Book;
import com.adrian.bookstoreapi.books.repository.BookRepository;
import com.adrian.bookstoreapi.common.constants.PaymentStatus;
import com.adrian.bookstoreapi.common.exceptions.ResourceNotFoundException;
import com.adrian.bookstoreapi.orders.dto.CreateOrderRequestDto;
import com.adrian.bookstoreapi.orders.dto.OrderResponseDto;
import com.adrian.bookstoreapi.orders.entity.Order;
import com.adrian.bookstoreapi.orders.entity.OrderItem;
import com.adrian.bookstoreapi.orders.repository.OrderItemRepository;
import com.adrian.bookstoreapi.orders.repository.OrderRepository;
import com.adrian.bookstoreapi.payments.service.PaymentGateway;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final BookRepository bookRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ModelMapper modelMapper;


    @Override
    @Transactional
    public OrderResponseDto create(CreateOrderRequestDto orderRequestDto, String authUserEmail) {
        Order order = new Order();

        // calc total amount
        BigDecimal totalAmount = BigDecimal.ZERO;

        List<OrderItem> orderItems = new ArrayList<>();
        for (Long bookId : orderRequestDto.getBookIds()) {
            Book book = findOneBookById(bookId);

            OrderItem orderItem = new OrderItem();
            orderItem.setBook(book);
            orderItem.setPriceAtPurchase(book.getPrice());
            orderItem.setDownloadsAvailable(3);
            orderItem.setOrder(order);

            orderItems.add(orderItem);

            // calc total amount
            totalAmount = totalAmount.add(book.getPrice());
        }

        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);
        order.setPaymentStatus(PaymentStatus.PENDING);

        orderRepository.save(order);
        orderItemRepository.saveAll(orderItems);

        return modelMapper.map(order, OrderResponseDto.class);
    }


    private Book findOneBookById(Long bookId) {
        return bookRepository.findById(bookId).orElseThrow(
                () -> new ResourceNotFoundException("Book", "ID", bookId)
        );
    }

}
