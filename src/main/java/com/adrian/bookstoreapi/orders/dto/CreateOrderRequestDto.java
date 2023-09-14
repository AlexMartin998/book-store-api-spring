package com.adrian.bookstoreapi.orders.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;


@Data
public class CreateOrderRequestDto {

    @NotNull
    private List<Long> bookIds;

}
