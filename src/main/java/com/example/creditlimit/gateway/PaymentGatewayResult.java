package com.example.creditlimit.gateway;

public record PaymentGatewayResult(boolean success, String gatewayReference, String failureReason) {
}
