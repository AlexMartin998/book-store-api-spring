package com.alex.security.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDetailsDto {
    private Date timeStamp;
    private String message;
    private String details;
}
