package com.example.creditlimit.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PolicyResponse(
        Long id,
        String policyNumber,
        String quotationNumber,
        String acdNumber,
        Long agentId,
        String agentName,
        Long customerId,
        String customerCode,
        String customerName,
        BigDecimal premiumAmount,
        BigDecimal paidAmount,
        BigDecimal outstanding,
        LocalDate policyStartDate,
        LocalDate policyEndDate,
        LocalDate nextDueDate,
        String status) {
}