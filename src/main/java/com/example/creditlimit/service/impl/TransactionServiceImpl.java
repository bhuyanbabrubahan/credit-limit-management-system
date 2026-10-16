package com.example.creditlimit.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.creditlimit.dto.TransactionResponse;
import com.example.creditlimit.entity.PaymentTransaction;
import com.example.creditlimit.mapper.DtoMapper;
import com.example.creditlimit.repository.PaymentTransactionRepository;
import com.example.creditlimit.service.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final PaymentTransactionRepository transactions;
    private final DtoMapper mapper;

    public TransactionServiceImpl(
            PaymentTransactionRepository transactions,
            DtoMapper mapper) {

        this.transactions = transactions;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponse> byCustomer(Long customerId) {

        List<PaymentTransaction> transactionList =
                transactions.findByCustomerIdOrderByCreatedAtDesc(customerId);

        // Print all transactions for the customer
        System.out.println("Transactions for Customer ID: " + customerId);

        transactionList.forEach(transaction ->
                System.out.println("Transaction Entity: " + transaction));

        return transactionList.stream()
                .map(mapper::transaction)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponse> byAgent(Long agentId) {

        List<PaymentTransaction> transactionList =
                transactions.findByAgentIdOrderByCreatedAtDesc(agentId);

        // Print all transactions for the agent
        System.out.println("Transactions for Agent ID: " + agentId);

        transactionList.forEach(transaction ->
                System.out.println("Transaction Entity: " + transaction));

        return transactionList.stream()
                .map(mapper::transaction)
                .toList();
    }
}