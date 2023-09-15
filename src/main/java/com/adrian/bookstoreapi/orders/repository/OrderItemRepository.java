package com.adrian.bookstoreapi.orders.repository;

import com.adrian.bookstoreapi.orders.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("SELECT oi FROM OrderItem oi WHERE oi.id = :id AND oi.order.id = :orderId")
    Optional<OrderItem> findOneByOrder(Long id, Long orderId);

}
