package com.example.creditlimit.dto;

import jakarta.validation.constraints.*;

public record PaymentRequest(
        @NotNull Long policyId,
        @NotBlank @Size(max = 100) String idempotencyKey) {
}