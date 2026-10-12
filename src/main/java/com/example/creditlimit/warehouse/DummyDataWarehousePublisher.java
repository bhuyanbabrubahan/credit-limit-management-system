package com.example.creditlimit.warehouse;

import com.example.creditlimit.entity.PaymentTransaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Placeholder for a future Kafka/ETL/Data Warehouse integration.
 */
@Component
public class DummyDataWarehousePublisher implements DataWarehousePublisher {

    private static final Logger log =
            LoggerFactory.getLogger(DummyDataWarehousePublisher.class);

    @Override
    public void publishPaymentEvent(PaymentTransaction transaction) {

        log.info(
                "DW-STUB payment event: tx={}, status={}, amount={}",
                transaction.getTransactionId(),
                transaction.getStatus(),
                transaction.getAmount());
    }
}