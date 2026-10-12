package com.example.creditlimit.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(
        String transactionId,
        String policyNumber,
        String customerCode,
        Long agentId,
        BigDecimal amount,
        BigDecimal paidAmount,
        String status,
        String gatewayReference,
        String failureReason,
        LocalDateTime paymentDate) {
}