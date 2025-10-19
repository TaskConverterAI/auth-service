package ru.tcai.auth.api.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import ru.tcai.auth.api.dto.response.ErrorResponse;
import ru.tcai.auth.core.exception.AuthServiceException;

import java.util.List;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class AuthControllerAdvice {
    private static final Marker MARKER = MarkerFactory.getMarker("AuthControllerAdvice");

    @ExceptionHandler(AuthServiceException.class)
    public ResponseEntity<ErrorResponse> handleHomeVaultException(AuthServiceException exception) {
        log(exception);

        return ResponseEntity.status(exception.getHttpStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(ErrorResponse.builder().error(exception.getMessage()).build());
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ErrorResponse> handleTokenExpiredException(TokenExpiredException exception) {
        log(exception);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ErrorResponse.builder().error("Access token is expired").build());
    }

    @ExceptionHandler(JWTVerificationException.class)
    public ResponseEntity<ErrorResponse> handleTokenVerificationException(JWTVerificationException exception) {
        log(exception);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ErrorResponse.builder().error("Access token is invalid").build());
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleTokenVerificationException(HttpMediaTypeNotSupportedException exception) {
        log(exception);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ErrorResponse.builder().error("MediaType is not supported").build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> {
                    if (error instanceof FieldError fieldError) {
                        return "%s: %s".formatted(fieldError.getField(), fieldError.getDefaultMessage());
                    }
                    return error.getDefaultMessage();
                })
                .toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.builder().errors(errors).build());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFoundException(NoResourceFoundException exception) {
        log(exception);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ErrorResponse.builder().error("No static resource found").build());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        log(exception);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ErrorResponse.builder().error(HttpStatus.BAD_REQUEST.getReasonPhrase()).build());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        log(exception);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ErrorResponse.builder().error("HTTP method is not allowed").build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        log(exception);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ErrorResponse.builder().error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()).build());
    }

    private void log(Exception exception) {
        log.info(MARKER, "Got: {}, message: {}", exception.getClass(), exception.getMessage());
    }

}
