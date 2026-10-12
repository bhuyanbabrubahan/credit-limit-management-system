package com.example.creditlimit.gateway;

import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Dummy gateway. No real money moves. A quotation/order reference containing
 * FAIL simulates a failed gateway response.
 */
@Component
public class DummyPaymentGateway implements PaymentGateway {

    @Override
    public PaymentGatewayResult pay(PaymentGatewayRequest request) {

        if (request.orderReference().toUpperCase().contains("FAIL")) {
            return new PaymentGatewayResult(
                    false,
                    null,
                    "Dummy gateway rejected the payment");
        }

        return new PaymentGatewayResult(
                true,
                "GW-" + UUID.randomUUID()
                        .toString()
                        .substring(0, 10)
                        .toUpperCase(),
                null);
    }
}