package com.adrian.bookstoreapi.payments.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;


@Data
public class PayPalOrderPaymentCaptureResponseDto {

    private String id;
    private String status;

    @JsonProperty("purchase_units")
    private List<PayPalPurchaseUnitDto> purchaseUnits; // contains our OrderId to identify a sale (link transactionId with orderId)

}
