package com.adrian.bookstoreapi.home.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class PaymentOrderRequestDto {

    @NotNull
    private List<Long> bookIds;

    @NotBlank
    private String successUrl;

}
