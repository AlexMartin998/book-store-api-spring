package com.adrian.bookstoreapi.payments.service;

import com.adrian.bookstoreapi.books.entity.Book;
import com.adrian.bookstoreapi.orders.entity.Order;
import com.adrian.bookstoreapi.orders.repository.OrderRepository;
import com.adrian.bookstoreapi.payments.dto.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class PayPalPaymentGateway implements PaymentGateway {

    @Value("${app.paypal.api-base}")
    private String PAYPAL_API_BASE;

    @Value("${app.paypal.client-id}")
    private String PAYPAL_CLIENT_ID;

    @Value("${app.paypal.secret}")
    private String PAYPAL_SECRET;

    private final OrderRepository orderRepository;

    @PostConstruct
    private void init() {
        Order order = orderRepository.findById(2L).orElseThrow();
        createOrder(order, "https://google.com", "https://google.com");
    }

    @Override
    public Object createOrder(Order order, String successUrl, String cancelUrl) {
        String url = String.format("%s/v2/checkout/orders", PAYPAL_API_BASE);

        PayPalOrderRequestDto orderRequest = new PayPalOrderRequestDto();
        orderRequest.setIntent(PayPalOrderRequestDto.Intent.CAPTURE);

        PayPalApplicationContextDto applicationContext = new PayPalApplicationContextDto();
        applicationContext.setBrandName("Book | Shop");
        applicationContext.setReturnUrl(successUrl);
        applicationContext.setCancelUrl(cancelUrl);

        orderRequest.setApplicationContext(applicationContext);


        // // create single purchase unit of sales order (1 unidad de compra)
        PayPalPurchaseUnitDto purchaseUnit = new PayPalPurchaseUnitDto();
        purchaseUnit.setReferenceId(order.getId().toString());  // orderId to link transactionId with orderId

        PayPalAmountDto purchaseAmount = new PayPalAmountDto();
        purchaseAmount.setCurrencyCode(PayPalAmountDto.CurrencyCode.USD);
        purchaseAmount.setValue(order.getTotalAmount().toString()); // paypal requires it as string   <--  Total PayPalAmountDto

        PayPalAmountDto itemsAmount = new PayPalAmountDto();        // for Breakdown of purchaseAmount
        itemsAmount.setCurrencyCode(PayPalAmountDto.CurrencyCode.USD);
        itemsAmount.setValue(order.getTotalAmount().toString());

        purchaseAmount.setBreakdown(new PayPalAmountDto.Breakdown(itemsAmount));

        purchaseUnit.setAmount(purchaseAmount);
        purchaseUnit.setItems(new ArrayList<>());


        // // set orderItems in paypal purchase unit
        order.getOrderItems().forEach(itemOrder -> {
            // // since the order is being BUILT in our backend, we have the actual prices, and therefore we can rely on them.
            // If we were to use the PayPal SDK in Angular, we would have to verify each price coming from the client with the price in DB.

            Book book = itemOrder.getBook();

            PayPalOrderItemDto payPalOrderItem = new PayPalOrderItemDto();
            payPalOrderItem.setName(book.getTitle());
            payPalOrderItem.setSku(book.getId().toString());   // or slug
            payPalOrderItem.setQuantity("1"); // 'cause it's a digital resource that is acquired in a single unit


            PayPalAmountDto paypalUnitAmount = new PayPalAmountDto();
            paypalUnitAmount.setCurrencyCode(PayPalAmountDto.CurrencyCode.USD);
            paypalUnitAmount.setValue(itemOrder.getPriceAtPurchase().toString());

            payPalOrderItem.setUnitAmount(paypalUnitAmount);
            purchaseUnit.getItems().add(payPalOrderItem);
        });

        orderRequest.setPurchaseUnits(Collections.singletonList(purchaseUnit));


        // // http request to create order in PayPal (Body needs to be sent as a JSON)
        String accessToken = getAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);   // send bearer token

        HttpEntity<PayPalOrderRequestDto> entity = new HttpEntity<>(orderRequest, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<PayPalOrderResponseDto> response = restTemplate.postForEntity(url, entity, PayPalOrderResponseDto.class);

        return response.getBody();
    }

    @Override
    public Object processPayment() {
        return null;
    }

    @Override
    public Object getTransactionStatus() {
        return null;
    }


    // // get paypal access token
    //    @PostConstruct  // in this case, it helps me to debug
    private String getAccessToken() {
        String url = String.format("%s/v1/oauth2/token", PAYPAL_API_BASE);
        RestTemplate restTemplate = new RestTemplate();  // http client in spring like axios in js

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(PAYPAL_CLIENT_ID, PAYPAL_SECRET);

        // Poder hacer request desde spring enviando 1 FROM_URLENCODED
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(form, headers);  // x-www-form-urlencoded

        // request with restTemplate
        ResponseEntity<PayPalAccessTokenResponseDto> response = restTemplate.postForEntity(url, entity, PayPalAccessTokenResponseDto.class);


        // response is a wrapper, so to access its data we need to use .getBody()
        return Objects.requireNonNull(response.getBody())
                .getAccessToken();

    }

}
