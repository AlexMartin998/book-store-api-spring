package com.adrian.bookstoreapi.home.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;


@Data
public class PaymentOrderRequestDto {

    @NotNull
    private List<Long> bookIds;

    @NotBlank
    private String successUrl;

    @NotBlank
    private String cancelUrl;

}
