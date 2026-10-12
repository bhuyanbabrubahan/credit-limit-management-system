package com.example.creditlimit.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(
        String transactionId,
        String policyNumber,
        String status,
        BigDecimal amount,
        BigDecimal paidAmount,
        String gatewayReference,
        String message,
        LocalDateTime paymentDate) {
}