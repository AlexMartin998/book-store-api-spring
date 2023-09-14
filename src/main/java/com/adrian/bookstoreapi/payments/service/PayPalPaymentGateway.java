package com.adrian.bookstoreapi.payments.service;

import com.adrian.bookstoreapi.orders.entity.Order;
import com.adrian.bookstoreapi.payments.dto.PayPalAccessTokenResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;


@Service
public class PayPalPaymentGateway implements PaymentGateway {

    @Value("${app.paypal.api-base}")
    private String PAYPAL_API_BASE;

    @Value("${app.paypal.client-id}")
    private String PAYPAL_CLIENT_ID;

    @Value("${app.paypal.secret}")
    private String PAYPAL_SECRET;


    @Override
    public Object createOrder(Order order, String successUrl, String cancelUrl) {
        return null;
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
