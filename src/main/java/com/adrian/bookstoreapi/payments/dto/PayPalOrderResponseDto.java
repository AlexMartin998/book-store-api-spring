package com.adrian.bookstoreapi.payments.dto;

import lombok.Data;

import java.util.List;


@Data
public class PayPalOrderResponseDto {

    private String id;
    private String status;
    private List<Link> links;


    @Data
    public static class Link {
        private String href;
        private String rel;
        private String method;
    }

}
