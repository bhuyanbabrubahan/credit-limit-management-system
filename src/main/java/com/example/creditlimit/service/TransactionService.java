package com.example.creditlimit.service;
import com.example.creditlimit.dto.TransactionResponse;
import java.util.List;
public interface TransactionService {
    List<TransactionResponse> byCustomer(Long customerId);
    List<TransactionResponse> byAgent(Long agentId);
}
