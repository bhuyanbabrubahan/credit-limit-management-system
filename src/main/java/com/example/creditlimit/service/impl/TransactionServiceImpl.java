package com.example.creditlimit.service.impl;

import com.example.creditlimit.dto.TransactionResponse;
import com.example.creditlimit.mapper.DtoMapper;
import com.example.creditlimit.repository.PaymentTransactionRepository;
import com.example.creditlimit.service.TransactionService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

        return transactions
                .findByCustomerIdOrderByCreatedAtDesc(customerId)
                .stream()
                .map(mapper::transaction)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponse> byAgent(Long agentId) {

        return transactions
                .findByAgentIdOrderByCreatedAtDesc(agentId)
                .stream()
                .map(mapper::transaction)
                .toList();
    }
}