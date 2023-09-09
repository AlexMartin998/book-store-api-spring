package com.adrian.bookstoreapi.orders.repository;

import com.adrian.bookstoreapi.orders.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
