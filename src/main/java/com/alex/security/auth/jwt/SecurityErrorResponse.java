package com.alex.security.auth.jwt;

import com.alex.security.common.dto.ErrorDetailsDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


@Component
@RequiredArgsConstructor
public class SecurityErrorResponse {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    private final ObjectMapper objectMapper;


    public void sendErrorResponse(HttpServletRequest request, HttpServletResponse response, int status, String message) throws IOException {
        ErrorDetailsDto errorDetails = ErrorDetailsDto.builder()
                .timeStamp(new Date())
                .message(message)
                .details(request.getRequestURI())
                .build();

        response.setStatus(status);
        response.setContentType("application/json");
        objectMapper.setDateFormat(dateFormat);
        response.getWriter().write(objectMapper.writeValueAsString(errorDetails));
    }
}
