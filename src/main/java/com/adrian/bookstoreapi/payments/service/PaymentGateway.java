package com.adrian.bookstoreapi.payments.service;

import com.adrian.bookstoreapi.orders.entity.Order;

public interface PaymentGateway {

    Object createOrder(Order order, String successUrl, String cancelUrl);

    Object processPayment();

    Object getTransactionStatus();

}
