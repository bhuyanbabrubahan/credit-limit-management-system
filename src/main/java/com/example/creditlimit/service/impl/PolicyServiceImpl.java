package com.example.creditlimit.service.impl;

import com.example.creditlimit.dto.PolicyResponse;
import com.example.creditlimit.exception.BusinessException;
import com.example.creditlimit.mapper.DtoMapper;
import com.example.creditlimit.repository.PolicyRepository;
import com.example.creditlimit.service.PolicyService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PolicyServiceImpl implements PolicyService {

    private final PolicyRepository policies;
    private final DtoMapper mapper;

    public PolicyServiceImpl(
            PolicyRepository policies,
            DtoMapper mapper) {

        this.policies = policies;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PolicyResponse> byAgent(Long agentId) {

        return policies.findByAgentIdOrderByCreatedAtDesc(agentId)
                .stream()
                .map(mapper::policy)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PolicyResponse> byCustomer(Long customerId) {

        return policies.findByCustomerIdOrderByCreatedAtDesc(customerId)
                .stream()
                .map(mapper::policy)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PolicyResponse get(Long policyId) {

        return mapper.policy(
                policies.findById(policyId)
                        .orElseThrow(() ->
                                new BusinessException(
                                        "Policy not found: " + policyId,
                                        org.springframework.http.HttpStatus.NOT_FOUND)));
    }
}