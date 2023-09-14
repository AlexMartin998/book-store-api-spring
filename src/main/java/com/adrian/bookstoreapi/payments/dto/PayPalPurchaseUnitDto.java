package com.adrian.bookstoreapi.payments.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;


@Data
public class PayPalPurchaseUnitDto {

    @JsonProperty("reference_id")
    private String referenceId;

    private PayPalAmountDto amount;
    private List<PayPalOrderItemDto> items;

}
