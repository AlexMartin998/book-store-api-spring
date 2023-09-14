package com.adrian.bookstoreapi.payments.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class PayPalAccessTokenResponseDto {

    @JsonProperty("access_token") // sets the name under which this property will be serialized
    private String accessToken;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("app_id")
    private String appId;

    @JsonProperty("expires_in")
    private Long expiresIn;

}
