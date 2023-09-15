package com.adrian.bookstoreapi.orders.service;

import com.adrian.bookstoreapi.books.entity.Book;
import com.adrian.bookstoreapi.books.repository.BookRepository;
import com.adrian.bookstoreapi.common.constants.OrderItemConstants;
import com.adrian.bookstoreapi.common.constants.PaymentStatus;
import com.adrian.bookstoreapi.common.exceptions.BadRequestException;
import com.adrian.bookstoreapi.common.exceptions.ResourceNotFoundException;
import com.adrian.bookstoreapi.common.exceptions.UnauthorizedException;
import com.adrian.bookstoreapi.orders.dto.CreateOrderRequestDto;
import com.adrian.bookstoreapi.orders.dto.OrderResponseDto;
import com.adrian.bookstoreapi.orders.entity.Order;
import com.adrian.bookstoreapi.orders.entity.OrderItem;
import com.adrian.bookstoreapi.orders.repository.OrderItemRepository;
import com.adrian.bookstoreapi.orders.repository.OrderRepository;
import com.adrian.bookstoreapi.storage.service.StorageService;
import com.adrian.bookstoreapi.users.entity.Usuario;
import com.adrian.bookstoreapi.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    @Autowired
    @Qualifier("FileSystemStorageService")
    private StorageService storageService;

    private final BookRepository bookRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;


    @Override
    @Transactional
    public OrderResponseDto create(CreateOrderRequestDto orderRequestDto, String authUserEmail) {
        Usuario customer = userService.findOneByEmail(authUserEmail);
        Order order = new Order();

        // calc total amount
        BigDecimal totalAmount = BigDecimal.ZERO;

        List<OrderItem> orderItems = new ArrayList<>();
        for (Long bookId : orderRequestDto.getBookIds()) {
            Book book = findOneBookById(bookId);

            OrderItem orderItem = new OrderItem();
            orderItem.setBook(book);
            orderItem.setPriceAtPurchase(book.getPrice());
            orderItem.setDownloadsAvailable(OrderItemConstants.Book.downloadsAvailable);
            orderItem.setOrder(order);

            orderItems.add(orderItem);

            // calc total amount
            totalAmount = totalAmount.add(book.getPrice());
        }

        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);
        order.setPaymentStatus(PaymentStatus.PENDING);
        order.setCustomer(customer);

        orderRepository.save(order);
        orderItemRepository.saveAll(orderItems);

        return modelMapper.map(order, OrderResponseDto.class);
    }

    @Override
    public OrderResponseDto findOne(Long id) {
        Order order = findOneById(id);

        return modelMapper.map(order, OrderResponseDto.class);
    }

    @Override
    public Object downloadBookByOrder(Long orderId, Long itemId) {
        Order order = findOneById(orderId);
        if (!order.getPaymentStatus().equals(PaymentStatus.PAID))
            throw new UnauthorizedException("The order hasn't been paid yet");

        OrderItem orderItem = findOneOrderItemByOrder(itemId, orderId);

        if (orderItem.getDownloadsAvailable() <= 0)
            throw new BadRequestException("Download credits have been exhausted");

        // upd downloads available
        orderItem.setDownloadsAvailable(orderItem.getDownloadsAvailable() - 1);
        orderItemRepository.save(orderItem);


        // serve book
        return storageService.loadAsResource(orderItem.getBook().getFilePath());
    }


    private Book findOneBookById(Long bookId) {
        return bookRepository.findById(bookId).orElseThrow(
                () -> new ResourceNotFoundException("Book", "ID", bookId)
        );
    }

    private Order findOneById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "ID", id));
    }

    private OrderItem findOneOrderItemByOrder(Long id, Long orderId) {
        return orderItemRepository.findOneByOrder(id, orderId).orElseThrow(
                () -> new ResourceNotFoundException("Order Item", "ID", id)
        );
    }

}
