package com.example.creditlimit.dto;

import java.math.BigDecimal;

public record CreditResponse(
        Long userId,
        String username,
        String role,
        BigDecimal totalLimit,
        BigDecimal allocatedToChildren,
        BigDecimal usedLimit,
        BigDecimal availableLimit) {
}