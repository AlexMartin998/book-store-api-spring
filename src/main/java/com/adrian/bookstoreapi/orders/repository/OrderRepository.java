package com.adrian.bookstoreapi.orders.repository;

import com.adrian.bookstoreapi.orders.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findAllByCustomerEmail(Pageable pageable, String email);

}
