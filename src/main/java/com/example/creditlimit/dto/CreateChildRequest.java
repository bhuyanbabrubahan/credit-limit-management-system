package com.example.creditlimit.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CreateChildRequest(
        @NotBlank @Size(max = 50) String username,
        @NotBlank @Size(max = 100) String fullName,
        @NotNull Long parentId,
        @NotNull @DecimalMin("0.01") BigDecimal creditLimit) {
}