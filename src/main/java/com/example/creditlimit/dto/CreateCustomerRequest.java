package com.example.creditlimit.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record CreateCustomerRequest(
        @NotBlank @Size(max = 30) String customerCode,
        @NotBlank @Size(max = 100) String fullName,
        @NotBlank @Email @Size(max = 150) String email,
        @NotBlank @Size(max = 20) String mobile,
        @NotNull @Past LocalDate dateOfBirth,
        @NotBlank @Size(max = 250) String address) {
}