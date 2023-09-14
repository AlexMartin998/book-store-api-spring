package com.adrian.bookstoreapi.payments.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;


@Data
public class PayPalOrderRequestDto {

    @JsonProperty("application_context")
    private PayPalApplicationContextDto applicationContext;

    private Intent intent;

    @JsonProperty("purchase_units")
    private List<PayPalPurchaseUnitDto> purchaseUnits;


    public enum Intent {
        CAPTURE
    }

}
