package com.example.creditlimit.service;
import com.example.creditlimit.dto.*;
import java.util.List;
public interface PolicyService {
    List<PolicyResponse> byAgent(Long agentId);
    List<PolicyResponse> byCustomer(Long customerId);
    PolicyResponse get(Long policyId);
}
