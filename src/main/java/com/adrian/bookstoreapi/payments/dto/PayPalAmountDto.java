package com.adrian.bookstoreapi.payments.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


@Data
public class PayPalAmountDto {

    @JsonProperty("currency_code")
    private CurrencyCode currencyCode;

    private String value;
    private Breakdown breakdown;

    public enum CurrencyCode {
        USD
    }


    @Data
    @RequiredArgsConstructor
    public static class Breakdown {

        @NonNull // to create a constructor with this property (@RequiredArgsConstructor)
        @JsonProperty("item_total")
        private PayPalAmountDto itemTotal;

    }

}
