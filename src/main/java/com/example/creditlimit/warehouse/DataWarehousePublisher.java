package com.example.creditlimit.warehouse;

import com.example.creditlimit.entity.PaymentTransaction;

public interface DataWarehousePublisher {

    void publishPaymentEvent(PaymentTransaction transaction);
}