package com.example.creditlimit.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AccountResponse(
        Long id,
        String username,
        String fullName,
        String role,
        Long parentId,
        BigDecimal creditLimit,
        BigDecimal allocatedToChildren,
        BigDecimal usedLimit,
        BigDecimal availableLimit,
        Boolean active,
        LocalDate effectiveFrom) {
}