package com.example.creditlimit.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateParentRequest(
        @NotBlank @Size(max = 50) String username,
        @NotBlank @Size(max = 100) String fullName,
        @NotNull @DecimalMin("0.00") BigDecimal creditLimit,
        @NotNull LocalDate effectiveFrom) {
}