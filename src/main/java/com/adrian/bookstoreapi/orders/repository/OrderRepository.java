package com.adrian.bookstoreapi.orders.repository;

import com.adrian.bookstoreapi.orders.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderRepository extends JpaRepository<Order, Long> {
}
