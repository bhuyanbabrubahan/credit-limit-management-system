package com.example.creditlimit.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BookPolicyRequest(
        @NotNull Long agentId,
        @NotNull Long customerId,
        @NotNull @DecimalMin("0.01") BigDecimal premiumAmount,
        @NotBlank @Size(max = 40) String quotationNumber,
        @NotBlank @Size(max = 40) String acdNumber,
        @NotNull LocalDate policyStartDate,
        @NotNull LocalDate policyEndDate,
        @NotNull LocalDate nextDueDate) {
}