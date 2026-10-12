package com.example.creditlimit.gateway;

import java.math.BigDecimal;

public record PaymentGatewayRequest(String orderReference, BigDecimal amount, String customerCode) {
}
