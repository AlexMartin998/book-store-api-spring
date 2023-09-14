package com.adrian.bookstoreapi.payments.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class PayPalOrderItemDto {

    private String name;
    private String sku;
    private String quantity;

    @JsonProperty("unit_amount")
    private PayPalAmountDto unitAmount;  // paypal require unitAmount per each orderItem

}
