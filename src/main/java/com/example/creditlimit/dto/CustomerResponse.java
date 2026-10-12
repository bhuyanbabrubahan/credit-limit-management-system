package com.example.creditlimit.dto;

import java.time.LocalDate;

public record CustomerResponse(
        Long id,
        String customerCode,
        String fullName,
        String email,
        String mobile,
        LocalDate dateOfBirth,
        String address,
        String status
) {
}
