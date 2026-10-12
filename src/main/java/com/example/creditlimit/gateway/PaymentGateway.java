package com.example.creditlimit.gateway;
public interface PaymentGateway {
	
    PaymentGatewayResult pay(PaymentGatewayRequest request);
}
