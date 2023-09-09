package com.alex.security.common.exceptions;

import com.alex.security.common.dto.ErrorDetailsDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;


// Handler de TODAS las exceptions de nuestra App. -- basta con esto, ya NOO necesita mas nada, ni ser importado ni notificado a Spring
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {  // extends para crear el handler del @Valid

    // // Authentication
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDetailsDto> handlerBadCredentialsException(
            BadCredentialsException exception,
            WebRequest webRequest
    ) {

        ErrorDetailsDto errorDetails = ErrorDetailsDto.builder()
                .timeStamp(new Date())
                .message("There was a problem logging in. Check your email and password or create an account")
                .details(webRequest.getDescription(false))
                .build();

        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    // // Authorization
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDetailsDto> handlerBadCredentialsException(
            AccessDeniedException exception,
            WebRequest webRequest
    ) {

        ErrorDetailsDto errorDetails = ErrorDetailsDto.builder()
                .timeStamp(new Date())
                .message("User without required permissions!")
                .details(webRequest.getDescription(false))
                .build();

        return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
    }

    // // UsernameNotFoundException
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorDetailsDto> handlerUsernameNotFoundException(
            UsernameNotFoundException exception,
            WebRequest webRequest
    ) {
        ErrorDetailsDto errorDetails = ErrorDetailsDto.builder()
                .timeStamp(new Date())
                .message("There was a problem logging in. Check your email and password or create an account")
                .details(webRequest.getDescription(false))
                .build();

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    // // Las Exceptions de Filter (Security|JWT) NO se atrapan aqui, las manejo en el JwtService & EntryPoint)
    // JwtService (JWT Exceptions)  |   EntryPoint (404) xq SpringSecurity maneja todos los 404 como 401


    // // User not found exception
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDetailsDto> handlerUserNotFoundException(
            UserNotFoundException exception,
            WebRequest webRequest
    ) {
        ErrorDetailsDto errorDetails = ErrorDetailsDto.builder()
                .timeStamp(new Date())
                .message(exception.getMessage())
                .details(webRequest.getDescription(false))
                .build();

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

}
