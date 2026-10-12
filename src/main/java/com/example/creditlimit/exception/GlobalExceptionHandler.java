package com.example.creditlimit.exception;

import com.example.creditlimit.dto.ApiError;

import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    ResponseEntity<ApiError> business(BusinessException e) {

        return ResponseEntity
                .status(e.getStatus())
                .body(
                        new ApiError(
                                LocalDateTime.now(),
                                e.getStatus().value(),
                                e.getStatus().getReasonPhrase(),
                                e.getMessage(),
                                Map.of()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiError> validation(
            MethodArgumentNotValidException e) {

        Map<String, String> errors =
                e.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .collect(
                                Collectors.toMap(
                                        x -> x.getField(),
                                        x -> x.getDefaultMessage(),
                                        (first, second) -> first));

        return ResponseEntity
                .badRequest()
                .body(
                        new ApiError(
                                LocalDateTime.now(),
                                400,
                                "Validation Failed",
                                "Request validation failed",
                                errors));
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ApiError> other(Exception e) {

        return ResponseEntity
                .status(500)
                .body(
                        new ApiError(
                                LocalDateTime.now(),
                                500,
                                "Internal Server Error",
                                e.getMessage(),
                                Map.of()));
    }
}