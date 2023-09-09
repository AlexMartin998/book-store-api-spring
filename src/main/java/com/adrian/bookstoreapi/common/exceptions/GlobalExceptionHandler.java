package com.adrian.bookstoreapi.common.exceptions;

import com.adrian.bookstoreapi.common.dto.ErrorDetailsDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.*;


// Handler de TODAS las exceptions de nuestra App. -- basta con esto, ya NOO necesita mas nada, ni ser importado ni notificado a Spring
//@ControllerAdvice
@RestControllerAdvice // semantico para res
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {  // extends para crear el handler del @Valid

    private final MessageSource messageSource;

    // // Authentication || UsernameNotFoundException
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDetailsDto> handlerBadCredentialsException(BadCredentialsException exception, WebRequest webRequest) {
        return createErrorResponse(
                exception,
                "There was a problem logging in. Check your email and password or create an account",
                HttpStatus.UNAUTHORIZED,
                webRequest
        );
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorDetailsDto> handlerBadCredentialsException(UnauthorizedException exception, WebRequest webRequest) {
        return createErrorResponse(
                exception,
                exception.getMessage(),
                HttpStatus.UNAUTHORIZED,
                webRequest
        );
    }

    // // Authorization
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDetailsDto> handlerAccessDeniedException(AccessDeniedException exception, WebRequest webRequest) {
        return createErrorResponse(exception, "User without required permissions", HttpStatus.FORBIDDEN, webRequest);
    }


    // // Las Exceptions de Filter (Security|JWT) NO se atrapan aqui, las manejo en el JwtService & EntryPoint)
    // JwtService (JWT Exceptions)  |   EntryPoint (404) xq SpringSecurity maneja todos los 404 como 401


    // // User not found exception
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDetailsDto> handlerUserNotFoundException(UserNotFoundException exception, WebRequest webRequest) {
        return createErrorResponse(exception, exception.getMessage(), HttpStatus.NOT_FOUND, webRequest);
    }

    // // Bad request exception
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorDetailsDto> handlerBadRequestException(BadRequestException exception, WebRequest webRequest) {
        return createErrorResponse(exception, exception.getMessage(), HttpStatus.BAD_REQUEST, webRequest);
    }

    // // Resource Not Found Exception
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetailsDto> handlerResourceNotFoundException(ResourceNotFoundException exception, WebRequest webRequest) {
        return createErrorResponse(exception, exception.getMessage(), HttpStatus.NOT_FOUND, webRequest);
    }

    // // Uploads
    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ErrorDetailsDto> handlerMultipartException(MultipartException exception, WebRequest webRequest) {
        return createErrorResponse(exception, "File is required", HttpStatus.BAD_REQUEST, webRequest);
    }


    // // Default Exception (for ALL others)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetailsDto> handlerGlobalException(Exception exception, WebRequest webRequest) {
        return createErrorResponse(exception, exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, webRequest);
    }


    // // Spring Validation (@Valid): handler de los errors del @Valid <- BindingResult
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request
    ) {
        Map<String, List<String>> errors = new HashMap<>();
        List<String> errorMessages = ex.getBindingResult().getAllErrors().stream().map(err -> {
            String fieldName = ((FieldError) err).getField();
            String message = messageSource.getMessage(err, Locale.getDefault()); // get messages.properties

            return fieldName.concat(": ").concat(message);
        }).toList();
        errors.put("errors", errorMessages);

        return new ResponseEntity<>(errors, status);
    }
    // without @Override (extends) and based on ProblemDetail (Spring Boot 3+)
    /*@ExceptionHandler(MethodArgumentNotValidException.class)
    ProblemDetail handleValidationError(MethodArgumentNotValidException manve) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);

        List<FieldError> fieldErrors = manve.getFieldErrors();
        List<String> errors = new ArrayList<>();

        for (FieldError fe : fieldErrors) {
            String message = messageSource.getMessage(fe, Locale.getDefault());
            errors.add(message);
        }
        problemDetail.setProperty("errors", errors);
        return problemDetail;
    }*/


    private ResponseEntity<ErrorDetailsDto> createErrorResponse(Exception exception, String message, HttpStatus httpStatus, WebRequest webRequest) {
        ErrorDetailsDto errorDetails = ErrorDetailsDto.builder()
                .timeStamp(new Date())
                .message(message)
                .details(webRequest.getDescription(false))
                .build();

        return new ResponseEntity<>(errorDetails, httpStatus);
    }

}
